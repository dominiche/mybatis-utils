package dominic.mybatis.provider;

import dominic.mybatis.utils.SqlBuildUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by herongxing on 2017/2/27 16:49.
 */
@Slf4j
public class BaseUpdateProvider {
    public static final String UPDATE = "update";
    public static final String INSERT = "insert";
    public static final String INSERT_WITH_ID = "insertWithId";
    public static final String BEAN = "bean";

    /**
     * 如果mybatis的版本是3.4以上的话参数不用Map来接受， 可以直接写为：@Param("sql") String sql
     * 这里为了兼容3.4以下版本的MyBatis都统一用Map来接受参数
     * @param map
     * @return
     */
    public String update(Map<String, Object> map) {
        String sql = (String) map.get("sql");
        log.debug(sql);
        return sql;
    }

    public <T> String insert(Map<String, Object> map) {
        Object bean = map.get(BaseUpdateProvider.BEAN);
        String idName = (String) map.get("idName");
        String tableName = (String) map.get("tableName");
        SQL sql = new SQL();
        sql.INSERT_INTO(tableName);
        buildValues(bean, idName, sql, true);
        log.debug(sql.toString());
        return sql.toString();
    }

    public <T> String insertWithId(Map<String, Object> map) {
        Object bean = map.get(BaseUpdateProvider.BEAN);
        String tableName = (String) map.get("tableName");
        SQL sql = new SQL();
        sql.INSERT_INTO(tableName);
        buildValues(bean, "", sql, false);//不需要idName
        log.debug(sql.toString());
        return sql.toString();
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
            field.setAccessible(true); //可以访问private
            try {
                Object value = field.get(bean);
                if(null != value){
                    sql.VALUES(SqlBuildUtils.getFieldName(field, isUseUnderscoreToCamelCase), "#{" + BaseUpdateProvider.BEAN +"." + fieldName + "}");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
