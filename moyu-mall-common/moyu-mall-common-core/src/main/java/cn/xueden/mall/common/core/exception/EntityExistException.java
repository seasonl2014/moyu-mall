package cn.xueden.mall.common.core.exception;

import org.springframework.util.StringUtils;

/**功能描述：实体存在异常
 * @Auther:http://www.xueden.cn
 * @Date:2020/3/22
 * @Description:cn.xueden.common.exception
 * @version:1.0
 */
public class EntityExistException extends RuntimeException{

    public EntityExistException(Class clazz, String field, String val) {
        super(EntityExistException.generateMessage(clazz.getSimpleName(), field, val));
    }

    private static String generateMessage(String entity, String field, String val) {
        return StringUtils.capitalize(entity)
                + " with " + field + " "+ val + " existed";
    }
}
