package cn.xueden.mall.web.service.impl;


import cn.xueden.mall.domain.MallESItem;
import cn.xueden.mall.domain.MallItem;
import cn.xueden.mall.repository.MallItemElasticsearchRepository;
import cn.xueden.mall.repository.MallItemRepository;
import cn.xueden.mall.repository.MallOrderItemRepository;
import cn.xueden.mall.web.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther:梁志杰
 * @Date:2020/8/12
 * @Description:cn.xueden.modules.mall.web.service.impl
 * @version:1.0
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private MallItemElasticsearchRepository itemRepository;

    @Autowired
    private MallItemRepository mallItemRepository;

    @Autowired
    private MallOrderItemRepository mallOrderItemRepository;

    /**
     * 同步单个索引
     *
     * @param type 0 更新索引 1 删除索引
     * @param itemId 商品 id
     * @return
     */
    @Override
    public void refreshItem(int type, Long itemId) {
        // 更新索引
        if (type == 0) {
            // 从数据库中查出数据
            MallItem tbItem = mallItemRepository.getOne(itemId);
            // 设置 ESItem
            if (tbItem != null) {
                MallESItem esItem = new MallESItem();
                esItem.setId(tbItem.getId());
                esItem.setProductId(tbItem.getId());
                esItem.setProductName(tbItem.getTitle());
                esItem.setSubTitle(tbItem.getSellPoint());
                esItem.setSalePrice(tbItem.getPrice().doubleValue());
                esItem.setPicUrl(tbItem.getImages()[0]);
                esItem.setCid(tbItem.getCid());
                if (tbItem.getLimitNum() > tbItem.getNum()) {
                    esItem.setLimit(tbItem.getNum());
                } else {
                    esItem.setLimit(tbItem.getLimitNum());
                }
                // 查询该商品订单数量
                int orderNum = mallOrderItemRepository.selectOrderNumByItemId(tbItem.getId());
                esItem.setOrderNum(orderNum);
                // 更新索引
                itemRepository.save(esItem);
            }
        }

        // 删除索引
        else {
            itemRepository.deleteById(itemId);
        }

    }

    /**
     * 同步所有索引
     *
     * @return
     */
    @Override
    public void importAllItems() {
        // 从数据库中查询所有商品
        List<MallItem> tbItemList = mallItemRepository.findAll();

        List<MallESItem> esItems = new ArrayList<>();
        for (MallItem tbItem : tbItemList) {
            MallESItem esItem = new MallESItem();
            esItem.setId(tbItem.getId());
            esItem.setProductId(tbItem.getId());
            esItem.setProductName(tbItem.getTitle());
            esItem.setSubTitle(tbItem.getSellPoint());
            esItem.setSalePrice(tbItem.getPrice().doubleValue());
            esItem.setPicUrl(tbItem.getImages()[0]);
            esItem.setCid(tbItem.getCid());
            if (tbItem.getLimitNum() > tbItem.getNum()) {
                esItem.setLimit(tbItem.getNum());
            } else {
                esItem.setLimit(tbItem.getLimitNum());
            }
            // 查询该商品订单数量
            int orderNum = mallOrderItemRepository.selectOrderNumByItemId(tbItem.getId());
            esItem.setOrderNum(orderNum);
            esItems.add(esItem);
        }

        // 批量新增
        itemRepository.saveAll(esItems);
    }
}
