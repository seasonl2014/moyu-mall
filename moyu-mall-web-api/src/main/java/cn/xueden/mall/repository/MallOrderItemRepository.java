package cn.xueden.mall.repository;



import cn.xueden.mall.common.jpa.domain.MallOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**功能描述：订单详情持久层
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/5
 * @Description:cn.xueden.modules.mall.web.repository
 * @version:1.0
 */
public interface MallOrderItemRepository extends JpaRepository<MallOrderItem, Long>, JpaSpecificationExecutor<MallOrderItem> {

    /**
     * 根据订单号获取订单项
     * @param orderId
     * @return
     */
    List<MallOrderItem> getByOrderId(String orderId);

    /**
     * 查询该商品订单数量
     *
     * @param itemId 商品 id
     * @return
     */
    @Query(value = "select count(*) from mall_order_item where item_id = ?1",nativeQuery = true)
    int selectOrderNumByItemId(Long itemId);
}
