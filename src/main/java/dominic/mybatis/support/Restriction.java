package dominic.mybatis.support;

import dominic.mybatis.utils.SQLInjectPolicy;
import dominic.mybatis.utils.SqlBuildUtils;
import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.List;

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

    public static <T> Restriction eq(String name, T value) {
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

    public static <T> Restriction notEq(String name, T value) {
        if (value instanceof String) {
            return Restriction.builder().condition(getName(name) + "!='" + SQLInjectPolicy.transform((String) value) + "'").build();
        }
        return Restriction.builder().condition(getName(name) + "!=" + value).build();
    }

    public static Restriction like(String name, String value) {
        return Restriction.builder().condition(getName(name) + " like CONCAT('" + SQLInjectPolicy.transform(value) + "','%')").build();
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

    public static Restriction dateGe(String name, String value) {
        return Restriction.builder().condition(getName(name) + " >='" + SQLInjectPolicy.transform(value) + "'").build();
    }

    public static Restriction dateLe(String name, String value) {
        return Restriction.builder().condition(getName(name) + " <='" + SQLInjectPolicy.transform(value) + "'").build();
    }

    public static Restriction sql(String restriction) {
        return Restriction.builder().condition(restriction).build();
    }

    @Override
    public String toSQL() {
        return condition;
    }
}
