package dominic.test;

import com.google.common.collect.Sets;
import dominic.mybatis.support.stream.Restrictions;
import dominic.mybatis.utils.RestrictionsUtils;
import dominic.test.dto.ExchangeBillSearchDTO;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Administrator:herongxing on 2017/6/28 14:13.
 */
public class RestrictionsUtilsTest {
    @Test
    public void testBuildConditionByBean() {
        ExchangeBillSearchDTO dto = new ExchangeBillSearchDTO();
        dto.setStatus(1);
        dto.setSupplierName("乐麦");
        dto.setApplyExchangeStartDate(new Date());
        dto.setApplyExchangeEndDate(new Date());
        dto.setSubItemNumbers(Sets.newHashSet("E12341","D45456","F567456"));
//        dto.setSubNumbers(Sets.newHashSet("O12341","O45456","O567456"));
//        dto.setExchangeNumbers(Sets.newHashSet("E12341","E45456","E567456"));
        String sql = RestrictionsUtils.buildConditions(dto).toString();
        System.out.println(sql);
    }

    @Test
    public void test() {
        Restrictions build = Restrictions.builder().eq("hello", "string").eq("efg",546).build();
        build = Restrictions.builder(build).greaterEqual("abc", 1234).build();
        System.out.println(build.SQL());
    }
}
