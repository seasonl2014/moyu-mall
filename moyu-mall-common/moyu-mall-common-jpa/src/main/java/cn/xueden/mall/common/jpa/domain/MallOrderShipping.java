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
import java.util.Date;

/**功能描述：订单物流信息实体
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/5
 * @Description:cn.xueden.modules.mall.domain
 * @version:1.0
 */
@Entity
@Getter
@Setter
@Table(name="mall_order_shipping")
public class MallOrderShipping implements Serializable {

    /**
     * 订单物流 id
     */
    @Id
    @Column(name = "order_id")
    @NotNull(groups = Update.class)
    private String orderId;

    /**
     * 收货人全名
     */
    private String receiverName;

    /**
     * 固定电话
     */
    private String receiverPhone;

    /**
     * 移动电话
     */
    private String receiverMobile;

    /**
     * 省份
     */
    private String receiverProvince;

    /**
     * 城市
     */
    private String receiverCity;

    /**
     * 区/县
     */
    private String receiverDistrict;

    /**
     * 收获地址
     */
    private String receiverAddress;

    /**
     * 邮政编码
     */
    private String receiverZip;

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

    public void copy(MallOrderShipping source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
