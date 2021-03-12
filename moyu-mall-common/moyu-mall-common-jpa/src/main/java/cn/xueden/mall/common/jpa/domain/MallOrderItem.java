package cn.xueden.mall.common.jpa.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**功能描述：订单详情实体类
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/5
 * @Description:cn.xueden.modules.mall.domain
 * @version:1.0
 */
@Entity
@Getter
@Setter
@Table(name="mall_order_item")
public class MallOrderItem implements Serializable {

    /**
     * 订单项 id
     */
    @Id
    @Column(name = "id")
    @NotNull(groups = Update.class)
    private String id;

    /**
     * 商品 id
     */
    private String itemId;

    /**
     * 订单 id
     */
    private String orderId;

    /**
     * 商品购买数量
     */
    private Integer num;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品单价
     */
    private BigDecimal price;

    /**
     * 商品总金额
     */
    private BigDecimal totalFee;

    /**
     * 商品图片地址
     */
    private String picPath;

    /**
     * 本周卖出总数
     */
    private Integer total;

    public @interface Update {}
}
