package dominic.mybatis.constants;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Created by Administrator:herongxing on 2018/1/10 14:08.
 */
public interface MybatisUtils {
    /**
     * 为与用户的参数名区分开，mybatis-utils用的参数名都要加paramPrefix
     */
    String PARAM_PREFIX = "mybatis_util_";

    /**
     * 参数map中sql的key
     */
    String SQL = PARAM_PREFIX + "sql";

    /**
     * 参数map中sql的key
     */
    String ID_NAME = PARAM_PREFIX + "id";
    /**
     * 参数map中bean的key
     */
    String BEAN_NAME = PARAM_PREFIX + "bean";
    /**
     * 参数map中tableName的key
     */
    String TABLE_NAME = PARAM_PREFIX + "tableName";


    String UPDATE_SEGMENT_PREFIX = "update";
    String CONDITION_SEGMENT_PREFIX = "condition";


    static String segment(String name) {
        return String.format("#{%s}", name);
    }

    static String paramSegment(String paramName) {
        return String.format("#{%s}", paramName);
    }
    static String paramName(String prefix, String name) {
        return prefix + "_" + name + "_" + RandomStringUtils.random(3, true, true);
    }

    static String wrapBeanName(int index) {
        return PARAM_PREFIX + "bean_" + index;
    }
}
