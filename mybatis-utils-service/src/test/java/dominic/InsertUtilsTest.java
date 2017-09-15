package dominic;

import dominic.mybatis.provider.BaseUpdateProvider;
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
                .description("description test")
                .key("TEST-1")
                .value("VALUE-1")
                .build();

        BaseUpdateProvider baseUpdateProvider = new BaseUpdateProvider();

        Map<String, Object> map = new HashMap<>();
        map.put("bean", dto);
        map.put("tableName", "ls_config");

        baseUpdateProvider.insert(map);
    }
}
