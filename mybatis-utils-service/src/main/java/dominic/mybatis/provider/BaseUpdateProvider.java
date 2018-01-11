package dominic.mybatis.provider;

import dominic.mybatis.constants.HandleNullScope;
import dominic.mybatis.constants.MybatisUtils;
import dominic.mybatis.utils.InsertUtils;
import dominic.mybatis.utils.SqlBuildUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * Created by herongxing on 2017/2/27 16:49.
 */
@Slf4j
public class BaseUpdateProvider {
    public static final String UPDATE = "update";
    public static final String SAVE = "save";
    public static final String INSERT = "insert";
    public static final String INSERT_LIST = "insertList";
    public static final String BEAN = "bean";

    /**
     * 如果mybatis的版本是3.4以上的话参数不用Map来接受， 可以直接写为：@Param("sql") String sql
     * 这里为了兼容3.4以下版本的MyBatis都统一用Map来接受参数
     * @param map
     * @return
     */
    public String update(Map<String, Object> map) {
        String sql = (String) map.get(MybatisUtils.SQL);
        log.debug(sql);
        return sql;
    }

    public String save(Map<String, Object> map) {
        Object bean = map.get(BaseUpdateProvider.BEAN);
        String idName = (String) map.get("idName");
        String tableName = (String) map.get("tableName");
        SQL sql = new SQL();
        sql.INSERT_INTO(tableName);
        buildValues(bean, idName, sql, true);
        log.debug(sql.toString());
        return sql.toString();
    }

    public String insert(Map<String, Object> map) {
        Object bean = map.get(BaseUpdateProvider.BEAN);
        String tableName = (String) map.get("tableName");
        SQL sql = new SQL();
        sql.INSERT_INTO(tableName);
        buildValues(bean, "", sql, false);//不需要idName
        log.debug(sql.toString());
        return sql.toString();
    }

    public String insertList(Map<String, Object> map) {
        String tableName = (String) map.get(MybatisUtils.TABLE_NAME);
        Collection collection = (Collection) map.get(MybatisUtils.BEAN_NAME);
        return InsertUtils.build(tableName, collection);
    }

    private <T> void buildValues(T bean, String idName, SQL sql, boolean ignoreId) {
        Field[] fields = bean.getClass().getDeclaredFields();
        boolean isUseUnderscoreToCamelCase = SqlBuildUtils.isUseUnderscoreToCamelCase(bean.getClass());
        for (Field field : fields) {
            String fieldName = field.getName();
            if(SqlBuildUtils.isIgnoreField(field) ){
                continue;
            }
            //忽略ID属性
            if(ignoreId){
                if(fieldName.equals(idName)){
                    continue;
                }
            }

            Object fieldValue = SqlBuildUtils.getFieldValue(field, bean);
            if (null == fieldValue && !SqlBuildUtils.isHandleNull(field, HandleNullScope.INSERT)) {
                continue;
            }
            sql.VALUES(SqlBuildUtils.getFieldName(field, isUseUnderscoreToCamelCase), "#{" + BaseUpdateProvider.BEAN +"." + fieldName + "}");

        }
    }
}
