package cn.xueden.mall.web.service;


import cn.xueden.mall.web.service.dto.BaseResult;
import cn.xueden.mall.web.service.dto.CartDto;

/**功能描述：购物车业务接口
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.web.service
 * @version:1.0
 */
public interface CartService {

    /**
     * 获取会员购物车列表
     *
     * @param userId 会员 id
     * @return
     */
    BaseResult getCartList(Long userId);

    /**
     * 购物车添加商品
     *
     * @param cart 购物车
     * @return
     */
    BaseResult addProduct(CartDto cart);

    /**
     * 修改购物车商品
     *
     * @param cart 购物车商品
     * @return
     */
    BaseResult editProduct(CartDto cart);

    /**
     * 购物车删除商品
     *
     * @param cart 购物车
     * @return
     */
    BaseResult delProduct(CartDto cart);

    /**
     * 全选购物车
     *
     * @param cart 购物车
     * @return
     */
    BaseResult editCheckAll(CartDto cart);

    /**
     * 删除购物车已选商品
     *
     * @param cart 购物车
     * @return
     */
    BaseResult delCartChecked(CartDto cart);
}
