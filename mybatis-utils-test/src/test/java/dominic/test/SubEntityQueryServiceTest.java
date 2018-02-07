package dominic.test;

import com.alibaba.fastjson.JSON;
import dominic.Application;
import dominic.dal.entity.Sub;
import dominic.dal.service.query.SubEntityQueryService;
import dominic.dto.SubEntityQueryDTO;
import dominic.mybatis.bean.PageParam;
import dominic.mybatis.support.Restriction;
import dominic.mybatis.support.appender.AbstractAppender;
import dominic.mybatis.support.appender.OrderSupportAppender;
import dominic.mybatis.support.build.OrderSupport;
import dominic.mybatis.support.stream.Restrictions;
import org.assertj.core.util.Lists;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class SubEntityQueryServiceTest {
    @Autowired
    private SubEntityQueryService subEntityQueryService;

    @Test
    public void testQueryByBean() {
        SubEntityQueryDTO subQueryDTO = SubEntityQueryDTO.builder()
                .userNames(Lists.newArrayList("406226662983"))
                .payDateStart(DateTime.parse("2017-06-20").toDate())
                .payDateEnd(DateTime.parse("2017-06-30").toDate())
//                .subDateStart(DateTime.parse("2017-06-25").toDate())
//                .subDateEnd(DateTime.parse("2017-06-30").toDate())
                .status(Lists.newArrayList(3,4))
                .build();

        List<Sub> subDTOList = subEntityQueryService.queryByBean(subQueryDTO);
        System.out.println(JSON.toJSONString(subDTOList));
    }

    @Test
    public void testQueryByBeanAndPage() {
        SubEntityQueryDTO subQueryDTO = SubEntityQueryDTO.builder()
                .userNames(Lists.newArrayList("406226662983"))
//                .payDateStart(DateTime.parse("2017-06-20").toDate())
                .payDateEnd(DateTime.parse("2017-06-30").toDate())
                .status(Lists.newArrayList(3,4))
                .build();

        PageParam pageParam = PageParam.builder().pageIndex(0).pageSize(10).build();
        AbstractAppender<OrderSupport> orderSupportAppender = OrderSupportAppender.newInstance()
                .append(OrderSupport.DESC("sub_date")).append(OrderSupport.DESC("actual_total"));
        List<Sub> subDTOList = subEntityQueryService.queryByBean(subQueryDTO, pageParam, orderSupportAppender);
        System.out.println(subDTOList);
    }

    @Test
    public void testQueryCount() {
        SubEntityQueryDTO subQueryDTO = SubEntityQueryDTO.builder()
                .payDateEnd(DateTime.parse("2018-06-30").toDate())
                .status(Lists.newArrayList(3,4))
                .build();
        long count = subEntityQueryService.queryCount(subQueryDTO);
        System.out.println(count);
    }

    @Test
    public void testQueryByRestriction() {
        List<Sub> subDTOList = subEntityQueryService.query(Restriction.eq("user_name", "406226662983"));
        System.out.println(subDTOList);
    }

    @Test
    public void testQueryByRestrictions() {
        Restrictions restrictions = Restrictions.builder().eq("user_name", "406226662983")
                .in("status", Lists.newArrayList(3,4))
                .lessEqual("pay_date", "2017-06-30").build();
        List<Sub> subDTOList = subEntityQueryService.query(restrictions);
        System.out.println(subDTOList);
    }

    @Test
    public void testQueryUnique() {
        Sub sub = subEntityQueryService.queryUnique(Restriction.eq("sub_number", "O1234"));
        System.out.println(sub);
    }

    @Test
    public void testQueryById() {
        Sub sub = subEntityQueryService.queryById(1L);
        System.out.println(sub);
    }

    @Test
    public void testQueryByIds() {
        List<Sub> subList = subEntityQueryService.queryById(Lists.newArrayList(1L, 2L));
        System.out.println(subList);
    }

    @Test
    public void testQuerySingleValue() {
        String userName = subEntityQueryService.querySingleValue("user_name", Restriction.eq("sub_number", "O1234"));
        System.out.println(userName);
    }


}
