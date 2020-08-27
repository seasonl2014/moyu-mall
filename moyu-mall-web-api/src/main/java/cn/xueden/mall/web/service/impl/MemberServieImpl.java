package cn.xueden.mall.web.service.impl;

import cn.xueden.mall.common.utils.*;
import cn.xueden.mall.domain.MallAddress;
import cn.xueden.mall.domain.MallMember;
import cn.xueden.mall.repository.MallAddressRepository;
import cn.xueden.mall.repository.MallMemberRepository;
import cn.xueden.mall.web.service.MemberService;
import cn.xueden.mall.web.service.dto.BaseResult;
import cn.xueden.mall.web.service.dto.MemberDto;
import cn.xueden.mall.web.service.dto.MemberLoginDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/**功能描述：会员业务接口实现
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = "MEMBER")
public class MemberServieImpl implements MemberService {

    private final RedisUtils redisUtils;

    private final MallMemberRepository mallMemberRepository;

    private final MallAddressRepository addressRepository;



    @Value("${mall.member.session.expire}")
    private Integer MEMBER_SESSION_EXPIRE;

    public MemberServieImpl(RedisUtils redisUtils, MallMemberRepository mallMemberRepository, MallAddressRepository addressRepository ) {
        this.redisUtils = redisUtils;
        this.mallMemberRepository = mallMemberRepository;
        this.addressRepository = addressRepository;

    }

    /**
     * 判断缓存中是否有登录信息
     * @param token 令牌
     * @return
     */
    @Override
    public BaseResult getMemberByToken(String token) {
        // 从 Redis 从获取登录信息
        //String json = (String) redisCacheManager.get("SESSION:" + token);
        String json = (String) redisUtils.get("SESSION:" + token);
        MallMember tbMember = null;
        try {
            tbMember = MapperUtil.json2pojo(json, MallMember.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 没有登录信息，已过期
        if (tbMember == null) {
            return BaseResult.fail("用户登录已过期！");
        }

        // 没有过期，再次登录，重新设置过期时间
        redisUtils.expire("SESSION:" + token, MEMBER_SESSION_EXPIRE);
        return BaseResult.success(tbMember);
    }

    /**
     * 会员登录
     * @param memberLogin 会员登录信息
     * @param session 会话信息
     * @return
     */
    @Override
    public BaseResult login(MemberLoginDto memberLogin, HttpSession session) {
        // 账号
        String account = memberLogin.getAccount();

        // 手机号正则表达式
        String phonePattern = "1[3|4|5|7|8][0-9]\\d{8}";

        // 邮箱正则表达式
        String emailPattern = "(\\w-*\\.*)+@(\\w-?)+(\\.\\w{2,})+";

        // 手机号登录
        if (Pattern.matches(phonePattern, account)) {
            MallMember tbMember = mallMemberRepository.findByPhone(account);
            return verLogin(memberLogin, tbMember, session);
        }

        // 邮箱登录
        else if (Pattern.matches(emailPattern, account)) {
            MallMember tbMember = mallMemberRepository.findByEmail(account);
            return verLogin(memberLogin, tbMember, session);
        }

        // 账号格式错误
        else {
            return BaseResult.fail("账号格式错误，只能为邮箱或手机号！");
        }
    }

    /**
     * 保存会员 session
     *
     * @param memberLogin 会员登录信息
     * @param tbMember 会员
     * @param session session
     * @return
     */
    private BaseResult verLogin(MemberLoginDto memberLogin, MallMember tbMember, HttpSession session) {

        // 获取 MD5 加密密码
        String md5Password = DigestUtils.md5DigestAsHex(memberLogin.getPassword().getBytes());

        if (tbMember != null && tbMember.getPassword().equals(md5Password)) {
            // 判断账户状态是否正常
            if (XuedenConstant.MEMBER_STATUS_BAN == (tbMember.getState())) {
                return BaseResult.fail("您的账号已被封禁，请联系管理员进行解封！");
            }

            // 自动登录，将登录信息保存在 Redis 中
            if (memberLogin.getAuto()) {
                try {
                    String token = UUID.randomUUID().toString();
                    tbMember.setToken(token);
                    // 将会员登录信息，保存到 Redis
                    redisUtils.set("SESSION:" + token, MapperUtil.obj2json(tbMember));
                    // 设置过期时间
                    redisUtils.expire("SESSION:" + token, MEMBER_SESSION_EXPIRE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 不自动登录，将登录信息保存在 HttpSession 中
            else {
                // 设置 token 为空
                tbMember.setToken("");
                session.setAttribute("memberLogin", tbMember);
            }

            return BaseResult.success(tbMember);
        }



        return BaseResult.fail("账号或密码错误！");
    }

    /**
     * 获取会员地址列表
     * @param userId 会员id
     * @return
     */
    @Override
    @Cacheable
    public BaseResult getAddressList(Long userId) {
        List<MallAddress> tbAddressList = addressRepository.findByUserId(userId);
        // 拼接详细地址
        for (MallAddress tbAddress : tbAddressList) {
            String state = tbAddress.getState();
            String city = tbAddress.getCity();
            String district = tbAddress.getDistrict();
            String streetName = tbAddress.getStreetName();
            StringBuffer detailsAddress = new StringBuffer();
            detailsAddress.append(state).append(" ").append(city).append(" ").append(district)
                    .append(" ").append(streetName);
            tbAddress.setDetailsAddress(detailsAddress.toString());
        }
        return BaseResult.success(tbAddressList);
    }

    /**
     * 新增会员收货地址
     * @param address 会员地址
     * @return
     */
    @Transactional
    @Override
    @CacheEvict(allEntries = true)
    public BaseResult addAdderss(MallAddress address) {
        if (address.getIsDefault()) {
            addressRepository.removeDefault(address.getUserId());
        }
        addressRepository.save(address);
        return BaseResult.success("会员增加地址");
    }

    @Transactional
    @Override
    @CacheEvict(allEntries = true)
    public BaseResult updateAddress(MallAddress resources) {
        MallAddress tbAddress = addressRepository.findById(resources.getId()).orElseGet(MallAddress::new);
        if (resources.getIsDefault()) {
            addressRepository.removeDefault(resources.getUserId());
        }
        ValidationUtil.isNull( tbAddress.getId(),"MallAddress","id",resources.getId());
        resources.setId(tbAddress.getId());
        addressRepository.save(resources);
        return BaseResult.success("会员修改地址成功");
    }

    /**
     * 删除会员地址
     * @param id 会员地址 id
     * @return
     */
    @Transactional
    @Override
    @CacheEvict(allEntries = true)
    public BaseResult delAddress(Long id) {
        addressRepository.deleteById(id);
        return BaseResult.success("会员删除地址成功");
    }



    /**
     * 修改会员昵称
     * @param member 会员请求数据
     * @return
     */
    @Transactional
    @Override
    public BaseResult updateUsername(MemberDto member) {

        // 会员 id
        Long userId = member.getUserId();

        // Redis key
        String token = member.getToken();

        // 用户名
        String username = member.getUsername();

        // 获取会员
        MallMember tbMember = mallMemberRepository.getOne(userId);
        if (tbMember == null) {
            return BaseResult.fail("获取用户信息失败");
        }

        // 更新数据库数据
        tbMember.setUsername(username);
        mallMemberRepository.save(tbMember);

        // 更新 Redis 缓存
        // Redis key
        String redisKey = "SESSION:" + token;
        String memberJson = null;
        try {
            memberJson = MapperUtil.obj2json(tbMember);
        } catch (Exception e) {
            e.printStackTrace();
        }
        redisUtils.set(redisKey, memberJson);

        return BaseResult.success(tbMember);
    }

    /**
     * 修改会员手机号
     * @param member
     * @return
     */
    @Transactional
    @Override
    public BaseResult updatePhone(MemberDto member) {

        // 会员 id
        Long userId = member.getUserId();

        // Redis key
        String token = member.getToken();

        // 手机号
        String phone = member.getPhone();

        // 获取会员
        MallMember tbMember = mallMemberRepository.getOne(userId);
        if (tbMember == null) {
            return BaseResult.fail("获取用户信息失败");
        }

        // 更新数据库数据
        tbMember.setPhone(phone);
        mallMemberRepository.save(tbMember);

        // 更新 Redis 缓存
        // Redis key
        String redisKey = "SESSION:" + token;
        String memberJson = null;
        try {
            memberJson = MapperUtil.obj2json(tbMember);
        } catch (Exception e) {
            e.printStackTrace();
        }
        redisUtils.set(redisKey, memberJson);

        return BaseResult.success(tbMember);
    }



    /**
     * 会员修改密码
     * @param member 会员信息
     * @return
     */
    @Transactional
    @Override
    public BaseResult updatePass(MemberDto member) {

        // 会员 id
        Long userId = member.getUserId();

        // Redis key
        String token = member.getToken();

        if(StringUtils.isBlank(token)){
            return BaseResult.fail("修改失败，token不存在");
        }

        // 密码
        String password = member.getPassword();
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 获取会员
        MallMember tbMember = mallMemberRepository.getOne(userId);
        if (tbMember == null) {
            return BaseResult.fail("获取用户信息失败");
        }

        // 更新数据库数据
        tbMember.setPassword(md5Password);
        mallMemberRepository.save(tbMember);

        // 更新 Redis 缓存
        // Redis key
        String redisKey = "SESSION:" + token;
        String memberJson = null;
        try {
            memberJson = MapperUtil.obj2jsonIgnoreNull(tbMember);
            if(memberJson!=null){
                redisUtils.set(redisKey, memberJson);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return BaseResult.success(tbMember);
    }

    @Override
    public BaseResult sendEmailCode(String email) {
        String code = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
        /*EmailCode emailCode = new EmailCode(email, code);
        emailUtil.sendTemplateEmail(email, "【xuedenMALL商城】修改邮箱", "update-email", emailCode);*/
        return BaseResult.success((Object)code);
    }

    /**
     * 从 Redis 中退出登录
     *
     * @param token 会员本地保存的 token
     * @return
     */
    @Override
    public BaseResult logout(String token) {
        String redisKey = "SESSION:" + token;
      boolean flag = redisUtils.del2(redisKey);
        if (!flag) {
            return BaseResult.fail("退出登录失败！");
        }
        return BaseResult.success("退出登录成功");
    }

    /**
     * 验证手机号是否存在
     *
     * @param phone
     * @return
     */
    @Override
    public BaseResult checkPhone(String phone) {
        MallMember tbMember = mallMemberRepository.findByPhone(phone);

        // 手机号已存在
        if (tbMember != null) {
            return BaseResult.success(true);
        }

        return BaseResult.success(false);
    }

    /**
     * 会员注册
     *
     * @param tbMember 会员
     * @return
     */
    @Transactional
    @Override
    public BaseResult register(MallMember tbMember) {
        tbMember.setPassword(DigestUtils.md5DigestAsHex(tbMember.getPassword().getBytes()));

        tbMember.setState(XuedenConstant.MEMBER_STATUS_NORMAL);
        tbMember.setFile(XuedenConstant.MEMBER_HEAD_PORTRAIT);
        mallMemberRepository.save(tbMember);
        return BaseResult.success();
    }

}
