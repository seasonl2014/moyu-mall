package cn.xueden.mall.web.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**订单支付接受请求
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/5
 * @Description:cn.xueden.modules.mall.web.service.dto
 * @version:1.0
 */
@Data
public class OrderPayDto implements Serializable {

    /**
     * 订单 id
     */
    private Long orderId;

    /**
     * 订单支付总金额
     */
    private BigDecimal orderTotal;

    /**
     * 订单支付状态
     */
    private String orderStatus;

    /**
     * 支付二维码
     */
    private String qrCode;

}
