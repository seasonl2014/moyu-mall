package cn.xueden.mall.web.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**功能描述：接受订单信息
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/5
 * @Description:cn.xueden.modules.mall.web.service.dto
 * @version:1.0
 */
@Data
public class OrderInfoDto implements Serializable {

    /**
     * 会员 id
     */
    private Long userId;

    /**
     * 地址 id
     */
    private Long addressId;

    /**
     * 电话
     */
    private String tel;

    /**
     * 收货人
     */
    private String userName;

    /**
     * 详细地址
     */
    private String streetName;

    /**
     * 订单总计
     */
    private BigDecimal orderTotal;

    /**
     * 订单商品
     */
    private List<CartProductDto> goodsList;
}
