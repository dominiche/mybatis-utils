package dominic.mybatis.support.appender;

import dominic.mybatis.support.build.JoinSupport;

/**
 * Created by herongxing on 2017/2/28 14:31.
 */
public class JoinSupportAppender extends AbstractAppender<JoinSupport> {

    public static JoinSupportAppender newInstance() {
        return new JoinSupportAppender();
    }

    @Override
    public String getSeparator() {
        return AbstractAppender.SPACE;
    }
}
