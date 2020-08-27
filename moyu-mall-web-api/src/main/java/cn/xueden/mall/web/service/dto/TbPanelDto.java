package cn.xueden.mall.web.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**功能描述：首页板块展示对象
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.service.dto
 * @version:1.0
 */
@Data
public class TbPanelDto implements Serializable {

    /**
     * 板块 id
     */
    private Integer id;

    /**
     * 板块名称
     */
    private String name;

    /**
     * 板块类型 0 轮播图 1 板块种类一 2 板块种类二 3 板块种类三
     */
    private Integer type;

    /**
     * 板块内容集合
     */
    private List<TbPanelContentDto> panelContentDtos;
}
