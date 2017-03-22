package dominic.test;

import com.google.common.collect.Lists;
import dominic.mybatis.support.Restriction;
import org.junit.Test;

/**
 * Created by Administrator:herongxing on 2017/3/22 19:07.
 */
public class RestrictionTest {
    @Test
    public void testIn() {
        Restriction in = Restriction.in("id", Lists.newArrayList("a'bc", "cd'f", "123"));
        System.out.println(in.toSQL());
        Restriction in2 = Restriction.in("id", Lists.newArrayList(1,2,3,4,5));
        System.out.println(in2.toSQL());
    }

    @Test
    public void testEq() {
        Restriction in = Restriction.eq("id", "a'bc");
        System.out.println(in.toSQL());
    }
}
