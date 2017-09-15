package dominic.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 遇到字段值为null时，插不插入这个值，默认不插入（即忽略该字段），true则插入值为NULL
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InsertNull {
    boolean value() default true;
}
