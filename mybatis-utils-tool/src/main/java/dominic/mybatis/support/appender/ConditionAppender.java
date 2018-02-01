package dominic.mybatis.support.appender;

import dominic.mybatis.support.Condition;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * Created by herongxing on 2017/2/28 15:14.
 */
public class ConditionAppender extends AbstractAppender<Condition> {
    @Override
    public String getSeparator() {
        return AbstractAppender.AND;
    }

    public static ConditionAppender newInstance() {
        return new ConditionAppender();
    }

    @Override
    public String SQL() {
        List<Condition> appenderList = getAppenderList();
        if (CollectionUtils.isEmpty(appenderList)) {
            return "";
        }

        //sort, 把所有的or语句都放到最后，方便构建sql
        appenderList.sort((o1, o2) -> {
            boolean or1 = Objects.equals(o1.getSeparator(), AbstractAppender.OR);
            boolean or2 = Objects.equals(o2.getSeparator(), AbstractAppender.OR);
            int result = 0;
            if (!or1 && or2 ) {
                result = -1;
            } else if (or1 && !or2) {
                result = 1;
            }
            return result;
        });

        boolean first = true;
        StringBuilder builder = new StringBuilder();
        for (Condition condition : appenderList) {
            if (first) {
                first = false;
                builder.append(condition.SQL());
            } else {
                builder.append(condition.getSeparator());
                builder.append(condition.SQL());
            }
        }
        return builder.toString();
    }
}
