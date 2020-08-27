package cn.xueden.mall.repository;


import cn.xueden.mall.domain.MallItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**功能描述：商品持久层
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.repository
 * @version:1.0
 */
public interface MallItemRepository extends JpaRepository<MallItem, Long>, JpaSpecificationExecutor<MallItem> {

    /**
     * 商品减少库存
     *
     * @param productId 商品 id
     * @param num 商品数量
     * @return
     */
    @Modifying
    @Query(value = "update mall_item set num = num - ?2 where id=?1",nativeQuery = true)
    int reduceItemNum(Long productId, int num);

}
