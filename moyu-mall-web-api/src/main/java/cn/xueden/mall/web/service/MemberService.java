package cn.xueden.mall.web.service;


import cn.xueden.mall.domain.MallAddress;
import cn.xueden.mall.domain.MallMember;
import cn.xueden.mall.web.service.dto.BaseResult;
import cn.xueden.mall.web.service.dto.MemberDto;
import cn.xueden.mall.web.service.dto.MemberLoginDto;

import javax.servlet.http.HttpSession;

/**功能描述：会员服务接口
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.service
 * @version:1.0
 */
public interface MemberService {

    /**
     * 判断缓存中是否有登录信息
     *
     * @param token 令牌
     * @return
     */
    BaseResult getMemberByToken(String token);

    /**
     * 会员登录
     *
     * @param memberLogin 会员登录信息
     * @param session 会话信息
     * @return
     */
    BaseResult login(MemberLoginDto memberLogin, HttpSession session);

    /**
     * 获取会员地址列表
     *
     * @param userId 会员id
     * @return
     */
    BaseResult getAddressList(Long userId);

    /**
     * 新增会员地址
     *
     * @param tbAddress 会员地址
     * @return
     */
    BaseResult addAdderss(MallAddress tbAddress);

    /**
     * 修改会员地址
     *
     * @param tbAddress 会员地址
     * @return
     */
    BaseResult updateAddress(MallAddress tbAddress);

    /**
     * 删除会员地址
     *
     * @param id 会员地址 id
     * @return
     */
    BaseResult delAddress(Long id);



    /**
     * 修改会员昵称
     *
     * @param member 会员请求数据
     * @return
     */
    BaseResult updateUsername(MemberDto member);

    /**
     * 修改会员手机号
     *
     * @param member 会员请求数据
     * @return
     */
    BaseResult updatePhone(MemberDto member);



    /**
     * 会员修改密码
     *
     * @param member 会员信息
     * @return
     */
    BaseResult updatePass(MemberDto member);

    /**
     * 发送 email 邮件
     *
     * @param email 邮箱
     * @return
     */
    BaseResult sendEmailCode(String email);

    /**
     * 从 Redis 中退出登录
     *
     * @param token 会员本地保存的 token
     * @return
     */
    BaseResult logout(String token);

    /**
     * 验证手机号是否存在
     *
     * @param phone
     * @return
     */
    BaseResult checkPhone(String phone);

    /**
     * 会员注册
     *
     * @param tbMember 会员
     * @return
     */
    BaseResult register(MallMember tbMember);
}
