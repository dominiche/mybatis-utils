package dominic.test;

import dominic.Application;
import dominic.dal.dao.query.SubQueryDAO;
import dominic.dal.entity.Sub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class SubQueryDAOTest {
    @Autowired
    private SubQueryDAO subQueryDAO;

    @Test
    public void queryByIdTest() {
        Sub sub = subQueryDAO.queryById(1L);
        System.out.println(sub);
    }
}
