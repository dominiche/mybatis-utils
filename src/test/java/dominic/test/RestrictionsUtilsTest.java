package dominic.test;

import com.google.common.collect.Sets;
import dominic.mybatis.utils.RestrictionsUtils;
import dominic.test.dto.ExchangeBillSearchDTO;
import org.junit.Test;

/**
 * Created by Administrator:herongxing on 2017/6/28 14:13.
 */
public class RestrictionsUtilsTest {
    @Test
    public void testBuildConditionByBean() {
        ExchangeBillSearchDTO dto = new ExchangeBillSearchDTO();
        dto.setStatus(1);
        dto.setSupplierName("乐麦");
        dto.setSubItemNumbers(Sets.newHashSet("E12341","D45456","F567456"));
        String sql = RestrictionsUtils.buildConditions(dto).toString();
        System.out.println(sql);
    }
}
