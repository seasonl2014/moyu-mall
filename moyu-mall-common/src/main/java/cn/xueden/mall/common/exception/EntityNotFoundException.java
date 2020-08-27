package cn.xueden.mall.common.exception;

import org.springframework.util.StringUtils;

/**功能描述：处理实体找不到异常
 * @Auther:http://www.xueden.cn
 * @Date:2020/3/22
 * @Description:cn.xueden.common.exception
 * @version:1.0
 */
public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(Class clazz, String field, String val) {
        super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), field, val));
    }

    private static String generateMessage(String entity, String field, String val) {
        return StringUtils.capitalize(entity)
                + " with " + field + " "+ val + " does not exist";
    }
}
