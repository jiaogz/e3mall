import cn.e3mall.common.utils.FastDFSClient;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class testFastDFS {
    /**
     * 测试fastDFS客户端上传文件
     * @throws Exception
     */
    @Test
    public void testUpload() throws Exception{

        //1.加载全局文件
        ClientGlobal.init("E:\\e3mall\\e3-manager-web\\src\\main\\resources\\conf\\client.conf");

        //2.创建trackerClient
        TrackerClient trackerClient = new TrackerClient();

        //3.获取trackerServer
        TrackerServer trackerServer = trackerClient.getConnection();

        //2.初始化storageClient
        StorageClient storageClient = new StorageClient(trackerServer,null);

        String[] strings = storageClient.upload_file("", "jpg", null);

        for (String str: strings) {

            System.out.println(str);
        }
    }

    @Test
    public void testFastDFSClient() throws Exception{

        FastDFSClient fastDFSClient = new FastDFSClient("E:\\e3mall\\e3-manager-web\\src\\main\\resources\\conf\\client.conf");

        String file = fastDFSClient.uploadFile("");

        System.out.println(file);
    }
}
