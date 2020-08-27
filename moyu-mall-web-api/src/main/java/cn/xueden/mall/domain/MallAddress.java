package cn.xueden.mall.domain;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**功能描述：用户地址实体类
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.domain
 * @version:1.0
 */
@Entity
@Getter
@Setter
@Table(name="mall_address")
public class MallAddress implements Serializable {

    /**
     * 地址 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 电话
     */
    private String tel;

    /**
     * 省份
     */
    private String state;

    /**
     * 省份编号
     */
    private String stateCode;

    /**
     * 城市
     */
    private String city;

    /**
     * 城市编号
     */
    private String cityCode;

    /**
     * 区/县
     */
    private String district;

    /**
     * 区/县 编号
     */
    private String districtCode;

    /**
     * 街道地址
     */
    private String streetName;

    /**
     * 是否为默认地址
     */
    private Boolean isDefault;

    /**
     * 创建日期
     */
    @CreationTimestamp
    private Date created;

    /**
     * 更新日期
     */
    @UpdateTimestamp
    private Date updated;

    /**
     * 详细地址
     */
    private String detailsAddress;

    public @interface Update {}
}
