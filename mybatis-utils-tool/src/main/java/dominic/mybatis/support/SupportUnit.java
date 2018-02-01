package dominic.mybatis.support;

import dominic.mybatis.constants.MybatisUtils;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * Created by herongxing on 2017/2/28 14:54.
 */
public interface SupportUnit {
    String SQL();

    static String directSQL(String sql, Map<String, Object> paramMap) {
        if (MapUtils.isEmpty(paramMap)) {
            return sql;
        }

        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String valueString = value.toString();
            if (value instanceof String) {
                valueString = "'" + valueString + "'";
            }
            sql = sql.replace(MybatisUtils.segment(key), valueString);
        }
        return sql;
    }
}
