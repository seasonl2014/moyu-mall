package cn.xueden.mall.common.jpa.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**功能描述：快递实体类
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/22
 * @Description:cn.xueden.modules.mall.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name="mall_express")
public class MallExpress implements Serializable {

    /** 快递编号 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /** 快递名称 */
    @Column(name = "express_name")
    private String expressName;

    /** 排序值 */
    @Column(name = "sort_order")
    private Integer sortOrder;

    /** 创建时间 */
    @Column(name = "created")
    @CreationTimestamp
    private Timestamp created;

    /** 更新时间 */
    @Column(name = "updated")
    @UpdateTimestamp
    private Timestamp updated;

    public void copy(MallExpress source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}