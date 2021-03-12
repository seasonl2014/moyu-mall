package cn.xueden.mall.web.service.dto;



import cn.xueden.mall.common.jpa.domain.MallESItem;
import lombok.Data;

import java.util.List;

/**功能描述：分类商品查询结果
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.web.service.dto
 * @version:1.0
 */
@Data
public class CateProductsResultDto {

    /**
     * 查询总条数
     */
    private long total;

    /**
     * 商品集合
     */
    private List<MallESItem> data;
}
