package cn.xueden.mall.web.service.impl;



import cn.xueden.mall.common.core.utils.MapperUtil;
import cn.xueden.mall.common.jpa.domain.MallESItem;
import cn.xueden.mall.common.jpa.domain.MallItem;
import cn.xueden.mall.common.jpa.domain.MallItemCat;
import cn.xueden.mall.common.jpa.domain.MallItemDesc;
import cn.xueden.mall.common.redis.utils.RedisUtils;
import cn.xueden.mall.repository.MallItemCatRepository;
import cn.xueden.mall.repository.MallItemDescRepository;
import cn.xueden.mall.repository.MallItemElasticsearchRepository;
import cn.xueden.mall.repository.MallItemRepository;
import cn.xueden.mall.web.service.ProductService;
import cn.xueden.mall.web.service.dto.BaseResult;
import cn.xueden.mall.web.service.dto.CateProductsResultDto;
import cn.xueden.mall.web.service.dto.CategoryProductPageInfoDto;
import cn.xueden.mall.web.service.dto.ProductDetDto;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
/*import org.springframework.data.elasticsearch.core.SearchResultMapper;*/
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**功能描述：商品业务接口实现类
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.web.service.impl
 * @version:1.0
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Value("${mall.product_item}")
    private String PRODUCT_ITEM;

    @Value("${mall.item_expire}")
    private int ITEM_EXPIRE;

    private final RedisUtils redisUtils;

    private final MallItemRepository itemRepository;

    private final MallItemDescRepository itemDescRepository;

    private final MallItemCatRepository itemCatRepository;

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    private final MallItemElasticsearchRepository itemElasticsearchRepository;

    public ProductServiceImpl(RedisUtils redisUtils, MallItemRepository itemRepository, MallItemDescRepository itemDescRepository, MallItemCatRepository itemCatRepository, ElasticsearchRestTemplate elasticsearchTemplate, MallItemElasticsearchRepository itemElasticsearchRepository) {
        this.redisUtils = redisUtils;
        this.itemRepository = itemRepository;
        this.itemDescRepository = itemDescRepository;
        this.itemCatRepository = itemCatRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.itemElasticsearchRepository = itemElasticsearchRepository;
    }

    /**
     * 获取商品详情
     * @param productId 商品 id
     * @return
     */
    @Override
    public BaseResult getProductDet(Long productId) {
        // Redis 商品详情缓存 key
        String redisKey = PRODUCT_ITEM + ":" + productId;

        // 先查询
        String redisJson = (String) redisUtils.get(redisKey);
        if (redisJson != null) {
            try {
                ProductDetDto productDet = MapperUtil.json2pojo(redisJson, ProductDetDto.class);
                // 重置商品缓存时间
                redisUtils.expire(redisKey, ITEM_EXPIRE);
                return BaseResult.success(productDet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 没有缓存，从数据库中查询
        MallItem tbItem = itemRepository.getOne(productId);
        if (tbItem != null) {
            ProductDetDto productDet = new ProductDetDto();
            productDet.setProductId(tbItem.getId());
            productDet.setProductName(tbItem.getTitle());
            productDet.setSubTitle(tbItem.getSellPoint());
            productDet.setSalePrice(tbItem.getPrice());
            if (tbItem.getLimitNum() > tbItem.getNum()) {
                tbItem.setLimitNum(tbItem.getNum());
            }
            productDet.setLimitNum(tbItem.getLimitNum());
            productDet.setNum(tbItem.getNum());
            productDet.setProductImageBig(tbItem.getImages()[0]);

            // 查询商品详情
            MallItemDesc tbItemDesc = itemDescRepository.getOne(productId);
            if (tbItemDesc != null) {
                productDet.setDetail(tbItemDesc.getItemDesc());
            }

            // 设置商品小图
            productDet.setProductImageSmall(tbItem.getImages());

            // 把结果添加至缓存中
            try {
                String productDetJson = MapperUtil.obj2json(productDet);
                redisUtils.set(redisKey, productDetJson);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return BaseResult.success(productDet);
        }

        return BaseResult.fail("获取商品详细信息失败");
    }


    /**
     * 获取分类商品
     * @param categoryProductPageInfo 分类商品查询信息
     * @return
     */
    @Override
    public BaseResult getByCategory(CategoryProductPageInfoDto categoryProductPageInfo) {
        CateProductsResultDto cateProductsResult = new CateProductsResultDto();

        // 分类 id
        Long cid = categoryProductPageInfo.getCid();

        // 查询 key
        String key = categoryProductPageInfo.getKey();

        // 排序类型 1 综合排序 2 销量排序 3 价格从低到高 4 价格从高到低
        String sort = categoryProductPageInfo.getSort();

        // 第几页
        Integer page = categoryProductPageInfo.getPage() - 1;

        // 每页大小
        Integer size = categoryProductPageInfo.getSize();

        // 最小值
        Integer priceGt = categoryProductPageInfo.getPriceGt();

        // 最大值
        Integer priceLt = categoryProductPageInfo.getPriceLte();

        // ElasticSearch 查询
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        if (cid != null) {
            // 查询该分类所有子分类
            List<MallItemCat> tbItemCats = itemCatRepository.findByPid(cid);
            List<Long> cids = new ArrayList<>();
            for (MallItemCat tbItemCat : tbItemCats) {
                Long id = tbItemCat.getId();
                cids.add(id);
            }
            cids.add(cid);
            queryBuilder.withQuery(QueryBuilders.termsQuery("cid", cids));
        }
        if (key != null) {
            queryBuilder.withQuery(QueryBuilders.multiMatchQuery(key, "productName", "subTitle"));
            queryBuilder.withHighlightFields(new HighlightBuilder.Field("productName"));
        }
        if (priceGt != null && priceLt != null) {
            queryBuilder.withQuery(QueryBuilders.rangeQuery("salePrice").gte(priceGt).lte(priceLt));
        }
        if (StringUtils.isNotBlank(categoryProductPageInfo.getSort())) {
            if ("2".equals(sort)) {
                queryBuilder.withSort(SortBuilders.fieldSort("orderNum").order(SortOrder.DESC));
            } else if ("3".equals(sort)) {
                queryBuilder.withSort(SortBuilders.fieldSort("salePrice").order(SortOrder.ASC));
            } else if ("4".equals(sort)) {
                queryBuilder.withSort(SortBuilders.fieldSort("salePrice").order(SortOrder.DESC));
            }
        }
        queryBuilder.withPageable(PageRequest.of(page, size));

        // 执行搜索
        List<MallESItem> esItemList = new ArrayList<>();
        long total = 0L;

        // 按关键字查询
       /* if (key != null) {
            AggregatedPage<MallESItem> esItems = elasticsearchTemplate.queryForPage(queryBuilder.build(), MallESItem.class, new SearchResultMapper() {
                @Override
                public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {
                    List<MallESItem> esItems = new ArrayList<>();
                    for (SearchHit hit : response.getHits()) {
                        if (response.getHits().getHits().length <= 0) {
                            return null;
                        }
                        Map<String, Object> source = hit.getSourceAsMap();
                        MallESItem esItem = new MallESItem();
                        Long id = (Long)source.get("id");
                        esItem.setId(id);
                        int cid = (int) source.get("cid");
                        esItem.setCid((long)cid);
                        Long productId = (Long) source.get("productId");
                        esItem.setProductId(productId);
                        String subTitle = (String) source.get("subTitle");
                        esItem.setSubTitle(subTitle);
                        Double salePrice = (Double) source.get("salePrice");
                        esItem.setSalePrice(salePrice);
                        String picUrl = (String) source.get("picUrl");
                        esItem.setPicUrl(picUrl);
                        int orderNum = (int) source.get("orderNum");
                        esItem.setOrderNum(orderNum);
                        int limit = (int) source.get("limit");
                        esItem.setLimit(limit);
                        HighlightField productName = hit.getHighlightFields().get("productName");
                        if (productName != null) {
                            esItem.setProductName(productName.fragments()[0].toString());
                        }
                        esItems.add(esItem);
                    }

                    if (esItems.size() > 0) {
                        return new AggregatedPageImpl<>((List<T>) esItems);
                    }

                    return null;
                }

                @Override
                public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                    return null;
                }
            });

            if (esItems != null) {
                total = esItems.getTotalElements();
                esItemList = esItems.getContent();
            }
        }
*/
        // 全部商品
        if (StringUtils.isBlank(key) && cid == null) {
            Iterable<MallESItem> esItems = itemElasticsearchRepository.search(queryBuilder.build());
            if (esItems != null) {
                esItemList = Lists.newArrayList(esItems);
                total = esItemList.size();
            }
        }

        // 按分类查询
        if (cid != null) {
            Page<MallESItem> search = itemElasticsearchRepository.search(queryBuilder.build());
            if (search != null) {
                esItemList = search.getContent();
                total = search.getTotalElements();
            }
        }

        cateProductsResult.setTotal(total);
        cateProductsResult.setData(esItemList);

        return BaseResult.success(cateProductsResult);
    }

    /**
     * 直接从数据库获取所有商品
     * @return
     */
    @Override
    public BaseResult getAllGoods() {
        //return itemDescRepository.findAll();
        return null;
    }

    /**
     * 快速搜索
     *
     * @param key 关键字
     * @return
     */
    @Override
    public BaseResult getQuickSearch(String key) {
        // 构建查询条件
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        // 添加基本查询条件
        searchQueryBuilder.withQuery(QueryBuilders.matchQuery("productName", key));
        // 初始化分页参数
        int page = 0;
        int size = 5;
        // 设置分页参数
        searchQueryBuilder.withPageable(PageRequest.of(page, size));
        Page<MallESItem> searchs = itemElasticsearchRepository.search(searchQueryBuilder.build());
        List<MallESItem> esItems = searchs.getContent();
        return BaseResult.success(esItems);
    }
}
