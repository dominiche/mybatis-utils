package dominic.mybatis.dao.update;

import dominic.mybatis.bean.IdContainer;
import dominic.mybatis.provider.BaseUpdateProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

/**
 * Created by herongxing on 2017/2/27 16:48.
 */
@Repository
public interface BaseUpdateDAO {
    @UpdateProvider(type = BaseUpdateProvider.class, method = BaseUpdateProvider.UPDATE)
    int update(Map<String, Object> map);

    @UpdateProvider(type = BaseUpdateProvider.class, method = BaseUpdateProvider.UPDATE)
    int updateBySQL(@Param("sql") String sql);



    @Options(useGeneratedKeys = true, keyProperty = "idContainer.id")
    @InsertProvider(type = BaseUpdateProvider.class, method = BaseUpdateProvider.SAVE)
    <T> int save(@Param(BaseUpdateProvider.BEAN) T bean,
                   @Param("idContainer") IdContainer idContainer, @Param("idName") String idName, @Param("tableName") String tableName);

    @InsertProvider(type = BaseUpdateProvider.class, method = BaseUpdateProvider.INSERT)
    <T> int insert(@Param(BaseUpdateProvider.BEAN) T bean, @Param("tableName") String tableName);

    @InsertProvider(type = BaseUpdateProvider.class, method = BaseUpdateProvider.INSERT_LIST)
    <T> int insertList(@Param(BaseUpdateProvider.BEAN) Collection<T> beans, @Param("tableName") String tableName);
}
