package cn.xueden.mall.repository;




import cn.xueden.mall.common.jpa.domain.MallItemDesc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.web.repository
 * @version:1.0
 */
public interface MallItemDescRepository extends JpaRepository<MallItemDesc, Long>, JpaSpecificationExecutor<MallItemDesc> {
}
