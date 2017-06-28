package dominic.mybatis.utils;

import dominic.mybatis.annotation.DateCondition;
import dominic.mybatis.annotation.DateRangePolicy;
import dominic.mybatis.annotation.DateTypePolicy;
import dominic.mybatis.support.Restriction;
import dominic.mybatis.utils.utils.DateUtils;
import dominic.mybatis.utils.utils.Separator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by Administrator:herongxing on 2017/6/28 14:08.
 */
@Slf4j
public class RestrictionsUtils {

    public static  <T> StringBuilder buildConditions(T t) {
        return doBuildCondition(t, "");
    }

    public static  <T> StringBuilder buildConditionsWithAlias(T t, String alias) {
        return doBuildCondition(t, alias + ".");
    }

    private static <T> StringBuilder doBuildCondition(T t, String prefix) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        Class<?> aClass = t.getClass();
        boolean isUseUnderscoreToCamelCase = SqlBuildUtils.isUseUnderscoreToCamelCase(aClass);
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (!SqlBuildUtils.isIgnoreField(field) ){
                field.setAccessible(true);
                try {
                    Object o = field.get(t);
                    if ( o != null) {
                        if (o instanceof String) {
                            String str = (String) o;
                            if (StringUtils.isNotBlank(str)) {
                                first = SqlBuildUtils.isFirstAndAppend(builder, first, Separator.SEPARATOR_AND);
                                builder.append(prefix);
                                DateCondition dateCondition = field.getAnnotation(DateCondition.class);
                                if (dateCondition !=null) {
                                    appendDateCondition(builder, str, dateCondition);
                                } else {
                                    builder.append(Restriction.like(SqlBuildUtils.getFieldName(field, isUseUnderscoreToCamelCase), str).SQL());
                                }
                            }
                        } else {
                            first = SqlBuildUtils.isFirstAndAppend(builder, first, Separator.SEPARATOR_AND);
                            builder.append(prefix);
                            builder.append(Restriction.eq(SqlBuildUtils.getFieldName(field, isUseUnderscoreToCamelCase), o).SQL());
                        }
                    }
                } catch (IllegalAccessException e) {
                    log.error("buildConditions: get field value exception:", e);
                }

            }
        }
        return builder;
    }

    private static void appendDateCondition(StringBuilder builder, String str, DateCondition dateCondition) {
        Date date = DateTime.parse(str).toDate();
        String columnName = dateCondition.column();
        DateTypePolicy dateType = dateCondition.dateType();
        DateRangePolicy range = dateCondition.range();
        if(dateType == DateTypePolicy.DATE){
            if(range == DateRangePolicy.BEGIN){
                String timeStr = DateUtils.DATE_FORMAT.format(date) + " 00:00:00";
                builder.append(Restriction.dateGe(columnName, timeStr).SQL());
            }else if(range == DateRangePolicy.END){
                String timeStr = DateUtils.DATE_FORMAT.format(date) + " 23:59:59";
                builder.append(Restriction.dateLe(columnName, timeStr).SQL());
            }
        }
        if(dateType == DateTypePolicy.DATE_TIME){
            String timeStr = DateUtils.TIME_FORMAT.format(date);
            if(range == DateRangePolicy.BEGIN){
                builder.append(Restriction.dateGe(columnName, timeStr).SQL());
            }else if(range == DateRangePolicy.END){
                builder.append(Restriction.dateLe(columnName, timeStr).SQL());
            }
        }
    }

    @Deprecated
    public static <T> StringBuilder buildConditionsByMyBatisParam(T t, String param) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        Class<?> aClass = t.getClass();
        boolean isUseUnderscoreToCamelCase = SqlBuildUtils.isUseUnderscoreToCamelCase(aClass);
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (!SqlBuildUtils.isIgnoreField(field) ){
                field.setAccessible(true);
                try {
                    Object o = field.get(t);
                    if ( o != null) {
                        if (o instanceof String) {
                            first = SqlBuildUtils.isFirstAndAppend(builder, first, Separator.SEPARATOR_AND);
                            DateCondition dateCondition = field.getAnnotation(DateCondition.class);
                            if (dateCondition !=null) {
                                appendDateCondition(builder, (String) o, dateCondition);
                            } else {
                                builder.append(SqlBuildUtils.getFieldName(field, isUseUnderscoreToCamelCase))
                                        .append(" like CONCAT(#{").append(param).append(".")
                                        .append(field.getName())
                                        .append("}, '%')");
                            }
                        } else {
                            first = SqlBuildUtils.isFirstAndAppend(builder, first, Separator.SEPARATOR_AND);
                            builder.append(SqlBuildUtils.getFieldName(field, isUseUnderscoreToCamelCase))
                                    .append("=#{").append(param).append(".")
                                    .append(field.getName())
                                    .append("}");
                        }
                    }
                } catch (IllegalAccessException e) {
                    log.error("buildConditionsByMyBatisParam: get field value exception:", e);
                }

            }
        }
        return builder;
    }
}
