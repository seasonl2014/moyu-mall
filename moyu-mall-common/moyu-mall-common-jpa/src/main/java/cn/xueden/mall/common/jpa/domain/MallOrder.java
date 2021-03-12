package cn.xueden.mall.common.jpa.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**功能描述：订单实体类
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/5
 * @Description:cn.xueden.modules.mall.domain
 * @version:1.0
 */
@Entity
@Getter
@Setter
@Table(name="mall_order")
public class MallOrder implements Serializable {
    /**
     * 订单 id
     */
    @Id
    @Column(name = "id")
    @NotNull(groups = Update.class)
    private String id;

    /**
     * 实付金额
     */
    private BigDecimal payment;

    /**
     * 支付类型 1 在线支付 2 货到付款
     */
    private Integer paymentType;

    /**
     * 邮费
     */
    private BigDecimal postFee;

    /**
     * 0 未付款
     * 1 已付款
     * 2 未发货
     * 3 已发货
     * 4 交易成功
     * 5 交易关闭
     * 6 交易失败
     */
    private Integer status;

    /**
     * 付款时间
     */
    private Date paymentTime;

    /**
     * 发货时间
     */
    private Date consignTime;

    /**
     * 交易完成时间
     */
    private Date endTime;

    /**
     * 交易关闭时间
     */
    private Date closeTime;

    /**
     * 物流名称
     */
    private String shippingName;

    /**
     * 物流单号
     */
    private String shippingCode;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 买家留言
     */
    private String buyerMessage;

    /**
     * 买家昵称
     */
    private String buyerNick;

    /**
     * 买家是否已经评论
     */
    private Boolean buyerComment;

    /**
     * 创建时间
     */
    @CreationTimestamp
    private Date created;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    private Date updated;

    public @interface Update {}

    public void copy(MallOrder source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
