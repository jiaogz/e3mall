import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class testSolrCloud {
    
    @Test
    public  void addDocument() throws Exception {
//        1.获取CloudSolrServer连接，参数为zkHost
        CloudSolrServer solrServer = new CloudSolrServer("");
//        2.设置defaultCollection
        solrServer.setDefaultCollection("collection2");
//        3.创建Document对象
        SolrInputDocument document = new SolrInputDocument();
//        4.设置域
        document.setField("id","");
        document.setField("item_title","测试");
//        5.将文档添加到索引库
        solrServer.add(document);
//        6.提交
        solrServer.commit();
    }

    @Test
    public void queryDocument() throws Exception {
//        1.获取CloudSolrServer连接，参数为zkHost
        CloudSolrServer solrServer = new CloudSolrServer("");
//        2.设置defaultCollection
        solrServer.setDefaultCollection("collection2");
//        3.获取查询对象
        SolrQuery query = new SolrQuery();
//        4.设置查询条件
        query.setQuery("*:*");
//        5.查询结果集，总记录数
        QueryResponse response = solrServer.query(query);
//        6.遍历结果集
        SolrDocumentList results = response.getResults();
        for (SolrDocument result : results) {
            System.out.println(result.get("id"));
            System.out.println(result.get("item_title"));
        }
    }

}
