package cn.xueden.mall.web.service;



import cn.xueden.mall.common.jpa.domain.MallOrder;
import cn.xueden.mall.web.service.dto.BaseResult;
import cn.xueden.mall.web.service.dto.MallOrderQueryCriteria;
import cn.xueden.mall.web.service.dto.OrderInfoDto;
import cn.xueden.mall.web.service.dto.OrderPayDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;

/**功能描述：订单接口
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/5
 * @Description:cn.xueden.modules.mall.web.service
 * @version:1.0
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * @param orderInfo 订单信息
     * @param request 请求 ip 验证
     * @return
     */
    BaseResult addOrder(OrderInfoDto orderInfo, HttpServletRequest request);

    /**
     * 根据订单 id 获取订单详情
     *
     * @param orderId 订单 id
     * @return
     */
    BaseResult getOrderDet(String orderId);

    /**
     * 订单支付
     *
     * @param orderPay 订单支付
     * @return
     */
    BaseResult payment(OrderPayDto orderPay);

    /**
     * 获取订单支付状态
     *
     * @param orderId 订单 id
     * @return
     */
    BaseResult getOrderStatus(String orderId);

    /**
     * 修改订单状态
     *
     * @param orderId 订单 id
     * @param orderStatus 订单状态
     */
    int updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 带条件分页获取订单列表
     *
     * @param criteria 查询条件
     * @param pageable 分页信息
     * @return
     */
    BaseResult getOrderList(MallOrderQueryCriteria criteria, Pageable pageable);

    /**
     * 确认收货
     *
     * @param order 订单
     * @return
     */
    BaseResult confirmReceipt(MallOrder order);

    /**
     * 删除订单
     *
     * @param order 订单
     * @return
     */
    BaseResult deleteOrder(MallOrder order);
}
