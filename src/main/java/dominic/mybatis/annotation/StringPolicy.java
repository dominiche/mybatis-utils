package dominic.mybatis.annotation;

import dominic.mybatis.constants.StringTypePolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by herongxing on 2017/3/2 12:17.
 */

/**
 * 仅在自动构建where条件时有用
 * 提供字符串类型条件支持,使用举例:
 @StringPolicy(StringTypePolicy.LIKE)
 private String name;
 则构建sql时对于sql为：name like ‘...%’
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StringPolicy {
    StringTypePolicy value();
}
