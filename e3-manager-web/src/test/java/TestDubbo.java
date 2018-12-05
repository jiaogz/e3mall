import cn.e3mall.service.ItemService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 测试dubbo服务
 */

public class TestDubbo {


    /**
     * 消费服务
     */
    @Test
    public void consumerServiceTest(){

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("springmvc.xml");

        context.start();

        ItemService itemService = context.getBean(ItemService.class);

        System.out.println(itemService.getItemById(10L));

        try {
            System.in.read();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
