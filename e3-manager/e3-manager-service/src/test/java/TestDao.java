import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 测试数据库连接
 */


public class TestDao {

    @Test
    public void testConncation(){

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-dao.xml");
    }
}
