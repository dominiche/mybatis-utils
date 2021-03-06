package dominic.mybatis.utils;

import dominic.mybatis.annotation.DatePolicy;
import dominic.mybatis.annotation.StringPolicy;
import dominic.mybatis.constants.DateRangePolicy;
import dominic.mybatis.constants.DateTypePolicy;
import dominic.mybatis.constants.StringTypePolicy;
import dominic.mybatis.support.Restriction;
import dominic.mybatis.support.stream.Restrictions;
import dominic.mybatis.utils.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Administrator:herongxing on 2017/6/28 14:08.
 */
@Slf4j
public class RestrictionsUtils {

    public static  <T> Restrictions buildConditions(T t) {
        return doBuildCondition(t, "");
    }

    public static  <T> Restrictions buildConditions(T t, String alias) {
        return doBuildCondition(t, alias + ".");
    }

    private static <T> Restrictions doBuildCondition(T t, String prefix) {
        Restrictions.RestrictionsBuilder builder = Restrictions.builder();
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
                if (null == fieldValue) {
                    continue;
                }

                String fieldName = prefix + SqlBuildUtils.getFieldNameUnescaped(field, isUseUnderscoreToCamelCase);
                if (fieldValue instanceof Collection) {
                    //collection
                    Collection collection = (Collection) fieldValue;
                    if (CollectionUtils.isNotEmpty(collection)) {
                        builder.in(fieldName, collection);
                    }
                } else if (fieldValue instanceof String) {
                    //String
                    String str = (String) fieldValue;
                    if (StringUtils.isNotBlank(str)) {
                        DatePolicy datePolicy = field.getAnnotation(DatePolicy.class);
                        if (datePolicy != null) {
                            //date type tag
                            appendDateCondition(builder, str, datePolicy);
                        } else {
                            StringPolicy stringPolicy = field.getAnnotation(StringPolicy.class);
                            if (null != stringPolicy) {
                                StringTypePolicy typePolicy = stringPolicy.value();
                                switch (typePolicy) {
                                    case EQUAL:
                                        builder.eq(fieldName, str);break;
                                    case LIKE:
                                        builder.like(fieldName, str);break;
                                    case LIKE_BOTH:
                                        builder.likeBoth(fieldName, str);break;
                                    default:
                                        builder.eq(fieldName, fieldValue);
                                }
                            } else {
                                builder.eq(fieldName, str);
                            }
                        }
                    }
                } else if (fieldValue instanceof Date) {
                    DatePolicy datePolicy = field.getAnnotation(DatePolicy.class);
                    if (datePolicy != null) {
                        Date date = (Date) fieldValue;
                        String columnName = datePolicy.column();
                        DateRangePolicy range = datePolicy.range();
                        if(range == DateRangePolicy.BEGIN){
                            String timeStr = DateUtils.TIME_FORMAT.format(date);
                            builder.and(getBeginRestriction(columnName, timeStr));
                        }else if(range == DateRangePolicy.END){
                            String timeStr = DateUtils.TIME_FORMAT.format(date);
                            builder.and(getEndRestriction(columnName, timeStr));
                        }
                    }
                } else {
                    builder.eq(fieldName, fieldValue);
                }

            } catch (IllegalAccessException e) {
                log.error("buildConditions: get field value exception:", e);
            }
        }
        return builder.build();
    }

    private static void appendDateCondition(Restrictions.RestrictionsBuilder builder, String str, DatePolicy datePolicy) {
        Date date = DateTime.parse(str).toDate();
        String columnName = datePolicy.column();
        DateTypePolicy dateType = datePolicy.dateType();
        DateRangePolicy range = datePolicy.range();
        if(dateType == DateTypePolicy.DATE){
            if(range == DateRangePolicy.BEGIN){
                String timeStr = DateUtils.DATE_FORMAT.format(date) + " 00:00:00";
                builder.and(getBeginRestriction(columnName, timeStr));
            }else if(range == DateRangePolicy.END){
                String timeStr = DateUtils.DATE_FORMAT.format(date) + " 23:59:59";
                builder.and(getEndRestriction(columnName, timeStr));
            }
        } else if(dateType == DateTypePolicy.DATE_TIME){
            String timeStr = DateUtils.TIME_FORMAT.format(date);
            if(range == DateRangePolicy.BEGIN){
                builder.and(getBeginRestriction(columnName, timeStr));
            }else if(range == DateRangePolicy.END){
                builder.and(getEndRestriction(columnName, timeStr));
            }
        }
    }

    private static Restriction getBeginRestriction(String columnName, String timeStr) {
        return Restriction.greaterEqual(columnName, timeStr);
    }

    private static Restriction getEndRestriction(String columnName, String timeStr) {
        return Restriction.lessEqual(columnName, timeStr);
    }
}
