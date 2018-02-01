package dominic.test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dominic.mybatis.support.Restriction;
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
        Restrictions build = Restrictions.builder().eq("hello", "string").eq("efg",546).notIn("word", Lists.newArrayList(1,2,3)).build();
        build = Restrictions.builder(build).greaterEqual("abc", 1234).build();
        System.out.println(build.SQL());
    }

    @Test
    public void testOr() {
        Restrictions build = Restrictions.builder().eq("hello", "string").eq("efg",546).build();
        build = Restrictions.builder().or(build).greaterEqual("abc", 1234).build();
        build = Restrictions.builder().eq("test", 568).or(build).or(Restriction.eq("orTest", 123)).greaterEqual("abc", 1234).build();
        build.or(Restriction.eq("haha", "holding"));
        System.out.println(build.SQL());
        System.out.println(build.directSQL());
    }
}
