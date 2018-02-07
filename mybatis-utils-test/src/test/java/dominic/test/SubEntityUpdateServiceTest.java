package dominic.test;

import com.google.common.collect.Lists;
import dominic.Application;
import dominic.dal.entity.Sub;
import dominic.dal.service.update.SubEntityUpdateService;
import dominic.mybatis.support.Restriction;
import dominic.mybatis.support.UpdateField;
import dominic.mybatis.support.stream.Restrictions;
import dominic.mybatis.support.stream.UpdateFields;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class SubEntityUpdateServiceTest {
    @Autowired
    private SubEntityUpdateService subEntityUpdateService;

    @Test
    public void testInsert() {
        Sub order = Sub.builder()
                .userName("dominic")
                .userId("123456")
                .subDate(new Date())
                .subNumber("O1234")
                .actualTotal(BigDecimal.valueOf(0.01))
                .status(0)
                .build();
        int result = subEntityUpdateService.insert(order);
        System.out.println(order);
    }

    @Test
    public void testInsertBatch() {
        Sub order1 = Sub.builder().userName("dominic").userId("123456").subDate(new Date())
                .subNumber("O1211").actualTotal(BigDecimal.valueOf(0.01)).status(0).build();
        Sub order2 = Sub.builder().userName("dominic").userId("123456").subDate(new Date())
                .subNumber("O1212").actualTotal(BigDecimal.valueOf(0.01)).status(0).build();
        ArrayList<Sub> list = Lists.newArrayList(order1, order2);
        int result = subEntityUpdateService.insert(list);
        System.out.println(result);
    }

    @Test
    public void testSave() {
        Sub order = Sub.builder()
                .userName("dominic")
                .userId("123456")
                .subDate(new Date())
                .subNumber("O1235")
                .actualTotal(BigDecimal.valueOf(0.01))
                .status(0)
                .build();
        int result = subEntityUpdateService.save(order);
        System.out.println(order);
    }

    @Test
    public void testUpdate() {
        Sub order = Sub.builder().payDate(new Date()).status(1).build();
        int result = subEntityUpdateService.update(order, Restriction.eq("sub_id", 1));
        System.out.println(result);
    }

    @Test
    public void testUpdateByQueryBean() {
        Sub order = Sub.builder().payDate(new Date()).status(1).build();
        Sub condition = Sub.builder()
                .userName("dominic")
                .subNumber("O1235")
                .build();
        int result = subEntityUpdateService.updateByQueryBean(order, condition);
        System.out.println(result);
    }

    @Test
    public void testUpdateField() {
        int result = subEntityUpdateService.update(UpdateField.set("status", 2), Restriction.eq("sub_id", 1));
        System.out.println(result);
    }

    @Test
    public void testUpdateFields() {
        UpdateFields updateFields = UpdateFields.builder().set("status", 2).set("pay_date", new Date()).build();
        Restrictions restrictions = Restrictions.builder().eq("user_name", "dominic").eq("sub_number", "O1235").build();
        int result = subEntityUpdateService.update(updateFields, restrictions);
        System.out.println(result);
    }


    @Test
    public void testUpdateById() {
        int result = subEntityUpdateService.updateById(UpdateField.set("status", 2), 1L);
        System.out.println(result);
    }

    @Test
    public void testUpdateByIds() {
        int result = subEntityUpdateService.updateById(UpdateField.set("status", 2), Lists.newArrayList(1L, 2L));
        System.out.println(result);
    }
}
