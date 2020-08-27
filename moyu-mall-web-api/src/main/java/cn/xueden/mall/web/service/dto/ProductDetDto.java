package cn.xueden.mall.web.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**功能描述：商品详情DTO
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.web.service.dto
 * @version:1.0
 */
@Data
public class ProductDetDto implements Serializable {

    /**
     * 商品 id
     */
    private Long productId;

    /**
     * 商品售价
     */
    private BigDecimal salePrice;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品子标题
     */
    private String subTitle;

    /**
     * 商品限制数量
     */
    private Integer limitNum;

    /**
     * 商品库存数量
     */
    private Integer num;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 商品大图
     */
    private String productImageBig;

    /**
     * 商品小图集合
     */
    private String[] productImageSmall;
}
