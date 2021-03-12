package cn.xueden.mall.common.jpa.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**功能描述：板块内容实体
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.domain
 * @version:1.0
 */
@Entity
@Getter
@Setter
@Table(name="mall_panel_content")
public class MallPanelContent implements Serializable {

    /**
     * 板块内容 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(groups = Update.class)
    private Integer id;

    /**
     * 所属板块 id
     */
    @Column(name = "panel_id",nullable = false)
    @NotNull
    private Integer pid;

    /**
     * 板块内容类型 0 关联商品 1 其他链接
     */
    private Integer type;

    /**
     * 关联商品 id
     */
    private Long productId;

    /**
     * 排列序号
     */
    private Integer sortOrder;

    /**
     * 其它链接
     */
    private String fullUrl;

    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 3d 轮播图备用1
     */
    private String picUrl2;

    /**
     * 3d 轮播图备用2
     */
    private String picUrl3;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 更新时间
     */
    private Date updated;

    /**
     * 关联商品价格
     */
    private BigDecimal salePrice;

    /**
     * 关联商品名称
     */
    private String productName;

    /**
     * 关联商品标题
     */
    private String subTitle;

    public @interface Update {}
}
