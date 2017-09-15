package dominic.mybatis.utils.utils;

import java.lang.reflect.Field;

/**
 * Created by Administrator:herongxing on 2017/8/29 15:49.
 */
public class LombokBuilderUtils {

    public static String builderUtils(Class<?> clazz, String prefix) {
        StringBuilder builder = new StringBuilder();

        String fullClazzName = clazz.getName();
        String[] split = fullClazzName.split("\\.");
        String clazzName = split[split.length-1];
        builder.append(clazzName).append(".").append("builder()").append("\n");

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            String capitalName = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
            builder.append(".").append(name).append("(").append(prefix).append(".get").append(capitalName).append("()").append(")").append("\n");
        }

        builder.append(".").append("build();");

        return builder.toString();
    }
}
