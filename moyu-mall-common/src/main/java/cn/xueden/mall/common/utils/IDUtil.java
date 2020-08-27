package cn.xueden.mall.common.utils;

import java.util.Random;

/**功能描述：id 工具类
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/5
 * @Description:cn.xueden.common.utils
 * @version:1.0
 */
public class IDUtil {

    /**
     * 随机 id 生成
     *
     * @return
     */
    public static Long getRandomId() {
        Long millis = System.currentTimeMillis();
        // 加上两位随机数
        Random random = new Random();
        int end = random.nextInt(99);
        // 如果不足两位前面补 0
        String str = millis + String.format("%02d", end);
        Long id = new Long(str);
        return id;
    }
}
