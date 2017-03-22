package dominic.mybatis.support;

/**
 * Created by herongxing on 2017/2/28 14:31.
 */
public class UpdateFieldsAppender extends AbstractAppender<UpdateField>{

    public static UpdateFieldsAppender newInstance() {
        return new UpdateFieldsAppender();
    }

    @Override
    public String getSeparator() {
        return AbstractAppender.COMMA;
    }
}
