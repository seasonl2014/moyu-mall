package cn.xueden.mall.web.service.dto;



import cn.xueden.mall.common.jpa.annotation.EnableXuedenQuery;
import lombok.Data;

/**功能描述：查询订单条件
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/6
 * @Description:cn.xueden.modules.mall.web.service.dto
 * @version:1.0
 */
@Data
public class MallOrderQueryCriteria {

    /**
     * 根据用户id查询
     */
    @EnableXuedenQuery
    private Long userId;
}
