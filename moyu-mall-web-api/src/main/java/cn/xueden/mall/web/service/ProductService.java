package cn.xueden.mall.web.service;


import cn.xueden.mall.web.service.dto.BaseResult;
import cn.xueden.mall.web.service.dto.CategoryProductPageInfoDto;

/**功能描述：商品业务接口
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.web.service
 * @version:1.0
 */
public interface ProductService {

    /**
     * 获取商品详情
     *
     * @param productId 商品 id
     * @return
     */
    BaseResult getProductDet(Long productId);

    /**
     * 获取分类商品
     *
     * @param categoryProductPageInfo 分类商品查询信息
     * @return
     */
    BaseResult getByCategory(CategoryProductPageInfoDto categoryProductPageInfo);

    /**
     * 直接从数据库获取所有商品
     *
     * @param
     * @return
     */
    BaseResult getAllGoods();

    /**
     * 快速搜索
     *
     * @param key 关键字
     * @return
     */
    BaseResult getQuickSearch(String key);

}
