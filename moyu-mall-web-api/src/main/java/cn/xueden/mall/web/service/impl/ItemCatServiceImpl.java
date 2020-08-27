package cn.xueden.mall.web.service.impl;

import cn.xueden.mall.common.utils.MapperUtil;
import cn.xueden.mall.common.utils.RedisUtils;
import cn.xueden.mall.domain.MallItemCat;
import cn.xueden.mall.repository.MallItemCatRepository;
import cn.xueden.mall.web.service.ItemCatService;
import cn.xueden.mall.web.service.dto.BaseResult;
import cn.xueden.mall.web.service.dto.TbCate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**功能描述：商品分类业务接口实现
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mallweb.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class ItemCatServiceImpl implements ItemCatService {

    private final RedisUtils redisUtils;

    private final MallItemCatRepository itemCatRepository;

    @Value("${mall.header_cate}")
    private String HEADER_CATE;

    public ItemCatServiceImpl(RedisUtils redisUtils, MallItemCatRepository itemCatRepository) {
        this.redisUtils = redisUtils;
        this.itemCatRepository = itemCatRepository;
    }

    /**
     * 获取分类列表
     * @return
     */
    @Override
    public BaseResult getCateList() {
        List<TbCate> tbCateList = new ArrayList<>();
        // 查询缓存
        try {
            // 有缓存则读取
            String json = (String) redisUtils.get(HEADER_CATE);
            if (json != null) {
                tbCateList = MapperUtil.json2list(json, TbCate.class);
                return BaseResult.success(tbCateList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 从数据库中查询
        List<MallItemCat> tbItemCats = itemCatRepository.findAll();
        // 删除根分类，所有商品
        tbItemCats.remove(0);
        List<TbCate> tbCates = new ArrayList<>();
        for (MallItemCat tbItemCat : tbItemCats) {
            // 如果是分级分类，进行遍历设值
            if (tbItemCat.getPid() == 0) {
                TbCate tbCate = new TbCate();
                tbCate.setId(tbItemCat.getId());
                tbCate.setName(tbItemCat.getName());
                // 查询父分类的所有子分类
                List<MallItemCat> tbItemCatList = itemCatRepository.findByPid(tbItemCat.getId());
                List<TbCate> tbCateSons = new ArrayList<>();
                for (MallItemCat itemCat : tbItemCatList) {
                    TbCate tbCateSon = new TbCate();
                    tbCateSon.setId(itemCat.getId());
                    tbCateSon.setName(itemCat.getName());
                    tbCateSon.setCatesons(null);
                    tbCateSons.add(tbCateSon);
                }
                tbCate.setCatesons(tbCateSons);
                tbCates.add(tbCate);
            }
        }

        // 把结果添加至缓存
        try {
            String tbCateListJson = null;
            if(tbCates!=null&&tbCates.size()>0){
                tbCateListJson = MapperUtil.obj2json(tbCates);
                redisUtils.set(HEADER_CATE, tbCateListJson);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return BaseResult.success(tbCates);
    }
}
