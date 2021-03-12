package cn.xueden.mall.common.core.utils;

import cn.hutool.core.util.ObjectUtil;

import cn.xueden.mall.common.core.exception.BadRequestException;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

/**功能描述：验证工具
 * @Auther:http://www.xueden.cn
 * @Date:2020/3/23
 * @Description:cn.xueden.common.utils
 * @version:1.0
 */
public class ValidationUtil {
    /**
     * 验证空
     */
    public static void isNull(Object obj, String entity, String parameter , Object value){
        if(ObjectUtil.isNull(obj)){
            String msg = entity + " 不存在: "+ parameter +" is "+ value;
            throw new BadRequestException(msg);
        }
    }

    /**
     * 验证是否为邮箱
     */
    public static boolean isEmail(String email) {
        return new EmailValidator().isValid(email, null);
    }
}
