package cn.xueden.mall.web.service.dto;


import cn.xueden.mall.domain.MallAddress;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**订单详情
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/5
 * @Description:cn.xueden.modules.mall.web.service.dto
 * @version:1.0
 */
@Data
public class OrderDto implements Serializable {

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 订单所属会员 id
     */
    private String userId;

    /**
     * 订单总计
     */
    private BigDecimal orderTotal;

    /**
     * 订单地址信息
     */
    private MallAddress tbAddress;

    /**
     * 订单商品集合
     */
    private List<CartProductDto> goodsList;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 订单创建时间
     */
    private String createDate;

    /**
     * 订单发货时间
     */
    private String consignDate;

    /**
     * 订单关闭时间
     */
    private String closeDate;

    /**
     * 订单完成时间
     */
    private String finishDate;

    /**
     * 订单支付时间
     */
    private String payDate;

    /**
     * 最晚支付时间
     */
    private String countTime;

    /**
     * 订单物流名称
     */
    private String shippingName;

    /**
     * 订单物流单号
     */
    private String shippingCode;
}
