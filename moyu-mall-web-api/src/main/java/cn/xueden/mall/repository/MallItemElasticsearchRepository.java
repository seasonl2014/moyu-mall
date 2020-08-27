package cn.xueden.mall.repository;


import cn.xueden.mall.domain.MallESItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**功能描述：Elasticsearch持久层
 * @Auther:http://www.xueden.cn
 * @Date:2020/4/4
 * @Description:cn.xueden.modules.mall.web.repository
 * @version:1.0
 */
public interface MallItemElasticsearchRepository extends ElasticsearchRepository<MallESItem, Long> {
}
