import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;

public class testActionMq {

    /**
     * 点对点的生产者：消息会保存在服务端，只有消费者接受以后才会删除消息
     * @throws Exception
     */
    @Test
    public void queueProducer() throws Exception{
//        1.创建一个连接工程对象，参数：actionMQ服务地址
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.246.128:61616");
//        2.获取连接对象
        Connection connection = factory.createConnection();
//        3.开启连接
        connection.start();
//        4.获取会话对象:false:不开启事务，Session.AUTO_ACKNOWLEDGE:自动应答
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
//        5.设置发送方式：queue、topic,参数：队列名称
        Queue queue = session.createQueue("test_Queue");
//        6.设置生产者
        MessageProducer producer = session.createProducer(queue);
//        7.设置消息
//        TextMessage message = session.createTextMessage("测试发送点对点消息");
        TextMessage message = new ActiveMQTextMessage();
        message.setText("第二种设置message的方式");
//        8.发送
        producer.send(message);
//        9.关闭连接
        producer.close();
        session.close();
        connection.close();
    }

    /**
     * 点对点消费消息
     * @throws Exception
     */
    @Test
    public void QueueConsumer() throws Exception {
//        1.创建连接工程，指定ActiveMq服务器地址
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.246.128:61616");
//        2.获取连接并启动
        Connection connection = factory.createConnection();
        connection.start();
//        3.创建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        4.设置队列形式：Queue
        Queue queue = session.createQueue("test_Queue");
//        5.创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
//        6.消费消息：设置一个消费监听器，消息到达时消费
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("消息："+ textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        //一直等待，敲击键盘结束
        System.in.read();
//        7.关闭资源
        consumer.close();
        session.close();
        connection.close();
    }

    /**
     * 广播的生产者：消息不在服务端保存，要是没有消费者消费也不保存
     * @throws Exception
     */
    @Test
    public void topicProducer() throws Exception{
//        1.创建一个连接工程对象，参数：actionMQ服务地址
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.246.128:61616");
//        2.获取连接对象
        Connection connection = factory.createConnection();
//        3.开启连接
        connection.start();
//        4.获取会话对象:false:不开启事务，Session.AUTO_ACKNOWLEDGE:自动应答
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
//        5.设置发送方式：queue、topic,参数：队列名称
        Topic topic = session.createTopic("test_topic");
//        6.设置生产者
        MessageProducer producer = session.createProducer(topic);
//        7.设置消息
        TextMessage message = session.createTextMessage("测试发送广播消息");
//        8.发送
        producer.send(message);
//        9.关闭连接
        producer.close();
        session.close();
        connection.close();
    }

    /**
     * 广播形式的消费消息
     * @throws Exception
     */
    @Test
    public void topicConsumer() throws Exception {
//        1.创建连接工程，指定ActiveMq服务器地址
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.246.128:61616");
//        2.获取连接并启动
        Connection connection = factory.createConnection();
        connection.start();
//        3.创建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        4.设置队列形式：Queue
        Topic topic = session.createTopic("test_topic");
//        5.创建消费者
        MessageConsumer consumer = session.createConsumer(topic);
//        6.消费消息：设置一个消费监听器，消息到达时消费
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("消息："+ textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("消费者1已经启动。。。");
        //一直等待，敲击键盘结束
        System.in.read();
//        7.关闭资源
        consumer.close();
        session.close();
        connection.close();
    }
}
