package cn.xueden.mall.web.controller;


import cn.xueden.mall.web.service.CartService;
import cn.xueden.mall.web.service.dto.BaseResult;
import cn.xueden.mall.web.service.dto.CartDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**功能描述：商城购物车控制层
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.web.controller
 * @version:1.0
 */
@RestController
@RequestMapping("member/cart")
@Api(tags = "购物车服务接口")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * 获取会员购物车列表
     *
     * @param userId 会员 id
     * @return
     */


    @GetMapping("getCartList")
    @ApiOperation(value = "获取购物车列表")
    public BaseResult getCartList(@RequestParam Long userId) {
        BaseResult baseResult = cartService.getCartList(userId);
        return baseResult;
    }

    /**
     * 添加商品到购物车
     *
     * @param cart 商品
     * @return
     */
    @PostMapping("addProduct")
    @ApiOperation(value = "购物车添加商品")
    public BaseResult addProduct(@RequestBody CartDto cart) {
        BaseResult baseResult = cartService.addProduct(cart);
        return baseResult;
    }

    /**
     * 修改购物车商品
     *
     * @param cart 购物车商品
     * @return
     */
    @PostMapping("editProduct")
    @ApiOperation(value = "修改购物车商品")
    public BaseResult editProduct(@RequestBody CartDto cart) {
        BaseResult baseResult = cartService.editProduct(cart);
        return baseResult;
    }

    /**
     * 删除购物车商品
     * @param cart
     * @return
     */

    @PostMapping("delProduct")
    @ApiOperation(value = "购物车删除商品")
    public BaseResult delProduct(@RequestBody CartDto cart) {
        BaseResult baseResult = cartService.delProduct(cart);
        return baseResult;
    }

    /**
     * 全选购物车
     *
     * @param cart 购物车
     * @return
     */
    @PostMapping("editCheckAll")
    @ApiOperation(value = "全选购物车")
    public BaseResult editCheckAll(@RequestBody CartDto cart) {
        BaseResult baseResult = cartService.editCheckAll(cart);
        return baseResult;
    }

    /**
     * 删除已选购物车商品
     *
     * @param cart 购物车
     * @return
     */
    @PostMapping("delChecked")
    @ApiOperation(value = "删除已选购物车商品")
    public BaseResult delCartChecked(@RequestBody CartDto cart) {
        BaseResult baseResult = cartService.delCartChecked(cart);
        return baseResult;
    }
}
