package cn.xueden.mall.web.service;

/**搜索服务
 * @Auther:梁志杰
 * @Date:2020/8/12
 * @Description:cn.xueden.modules.mall.web.service
 * @version:1.0
 */
public interface SearchService {

    /**
     * 同步单个索引
     *
     * @param type 0 更新索引 1 删除索引
     * @param itemId 商品 id
     * @return
     */
    void refreshItem(int type, Long itemId);

    /**
     * 同步所有索引
     *
     * @return
     */
    void importAllItems();

}
