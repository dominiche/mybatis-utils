package dominic.mybatis.support;

import dominic.mybatis.utils.SQLInjectPolicy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by herongxing on 2017/2/27 18:39.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateField implements ISupport{
    /**
     * 直接是set的内容，如：
     * status=1
     * zoneCode='TGQ123434'
     */
    private String field;

    public static <T> UpdateField set(String name, T value) {
        if (value instanceof String) {
            return UpdateField.builder().field(name + "='" + SQLInjectPolicy.transform((String) value) + "'").build();
        }
        return UpdateField.builder().field(name + "=" + value).build();
    }

    public static UpdateField setBySql(String field) {
        return UpdateField.builder().field(field).build();
    }

    @Override
    public String toSQL() {
        return field;
    }
}
