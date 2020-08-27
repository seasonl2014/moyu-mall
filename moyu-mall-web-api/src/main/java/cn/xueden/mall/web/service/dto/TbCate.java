package cn.xueden.mall.web.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.service.dto
 * @version:1.0
 */
@Data
public class TbCate implements Serializable {

    /**
     * 分类 id
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类的子分类
     */
    private List<TbCate> catesons;
}
