package cn.xueden.mall.repository;


import cn.xueden.mall.domain.MallPanelContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**功能描述：板块内容持久层
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.repository
 * @version:1.0
 */
public interface MallPanelContentRepository extends JpaRepository<MallPanelContent, Long>, JpaSpecificationExecutor<MallPanelContent> {

    /**
     * 根据 PID 查询
     * @param id pid
     * @return /
     */
    List<MallPanelContent> findByPid(Integer id);
}
