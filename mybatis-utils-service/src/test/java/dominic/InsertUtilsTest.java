package dominic;

import com.google.common.collect.Lists;
import dominic.mybatis.provider.BaseUpdateProvider;
import dominic.mybatis.utils.InsertUtils;
import dominic.test.dto.LsConfig;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator:herongxing on 2017/6/28 14:13.
 */
public class InsertUtilsTest {

    @Test
    public void testSingleInsert() {
        LsConfig dto = LsConfig.builder()
//                .id(123)
                .name("----test--------")
//                .description("description test")
                .key("TEST-1")
                .value("VALUE-1")
                .build();

        BaseUpdateProvider baseUpdateProvider = new BaseUpdateProvider();

        Map<String, Object> map = new HashMap<>();
        map.put("bean", dto);
        map.put("tableName", "ls_config");

        baseUpdateProvider.insert(map);
    }

    @Test
    public void testSingleInsert2() {
        LsConfig dto = LsConfig.builder()
//                .id(123)
                .name("----test--------")
//                .description("description test")
                .key("TEST-1")
                .value("VALUE-1")
                .build();
        String sql = InsertUtils.build("ls_config", dto);
        System.out.println(sql);
    }

    @Test
    public void testInsertList() {
        LsConfig dto = LsConfig.builder()
//                .id(123)
                .name("----test--------")
//                .description("description test")
                .key("TEST-1")
                .value("VALUE-1")
                .build();
        LsConfig dto2 = LsConfig.builder()
//                .id(123)
                .name("----test2--------")
//                .description("description test")
                .key("TEST-1")
                .value("VALUE-1")
                .build();
        String sql = InsertUtils.build("ls_config", Lists.newArrayList(dto,dto2));
        System.out.println(sql);
    }
}
