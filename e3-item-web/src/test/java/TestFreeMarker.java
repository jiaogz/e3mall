import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public class TestFreeMarker {

    @Test
    public void TestFreeMarker() throws Exception {
        //1.创建一个模板文件
        //2.创建一个Configuration对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //3.设置模板文件路径和编码格式
        configuration.setDirectoryForTemplateLoading(new File("E:\\e3mall\\e3-item-web\\src\\main\\webapp\\WEB-INF\\ftl\\student.ftl"));
        configuration.setDefaultEncoding("utf-8");
        //4.加载模板
        Template template = configuration.getTemplate("student.ftl");
        //5.生成静态页面
        Map date = new HashMap<>();
        date.put("hello","hello freemarker");
        date.put("key","基础变量");
//        POJO对象测试
        Student student = new Student(1,"小明",18,"北京");
        date.put("student",student);

        //list测试
        List list = new ArrayList();
        Student student1 = new Student(1,"小明",18,"北京");
        Student student2 = new Student(1,"小明",18,"北京");
        Student student3 = new Student(1,"小明",18,"北京");
        Student student4 = new Student(1,"小明",18,"北京");
        list.add(student1);
        list.add(student2);
        list.add(student3);
        list.add(student4);

        date.put("list",list);

        //日期
        date.put("date",new Date());

        //null值
        date.put("val",null);

        Writer writer = new FileWriter("E:\\e3mall\\e3-item-web\\student.html");
        template.process(date,writer);
        //关闭流
        writer.close();
    }
}
