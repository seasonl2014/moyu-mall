package cn.xueden.mall.repository;



import cn.xueden.mall.common.jpa.domain.MallPanel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**功能描述:板块持久层
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.repository
 * @version:1.0
 */
public interface MallPanelRepository extends JpaRepository<MallPanel, Long>, JpaSpecificationExecutor<MallPanel> {

    /**
     * 获取板块
     * @return
     */
    @Query(value = "select id, name, type, position from mall_panel order by sort_order",nativeQuery = true)
    List<MallPanel> findAllPanel();
}
