package cn.xueden.mall.repository;


import cn.xueden.mall.domain.MallAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**功能描述：会员地址持久层
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.web.repository
 * @version:1.0
 */
public interface MallAddressRepository extends JpaRepository<MallAddress, Long>, JpaSpecificationExecutor<MallAddress> {

    /**
     * 获取会员地址列表
     *
     * @param userId 会员 id
     * @return
     */
    List<MallAddress> findByUserId(Long userId);

    /**
     * 移除默认地址
     *
     * @param userId 用户地址
     */
    @Modifying
    @Query(value = "update mall_address set is_default=false  where user_id=?1",nativeQuery = true)
    int removeDefault(Long userId);
}
