package dominic.mybatis.support;

import dominic.mybatis.utils.SQLInjectPolicy;
import dominic.mybatis.utils.utils.DateUtils;
import lombok.*;

import java.util.Date;

/**
 * Created by herongxing on 2017/2/27 18:39.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateField implements UpdateFieldUnit {
    /**
     * 直接是set的内容，如：
     * status=1
     * zoneCode='TGQ123434'
     */
    private String field;

    public static <T> UpdateField set(@NonNull String name, @NonNull T value) {
        if (value instanceof String) {
            return UpdateField.builder().field(name + "='" + SQLInjectPolicy.transform((String) value) + "'").build();
        } else if (value instanceof Date) {
            return UpdateField.builder().field(name + "='" + DateUtils.TIME_FORMAT.format((Date) value) + "'").build();
        }
        return UpdateField.builder().field(name + "=" + value).build();
    }

    public static UpdateField setBySql(@NonNull String field) {
        return UpdateField.builder().field(field).build();
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
