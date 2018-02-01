package dominic.mybatis.support;

import com.google.common.base.Preconditions;
import dominic.mybatis.support.appender.AbstractAppender;
import lombok.Data;
import lombok.NonNull;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

/**
 * Created by Administrator:herongxing on 2018/1/31 17:44.
 */
@Data
public class Condition implements SupportUnit {

    private RestrictionUnit restriction;

    /**
     * 表示该条件表达式是与还是或条件，取值为"AND"、"OR"
     * null 的话默认为 AND
     */
    private String separator = AbstractAppender.AND;

    public static Condition create(RestrictionUnit restriction) {
        return create(restriction, AbstractAppender.AND);
    }

    public static Condition create(@NonNull RestrictionUnit restriction, String separator) {
        if (StringUtils.isBlank(separator)) {
            separator = AbstractAppender.AND;
        }

        Preconditions.checkArgument(AbstractAppender.AND.equals(separator) || AbstractAppender.OR.equals(separator),
                "separator只允许为and或or");

        Condition condition = new Condition();
        condition.setRestriction(restriction);
        condition.setSeparator(separator);
        return condition;
    }

    @Override
    public String SQL() {
        if (Objects.equals(separator, AbstractAppender.OR)) {
            return "(" + restriction.SQL() + ")";
        } else {
            return restriction.SQL();
        }
    }

    @Override
    public String toString() {
        return SQL();
    }
}
