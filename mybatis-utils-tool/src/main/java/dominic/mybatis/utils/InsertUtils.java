package dominic.mybatis.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import dominic.mybatis.annotation.TableName;
import dominic.mybatis.constants.HandleNullScope;
import dominic.mybatis.constants.MybatisUtils;
import dominic.mybatis.utils.utils.Separator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator:herongxing on 2017/8/23 17:19.
 */
@Slf4j
public class InsertUtils {

    public static <T> String build(String tableName, Collection<T> collection) {
        Preconditions.checkArgument(StringUtils.isNotBlank(tableName), "tableName can not be blank!");
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(collection), "collection can not be empty!");

        return buildCollection(collection, tableName);
    }

    /**
     * 不开放单个bean的支持，单个insert直接用dominic.mybatis.service.AbstractUpdateService#insert(java.lang.Object)
     */
/*    public static <T> String build(@NonNull T t) {
        Class<?> aClass = t.getClass();
        TableName tableNameAnnotation = aClass.getAnnotation(TableName.class);
        if (null == tableNameAnnotation) {
            throw new RuntimeException("parameter bean does not have TableName annotation, can not get it's table name!");
        }
        String tableName = tableNameAnnotation.value();
        ArrayList<T> list = new ArrayList<>();
        list.add(t);
        return build(tableName, list);
    }*/

    public static <T> String build(Collection<T> collection) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(collection), "collection can't be empty!");

        Object object = collection.iterator().next();
        Class<?> aClass = object.getClass();

        TableName tableNameAnnotation = aClass.getAnnotation(TableName.class);
        if (null == tableNameAnnotation) {
            throw new RuntimeException("parameter bean does not have TableName annotation, can not get it's table name!");
        }
        String tableName = tableNameAnnotation.value();
        return build(tableName, collection);
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
            if (null == fieldValue && !SqlBuildUtils.isHandleNull(field, HandleNullScope.INSERT)) {
                continue;
            }

            fieldNameList.add(field);
            builder.append(SqlBuildUtils.getFieldName(field, isUseUnderscoreToCamelCase));
            builder.append(Separator.SEPARATOR_COMMA);
        }
        builder.deleteCharAt(builder.length()-1);//remove redundant comma
        builder.append(")");

        builder.append(" VALUES ");
        buildValues(0, object, fieldNameList, builder);

        return builder;
    }

    private static void buildValues(int index, Object object, List<Field> fieldNameList, StringBuilder builder) {
        builder.append("(");
        for (Field field : fieldNameList) {
            Object fieldValue = SqlBuildUtils.getFieldValue(field, object);
            if (fieldValue instanceof Collection) {
                throw new RuntimeException("insert 不支持bean的字段值为集合类型");
            }
            builder.append(MybatisUtils.segment(MybatisUtils.wrapBeanName(index) + "." + field.getName()));
            builder.append(Separator.SEPARATOR_COMMA);
        }
        builder.deleteCharAt(builder.length()-1);//remove redundant comma
        builder.append(")");
    }

    private static String buildCollection(Collection collection, String tableName) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(collection), "collection can not be empty!");
        List<Field> fieldNameList = Lists.newArrayList();
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (Object object : collection) {
            if (CollectionUtils.isEmpty(fieldNameList)) {
                builder = buildSingle(object, tableName, fieldNameList);
            } else {
                builder.append(Separator.SEPARATOR_COMMA);
                buildValues(index, object, fieldNameList, builder);
            }
            ++index;
        }
        return builder.toString();
    }
}
