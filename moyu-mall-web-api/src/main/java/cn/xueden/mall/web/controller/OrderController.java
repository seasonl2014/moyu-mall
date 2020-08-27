package cn.xueden.mall.web.controller;

import cn.xueden.mall.common.utils.XuedenConstant;
import cn.xueden.mall.domain.MallOrder;
import cn.xueden.mall.web.service.OrderService;
import cn.xueden.mall.web.service.dto.BaseResult;
import cn.xueden.mall.web.service.dto.MallOrderQueryCriteria;
import cn.xueden.mall.web.service.dto.OrderInfoDto;
import cn.xueden.mall.web.service.dto.OrderPayDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**功能描述：订单管理控制层
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/5
 * @Description:cn.xueden.modules.mall.web.controller
 * @version:1.0
 */
@RestController
@RequestMapping("order")
@Api(tags = "商城：订单服务接口")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 创建订单
     *
     * @param orderInfo 订单信息
     * @return
     */

    @PostMapping("addOrder")
    @ApiOperation(value = "创建订单")
    public BaseResult addOrder(@RequestBody OrderInfoDto orderInfo, HttpServletRequest request) {
        BaseResult baseResult = orderService.addOrder(orderInfo, request);
        return baseResult;
    }

    /**
     * 通过 id 获取订单
     *
     * @param orderId 订单 id
     * @return
     */
    @GetMapping("getOrderDet")
    @ApiOperation(value = "通过id获取订单详情")
    public BaseResult getOrderDet(@RequestParam String orderId) {
        BaseResult baseResult = orderService.getOrderDet(orderId);
        return baseResult;
    }

    /**
     * 订单支付
     *
     * @param orderPay
     * @return
     */
    @PostMapping("payment")
    @ApiOperation(value = "订单支付")
    public BaseResult payment(@RequestBody OrderPayDto orderPay) {
        BaseResult baseResult = orderService.payment(orderPay);
        return baseResult;
    }

    /**
     * 获取订单支付状态
     *
     * @param orderId 订单 id
     * @return
     */
    @GetMapping("getOrderStatus")
    @ApiOperation(value = "获取订单支付状态")
    public BaseResult getOrderStatus(@RequestParam String orderId) {
        BaseResult baseResult = orderService.getOrderStatus(orderId);
        return baseResult;
    }

    /**
     * 修改订单支付状态
     *
     * @param orderId 订单 id
     * @return
     */
    @GetMapping("updateOrderStatus")
    @ApiOperation(value = "修改订单支付状态")
    public BaseResult updateOrderStatus(@RequestParam String orderId) {
        orderService.updateOrderStatus(orderId,1);
        return null;
    }

    /**
     * 获取用户订单列表
     *
     * @return
     */
    @GetMapping("orderList")
    @ApiOperation(value = "获取用户所有订单")
    public BaseResult getOrderList(MallOrderQueryCriteria orderQueryCriteria,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "5") int size) {

        // 排序方式，这里是以“id”为标准进行降序
        Sort sort = Sort.by(Sort.Direction.DESC, "id");  // 这里的"id"是实体类的主键，记住一定要是实体类的属性，而不能是数据库的字段
        Pageable pageable = PageRequest.of(page-1,size,sort);
        BaseResult baseResult = orderService.getOrderList(orderQueryCriteria, pageable);
        return baseResult;
    }

    /**
     * 确认收货
     *
     * @param order 订单
     * @return
     */
    @PostMapping("confirmReceipt")
    @ApiOperation(value = "确认收货")
    public BaseResult confirmReceipt(@RequestBody MallOrder order) {
        BaseResult baseResult = orderService.confirmReceipt(order);
        return baseResult;
    }

    /**
     * 删除订单
     *
     * @param order 订单
     * @return
     */
    @PostMapping("deleteOrder")
    @ApiOperation(value = "删除订单")
    public BaseResult deleteOrder(@RequestBody MallOrder order) {
        BaseResult baseResult = orderService.deleteOrder(order);
        return baseResult;
    }

    /**
     * 订单支付回调
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("/notifyUrl")
    public String notifyUrl(HttpServletRequest  request) throws IOException {
        System.out.println("------------------------是否进入到回调方法");
        String code = request.getParameter("code");
        System.out.println("------------------------请求参数code：" + code);
        String outTradeNo = request.getParameter("outTradeNo");
        System.out.println("------------------------请求参数outTradeNo：" + outTradeNo);
        String money = request.getParameter("money");
        System.out.println("------------------------请求参数money：" + money);
        if(code.equals("1")){
            //修改订单为已支付
            orderService.updateOrderStatus(outTradeNo, XuedenConstant.ORDER_STATUS_PAY);
        }else{
            return "FAILURE";
        }
        return "SUCCESS";
    }
}
