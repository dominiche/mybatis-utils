package dominic.mybatis.utils;

import dominic.mybatis.annotation.ColumnName;
import dominic.mybatis.annotation.InsertNull;
import dominic.mybatis.annotation.MyTransient;
import dominic.mybatis.annotation.UseUnderScoreToCamelCase;
import dominic.mybatis.support.Restriction;
import dominic.mybatis.utils.utils.DateUtils;
import dominic.mybatis.utils.utils.Separator;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Date;


/**
 * Created by herongxing on 2017/2/20 16:28.
 */
@Slf4j
public class SqlBuildUtils {
    //======================select fields start=============================//
    public static <T> StringBuilder getFields(T t) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        Class<?> aClass = t.getClass();
        boolean isUseUnderscoreToCamelCase = SqlBuildUtils.isUseUnderscoreToCamelCase(aClass);
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (!isIgnoreField(field) ){
                first = isFirstAndAppend(builder, first, Separator.SEPARATOR_COMMA);
                builder.append(getFieldName(field, isUseUnderscoreToCamelCase));
            }
        }
        return builder;
    }

    public static String getFieldName(Field field, boolean isUseUnderscoreToCamelCase) {
        String name = getFieldNameUnescaped(field, isUseUnderscoreToCamelCase);
        return "`" + name + "`";
    }

    public static Object getFieldValue(Field field, Object object) {
        field.setAccessible(true);
        Object fieldValue = null;
        try {
            fieldValue = field.get(object);
        } catch (IllegalAccessException e) {
            log.error(String.format("get field '%s' exception: ", field.getName()), e);
        }

        return fieldValue;
    }

    public static String getValueString(Object value) {
        if (null == value) {
            return "NULL";
        }

        if (value instanceof String) {
            return "'" + SQLInjectPolicy.transform((String) value) + "'";
        } else if (value instanceof Date) {
            return "'" + DateUtils.TIME_FORMAT.format((Date) value) + "'";
        }
        return value.toString();
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

    public static String doUnderscoreToCamelCase(String fieldName) {
        StringBuilder sb = new StringBuilder(fieldName.length());
        String[] fields = fieldName.split("_");
        String temp;
        sb.append(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            temp = fields[i].trim();
            sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
        }
        return sb.toString();
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
                first = isFirstAndAppend(builder, first, Separator.SEPARATOR_COMMA);
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
                first = isFirstAndAppend(builder, first, Separator.SEPARATOR_COMMA);
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
                first = isFirstAndAppend(builder, first, Separator.SEPARATOR_COMMA);
                builder.append(alias).append(".").append(getFieldName(field, isUseUnderscoreToCamelCase));
            }
        }
        return builder;
    }
    //======================select fields end=============================//


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

    public static boolean isInsertNull(Field field) {
        return isInsertNull(field, false);//默认null值不插入null
    }

    private static boolean isInsertNull(Field field, boolean defaultInsertNull) {
        InsertNull insertNull = field.getAnnotation(InsertNull.class);
        if (null != insertNull) {
            return insertNull.value();
        } else {
            return defaultInsertNull;
        }
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
                        first = isFirstAndAppend(builder, first, Separator.SEPARATOR_COMMA);
                        builder.append(prefix);
                        builder.append(Restriction.eq(getFieldName(field, isUseUnderscoreToCamelCase), o).SQL());
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
