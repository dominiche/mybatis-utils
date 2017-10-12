package dominic.mybatis.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import dominic.mybatis.annotation.TableName;
import dominic.mybatis.utils.utils.Separator;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator:herongxing on 2017/8/23 17:19.
 */
@Slf4j
public class InsertUtils {

    public static <T> String build(@NonNull String tableName, @NonNull T t) {
        Preconditions.checkArgument(null != t, "bean can not be null!");

        if (t instanceof Collection) {
            Collection collection = (Collection) t;
            return buildCollection(collection, tableName);

        } else {
            return buildSingle(t, tableName, Lists.newArrayList()).toString();
        }
    }

    public static <T> String build(@NonNull T t) {
        Class<?> aClass;
        if (t instanceof Collection) {
            Collection collection = (Collection) t;
            Object object = collection.iterator().next();
            aClass = object.getClass();

        } else {
            aClass = t.getClass();
        }

        TableName tableNameAnnotation = aClass.getAnnotation(TableName.class);
        if (null == tableNameAnnotation) {
            throw new RuntimeException("parameter bean does not have TableName annotation, can not get it's table name!");
        }
        String tableName = tableNameAnnotation.value();
        return build(tableName, t);
    }

    private static StringBuilder buildSingle(Object object, String tableName, List<Field> fieldNameList) {
        StringBuilder builder = new StringBuilder("");
        if (null == object) {
            return builder;
        }

        builder.append("INSERT INTO ").append(tableName).append("(");
        Class<?> tClass = object.getClass();
        boolean isUseUnderscoreToCamelCase = SqlBuildUtils.isUseUnderscoreToCamelCase(tClass);
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            if (SqlBuildUtils.isIgnoreField(field)) {
                continue;
            }
            Object fieldValue = SqlBuildUtils.getFieldValue(field, object);
            if (null == fieldValue && !SqlBuildUtils.isInsertNull(field)) {
                continue;
            }

            fieldNameList.add(field);
            builder.append(SqlBuildUtils.getFieldName(field, isUseUnderscoreToCamelCase));
            builder.append(Separator.SEPARATOR_COMMA);
        }
        builder.deleteCharAt(builder.length()-1);//remove redundant comma
        builder.append(")");

        builder.append(" VALUES ");
        buildValues(object, fieldNameList, builder);

        return builder;
    }

    private static void buildValues(Object object, List<Field> fieldNameList, StringBuilder builder) {
        builder.append("( ");
        for (Field field : fieldNameList) {
            Object fieldValue = SqlBuildUtils.getFieldValue(field, object);
            if (fieldValue instanceof Collection) {
                throw new RuntimeException("insert 不支持bean的字段值为集合类型");
            }
            builder.append(SqlBuildUtils.getValueString(fieldValue));
            builder.append(Separator.SEPARATOR_COMMA);
        }
        builder.deleteCharAt(builder.length()-1);//remove redundant comma
        builder.append(")");
    }

    private static String buildCollection(Collection collection, String tableName) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(collection), "collection can not be empty!");
        List<Field> fieldNameList = Lists.newArrayList();
        StringBuilder builder = new StringBuilder();
        for (Object object : collection) {
            if (CollectionUtils.isEmpty(fieldNameList)) {
                builder = buildSingle(object, tableName, fieldNameList);
            } else {
                builder.append(Separator.SEPARATOR_COMMA);
                buildValues(object, fieldNameList, builder);
            }
        }
        return builder.toString();
    }
}
