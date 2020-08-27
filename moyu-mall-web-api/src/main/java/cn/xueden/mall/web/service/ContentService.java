package cn.xueden.mall.web.service;


import cn.xueden.mall.web.service.dto.BaseResult;

/**功能描述：获取首页各板块内容接口
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.service
 * @version:1.0
 */
public interface ContentService {

    /**
     * 获取首页板块内容
     *
     * @return
     */
    BaseResult getHome();
}
