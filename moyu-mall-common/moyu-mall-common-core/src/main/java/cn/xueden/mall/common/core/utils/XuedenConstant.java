package cn.xueden.mall.common.core.utils;

/**功能描述：常用静态常量
 * @Auther:http://www.xueden.cn
 * @Date:2020/3/23
 * @Description:cn.xueden.common.utils
 * @version:1.0
 */
public class XuedenConstant {

    //YunGouOS平台支付宝商户号
    public static final String YUNGOUOSALIPAYMCHID = "111";

    //YunGouOS平台支付结束后返回地址
    public static final String YUNGOUORETURNURL="http://vueboot.xueden.cn/order/notifyUrl";

    //YunGouOS平台密钥
    public static final String YUNGOUOSKEY = "111";

    public static final String RESET_PASS = "重置密码";

    public static final String RESET_MAIL = "重置邮箱";

    public static final String SEND_MAIL_TITLE = "moyu-mall 墨鱼商城";

    /**
     * ###### 订单状态 0 未支付，1 已支付，2 未发货，3 已发货，4 交易成功，5 交易关闭
     */

    /**
     * 未支付
     */
    public static final Integer ORDER_STATUS_NOPAY = 0;

    /**
     * 已支付
     */
    public static final Integer ORDER_STATUS_PAY = 1;

    /**
     * 未发货
     */
    public static final Integer ORDER_STATUS_NOSEND = 2;

    /**
     * 已发货
     */
    public static final Integer ORDER_STATUS_SEND = 3;

    /**
     * 交易成功
     */
    public static final Integer ORDER_STATUS_SUCCESS = 4;

    /**
     * 交易关闭
     */
    public static final Integer ORDER_STATUS_CLOSE = 5;

    /**
     * ####### 会员状态 1 正常，2 封禁
     */

    /**
     * 会员状态正常
     */
    public static final Integer MEMBER_STATUS_NORMAL = 1;

    /**
     * 会员默认登录密码
     */
    public static final String MEMBER_LONG_PASSWORD = "123456";

    /**
     * 会员默认头像
     */
    public static final String MEMBER_HEAD_PORTRAIT = "http://java.goodym.cn/touxiang.png";

    /**
     * 会员状态封禁
     */
    public static final Integer MEMBER_STATUS_BAN = 2;

    /**
     * 用于IP定位转换
     */
    public static final String REGION = "内网IP|内网IP";

    /**
     * 常用接口
     */
    public static class Url{
        public static final String SM_MS_URL = "https://sm.ms/api";
    }

}
