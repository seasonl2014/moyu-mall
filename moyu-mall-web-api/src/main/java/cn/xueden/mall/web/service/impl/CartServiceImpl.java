package cn.xueden.mall.web.service.impl;

import cn.xueden.mall.common.utils.MapperUtil;
import cn.xueden.mall.common.utils.RedisUtils;
import cn.xueden.mall.domain.MallItem;
import cn.xueden.mall.repository.MallItemRepository;
import cn.xueden.mall.web.service.CartService;
import cn.xueden.mall.web.service.dto.BaseResult;
import cn.xueden.mall.web.service.dto.CartDto;
import cn.xueden.mall.web.service.dto.CartProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**功能描述：购物车业务接口实现类
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.web.service.impl
 * @version:1.0
 */
@Service
public class CartServiceImpl implements CartService {

    private final RedisUtils redisUtils;

    private final MallItemRepository itemRepository;

    @Value("${mall.cart_pre}")
    private String CART_PRE;

    public CartServiceImpl(RedisUtils redisUtils, MallItemRepository itemRepository) {
        this.redisUtils = redisUtils;
        this.itemRepository = itemRepository;
    }

    /**
     * 获取会员购物车列表
     * @param userId 会员 id
     * @return
     */
    @Override
    public BaseResult getCartList(Long userId) {

        // Redis key
        String redisKey = CART_PRE + ":" + userId;

        // 获取会员所有购物车商品
        Map<Object, Object> cartMap = redisUtils.hmget(redisKey);

        // 封装成购物车集合
        List<CartProductDto> cartProducts = new ArrayList<>();
        if (cartMap != null) {
            for (Object key: cartMap.keySet()) {
                String productJson = (String) cartMap.get(key);
                CartProductDto cartProduct = new CartProductDto();
                try {
                    cartProduct = MapperUtil.json2pojo(productJson, CartProductDto.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    return BaseResult.fail("获取购物车失败");
                }
                if (cartProduct != null) {
                    cartProducts.add(cartProduct);
                }
            }
        }

        return BaseResult.success(cartProducts);
    }


    /**
     * 购物车添加商品
     * @param cart 购物车
     * @return
     */
    @Override
    public BaseResult addProduct(CartDto cart) {

        // Redis key CART:userId
        String cartKey = CART_PRE + ":" + cart.getUserId();

        // HASH key
        String hashKey = String.valueOf(cart.getProductId());

        // 判断缓存中该会员购物车中是否有该商品
        boolean hasHash = redisUtils.hHasKey(cartKey, hashKey);
        // 有商品，数量相加
        if (hasHash) {
            try {
                String cartJson = (String) redisUtils.hget(cartKey, hashKey);
                if (cartJson != null) {
                    CartProductDto redisCartProduct = null;
                    redisCartProduct = MapperUtil.json2pojo(cartJson, CartProductDto.class);
                    redisCartProduct.setProductNum(redisCartProduct.getProductNum() + cart.getProductNum());
                    String redisJson = MapperUtil.obj2json(redisCartProduct);
                    redisUtils.hset(cartKey, hashKey, redisJson);
                    return BaseResult.success("添加商品到购物车成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 没有商品，新增一条
        try {
            MallItem tbItem = itemRepository.getOne(cart.getProductId());
            if (tbItem == null) {
                return BaseResult.fail("要添加到的购物车的商品不存在");
            }
            CartProductDto cartProduct = new CartProductDto();
            cartProduct.setProductId(tbItem.getId());
            cartProduct.setProductName(tbItem.getTitle());
            cartProduct.setProductNum(cart.getProductNum());
            cartProduct.setProductImg(tbItem.getImages()[0]);
            cartProduct.setLimitNum(tbItem.getLimitNum());
            cartProduct.setSalePrice(tbItem.getPrice());
            cartProduct.setChecked("0");
            String redisJson = MapperUtil.obj2json(cartProduct);
            redisUtils.hset(cartKey, hashKey, redisJson);
            return BaseResult.success("添加商品到购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BaseResult.fail("添加商品到购物车失败");
    }


    /**
     * 修改购物车商品
     * @param cart 购物车商品
     * @return
     */
    @Override
    public BaseResult editProduct(CartDto cart) {

        // Redis key
        String redisKey = CART_PRE + ":" + cart.getUserId();

        // Hash key
        String hashKey = String.valueOf(cart.getProductId());

        // 修改缓存
        try {
            String oldCartJson = (String) redisUtils.hget(redisKey, hashKey);
            CartProductDto oldCartProduct = MapperUtil.json2pojo(oldCartJson, CartProductDto.class);
            oldCartProduct.setChecked(cart.getChecked());
            oldCartProduct.setProductNum(cart.getProductNum());
            String newCartProductJson = MapperUtil.obj2json(oldCartProduct);
            if (newCartProductJson != null) {
                redisUtils.hset(redisKey, hashKey, newCartProductJson);
                return BaseResult.success("编辑购物车商品成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return BaseResult.fail("编辑购物车商品失败");
    }

    /**
     * 购物车删除商品
     * @param cart 购物车
     * @return
     */
    @Override
    public BaseResult delProduct(CartDto cart) {

        // Redis key
        String redisKey = CART_PRE + ":" + cart.getUserId();

        // Hash key
        String hashKey = String.valueOf(cart.getProductId());

        // 删除缓存
        redisUtils.hdel(redisKey, hashKey);

        return BaseResult.success("删除购物车商品成功");
    }

    /**
     * 全选购物车
     * @param cart 购物车
     * @return
     */
    @Override
    public BaseResult editCheckAll(CartDto cart) {

        // Redis key
        String redisKey = CART_PRE + ":" + cart.getUserId();

        Map<Object, Object> hashByKey = redisUtils.hmget(redisKey);
        for (Object key : hashByKey.keySet()) {
            String oldCartProductJson = (String) hashByKey.get(key);
            try {
                CartProductDto cartProduct = MapperUtil.json2pojo(oldCartProductJson, CartProductDto.class);
                if (cartProduct != null) {
                    if ("true".equals(cart.getChecked())) {
                        cartProduct.setChecked("1");
                    } else {
                        cartProduct.setChecked("0");
                    }
                }
                String newCartProduct = MapperUtil.obj2json(cartProduct);
                redisUtils.hset(redisKey, String.valueOf(key), newCartProduct);
            } catch (Exception e) {
                e.printStackTrace();
                return BaseResult.fail("购物车全选失败");
            }
        }

        return BaseResult.success("购物车全选成功");
    }

    /**
     * 删除购物车已选商品
     *
     * @param cart 购物车
     * @return
     */
    @Override
    public BaseResult delCartChecked(CartDto cart) {

        // Redis key
        String redisKey = CART_PRE + ":" + cart.getUserId();

        Map<Object, Object> hashByKey = redisUtils.hmget(redisKey);
        for (Object key : hashByKey.keySet()) {
            String oldCartProductJson = (String) hashByKey.get(key);
            try {
                CartProductDto cartProduct = MapperUtil.json2pojo(oldCartProductJson, CartProductDto.class);
                if (cartProduct != null && "1".equals(cartProduct.getChecked())) {
                    redisUtils.hdel(redisKey, key);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return BaseResult.fail("删除购物车已选商品失败");
            }
        }

        return BaseResult.success("删除购物车已选商品成功");
    }

}
