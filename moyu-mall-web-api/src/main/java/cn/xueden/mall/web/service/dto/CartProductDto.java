package cn.xueden.mall.web.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**功能描述：购物车商品DTO
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.web.service.dto
 * @version:1.0
 */
@Data
public class CartProductDto implements Serializable {

    /**
     * 商品 id
     */
    private Long productId;

    /**
     * 商品售价
     */
    private BigDecimal salePrice;

    /**
     * 商品数量
     */
    private int productNum;

    /**
     * 商品限制购买数量
     */
    private int limitNum;

    /**
     * 商品是否被选中
     */
    private String checked;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品图片地址
     */
    private String productImg;
}
