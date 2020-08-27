package cn.xueden.mall.web.controller;


import cn.xueden.mall.web.service.ContentService;
import cn.xueden.mall.web.service.ItemCatService;
import cn.xueden.mall.web.service.ProductService;
import cn.xueden.mall.web.service.dto.BaseResult;
import cn.xueden.mall.web.service.dto.CategoryProductPageInfoDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**功能描述：商城商品控制器
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.controller
 * @version:1.0
 */
@RestController
@RequestMapping("goods")
@Api(tags = "商品服务接口")
public class GoodsController {

    private final ItemCatService itemCatService;

    private final ContentService contentService;

    private final ProductService productService;

    public GoodsController(ItemCatService itemCatService, ContentService contentService, ProductService productService) {
        this.itemCatService = itemCatService;
        this.contentService = contentService;
        this.productService = productService;
    }

    /**
     * 获取分类信息
     *
     * @return
     */

    @GetMapping("cateList")
    @ApiOperation(value = "获取分类列表")
    public BaseResult getCateList() {
        BaseResult baseResult = itemCatService.getCateList();
        return baseResult;
    }

    /**
     * 获取首页各板块内容
     *
     * @return
     */
    @GetMapping("home")
    @ApiOperation(value = "获取首页各板块内容")
    public BaseResult getProductHome() {
        BaseResult baseResult = contentService.getHome();
        return baseResult;
    }

    /**
     * 获取商品详情
     *
     * @param productId 商品 id
     * @return
     */
    @GetMapping("productDet")
    @ApiOperation(value = "获取商品详情")
    public BaseResult getProductDet(@RequestParam Long productId) {
        BaseResult baseResult = productService.getProductDet(productId);
        return baseResult;
    }

    /**
     * 分页商品查询
     *
     * @param categoryProductPageInfo
     * @return
     */
    @GetMapping("getCategoryGoods")
    @ApiOperation(value = "所有商品")
    public BaseResult getCategoryProducts(CategoryProductPageInfoDto categoryProductPageInfo) {
        BaseResult baseResult = productService.getByCategory(categoryProductPageInfo);
        return baseResult;
    }

    /**
     * 直接从数据库获取商品查询
     *
     * @param categoryProductPageInfo
     * @return
     */
    @GetMapping("getAllGoods")
    @ApiOperation(value = "从数据库获取所有商品")
    public BaseResult getAllGoods(CategoryProductPageInfoDto categoryProductPageInfo) {
        BaseResult baseResult = productService.getByCategory(categoryProductPageInfo);
        return baseResult;
    }

    /**
     * 快速搜索
     *
     * @param key 搜索关键字
     * @return
     */
    @GetMapping(value = "quickSearch")
    @ApiOperation("快速搜索")
    public BaseResult getQuickSearch(@RequestParam(defaultValue = "") String key) {
        BaseResult baseResult = productService.getQuickSearch(key);
        return BaseResult.success(baseResult);
    }
}
