package dominic.mybatis.dao.query;

import dominic.mybatis.constants.MybatisUtils;
import dominic.mybatis.provider.BaseQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by herongxing on 2017/2/20 14:11.
 */
@Repository
public interface BaseQueryDAO {
    @SelectProvider(type = BaseQueryProvider.class, method = BaseQueryProvider.QUERY_BY_SQL)
    List<HashMap<String, Object>> query(Map<String, Object> map);

    @SelectProvider(type = BaseQueryProvider.class, method = BaseQueryProvider.QUERY_BY_SQL)
    long queryCountBySql(Map<String, Object> map);

    @SelectProvider(type = BaseQueryProvider.class, method = BaseQueryProvider.QUERY_BY_SQL)
    HashMap<String, Object> queryUnique(Map<String, Object> map);

    @SelectProvider(type = BaseQueryProvider.class, method = BaseQueryProvider.QUERY_BY_SQL)
    <T> T querySingleValueBySql(Map<String, Object> map, Class<T> singleValueClassType);



    @SelectProvider(type = BaseQueryProvider.class, method = BaseQueryProvider.QUERY_BY_SQL)
    List<HashMap<String, Object>> queryBySql(@Param(MybatisUtils.SQL) String sql);

    @SelectProvider(type = BaseQueryProvider.class, method = BaseQueryProvider.QUERY_BY_SQL)
    List<HashMap<String, Object>> querySpecialListBySql(@Param(MybatisUtils.SQL) String sql);

    @SelectProvider(type = BaseQueryProvider.class, method = BaseQueryProvider.QUERY_BY_SQL)
    HashMap<String, Object> querySpecialBySql(@Param(MybatisUtils.SQL) String sql);
}
