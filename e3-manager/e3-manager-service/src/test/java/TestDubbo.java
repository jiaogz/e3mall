import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 测试dubbo服务
 */

public class TestDubbo {


    /**
     * 发布服务
     */
    @Test
    public void consumerServiceTest(){

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-service.xml");

        context.start();

        System.out.println("发布服务");

        try{
            //一直运行服务
            System.in.read();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
