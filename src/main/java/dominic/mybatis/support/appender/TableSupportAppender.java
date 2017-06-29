package dominic.mybatis.support.appender;

import dominic.mybatis.support.build.TableSupport;

/**
 * Created by herongxing on 2017/2/28 14:31.
 */
public class TableSupportAppender extends AbstractAppender<TableSupport> {

    public static TableSupportAppender newInstance() {
        return new TableSupportAppender();
    }

    @Override
    public String getSeparator() {
        return AbstractAppender.COMMA;
    }
}
