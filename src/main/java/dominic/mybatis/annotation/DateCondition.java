package dominic.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by herongxing on 2017/3/2 12:17.
 */

/**
 * 提供查询时间条件支持，使用举例:
 @DateCondition(range = DateRangePolicy.BEGIN, column = "createTime")
 private String createTimeBegin;
 @DateCondition(range = DateRangePolicy.END, column = "createTime")
 private String createTimeEnd;
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateCondition {
    DateRangePolicy range();

    DateTypePolicy dateType() default DateTypePolicy.DATE;

    String column();
}
