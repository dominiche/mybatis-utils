package dominic.mybatis.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 表字段名
 * 优先级最高
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnName {
    String value();
}
