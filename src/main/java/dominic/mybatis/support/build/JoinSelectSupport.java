package dominic.mybatis.support.build;

import dominic.mybatis.bean.PageParam;
import com.google.common.base.Preconditions;
import dominic.mybatis.support.JoinSupport;
import dominic.mybatis.support.OrderSupport;
import dominic.mybatis.support.TableSupport;
import dominic.mybatis.support.appender.AbstractAppender;
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
public class JoinSelectSupport implements SelectSupportUnit {
    /**
     * select部分，带有表别名
     */
    private String selectFields;

    private TableSupport tableSupport;

    private AbstractAppender<JoinSupport> joinSupportAppender;
    /**
     * where部分，应带有表别名
     */
    private String conditions;
    /**
     * orders部分，应带有表别名
     */
    private AbstractAppender<OrderSupport> orderSupportAppender;

    private PageParam pageParam;

    @Override
    public String SQL() {
        Preconditions.checkNotNull(selectFields, "selectFields 不能为空！");
        Preconditions.checkNotNull(tableSupport, "tableSupport 不能为空！");
        StringBuilder builder = new StringBuilder("select ");
        builder.append(selectFields)
                .append(" from ")
                .append(tableSupport.SQL());
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
        Preconditions.checkNotNull(selectFields, "selectFields 不能为空！");
        Preconditions.checkNotNull(tableSupport, "tableSupport 不能为空！");
        StringBuilder builder = new StringBuilder("select ");
        builder.append("count(1)")
                .append(" from ")
                .append(tableSupport.SQL());
        afterTable(builder);
        return builder.toString();
    }

    private void afterTable(StringBuilder builder) {
        if (joinSupportAppender != null) {
            builder.append(" ").append(joinSupportAppender.SQL());
        }
        if (StringUtils.isNotBlank(conditions)) {
            builder.append(" where ").append(conditions);
        }
        if (orderSupportAppender != null) {
            builder.append(" order by ").append(orderSupportAppender.SQL());

        }
    }
}
