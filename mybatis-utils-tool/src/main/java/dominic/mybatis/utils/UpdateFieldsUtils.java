package dominic.mybatis.utils;

import dominic.mybatis.constants.HandleNullScope;
import dominic.mybatis.support.stream.UpdateFields;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * Created by Administrator:herongxing on 2017-8-12 14:23:27.
 */
@Slf4j
public class UpdateFieldsUtils {

    public static  <T> UpdateFields buildUpdateFields(T t) {
        return doBuildUpdateFields(t, "");
    }

    public static  <T> UpdateFields buildUpdateFields(T t, String alias) {
        return doBuildUpdateFields(t, alias + ".");
    }

    private static <T> UpdateFields doBuildUpdateFields(T t, String prefix) {
        UpdateFields.UpdateFieldsBuilder builder = UpdateFields.builder();
        if (null == t) {
            return builder.build();
        }

        Class<?> aClass = t.getClass();
        boolean isUseUnderscoreToCamelCase = SqlBuildUtils.isUseUnderscoreToCamelCase(aClass);
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            if (SqlBuildUtils.isIgnoreField(field)) {
                continue;
            }

            field.setAccessible(true);
            try {
                Object fieldValue = field.get(t);
                if (null == fieldValue && !SqlBuildUtils.isHandleNull(field, HandleNullScope.UPDATE)) {
                    continue;
                }

                String fieldName = prefix + SqlBuildUtils.getFieldName(field, isUseUnderscoreToCamelCase);
                if (fieldValue instanceof Collection) {
                    //collection
                    Collection collection = (Collection) fieldValue;
                    if (CollectionUtils.isNotEmpty(collection)) {
//                        builder.set(fieldName, collection);
                        throw new RuntimeException("不支持update set 集合类型");
                    }
                } else {
                    builder.set(fieldName, fieldValue);
                }

            } catch (IllegalAccessException e) {
                log.error("buildConditions: get field value exception:", e);
            }
        }
        return builder.build();
    }
}
