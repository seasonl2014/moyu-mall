package cn.xueden.mall.web.service.dto;

import lombok.Data;

import java.io.Serializable;

/**购物车dto
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.web.service.dto
 * @version:1.0
 */
@Data
public class CartDto implements Serializable {

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 商品 id
     */
    private Long productId;

    /**
     * 是否选中
     */
    private String checked;

    /**
     * 商品数量
     */
    private int productNum;
}
