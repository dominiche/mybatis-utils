package dominic.mybatis.support.appender;

import dominic.mybatis.support.UpdateField;

/**
 * Created by herongxing on 2017/2/28 14:31.
 */
public class UpdateFieldAppender extends AbstractAppender<UpdateField> {

    public static UpdateFieldAppender newInstance() {
        return new UpdateFieldAppender();
    }

    @Override
    public String getSeparator() {
        return AbstractAppender.COMMA;
    }
}
