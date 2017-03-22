package dominic.mybatis.dao;

import dominic.mybatis.provider.BaseQueryProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by herongxing on 2017/2/20 14:11.
 */
@Repository
public interface BaseQueryDAO {
    @SelectProvider(type = BaseQueryProvider.class, method = BaseQueryProvider.QUERY_BY_SQL)
    List<HashMap<String, Object>> queryBySql(@Param("sql") String sql);

    @SelectProvider(type = BaseQueryProvider.class, method = BaseQueryProvider.QUERY_BY_SQL)
    int queryCountBySql(@Param("sql") String sql);

    @SelectProvider(type = BaseQueryProvider.class, method = BaseQueryProvider.QUERY_BY_SQL)
    HashMap<String, Object> queryUniqueBySql(@Param("sql") String sql);

    @SelectProvider(type = BaseQueryProvider.class, method = BaseQueryProvider.QUERY_BY_SQL)
    List<HashMap<String, Object>> querySpecialListBySql(@Param("sql") String sql);

    @SelectProvider(type = BaseQueryProvider.class, method = BaseQueryProvider.QUERY_BY_SQL)
    HashMap<String, Object> querySpecialBySql(@Param("sql") String sql);

    @SelectProvider(type = BaseQueryProvider.class, method = BaseQueryProvider.QUERY_BY_SQL)
    <T> T querySingleValueBySql(@Param("sql") String sql, Class<T> singleValueClassType);


//    @SelectProvider()
//    T queryById(@Param("id") int id);
}
