import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 监听商品添加 生成静态页面
 */
public class HTMLGenListen implements MessageListener {
    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfig freeMarkerConfig;
    @Value("${ITEM_HTML_PATH}")
    private static String ITEM_HTML_PATH;

    @Override
    public void onMessage(Message message) {
        try {
            //创建一个模板对象
            //获取商品信息
            TextMessage textMessage = (TextMessage) message;
            Long itemId = new Long(textMessage.getText());
            TbItem tbItem = itemService.getItemById(itemId);
            Item item = new Item(tbItem);
            TbItemDesc itemDesc = itemService.getItemDescById(itemId);
            //加载模板对象
            Configuration configuration = freeMarkerConfig.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            //封装商品信息
            Map date = new HashMap<>();
            date.put("item",item);
            date.put("itemDesc",itemDesc);

            Writer out = new FileWriter(new File(ITEM_HTML_PATH + "/" + itemId +".html"));
            template.process(date,out);

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
