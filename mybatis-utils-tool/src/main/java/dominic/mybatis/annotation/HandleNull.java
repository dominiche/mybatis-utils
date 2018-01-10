package dominic.mybatis.annotation;

import dominic.mybatis.constants.HandleNullScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用途：遇到字段值为null时，插不插入这个值，true则插入值为NULL，false则insert语句忽略该字段
 * 注意：没有该注解时，默认处理方式是null值不处理。
 * @since 1.4.0-SNAPSHOT,update bean 也支持InsertNull用法，即默认null不更新，有该注解且为true则更新为null
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleNull {
    /**
     * 是否处理null值
     */
    boolean value() default true;

    /**
     * null值处理范围，默认是只处理insert null
     */
    HandleNullScope scope() default HandleNullScope.INSERT;
}
