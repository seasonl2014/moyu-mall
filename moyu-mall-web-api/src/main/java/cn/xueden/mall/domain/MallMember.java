package cn.xueden.mall.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**功能描述：会员实体类
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.domain
 * @version:1.0
 */
@Data
@Entity
@Table(name = "mall_member")
@JsonIgnoreProperties(value={"hibernateLazyInitializer"})
public class MallMember implements Serializable {

    /**
     * 会员 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 会员用户名
     */
    private String username;

    /**
     * 会员密码
     */
    private String password;

    /**
     * 会员手机号
     */
    private String phone;

    /**
     * 会员邮箱
     */
    private String email;

    /**
     * 会员性别
     */
    private String sex;

    /**
     * 会员状态
     */
    private Integer state;

    /**
     * 会员头像
     */
    private String file;

    /**
     * 会员描述
     */
    private String description;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created")
    private Date createTime;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    private Date updated;

    /**
     * 前台登录 Token
     */
    private String token;

    public @interface Update {}

    public void copy(MallMember source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
