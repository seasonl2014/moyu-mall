package cn.xueden.mall.web.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**订单分页信息
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/5
 * @Description:cn.xueden.modules.mall.web.service.dto
 * @version:1.0
 */
@Data
public class PageOrderDto implements Serializable {

    /**
     * 总条数
     */
    private long total;

    /**
     * 分页数据
     */
    private List<OrderDto> data;
}
