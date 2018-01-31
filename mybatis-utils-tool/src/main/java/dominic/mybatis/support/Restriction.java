package dominic.mybatis.support;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import dominic.mybatis.constants.MybatisUtils;
import dominic.mybatis.utils.utils.Separator;
import lombok.*;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

import static dominic.mybatis.utils.SqlBuildUtils.isFirstAndAppend;

/**
 * Created by herongxing on 2017/2/27 18:39.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Restriction extends RestrictionUnit {
    private String condition;

    private static <T> void buildParamMap(String paramName, @NonNull T value, Restriction build) {
        HashMap<String, Object> hashMap = Maps.newHashMap();
        hashMap.put(paramName, value);
        build.setParamMap(hashMap);
    }

    private static Restriction buildRestriction(String name, String middleSign, Object value) {
        String fieldName = getName(name);
        if (Objects.isNull(value)) {
            Restriction build = Restriction.builder().condition(fieldName + middleSign).build();
            build.setParamMap(Maps.newHashMap());
            return build;
        } else {
            String paramName = MybatisUtils.paramName(MybatisUtils.CONDITION_SEGMENT_PREFIX, name);
            Restriction build = Restriction.builder().condition(fieldName + middleSign + MybatisUtils.paramSegment(paramName)).build();
            HashMap<String, Object> hashMap = Maps.newHashMap();
            hashMap.put(paramName, value);
            build.setParamMap(hashMap);
            return build;
        }
    }

    private static String getName(String name) {
        String result = name;
        String[] split = name.split("\\.");
        if (split.length == 2) {
            //联表的条件，有表别名前缀
            String prefix = split[0];
            String realName = split[1];
            if (!realName.startsWith("`")) {
                result = prefix + "." + "`" + realName + "`";
            }
        } else {
            if (!name.startsWith("`")) {
                result = "`" + name + "`";
            }
        }
        return result;
    }

    public static <T> Restriction eq(String name, @NonNull T value) {
        return buildRestriction(name, "=", value);
    }

    public static <T> Restriction notEq(String name, @NonNull T value) {
        return buildRestriction(name, "!=", value);
    }

    public static Restriction greaterEqual(String name, Object value) {
        return buildRestriction(name, ">=", value);
    }
    public static Restriction greaterThan(String name, Object value) {
        return buildRestriction(name, ">", value);
    }
    public static Restriction lessEqual(String name, Object value) {
        return buildRestriction(name, "<=", value);
    }
    public static Restriction lessThan(String name, Object value) {
        return buildRestriction(name, "<", value);
    }

    public static Restriction isNull(String name) {
        return buildRestriction(name, "is NULL", null);
    }

    public static Restriction isNotNull(String name) {
        return buildRestriction(name, "is NOT NULL", null);
    }

    public static Restriction like(String name, @NonNull String value) {
        String fieldName = getName(name);
        String paramName = MybatisUtils.paramName(MybatisUtils.CONDITION_SEGMENT_PREFIX, name);
        Restriction build = Restriction.builder().condition(fieldName + " like CONCAT(" + MybatisUtils.paramSegment(paramName) + ",'%')").build();
        buildParamMap(paramName, value, build);
        return build;
    }

    public static Restriction likeBoth(String name, @NonNull String value) {
        String fieldName = getName(name);
        String paramName = MybatisUtils.paramName(MybatisUtils.CONDITION_SEGMENT_PREFIX, name);
        Restriction build = Restriction.builder().condition(fieldName + " like CONCAT('%'," + MybatisUtils.paramSegment(paramName) + ",'%')").build();
        buildParamMap(name, value, build);
        return build;
    }

    public static <R> Restriction in(String name, Collection<R> collection) {
        return inRestriction(name, collection, "IN");
    }

    public static <R> Restriction notIn(String name, Collection<R> collection) {
        return inRestriction(name, collection, "NOT IN");
    }

    private static <R> Restriction inRestriction(String name, Collection<R> collection, String inSign) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(collection), "arg list can't be empty!");
        boolean first = true;
        StringBuilder builder = new StringBuilder(getName(name));
        builder.append(" ").append(inSign).append(" (");
        int count = 0;
        HashMap<String, Object> hashMap = Maps.newHashMap();
        String paramName = MybatisUtils.paramName(MybatisUtils.CONDITION_SEGMENT_PREFIX, name);
        for (R r : collection) {
            first = isFirstAndAppend(builder, first, Separator.SEPARATOR_COMMA);
            String key = paramName + "_" + count;
            builder.append(MybatisUtils.paramSegment(key));
            hashMap.put(key, r);
            ++count;
        }
        builder.append(")");

        Restriction build = Restriction.builder().condition(builder.toString()).build();
        build.setParamMap(hashMap);
        return build;
    }

    public static Restriction sql(@NonNull String restriction) {
        Restriction build = Restriction.builder().condition(restriction).build();
        build.setParamMap(Maps.newHashMap());
        return build;
    }

    @Override
    public String SQL() {
        return condition;
    }

    @Override
    public String toString() {
        return SQL();
    }
}
