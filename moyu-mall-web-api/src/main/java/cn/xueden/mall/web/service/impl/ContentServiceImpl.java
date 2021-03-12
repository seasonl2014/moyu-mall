package cn.xueden.mall.web.service.impl;




import cn.xueden.mall.common.core.utils.MapperUtil;
import cn.xueden.mall.common.jpa.domain.MallItem;
import cn.xueden.mall.common.redis.utils.RedisUtils;
import cn.xueden.mall.repository.MallItemRepository;
import cn.xueden.mall.repository.MallPanelContentRepository;
import cn.xueden.mall.repository.MallPanelRepository;
import cn.xueden.mall.web.service.ContentService;
import cn.xueden.mall.web.service.dto.BaseResult;
import cn.xueden.mall.web.service.dto.TbPanelContentDto;
import cn.xueden.mall.web.service.dto.TbPanelDto;
import cn.xueden.mall.web.service.mapper.MallPanelContentMapper;
import cn.xueden.mall.web.service.mapper.MallPanelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**功能描述：获取首页各板块内容接口实现
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class ContentServiceImpl implements ContentService {

    private final RedisUtils redisUtils;

    private final MallItemRepository itemRepository;

    private final MallPanelRepository panelRepository;

    private final MallPanelContentRepository panelContentRepository;

    private final MallPanelContentMapper panelContentMapper;

    private final MallPanelMapper panelMapper;

    /**
     * 首页板块内容的 key
     */
    @Value("${mall.product_home}")
    private String PRODUCT_HOME;

    public ContentServiceImpl(RedisUtils redisUtils, MallItemRepository itemRepository, MallPanelRepository panelRepository, MallPanelContentRepository panelContentRepository, MallPanelContentMapper panelContentMapper, MallPanelMapper panelMapper) {
        this.redisUtils = redisUtils;
        this.itemRepository = itemRepository;
        this.panelRepository = panelRepository;
        this.panelContentRepository = panelContentRepository;
        this.panelContentMapper = panelContentMapper;
        this.panelMapper = panelMapper;
    }

    /**
     * 获取首页板块内容
     * @return
     */
    @Override
    public BaseResult getHome() {
        List<TbPanelDto> tbPanelDtos = new ArrayList<>();

        // 查询缓存
        try {
            // 有缓存从缓存读取
            String tbPanelsJson = (String) redisUtils.get(PRODUCT_HOME);
            if (tbPanelsJson != null) {
                tbPanelDtos = MapperUtil.json2list(tbPanelsJson, TbPanelDto.class);
                return BaseResult.success(tbPanelDtos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 没有缓存从数据库中读取
        tbPanelDtos =  panelMapper.toDto(panelRepository.findAll());
        for (TbPanelDto tbPanelDto : tbPanelDtos) {
            List<TbPanelContentDto> tbPanelContents = panelContentMapper.toDto(panelContentRepository.findByPid(tbPanelDto.getId()));
            // 设置商品相关信息
            for (TbPanelContentDto tbPanelContentDto : tbPanelContents) {
                MallItem tbItem = itemRepository.getOne(tbPanelContentDto.getProductId());
                if (tbItem != null) {
                    tbPanelContentDto.setSalePrice(tbItem.getPrice());
                    tbPanelContentDto.setProductName(tbItem.getTitle());
                    tbPanelContentDto.setSubTitle(tbItem.getSellPoint());
                    Integer limitNum = tbItem.getLimitNum();
                    Integer num = tbItem.getNum();
                    if (limitNum > num) {
                        limitNum = num;
                    }
                    tbPanelContentDto.setLimit(limitNum);
                }
            }
            tbPanelDto.setPanelContentDtos(tbPanelContents);
        }

        // 将数据存入缓存
        try {
            String tbPanelsJson = MapperUtil.obj2json(tbPanelDtos);
            redisUtils.set(PRODUCT_HOME, tbPanelsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return BaseResult.success(tbPanelDtos);
    }
}
