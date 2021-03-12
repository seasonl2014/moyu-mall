package cn.xueden.mall.common.jpa.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**功能描述：商品类目实体类
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.domain
 * @version:1.0
 */
@Entity
@Getter
@Setter
@Table(name="mall_item_cat")
public class MallItemCat implements Serializable {

    /**
     * 类目 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 父分类 ID = 0 代表一级根分类
     */
    @Column(name = "parent_id",nullable = false)
    @NotNull
    private Long pid;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类状态
     */
    private Integer status;

    /**
     * 排列序号
     */
    private Integer sortOrder;

    /**
     * 是否为父分类 1 为 true 0 为 false
     */
    private Boolean isParent;

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

    public void copy(MallItemCat source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
