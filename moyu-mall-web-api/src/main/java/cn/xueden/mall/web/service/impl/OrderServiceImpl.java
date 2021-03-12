package cn.xueden.mall.web.service.impl;



import cn.xueden.mall.common.core.utils.IDUtil;
import cn.xueden.mall.common.core.utils.MapperUtil;
import cn.xueden.mall.common.core.utils.StringUtils;
import cn.xueden.mall.common.core.utils.XuedenConstant;
import cn.xueden.mall.common.jpa.domain.*;
import cn.xueden.mall.common.jpa.utils.QueryHelp;
import cn.xueden.mall.common.redis.utils.RedisUtils;
import cn.xueden.mall.repository.*;
import cn.xueden.mall.web.service.OrderService;
import cn.xueden.mall.web.service.dto.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**功能描述：订单业务接口实现类
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/5
 * @Description:cn.xueden.modules.mall.web.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = false)
public class OrderServiceImpl implements OrderService {

    @Value("${mall.add_order}")
    private String ADD_ORDER;

    @Value("${mall.cart_pre}")
    private String CART_PRE;

    @Value("${mall.product_item}")
    private String PRODUCT_ITEM;

    @Value("${mall.order_pay}")
    private String ORDER_PAY;

    @Value("${file.path}")
    private String path;


    private final RedisUtils redisUtils;

    private final MallMemberRepository memberRepository;

    private final MallOrderRepository orderRepository;

    private final MallOrderItemRepository orderItemRepository;

    private final MallItemRepository itemRepository;

    private final MallOrderShippingRepository orderShippingRepository;

    public OrderServiceImpl(RedisUtils redisUtils, MallMemberRepository memberRepository, MallOrderRepository orderRepository, MallOrderItemRepository orderItemRepository, MallItemRepository itemRepository, MallOrderShippingRepository orderShippingRepository) {
        this.redisUtils = redisUtils;
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.itemRepository = itemRepository;
        this.orderShippingRepository = orderShippingRepository;
    }

    /**
     * 创建订单
     * @param orderInfo 订单信息
     * @param request 请求 ip 验证
     * @return
     */
    @Transactional
    @Override
    public BaseResult addOrder(OrderInfoDto orderInfo, HttpServletRequest request) {

        // 用户 id
        Long userId = orderInfo.getUserId();

        // 地址收货人
        String username = orderInfo.getUserName();

        // 订单商品
        List<CartProductDto> goods = orderInfo.getGoodsList();

        if (userId == null || StringUtils.isBlank(username) || goods == null || goods.size() == 0) {
            return BaseResult.fail("请求信息异常");
        }

        // 请求 ip 地址
        String ip = StringUtils.getIp(request);
        if("0:0:0:0:0:0:0:1".equals(ip)){
            ip="127.0.0.1";
        }

        // Redis key，防止恶意请求
        String redisKey = ADD_ORDER + ":" + ip;
        String temp = (String) redisUtils.get(redisKey);
        if (StringUtils.isNotBlank(temp)) {
            return BaseResult.fail("您提交的太频繁了，请您稍后再试");
        }

        MallMember tbMember = memberRepository.getOne(userId);
        if (tbMember == null) {
            return BaseResult.fail("获取下单用户失败");
        }

        // 生成订单
        MallOrder tbOrder = new MallOrder();
        tbOrder.setId(String.valueOf(IDUtil.getRandomId()));
        tbOrder.setUserId(userId);
        if (StringUtils.isBlank(tbMember.getUsername())) {
            tbOrder.setBuyerNick(tbMember.getPhone());
        } else {
            tbOrder.setBuyerNick(tbMember.getUsername());
        }
        tbOrder.setPayment(orderInfo.getOrderTotal());
        tbOrder.setCreated(new Date());
        tbOrder.setUpdated(new Date());
        // 0: 未付款 1: 已付款 2: 未发货 3: 已发货 4: 交易成功 5: 交易关闭
        tbOrder.setStatus(0);

        // 添加订单
        orderRepository.save(tbOrder);

        // 生成订单信息
        List<CartProductDto> goodsList = orderInfo.getGoodsList();
        for (CartProductDto cartProduct : goodsList) {
            MallOrderItem tbOrderItem = new MallOrderItem();
            Long orderItemId = IDUtil.getRandomId();
            tbOrderItem.setId(orderItemId.toString());
            tbOrderItem.setItemId(cartProduct.getProductId().toString());
            tbOrderItem.setOrderId(tbOrder.getId());
            tbOrderItem.setNum(cartProduct.getProductNum());
            tbOrderItem.setTitle(cartProduct.getProductName());
            tbOrderItem.setPrice(cartProduct.getSalePrice());
            tbOrderItem.setTotalFee(cartProduct.getSalePrice().multiply(BigDecimal.valueOf(cartProduct.getProductNum())));
            tbOrderItem.setPicPath(cartProduct.getProductImg());
            orderItemRepository.save(tbOrderItem);

            // 删除会员购物车中的该商品
            // Redis key
            String cartProductKey = CART_PRE + ":" + orderInfo.getUserId();
            // Hash Key
            String hashKey = cartProduct.getProductId().toString();
            // 删除记录
            redisUtils.del(cartProductKey, hashKey);
        }

        // 生成物流信息
        MallOrderShipping tbOrderShipping = new MallOrderShipping();
        tbOrderShipping.setOrderId(tbOrder.getId());
        tbOrderShipping.setReceiverName(orderInfo.getUserName());
        tbOrderShipping.setReceiverAddress(orderInfo.getStreetName());
        tbOrderShipping.setReceiverPhone(orderInfo.getTel());
        orderShippingRepository.save(tbOrderShipping);

        // 修改商品库存信息
        for (CartProductDto good : goods) {
            Long productId = good.getProductId();
            // 商品库存
            itemRepository.reduceItemNum(productId, good.getProductNum());
            // 删除 Redis 商品缓存信息
            String redisItemKey = PRODUCT_ITEM + ":" + productId;
            redisUtils.del(redisItemKey);
        }

        // 设置 Redis ip 缓存
        redisUtils.set(redisKey, "ADD_ORDER");
        redisUtils.expire(redisKey, 60);
        return BaseResult.success((Object)tbOrder.getId());
    }

