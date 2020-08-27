package cn.xueden.mall.web.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**功能描述：板块内容数据传输对象
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.service.dto
 * @version:1.0
 */
@Data
public class TbPanelContentDto implements Serializable {

    /**
     * 板块内容 id
     */
    private Long id;

    /**
     * 板块内容类型，0 关联商品 1 其他链接
     */
    private Integer type;

    /**
     * 类型为其他链接的跳转地址
     */
    private String fullUrl;

    /**
     * 板块内容图片
     */
    private String picUrl;

    /**
     * 轮播图备用图片1
     */
    private String picUrl2;

    /**
     * 轮播图备用图片2
     */
    private String picUrl3;


    /**
     * 关联商品 id
     */
    private Long productId;

    /**
     * 关联商品售价
     */
    private BigDecimal salePrice;

    /**
     * 关联商品商品名称
     */
    private String productName;

    /**
     * 关联商品副标题
     */
    private String subTitle;

    /**
     * 商品限制购买数量
     */
    private int limit;
}
