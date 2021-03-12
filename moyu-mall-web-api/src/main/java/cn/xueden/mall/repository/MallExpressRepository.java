package cn.xueden.mall.repository;



import cn.xueden.mall.common.jpa.domain.MallExpress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：快递持久层
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/22
 * @Description:cn.xueden.modules.mall.admin.repository
 * @version:1.0
 */
public interface MallExpressRepository extends JpaRepository<MallExpress, Integer>, JpaSpecificationExecutor<MallExpress> {
}