    /**
     * 根据订单 id 获取订单详情
     * @param orderId 订单 id
     * @return
     */
    @Override
    public BaseResult getOrderDet(String orderId) {

        OrderDto order = new OrderDto();

        // 获取订单信息
        MallOrder tbOrder = orderRepository.getOne(orderId);
        if (tbOrder == null) {
            return BaseResult.fail("通过id获取订单详情失败");
        }

        // 订单 id
        order.setOrderId(tbOrder.getId());

        // 会员 id
        order.setUserId(tbOrder.getUserId().toString());

        // 订单状态
        order.setOrderStatus(tbOrder.getStatus());

        // 未支付，最晚支付时间
        if (order.getOrderStatus() == 0) {
            Date createDate = tbOrder.getCreated();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(createDate);
            calendar.add(Calendar.HOUR, 2);
            String countTime = calendar.getTime().getTime() + "";
            order.setCountTime(countTime);
        }

        // 订单创建时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createDate = simpleDateFormat.format(tbOrder.getCreated());
        order.setCreateDate(createDate);

        // 订单支付时间
        if (tbOrder.getPaymentTime() != null) {
            String payDate = simpleDateFormat.format(tbOrder.getPaymentTime());
            order.setPayDate(payDate);
        }

        // 订单发货时间
        if (tbOrder.getConsignTime() != null) {
            String consignDate = simpleDateFormat.format(tbOrder.getConsignTime());
            order.setConsignDate(consignDate);
        }

        // 订单关闭时间
        if (tbOrder.getCloseTime() != null) {
            String closeDate = simpleDateFormat.format(tbOrder.getCloseTime());
            order.setCloseDate(closeDate);
        }

        // 订单完成时间
        if (tbOrder.getEndTime() != null && tbOrder.getStatus() == 4) {
            String endDate = simpleDateFormat.format(tbOrder.getEndTime());
            order.setFinishDate(endDate);
        }

        // 地址
        MallOrderShipping tbOrderShipping = orderShippingRepository.getOne(tbOrder.getId());
        MallAddress tbAddress = new MallAddress();
        tbAddress.setUserName(tbOrderShipping.getReceiverName());
        tbAddress.setStreetName(tbOrderShipping.getReceiverAddress());
        tbAddress.setTel(tbOrderShipping.getReceiverPhone());
        order.setTbAddress(tbAddress);

        // 订单总计
        order.setOrderTotal(tbOrder.getPayment());

        // 订单商品列表
        List<MallOrderItem> tbOrderItemList = orderItemRepository.getByOrderId(tbOrder.getId());
        List<CartProductDto> cartProductList = new ArrayList<>();
        for (MallOrderItem tbOrderItem : tbOrderItemList) {
            CartProductDto cartProduct = new CartProductDto();
            cartProduct.setProductId(Long.parseLong(tbOrderItem.getItemId()));
            cartProduct.setProductName(tbOrderItem.getTitle());
            cartProduct.setSalePrice(tbOrderItem.getPrice());
            cartProduct.setProductNum(tbOrderItem.getNum());
            cartProduct.setProductImg(tbOrderItem.getPicPath());
            cartProductList.add(cartProduct);
        }

        // 订单物流信息，如果已发货，或者交易完成查询物流信息
        if (tbOrder.getStatus() == 3 || tbOrder.getStatus() == 4) {
            order.setShippingName(tbOrder.getShippingName());
            order.setShippingCode(tbOrder.getShippingCode());
        }

        order.setGoodsList(cartProductList);
        return BaseResult.success(order);
    }


