package cn.xueden.mall.common.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**功能描述：公共DTO
 * @Auther:http://www.xueden.cn
 * @Date:2020/3/22
 * @Description:cn.xueden.common.base
 * @version:1.0
 */
@Getter
@Setter
public class BaseDTO implements Serializable {

    private Boolean isDelete;

    private Timestamp createTime;

    private Timestamp updateTime;

    @Override
    public String toString() {
        return "BaseDTO{" +
                "isDelete=" + isDelete +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
