package cn.xueden.mall.web.service.dto;

import lombok.Data;

import java.io.Serializable;

/**功能描述：会员接受登录信息类
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.web.service.dto
 * @version:1.0
 */
@Data
public class MemberLoginDto implements Serializable {

    /**
     * 账号：手机号或邮箱
     */
    private String account;

    /**
     * 邮箱
     */
    private String password;

    /**
     * 是否自动登录，否 账号信息存 session， 是 账号信息存 Redis Session
     */
    private Boolean auto;
}
