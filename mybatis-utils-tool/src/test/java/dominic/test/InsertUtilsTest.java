package dominic.test;

import com.google.common.collect.Lists;
import dominic.mybatis.support.stream.UpdateFields;
import dominic.mybatis.utils.InsertUtils;
import dominic.mybatis.utils.UpdateFieldsUtils;
import dominic.test.dto.LsConfig;
import org.junit.Test;

/**
 * Created by Administrator:herongxing on 2017/6/28 14:13.
 */
public class InsertUtilsTest {
    @Test
    public void testBuildConditionByBean() {
        LsConfig dto = LsConfig.builder()
//                .id(123)
                .name("----test--------")
                .description("description test")
                .key("TEST-1")
                .value("VALUE-1")
                .build();
        LsConfig dto2 = LsConfig.builder()
//                .id(123)
                .name("----test--------")
                .description("description test")
                .key("TEST-2")
                .value("VALUE-2")
                .build();
        LsConfig dto3 = LsConfig.builder()
//                .id(123)
                .name("----test--------")
                .description("description test")
                .key("TEST-3")
                .value("VALUE-3")
                .build();
        String sql = InsertUtils.build(Lists.newArrayList(dto, dto2, dto3));
        System.out.println(sql);

        String sql2 = InsertUtils.build(dto3);
        System.out.println(sql2);
    }

    @Test
    public void testNullUpdateByBean() {
        LsConfig dto2 = LsConfig.builder()
//                .id(123)
                .name("----test--------")
//                .description("description test")
                .key("TEST-2")
                .value("VALUE-2")
                .build();
        UpdateFields fields = UpdateFieldsUtils.buildUpdateFields(dto2);
        System.out.println(fields.SQL());
    }
}
