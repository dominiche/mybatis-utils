package dominic.mybatis.provider;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Created by herongxing on 2017/2/23 19:56.
 */
@Slf4j
public class BaseQueryProvider {
    public static final String QUERY_BY_SQL = "queryBySql";

    /**
     * 如果mybatis的版本是3.4以上的话参数不用Map来接受， 可以直接写为：@Param("sql") String sql
     * 这里为了兼容3.4以下版本的MyBatis都统一用Map来接受参数
     * @param map
     * @return
     */
    public String queryBySql(Map<String, Object> map) {
        String sql = (String) map.get("sql");
        log.debug(sql);
        return sql;
    }
}
