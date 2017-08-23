package dominic.mybatis.support.appender;

import dominic.mybatis.support.build.OrderSupport;

/**
 * Created by herongxing on 2017/2/28 15:14.
 */
public class OrderSupportAppender extends AbstractAppender<OrderSupport> {
    @Override
    public String getSeparator() {
        return AbstractAppender.COMMA;
    }

    public static OrderSupportAppender newInstance() {
        return new OrderSupportAppender();
    }
}
