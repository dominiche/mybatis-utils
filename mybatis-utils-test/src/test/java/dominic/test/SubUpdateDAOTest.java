package dominic.test;

import dominic.Application;
import dominic.dal.dao.update.SubUpdateDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class SubUpdateDAOTest {
    @Autowired
    private SubUpdateDAO subUpdateDAO;

    @Test
    public void queryByIdTest() {
        int result = subUpdateDAO.updateStatusById(1L, 5);
        System.out.println(result);
    }
}
