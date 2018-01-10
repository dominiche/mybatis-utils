package dominic.mybatis.support;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import dominic.mybatis.constants.MybatisUtils;
import dominic.mybatis.utils.utils.Separator;
import lombok.*;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;

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

    public static <T> Restriction eq(String name, @NonNull T value) {
        Restriction build = Restriction.builder().condition(getName(name) + "=" + MybatisUtils.segment(MybatisUtils.CONDITION_SEGMENT_PREFIX, name)).build();
        buildParamMap(name, value, build);
        return build;
    }

    private static <T> void buildParamMap(String name, @NonNull T value, Restriction build) {
        HashMap<String, Object> hashMap = Maps.newHashMap();
        hashMap.put(name, value);
        build.setParamMap(hashMap);
    }

    private static String getName(String name) {
        String result = name;
        String[] split = name.split("\\.");
        if (split.length == 2) {
            //联表的条件，有表别名前缀
            String prefix = split[0];
            String realName = split[1];
            if (realName.startsWith("`")) {
                result = prefix + "." + realName;
            } else {
                result = prefix + "." + "`" + realName + "`";
            }
        } else {
            if (!name.startsWith("`")) {
                result = "`" + name + "`";
            }
        }
        return result;
    }

    public static <T> Restriction notEq(String name, @NonNull T value) {
        Restriction build = Restriction.builder().condition(getName(name) + "!=" + MybatisUtils.segment(MybatisUtils.CONDITION_SEGMENT_PREFIX, name)).build();
        buildParamMap(name, value, build);
        return build;
    }

    public static Restriction isNull(String name) {
        Restriction build = Restriction.builder().condition(getName(name) + "is NULL").build();
        build.setParamMap(Maps.newHashMap());
        return build;
    }

    public static Restriction isNotNull(String name) {
        Restriction build = Restriction.builder().condition(getName(name) + "is NOT NULL").build();
        build.setParamMap(Maps.newHashMap());
        return build;
    }

    public static Restriction like(String name, @NonNull String value) {
        Restriction build = Restriction.builder().condition(getName(name) + " like CONCAT(" + MybatisUtils.segment(MybatisUtils.CONDITION_SEGMENT_PREFIX, name) + ",'%')").build();
        buildParamMap(name, value, build);
        return build;
    }

    public static Restriction likeBoth(String name, @NonNull String value) {
        Restriction build = Restriction.builder().condition(getName(name) + " like CONCAT('%'," + MybatisUtils.segment(MybatisUtils.CONDITION_SEGMENT_PREFIX, name) + ",'%')").build();
        buildParamMap(name, value, build);
        return build;
    }

    public static <R> Restriction in(String name, Collection<R> collection) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(collection), "arg list can't be empty!");
        boolean first = true;
        StringBuilder builder = new StringBuilder(getName(name));
        builder.append(" IN (");
        int count = 0;
        HashMap<String, Object> hashMap = Maps.newHashMap();
        for (R r : collection) {
            first = isFirstAndAppend(builder, first, Separator.SEPARATOR_COMMA);
            String key = name + "_" + count;
            builder.append(MybatisUtils.segment(MybatisUtils.CONDITION_SEGMENT_PREFIX, key));
            hashMap.put(key, r);
            ++count;
        }
        builder.append(")");

        Restriction build = Restriction.builder().condition(builder.toString()).build();
        build.setParamMap(hashMap);
        return build;
    }

//    @Deprecated
//    public static Restriction dateGe(String name, @NonNull String value) {
//        return Restriction.builder().condition(getName(name) + " >='" + SQLInjectPolicy.transform(value) + "'").build();
//    }
//
//    @Deprecated
//    public static Restriction dateLe(String name, @NonNull String value) {
//        return Restriction.builder().condition(getName(name) + " <='" + SQLInjectPolicy.transform(value) + "'").build();
//    }



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
        Restriction build = Restriction.builder().condition(getName(name) + " " + equalSign + " " + MybatisUtils.segment(MybatisUtils.CONDITION_SEGMENT_PREFIX, name)).build();
        buildParamMap(name, value, build);
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
