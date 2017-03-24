package dominic.mybatis.utils;

import dominic.mybatis.annotation.*;
import dominic.mybatis.support.Restriction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by herongxing on 2017/2/20 16:28.
 */
@Slf4j
public class SqlBuildUtils {
    private static final String SEPARATOR_AND = " and ";
    public static final String SEPARATOR_COMMA = ",";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //======================select fields start=============================//
    public static <T> StringBuilder getFields(T t) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        Class<?> aClass = t.getClass();
        boolean isUseUnderscoreToCamelCase = SqlBuildUtils.isUseUnderscoreToCamelCase(aClass);
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (!isIgnoreField(field) ){
                first = isFirstAndAppend(builder, first, SqlBuildUtils.SEPARATOR_COMMA);
                builder.append(getFieldName(field, isUseUnderscoreToCamelCase));
            }
        }
        return builder;
    }

    public static String getFieldName(Field field, boolean isUseUnderscoreToCamelCase) {
        String name = getFieldNameUnescaped(field, isUseUnderscoreToCamelCase);
        return "`" + name + "`";
    }

    public static String getFieldNameUnescaped(Field field, boolean isUseUnderscoreToCamelCase) {
        String name;
        ColumnName columnName = field.getAnnotation(ColumnName.class);
        if (null != columnName) {
            name = columnName.value();
        } else {
            name = camelCaseToUnderscore(field.getName(), isUseUnderscoreToCamelCase);
        }
        return name;
    }

    /**
     *尽量使用camelCaseToUnderscore(String fieldName, boolean isUseUnderscoreToCamelCase)这个方法
     */
    @Deprecated
    public static String camelCaseToUnderscore(String fieldName, Class<?> clazz) {
        StringBuilder name = new StringBuilder();
        boolean flag = isUseUnderscoreToCamelCase(clazz);
        if (flag) {
            doCamelCaseToUnderscore(fieldName, name);
        } else {
            name.append(fieldName);
        }
        return name.toString();
    }

    public static String camelCaseToUnderscore(String fieldName, boolean isUseUnderscoreToCamelCase) {
        StringBuilder name = new StringBuilder();
        if (isUseUnderscoreToCamelCase) {
            doCamelCaseToUnderscore(fieldName, name);
        } else {
            name.append(fieldName);
        }
        return name.toString();
    }

    private static void doCamelCaseToUnderscore(String fieldName, StringBuilder name) {
        for (char c : fieldName.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                name.append("_").append((char) (c + 32)); //to lower case
            }  else {
                name.append(c);
            }
        }
    }

    /**
     * 是否开启了驼峰转下划线
     */
    public static boolean isUseUnderscoreToCamelCase(Class<?> clazz) {
        boolean flag = false;
        UseUnderScoreToCamelCase annotation = clazz.getAnnotation(UseUnderScoreToCamelCase.class);
        if (null != annotation) {
            flag = true;
        }
        return flag;
    }

    public static <T> StringBuilder getFieldsWithAlias(T t, String alias) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        Class<?> aClass = t.getClass();
        boolean isUseUnderscoreToCamelCase = SqlBuildUtils.isUseUnderscoreToCamelCase(aClass);
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (!isIgnoreField(field) ){
                first = isFirstAndAppend(builder, first, SqlBuildUtils.SEPARATOR_COMMA);
                builder.append(alias).append(".").append(getFieldName(field, isUseUnderscoreToCamelCase));
            }
        }
        return builder;
    }

    public static StringBuilder getFieldsByClass(Class<?> aClass) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        boolean isUseUnderscoreToCamelCase = SqlBuildUtils.isUseUnderscoreToCamelCase(aClass);
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (!isIgnoreField(field) ){
                first = isFirstAndAppend(builder, first, SqlBuildUtils.SEPARATOR_COMMA);
                builder.append(getFieldName(field, isUseUnderscoreToCamelCase));
            }
        }
        return builder;
    }

    public static StringBuilder getFieldsByClassWithAlias(Class<?> aClass, String alias) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        boolean isUseUnderscoreToCamelCase = SqlBuildUtils.isUseUnderscoreToCamelCase(aClass);
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (!isIgnoreField(field) ){
                first = isFirstAndAppend(builder, first, SqlBuildUtils.SEPARATOR_COMMA);
                builder.append(alias).append(".").append(getFieldName(field, isUseUnderscoreToCamelCase));
            }
        }
        return builder;
    }
    //======================select fields end=============================//


    //======================condition fields start=============================//
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
            if (!isIgnoreField(field) ){
                field.setAccessible(true);
                try {
                    Object o = field.get(t);
                    if ( o != null) {
                        if (o instanceof String) {
                            String str = (String) o;
                            if (StringUtils.isNotBlank(str)) {
                                first = isFirstAndAppend(builder, first, SqlBuildUtils.SEPARATOR_AND);
                                builder.append(prefix);
                                DateCondition dateCondition = field.getAnnotation(DateCondition.class);
                                if (dateCondition !=null) {
                                    appendDateCondition(builder, str, dateCondition);
                                } else {
                                    builder.append(Restriction.like(getFieldName(field, isUseUnderscoreToCamelCase), str).toSQL());
                                }
                            }
                        } else {
                            first = isFirstAndAppend(builder, first, SqlBuildUtils.SEPARATOR_AND);
                            builder.append(prefix);
                            builder.append(Restriction.eq(getFieldName(field, isUseUnderscoreToCamelCase), o).toSQL());
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
                String timeStr = DATE_FORMAT.format(date) + " 00:00:00";
                builder.append(Restriction.dateGe(columnName, timeStr).toSQL());
            }else if(range == DateRangePolicy.END){
                String timeStr = DATE_FORMAT.format(date) + " 23:59:59";
                builder.append(Restriction.dateLe(columnName, timeStr).toSQL());
            }
        }
        if(dateType == DateTypePolicy.DATE_TIME){
            String timeStr = TIME_FORMAT.format(date);
            if(range == DateRangePolicy.BEGIN){
                builder.append(Restriction.dateGe(columnName, timeStr).toSQL());
            }else if(range == DateRangePolicy.END){
                builder.append(Restriction.dateLe(columnName, timeStr).toSQL());
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
            if (!isIgnoreField(field) ){
                field.setAccessible(true);
                try {
                    Object o = field.get(t);
                    if ( o != null) {
                        if (o instanceof String) {
                            first = isFirstAndAppend(builder, first, SqlBuildUtils.SEPARATOR_AND);
                            DateCondition dateCondition = field.getAnnotation(DateCondition.class);
                            if (dateCondition !=null) {
                                appendDateCondition(builder, (String) o, dateCondition);
                            } else {
                                builder.append(getFieldName(field, isUseUnderscoreToCamelCase))
                                        .append(" like CONCAT(#{").append(param).append(".")
                                        .append(field.getName())
                                        .append("}, '%')");
                            }
                        } else {
                            first = isFirstAndAppend(builder, first, SqlBuildUtils.SEPARATOR_AND);
                            builder.append(getFieldName(field, isUseUnderscoreToCamelCase))
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
    //======================condition fields end=============================//


    public static boolean isFirstAndAppend(StringBuilder builder, boolean first, String separator) {
        if (!first){
            builder.append(separator);
        }
        return false;
    }

    public static boolean isIgnoreField(Field field) {
        if("serialVersionUID".equals(field.getName())) { // 过滤serialVersionUID此属性
            return true;
        }

        MyTransient myTransient = field.getAnnotation(MyTransient.class);
        if (myTransient !=null) {
            return true;
        }

//        if(Modifier.isStatic(field.getModifiers())) { //过滤静态属性
//            return true;
//        }
        if(field.getType() == int.class || field.getType() == long.class ||
                field.getType() == double.class || field.getType() == short.class ||
                field.getType() == byte.class || field.getType() == float.class ||
                field.getType() == boolean.class || field.getType() == char.class){
            return true;
        }
        return false;
    }


    //==============update Fields start=======================//
    public static  <T> StringBuilder buildUpdateFields(T t) {
        return doBuildUpdateFields(t, "");
    }

    public static  <T> StringBuilder buildUpdateFieldsWithAlias(T t, String alias) {
        return doBuildUpdateFields(t, alias + ".");
    }

    private static <T> StringBuilder doBuildUpdateFields(T t, String prefix) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        Class<?> aClass = t.getClass();
        boolean isUseUnderscoreToCamelCase = SqlBuildUtils.isUseUnderscoreToCamelCase(aClass);
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (!isIgnoreField(field) ){
                field.setAccessible(true);
                try {
                    Object o = field.get(t);
                    if ( o != null) {
                        first = isFirstAndAppend(builder, first, SqlBuildUtils.SEPARATOR_COMMA);
                        builder.append(prefix);
                        builder.append(Restriction.eq(getFieldName(field, isUseUnderscoreToCamelCase), o).toSQL());
                    }
                } catch (IllegalAccessException e) {
                    log.error("doBuildUpdateFields: get field value exception:", e);
                }

            }
        }
        return builder;
    }
    //==============update Fields end=======================//
}
