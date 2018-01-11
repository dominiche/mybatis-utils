package dominic.mybatis.support.build;

import com.google.common.base.Preconditions;
import dominic.mybatis.support.SupportUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * Created by herongxing on 2017/2/28 14:24.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSupport implements SupportUnit {
    /**
     * update fields部分
     */
    private String updateFields;

    private String tableName;
    /**
     * where部分
     */
    private String conditions;

    @Override
    public String toString() {
        return SQL();
    }

    @Override
    public String SQL() {
        Preconditions.checkNotNull(updateFields, "updateFields 不能为空！");
        Preconditions.checkNotNull(tableName, "tableName 不能为空！");
        StringBuilder builder = new StringBuilder("update ").append(tableName).append(" set ");
        builder.append(updateFields);
        if (StringUtils.isNotBlank(conditions)) {
            builder.append(" where ").append(conditions);
        }
        return builder.toString();
    }
}
