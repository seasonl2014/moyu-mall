package cn.xueden.mall.web.service.mapper;


import cn.xueden.mall.common.jpa.base.BaseMapper;
import cn.xueden.mall.common.jpa.domain.MallPanelContent;
import cn.xueden.mall.web.service.dto.TbPanelContentDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**功能描述：板块内容实体映射到dto
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.service.mapper
 * @version:1.0
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MallPanelContentMapper extends BaseMapper<TbPanelContentDto, MallPanelContent> {
}
