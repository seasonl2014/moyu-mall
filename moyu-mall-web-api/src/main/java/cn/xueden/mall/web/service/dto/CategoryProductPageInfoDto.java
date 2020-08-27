package cn.xueden.mall.web.service.dto;

import lombok.Data;

import java.io.Serializable;

/**功能描述：分类商品查询分页信息
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.web.service.dto
 * @version:1.0
 */
@Data
public class CategoryProductPageInfoDto implements Serializable {

    /**
     * 当前第几页
     */
    private Integer page;

    /**
     * 每页显示的个数
     */
    private Integer size;

    /**
     * 排序条件
     */
    private String sort;

    /**
     * 查询条件
     */
    private String key;

    /**
     * 分类id
     */
    private Long cid;

    /**
     * 最高价格
     */
    private Integer priceGt;

    /**
     * 最低价格
     */
    private Integer priceLte;
}
