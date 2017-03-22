package dominic.mybatis.support;

/**
 * Created by herongxing on 2017/2/28 15:14.
 */
public class RestrictionSupportAppender extends AbstractAppender<Restriction> {
    @Override
    public String getSeparator() {
        return AbstractAppender.AND;
    }

    public static RestrictionSupportAppender newInstance() {
        return new RestrictionSupportAppender();
    }
}
