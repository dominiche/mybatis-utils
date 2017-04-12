package dominic.mybatis.support;

import com.google.common.base.Preconditions;
import dominic.mybatis.utils.SQLInjectPolicy;
import dominic.mybatis.utils.SqlBuildUtils;
import lombok.*;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;

import static dominic.mybatis.utils.SqlBuildUtils.isFirstAndAppend;

/**
 * Created by herongxing on 2017/2/27 18:39.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Restriction implements ISupport {
    private String condition;

    public static <T> Restriction eq(String name, @NonNull T value) {
        if (value instanceof String) {
            return Restriction.builder().condition(getName(name) + "='" + SQLInjectPolicy.transform((String) value) + "'").build();
        }
        return Restriction.builder().condition(getName(name) + "=" + value).build();
    }

    private static String getName(String name) {
        String[] split = name.split("\\.");
        if (split.length == 2) {
            //联表的条件，有表别名前缀
            return split[0] + "." + "`" + split[1] + "`";
        } else {
            return "`" + name + "`";
        }
    }

    public static <T> Restriction notEq(String name, @NonNull T value) {
        if (value instanceof String) {
            return Restriction.builder().condition(getName(name) + "!='" + SQLInjectPolicy.transform((String) value) + "'").build();
        }
        return Restriction.builder().condition(getName(name) + "!=" + value).build();
    }

    public static Restriction like(String name, @NonNull String value) {
        return Restriction.builder().condition(getName(name) + " like CONCAT('" + SQLInjectPolicy.transformForLike(value) + "','%')").build();
    }

    public static Restriction likeBoth(String name, @NonNull String value) {
        return Restriction.builder().condition(getName(name) + " like CONCAT('%','" + SQLInjectPolicy.transformForLike(value) + "','%')").build();
    }

    public static <R> Restriction in(String name, Collection<R> collection) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(collection), "arg list can't be empty!");
        boolean first = true;
        R next = collection.iterator().next();
        boolean isString = false;
        if (next instanceof String) {
            isString = true;
        }
        StringBuilder builder = new StringBuilder(getName(name));
        builder.append(" IN (");
        for (R r : collection) {
            first = isFirstAndAppend(builder, first, SqlBuildUtils.SEPARATOR_COMMA);
            if (isString) {
                builder.append("'").append(SQLInjectPolicy.transform((String) r)).append("'");
            } else {
                builder.append(r);
            }
        }
        builder.append(")");

        return Restriction.builder().condition(builder.toString()).build();
    }

    @Deprecated
    public static Restriction dateGe(String name, @NonNull String value) {
        return Restriction.builder().condition(getName(name) + " >='" + SQLInjectPolicy.transform(value) + "'").build();
    }

    @Deprecated
    public static Restriction dateLe(String name, @NonNull String value) {
        return Restriction.builder().condition(getName(name) + " <='" + SQLInjectPolicy.transform(value) + "'").build();
    }

    public static Restriction greaterEqual(String name, Object value) {
        return getEqualRestriction(name, value, ">=");
    }
    public static Restriction greaterThan(String name, Object value) {
        return getEqualRestriction(name, value, ">");
    }
    public static Restriction lessEqual(String name, Object value) {
        return getEqualRestriction(name, value, "<=");
    }
    public static Restriction lessThan(String name, Object value) {
        return getEqualRestriction(name, value, "<");
    }
    private static Restriction getEqualRestriction(String name, @NonNull Object value, String equalSign) {
        StringBuilder builder = new StringBuilder(getName(name));
        builder.append(" ").append(equalSign).append(" ");
        if (value instanceof String) {
            builder.append("'").append(SQLInjectPolicy.transform((String) value)).append("'");
        } else {
            builder.append(value);
        }
        return Restriction.builder().condition(builder.toString()).build();
    }

    public static Restriction sql(@NonNull String restriction) {
        return Restriction.builder().condition(restriction).build();
    }

    @Override
    public String SQL() {
        return condition;
    }
}
