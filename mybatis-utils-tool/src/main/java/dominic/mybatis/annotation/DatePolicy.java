package dominic.mybatis.annotation;

import dominic.mybatis.constants.DateRangePolicy;
import dominic.mybatis.constants.DateTypePolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by herongxing on 2017/3/2 12:17.
 */

/**
 * 仅在自动构建where条件时有用
 * 提供字符串类型的时间字段sql构建支持，使用举例:
 @DatePolicy(range = DateRangePolicy.BEGIN, column = "createTime")
 private String createTimeBegin;
 @DatePolicy(range = DateRangePolicy.END, column = "createTime")
 private String createTimeEnd;
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatePolicy {
    DateRangePolicy range();

    DateTypePolicy dateType() default DateTypePolicy.DATE;

    String column();

    //暂不支持，默认是>=或<=,不支持>和<
//    boolean equal() default true;
}
