package dominic.mybatis.support.appender;


import dominic.mybatis.support.SupportUnit;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by herongxing on 2017/2/28 14:39.
 */
@Data
public abstract class AbstractAppender<T extends SupportUnit> implements SupportUnit {
    public static final String COMMA = ",";
    public static final String SPACE = " ";
    public static final String AND = " and ";


    private List<T> appenderList = new ArrayList<>();

    public abstract String getSeparator();

    public AbstractAppender<T> append(T t) {
        appenderList.add(t);
        return this;
    }

    @Override
    public String SQL() {
        if (CollectionUtils.isEmpty(appenderList)) {
            return "";
        }
        boolean first = true;
        StringBuilder builder = new StringBuilder();
        for (T support : appenderList) {
            if (first) {
                first = false;
            } else {
                builder.append(getSeparator());
            }
            builder.append(support.SQL());
        }
        return builder.toString();
    }
}
