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

/**
 * Created by Administrator:herongxing on 2017/8/23 17:19.
 */
@Slf4j
public class InsertUtils {

    public static <T> String build(@NonNull String tableName, @NonNull T t) {
        Preconditions.checkArgument(null != t, "bean can not be null!");

        StringBuilder builder = new StringBuilder("INSERT INTO ");
        builder.append(tableName);

        if (t instanceof Collection) {
            Collection collection = (Collection) t;
            return doBuild(builder, collection);

        } else {
            return doBuild(builder, Lists.newArrayList(t));
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

    private static String doBuild(StringBuilder builder, Collection collection) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(collection), "collection can not be empty!");
        boolean firstBean = true;
        StringBuilder values = new StringBuilder(" VALUES ");
        builder.append("(");
        for (Object object : collection) {
            if (null == object) {
                continue;
            }

            Class<?> tClass = object.getClass();
            if (!firstBean) {
                values.append(Separator.SEPARATOR_COMMA);//if not the first bean values add Separator.SEPARATOR_COMMA
            }
            values.append("(");

            boolean firstField = true;
            boolean isUseUnderscoreToCamelCase = SqlBuildUtils.isUseUnderscoreToCamelCase(tClass);
            Field[] fields = tClass.getDeclaredFields();
            for (Field field : fields) {
                if (!SqlBuildUtils.isIgnoreField(field) ){
                    if (firstBean) {
                        if (!firstField) {
                            builder.append(Separator.SEPARATOR_COMMA);
                        }
                        builder.append(SqlBuildUtils.getFieldName(field, isUseUnderscoreToCamelCase));
                    }

                    if (!firstField) {
                        values.append(Separator.SEPARATOR_COMMA);
                    }
                    Object fieldValue = SqlBuildUtils.getFieldValue(field, object);
                    if (fieldValue instanceof Collection) {
                        throw new RuntimeException("insert 不支持bean的字段值为集合类型");
                    }
                    values.append(SqlBuildUtils.getValueString(fieldValue));
                    firstField = false;
                }
            }
            values.append(")");

            firstBean = false;
        }
        builder.append(")");
        builder.append(" ").append(values);

        return builder.toString();
    }
}
