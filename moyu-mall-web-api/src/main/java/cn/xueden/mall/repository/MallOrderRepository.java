package cn.xueden.mall.repository;


import cn.xueden.mall.domain.MallOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**功能描述：订单持久层
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/5
 * @Description:cn.xueden.modules.mall.web.repository
 * @version:1.0
 */
public interface MallOrderRepository extends JpaRepository<MallOrder, String>, JpaSpecificationExecutor<MallOrder> {

    /**
     *
     * @param orderId 订单 id
     * @param orderStatus 订单状态
     * @return
     */
    @Modifying
    @Query(value = "update mall_order set status = ?2 where id= ?1",nativeQuery = true)
    int updateOrderStatus(String orderId, Integer orderStatus);
}
