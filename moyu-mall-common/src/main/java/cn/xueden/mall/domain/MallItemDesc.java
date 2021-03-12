package cn.xueden.mall.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**功能描述：商品详情实体类
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.domain
 * @version:1.0
 */
@Entity
@Getter
@Setter
@Table(name="mall_item_desc")
public class MallItemDesc implements Serializable {

    /**
     * 商品 id
     */
    @Id
    @Column(name = "item_id")
    @NotNull(groups = Update.class)
    private Long itemId;

    /**
     * 商品描述信息
     */
    private String itemDesc;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 更新时间
     */
    private Date updated;

    public @interface Update {}

}
