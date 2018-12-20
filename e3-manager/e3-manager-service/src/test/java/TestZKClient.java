import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * Zk客户端
 */
public class TestZKClient {

    // 会话超时时间，设置为系统默认时间一致
    private static final int SESSION_TIMEOUT = 30000;

    // 创建ZooKeeper实例
    ZooKeeper zk;

    // 创建Watcher实例
    Watcher wh = new Watcher() {
        public void process(WatchedEvent event) {
            System.out.println(event.toString());
        }
    };

    // 初始化ZooKeeper实例
    private void createZKInstance() throws Exception {
        zk = new ZooKeeper("192.168.224.170:2181", SESSION_TIMEOUT, wh);
    }

    private void ZKOperations() throws Exception {
        System.out.println("\n1. 创建 ZooKeeper 节点 (znode: zoo2, 数据: myData2, " +
                "权限: OPEN_ACL_UNSAFE, 节点类型: Persistent)");
        zk.create("/zoo2", "myData2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        System.out.println("\n2. 查看是否创建成功:");
        System.out.println(new String(zk.getData("/zoo2", false, null)));

        System.out.println("\n3. 修改节点数据");
        Stat setDataStat = zk.setData("/zoo2", "zhangyi03201509192339".getBytes(), -1);


        System.out.println("\n4. 查看是否修改成功:");
        System.out.println(new String(zk.getData("/zoo2", false, null)));

        System.out.println("\n5. 添加子节点");
        for (int index = 0; index < 3; index++) {
            zk.create("/zoo2/" + "child_" + (index+1), ("child_" + (index+1)).getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        System.out.println("\n6. 查看所有子节点:");
        List<String> childList = zk.getChildren("/zoo2", false);
        for (String child : childList) {
            System.out.println("\t\t\t 子节点:" + child);
        }

        System.out.println("\n7. 删除所有子节点");
        for (String child : childList) {
            zk.delete("/zoo2" + "/" + child, -1);
        }

        System.out.println("\n8. 删除节点");
        zk.delete("/zoo2", -1);

        System.out.println("\n9. 查看节点是否被删除:");
        System.out.println("节点状态: [" + zk.exists("/zoo2", false) + "]");
    }

    private void ZKClose() throws Exception {
        zk.close();
    }

}
