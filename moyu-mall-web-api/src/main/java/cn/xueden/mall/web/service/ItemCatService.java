package cn.xueden.mall.web.service;


import cn.xueden.mall.web.service.dto.BaseResult;

/**功能描述：商品分类接口
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.service
 * @version:1.0
 */
public interface ItemCatService {

    /**
     * 获取分类列表
     *
     * @return
     */
    BaseResult getCateList();
}
