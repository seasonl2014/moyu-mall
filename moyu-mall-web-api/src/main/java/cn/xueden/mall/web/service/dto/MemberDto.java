package cn.xueden.mall.web.service.dto;

import lombok.Data;

import java.io.Serializable;

/**接收会员操作请求
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/6
 * @Description:cn.xueden.modules.mall.web.service.dto
 * @version:1.0
 */
@Data
public class MemberDto implements Serializable {

    /**
     * 会员 id
     */
    private Long userId;

    /**
     * 会员缓存 token
     */
    private String token;

    /**
     * 会员头像 base64
     */
    private String imgData;

    /**
     * 会员名称
     */
    private String username;

    /**
     * 会员手机号
     */
    private String phone;

    /**
     * 会员密码
     */
    private String password;

    /**
     * 会员邮箱
     */
    private String email;


}
