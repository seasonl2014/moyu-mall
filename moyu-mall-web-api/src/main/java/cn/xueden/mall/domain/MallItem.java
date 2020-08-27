package cn.xueden.mall.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**功能描述：商品实体
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.domain
 * @version:1.0
 */
@Entity
@Getter
@Setter
@Table(name="mall_item")
public class MallItem implements Serializable {

    /**
     * 商品 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品卖点
     */
    private String sellPoint;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品库存
     */
    private Integer num;

    /**
     * 一次最多购买多少件
     */
    private Integer limitNum;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 商品分类 id
     */
    private Long cid;

    /**
     * 商品状态
     */
    private Integer status;

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

    /**
     * 获取图片地址数组
     *
     * @return
     */
    public String[] getImages() {
        if (!StringUtils.isBlank(this.image)) {
            String[] images = this.image.split(",");
            return images;
        }
        return null;
    }

    public void copy(MallItem source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
