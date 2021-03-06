package dominic.mybatis.support.build;

import dominic.mybatis.support.SupportUnit;
import lombok.Data;

/**
 * Created by herongxing on 2017/2/28 15:08.
 */
@Data
public class OrderSupport implements SupportUnit {
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";
    /**
     * 排序的字段
     */
    private String orderField;
    /**
     * 排序方向，及ASC或DESC
     */
    private String direction;

    public static OrderSupport ASC(String orderField) {
        OrderSupport support = new OrderSupport();
        support.setOrderField(orderField);
        support.setDirection(OrderSupport.ASC);
        return support;
    }

    public static OrderSupport DESC(String orderField) {
        OrderSupport support = new OrderSupport();
        support.setOrderField(orderField);
        support.setDirection(OrderSupport.DESC);
        return support;
    }

    @Override
    public String SQL() {
        return orderField + " " + direction;
    }

    @Override
    public String toString() {
        return SQL();
    }
}
