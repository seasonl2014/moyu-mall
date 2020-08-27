package cn.xueden.mall.web.controller;


import cn.xueden.mall.domain.MallAddress;
import cn.xueden.mall.domain.MallMember;
import cn.xueden.mall.web.service.MemberService;
import cn.xueden.mall.web.service.dto.BaseResult;
import cn.xueden.mall.web.service.dto.MemberDto;
import cn.xueden.mall.web.service.dto.MemberLoginDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**功能描述：商城前台会员控制器
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.controller
 * @version:1.0
 */
@RestController
@Api(tags = "会员服务接口")
@RequestMapping("member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 商城会员注册
     *
     * @param tbMember 会员
     * @return
     */

    @PostMapping("register")
    @ApiOperation(value = "会员注册")
    public BaseResult register(@RequestBody MallMember tbMember) {
        BaseResult baseResult = memberService.register(tbMember);
        return baseResult;
    }

    /**
     * 验证会员是否登录
     *
     * @param token 会员本地 token
     * @return
     */
    @GetMapping("checkLogin")
    @ApiOperation(value = "验证用户是否登录")
    public BaseResult checkLogin(@RequestParam(defaultValue = "") String token, HttpSession session) {
        // 会员信息存在 session 中
        if ("".equals(token)) {
            // 判判断 HttpSession 中是否有登录信息
            MallMember tbMember = (MallMember) session.getAttribute("memberLogin");
            if (tbMember != null) {
                // 用户未选择自动登录，session 中有登录信息
                return BaseResult.success(tbMember);
            } else {
                return BaseResult.fail("用户未登录！");
            }
        }
        // 会员信息存在 Redis 中
        BaseResult baseResult = memberService.getMemberByToken(token);
        return baseResult;
    }





    /**
     * 会员登录
     *
     * @param memberLogin 会员登录信息
     * @return
     */
    @PostMapping("login")
    @ApiOperation(value = "用户登录")
    public BaseResult login(@RequestBody MemberLoginDto memberLogin, HttpSession session) {
        BaseResult baseResult = memberService.login(memberLogin, session);
        return baseResult;
    }

    /**
     * 获取会员地址列表
     *
     * @param userId 会员 id
     * @return
     */
    @GetMapping("addressList")
    @ApiOperation(value = "获取会员地址列表")
    public BaseResult getAddressList(@RequestParam Long userId) {
        BaseResult baseResult = memberService.getAddressList(userId);
        return baseResult;
    }

    /**
     * 新增会员地址
     *
     * @param address 会员地址
     * @return
     */
    @PostMapping("addressAdd")
    @ApiOperation(value = "新增会员地址")
    public BaseResult addressAdd(@RequestBody MallAddress address) {
        BaseResult baseResult = memberService.addAdderss(address);
        return baseResult;
    }

    /**
     * 修改会员收货地址
     *
     * @param address 会员地址
     * @return
     */
    @PostMapping("addressUpdate")
    @ApiOperation(value = "修改会员地址")
    public BaseResult addressUpdate(@RequestBody MallAddress address) {
        BaseResult baseResult = memberService.updateAddress(address);
        return baseResult;
    }

    /**
     * 删除会员地址
     *
     * @param tbAddress 会员地址
     * @return
     */
    @PostMapping("addressDel")
    @ApiOperation(value = "删除会员地址")
    public BaseResult addressDel(@RequestBody MallAddress tbAddress) {
        BaseResult baseResult = memberService.delAddress(tbAddress.getId());
        return baseResult;
    }


    /**
     * 修改会员昵称
     *
     * @param member 会员数据接收对象
     * @return
     */
    @PostMapping("updateUsername")
    @ApiOperation(value = "修改会员昵称")
    public BaseResult updateUsername(@RequestBody MemberDto member) {
        BaseResult baseResult = memberService.updateUsername(member);
        return baseResult;
    }

    /**
     * 修改会员手机号
     *
     * @param member 会员数据接收对象
     * @return
     */
    @PostMapping("updatePhone")
    @ApiOperation(value = "修改会员手机号")
    public BaseResult updatePhone(@RequestBody MemberDto member) {
        BaseResult baseResult = memberService.updatePhone(member);
        return baseResult;
    }




    /**
     * 修改密码
     *
     * @param member 会员数据接收对象
     * @return
     */
    @PostMapping("updatePass")
    @ApiOperation(value = "修改密码")
    public BaseResult updatePassword(@RequestBody MemberDto member) {
        BaseResult baseResult = memberService.updatePass(member);
        return baseResult;
    }

    /**
     * 修改邮箱发送验证码
     *
     * @param email 邮箱
     * @return
     */
    @GetMapping("sendEmailCode")
    @ApiOperation(value = "修改邮箱发送验证码")
    public BaseResult sendEmailCode(@RequestParam String email) {
        BaseResult baseResult = memberService.sendEmailCode(email);
        return baseResult;
    }

    /**
     * 退出登录
     *
     * @param token 会员本地的 token
     * @return
     */
    @GetMapping("logout")
    @ApiOperation(value = "退出商城")
    public BaseResult logout(@RequestParam(defaultValue = "") String token, HttpSession session) {
        // 从 session 中退出登录
        if ("".equals(token)) {
            session.removeAttribute("memberLogin");
            return BaseResult.success("退出登录成功!");
        }
        // 从 Redis 中退出登录
        BaseResult baseResult = memberService.logout(token);
        return baseResult;
    }

    /**
     * 验证手机号是否存在
     *
     * @param phone 手机号
     * @return
     */
    @GetMapping("checkphone/{phone}")
    @ApiOperation(value = "验证手机号是存在")
    public BaseResult checkPhone(@PathVariable String phone) {
        BaseResult baseResult = memberService.checkPhone(phone);
        return baseResult;
    }


}
