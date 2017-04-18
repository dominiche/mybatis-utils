package dominic.mybatis.support.appender;

import dominic.mybatis.support.Restriction;

/**
 * Created by herongxing on 2017/2/28 15:14.
 */
public class RestrictionAppender extends AbstractAppender<Restriction> {
    @Override
    public String getSeparator() {
        return AbstractAppender.AND;
    }

    public static RestrictionAppender newInstance() {
        return new RestrictionAppender();
    }
}
