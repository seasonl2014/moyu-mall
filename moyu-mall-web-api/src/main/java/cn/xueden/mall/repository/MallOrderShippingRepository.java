package cn.xueden.mall.repository;




import cn.xueden.mall.common.jpa.domain.MallOrderShipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：订单物流持久层
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/5
 * @Description:cn.xueden.modules.mall.web.repository
 * @version:1.0
 */
public interface MallOrderShippingRepository extends JpaRepository<MallOrderShipping, String>, JpaSpecificationExecutor<MallOrderShipping> {
}
