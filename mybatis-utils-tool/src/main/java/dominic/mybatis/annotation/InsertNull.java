package dominic.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用途：遇到字段值为null时，插不插入这个值，true则插入值为NULL，false则insert语句忽略该字段
 * 注意：没有该注解时，默认处理方式是null值插入为NULL(即如果没有遇到以下的情形可以不需要用InsertNull注解)，
 * 如果某字段为not null，但是数据库会给默认值，则bean的该字段值可以为nul但要加上注解InsertNull(false)
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InsertNull {
    boolean value() default true;
}