    /**
     * 支付宝订单支付
     * @param orderPay 订单支付
     * @return
     */
    @Transactional
    @Override
    public BaseResult payment(OrderPayDto orderPay) {

        String result = null;
        try{
           /* result = AliPay.nativePay(orderPay.getOrderId().toString() + "", ""+orderPay.getOrderTotal()+"", XuedenConstant.YUNGOUOSALIPAYMCHID, "用户购物支付【电商平台】", "1", null, XuedenConstant.YUNGOUORETURNURL, XuedenConstant.YUNGOUOSKEY);
            // 生成指定url对应的二维码到文件，宽和高都是300像素
            QrCodeUtil.generate(result, 300, 300, FileUtil.file(path+"qrcode-"+orderPay.getOrderId()+".jpg"));
        */
        }catch (Exception e){
            e.printStackTrace();
        }

         // 将订单信息保存在 redis 中
        // redisKey
        String redisKey = ORDER_PAY + ":" + orderPay.getOrderId();
        try {
            String orderPayJson = MapperUtil.obj2json(orderPay);
            if (orderPayJson != null) {
                redisUtils.set(redisKey, orderPayJson);
                redisUtils.expire(redisKey, 60 * 60 * 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        orderPay.setQrCode(path+"qrcode-"+orderPay.getOrderId()+".jpg");
        return BaseResult.success(orderPay);
    }

    /**
     * 获取订单支付状态
     * @param orderId 订单 id
     * @return
     */
    @Override
    public BaseResult getOrderStatus(String orderId) {
        // 获取订单信息
        MallOrder tbOrder = orderRepository.getOne(orderId);
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderStatus(tbOrder.getStatus());
        orderDto.setOrderId(orderId);
        return BaseResult.success(orderDto);

    }

    /**
     * 更新订单状态
     * @param orderId 订单 id
     * @param orderStatus 订单状态
     * @return
     */
    @Transactional
    @Override
    public int updateOrderStatus(String orderId, Integer orderStatus) {

        // 修改缓存状态
        String redisKey = ORDER_PAY + ":" + orderId;

        // 更新缓存
        String orderPayJson = (String) redisUtils.get(redisKey);

        if (orderPayJson != null) {
            try {
                OrderPayDto orderPay = MapperUtil.json2pojo(orderPayJson, OrderPayDto.class);
                orderPay.setOrderStatus((XuedenConstant.ORDER_STATUS_PAY).toString());
                String newPayJson = MapperUtil.obj2json(orderPay);
                if (newPayJson != null) {
                    redisUtils.set(redisKey, newPayJson);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return orderRepository.updateOrderStatus(orderId, orderStatus);
    }

    /**
     * 带条件分页获取订单列表
     * @param criteria 查询条件
     * @param pageable 分页信息
     * @return
     */
    @Override
    public BaseResult getOrderList(MallOrderQueryCriteria criteria, Pageable pageable) {

        Page<MallOrder> page = orderRepository.findAll((root, query, cb) -> QueryHelp.getPredicate(root, criteria, cb), pageable);
        List<MallOrder> tbOrderList = new ArrayList<MallOrder>();

        if(page.hasContent()){
            tbOrderList = page.getContent();
        }

        PageOrderDto pageOrder = new PageOrderDto();
        List<OrderDto> orders = new ArrayList<>();

        for (MallOrder tbOrder : tbOrderList) {
            OrderDto order = new OrderDto();
            // 订单id
            order.setOrderId(tbOrder.getId());
            // 会员id
            order.setUserId(tbOrder.getUserId().toString());
            // 订单状态
            order.setOrderStatus(tbOrder.getStatus());
            // 创建时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String created = simpleDateFormat.format(tbOrder.getCreated());
            order.setCreateDate(created);
            // 订单支付时间
            if (tbOrder.getPaymentTime() != null) {
                String payDate = simpleDateFormat.format(tbOrder.getPaymentTime());
                order.setPayDate(payDate);
            }
            // 订单发货时间
            if (tbOrder.getConsignTime() != null) {
                String consignDate = simpleDateFormat.format(tbOrder.getConsignTime());
                order.setConsignDate(consignDate);
            }
            // 订单关闭时间
            if (tbOrder.getCloseTime() != null) {
                String closeDate = simpleDateFormat.format(tbOrder.getCloseTime());
                order.setCloseDate(closeDate);
            }
            // 订单完成时间
            if (tbOrder.getEndTime() != null && tbOrder.getStatus() == 4) {
                String endDate = simpleDateFormat.format(tbOrder.getEndTime());
                order.setFinishDate(endDate);
            }
            // 地址
            MallOrderShipping tbOrderShipping = orderShippingRepository.getOne(tbOrder.getId());
            MallAddress tbAddress = new MallAddress();
            tbAddress.setUserName(tbOrderShipping.getReceiverName());
            tbAddress.setStreetName(tbOrderShipping.getReceiverAddress());
            tbAddress.setTel(tbOrderShipping.getReceiverPhone());
            order.setTbAddress(tbAddress);
            // 订单总计
            order.setOrderTotal(tbOrder.getPayment());
            // 订单商品列表
            List<MallOrderItem> tbOrderItemList = orderItemRepository.getByOrderId(tbOrder.getId());
            List<CartProductDto> cartProductList = new ArrayList<>();
            for (MallOrderItem tbOrderItem : tbOrderItemList) {
                CartProductDto cartProduct = new CartProductDto();
                cartProduct.setProductId(Long.parseLong(tbOrderItem.getItemId()));
                cartProduct.setProductName(tbOrderItem.getTitle());
                cartProduct.setSalePrice(tbOrderItem.getPrice());
                cartProduct.setProductNum(tbOrderItem.getNum());
                cartProduct.setProductImg(tbOrderItem.getPicPath());
                cartProductList.add(cartProduct);
            }
            order.setGoodsList(cartProductList);
            orders.add(order);
        }

        pageOrder.setTotal(page.getTotalElements());
        pageOrder.setData(orders);

        return BaseResult.success(pageOrder);
    }

    /**
     * 确认收货
     *
     * @param order 订单
     * @return
     */
    @Transactional
    @Override
    public BaseResult confirmReceipt(MallOrder order) {

        // 请求信息为空
        if (order.getUserId()==null||StringUtils.isBlank(order.getId())) {
            return BaseResult.fail("操作异常");
        }

        // 确认收货，修改订单 status 字段
        orderRepository.updateOrderStatus(order.getId(),XuedenConstant.ORDER_STATUS_SUCCESS);
        return BaseResult.success("确认收货成功");
    }

    /**
     * 删除订单
     *
     * @param order 订单
     * @return
     */
    @Override
    public BaseResult deleteOrder(MallOrder order) {
        // 请求信息为空
        if (order.getUserId()==null || StringUtils.isBlank(order.getId())) {
            return BaseResult.fail("操作异常");
        }

        // 删除订单
        orderRepository.deleteById(order.getId());

        // 删除订单商品
        //根据订单Id获取商品列表
        List<MallOrderItem> orderItemList =  orderItemRepository.getByOrderId(order.getId());
        if (orderItemList!=null&&orderItemList.size()>0) {
            orderItemRepository.deleteInBatch(orderItemList);
        }

        // 删除订单物流信息
        orderShippingRepository.deleteById(order.getId());
        return BaseResult.success("删除订单成功");
    }
}
