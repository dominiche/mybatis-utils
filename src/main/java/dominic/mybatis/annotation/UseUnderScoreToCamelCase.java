package dominic.mybatis.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 开启下划线转驼峰
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface UseUnderScoreToCamelCase {
}
