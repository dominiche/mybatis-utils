package dominic.mybatis.utils;

/**
 * Created by herongxing on 2017/2/20 19:54.
 */
public class SQLInjectPolicy {
    //防注入
    public static String transform(String srcSql) {
        srcSql = srcSql.replaceAll("\\\\", "\\\\\\\\");
        srcSql = srcSql.replaceAll("\'", "\\\\\'");
//        srcSql = srcSql.replaceAll("_", "\\\\_");
        srcSql = srcSql.replaceAll("\"", "\\\\\"");
        return srcSql;
    }
}
