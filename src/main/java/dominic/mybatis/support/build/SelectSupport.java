package dominic.mybatis.support.build;

import dominic.mybatis.bean.PageParam;
import com.google.common.base.Preconditions;
import dominic.mybatis.support.OrderSupport;
import dominic.mybatis.support.appender.AbstractAppender;
import dominic.mybatis.support.build.ISelectSupport;
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
public class SelectSupport implements ISelectSupport {
    /**
     * select部分
     */
    private String selectFields;

    private String tableName;
    /**
     * where部分
     */
    private String conditions;
    /**
     * orders部分
     */
    private AbstractAppender<OrderSupport> orderSupportAppender;

    private PageParam pageParam;

    @Override
    public String SQL() {
        Preconditions.checkNotNull(selectFields, "selectFields 不能为空！");
        Preconditions.checkNotNull(tableName, "tableName 不能为空！");
        StringBuilder builder = new StringBuilder("select ");
        builder.append(selectFields)
                .append(" from ")
                .append(tableName);
        afterTable(builder);
        if (pageParam != null) {
            if (pageParam.getPageSize() != null && pageParam.getPageSize() > 0) {
                if (pageParam.getPageIndex() == null || pageParam.getPageIndex() < 0) {
                    pageParam.setPageIndex(0);
                }
                builder.append(pageParam.SQL());
            }
        }
        return builder.toString();
    }

    @Override
    public String countSQL() {
        Preconditions.checkNotNull(tableName, "tableName 不能为空！");
        StringBuilder builder = new StringBuilder("select ");
        builder.append("count(1)")
                .append(" from ")
                .append(tableName);
        afterTable(builder);
        return builder.toString();
    }

    private void afterTable(StringBuilder builder) {
        if (StringUtils.isNotBlank(conditions)) {
            builder.append(" where ").append(conditions);
        }
        if (orderSupportAppender != null) {
            builder.append(" order by ").append(orderSupportAppender.SQL());

        }
    }
}
