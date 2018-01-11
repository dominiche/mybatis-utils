package dominic.mybatis.support;

import com.google.common.collect.Maps;
import dominic.mybatis.constants.MybatisUtils;
import lombok.*;

import java.util.HashMap;

/**
 * Created by herongxing on 2017/2/27 18:39.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateField extends UpdateFieldUnit {
    /**
     * 直接是set的内容，如：
     * status=1
     * zoneCode='TGQ123434'
     *
     * --->status=#{status}
     */
    private String field;

    public static <T> UpdateField set(@NonNull String name, T value) {
        String field = name + "=" + MybatisUtils.segment(MybatisUtils.UPDATE_SEGMENT_PREFIX, name);
        UpdateField build = UpdateField.builder().field(field).build();
        HashMap<String, Object> hashMap = Maps.newHashMap();
        hashMap.put(MybatisUtils.keyName(MybatisUtils.UPDATE_SEGMENT_PREFIX, name), value);
        build.setParamMap(hashMap);
        return build;
    }

    public static UpdateField setBySql(@NonNull String field) {
        UpdateField build = UpdateField.builder().field(field).build();
        build.setParamMap(Maps.newHashMap());
        return build;
    }

    @Override
    public String SQL() {
        return field;
    }

    @Override
    public String toString() {
        return SQL();
    }
}
