package cn.xueden.mall.web.service.mapper;


import cn.xueden.mall.common.jpa.base.BaseMapper;
import cn.xueden.mall.common.jpa.domain.MallPanel;
import cn.xueden.mall.web.service.dto.TbPanelDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**功能描述：板块实体映射dto
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.service.mapper
 * @version:1.0
 */
@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MallPanelMapper extends BaseMapper<TbPanelDto, MallPanel> {
}
