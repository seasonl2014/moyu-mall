package cn.xueden.mall.repository;


import cn.xueden.mall.domain.MallItemCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**功能描述：商品类别持久层
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.repository
 * @version:1.0
 */
public interface MallItemCatRepository extends JpaRepository<MallItemCat, Long>, JpaSpecificationExecutor<MallItemCat> {

    /**
     * 根据 PID 查询
     * @param id pid
     * @return /
     */
    List<MallItemCat> findByPid(Long id);

    /**
     * 根据ID查询名称
     * @param id ID
     * @return /
     */
    @Query(value = "select name from mall_item_cat where id = ?1",nativeQuery = true)
    String findNameById(Long id);
}
