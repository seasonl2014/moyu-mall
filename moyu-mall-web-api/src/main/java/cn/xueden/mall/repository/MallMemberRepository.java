package cn.xueden.mall.repository;



import cn.xueden.mall.domain.MallMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.web.repository
 * @version:1.0
 */
public interface MallMemberRepository extends JpaRepository<MallMember, Long>, JpaSpecificationExecutor<MallMember> {

    /**
     * 根据手机号查询
     * @param phone 手机号
     * @return /
     */
    MallMember findByPhone(String phone);

    /**
     * 根据手机号查询
     * @param email 邮箱
     * @return /
     */
    MallMember findByEmail(String email);

    /**
     * 会员修改密码
     * @param account
     * @param password
     * @return
     */
    @Modifying
    @Query(value = "update mall_member set password = ?2 where phone = ?1 or email =?1",nativeQuery = true)
    int updatePassword(String account, String password);
}
