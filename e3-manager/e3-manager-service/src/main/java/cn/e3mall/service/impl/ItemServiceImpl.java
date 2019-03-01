package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JedisClient;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.print.attribute.standard.Destination;
import java.util.Date;
import java.util.List;

/**
 * 商品管理Service
 * <p>Title: ItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service(value="itemServiceImpl")
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;

	@Autowired
    private TbItemDescMapper itemDescMapper;

	@Autowired
    private JmsTemplate jmsTemplate;

//	根据ID导入
	@Resource
    private Destination topicDestination;

	@Autowired
    private JedisClient jedisClient;

	@Value("${ITEM_INFO_PRE}")
	private static String ITEM_INFO_PRE;

    @Value("${ITEM_TIMEOUT}")
    private static Integer ITEM_TIMEOUT;

	@Override
	public TbItem getItemById(long itemId) {

	    //查询缓存
        try {
            String str = jedisClient.get(ITEM_INFO_PRE + ":" + itemId + ":BASE");
            if (StringUtils.isNotBlank(str)){
                return JsonUtils.jsonToPojo(str,TbItem.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

		//根据主键查询
		//TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {

		    //添加缓存
            try {
                jedisClient.set(ITEM_INFO_PRE +":" +itemId +":BASE", JsonUtils.objectToJson(list.get(0)));
                jedisClient.expire(ITEM_INFO_PRE +":" +itemId +":BASE",ITEM_TIMEOUT);
            } catch (Exception e) {
                e.printStackTrace();
            }

			return list.get(0);
		}
		return null;
	}

	@Override
	public EasyUIDataGridResult getTbItemlList(int page, int rows) {
		System.out.println("=================");
        PageHelper.startPage(page,rows);

        TbItemExample tbItemExample = new TbItemExample();

        List<TbItem> tbItems = itemMapper.selectByExample(tbItemExample);

        //取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItems);

        //创建返回结果对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(tbItems);

        return result;

	}

	@Override
	public E3Result saveItem(TbItem tbItem, String desc) {

	    final long itemId = IDUtils.genItemId();
	    Date date = new Date();
        //补全商品属性
	    tbItem.setId(itemId);
	    tbItem.setStatus((byte) 1);
	    tbItem.setCreated(date);
	    tbItem.setUpdated(date);

	    itemMapper.insert(tbItem);

	    //新增商品描述
        TbItemDesc tbItemDesc = new TbItemDesc();
        // 5、补全TbItemDesc的属性
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);

        itemDescMapper.insert(tbItemDesc);

        //发送消息
//        jmsTemplate.send(topicDestination, new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                TextMessage message = session.createTextMessage(itemId +"");
//                return message;
//            }
//        });

		return E3Result.ok();
	}

    @Override
    public E3Result deleItemByID(Long ids) {

	    TbItemExample tbItemExample = new TbItemExample();
        Criteria criteria = tbItemExample.createCriteria();
        criteria.andIdEqualTo(ids);
        int i = itemMapper.deleteByExample(tbItemExample);

        if (i!= 0){
            return E3Result.ok();
        }
        return null;
    }

    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        //查询缓存
        try {
            String str = jedisClient.get(ITEM_INFO_PRE + ":" + itemId + ":DESC");
            if (StringUtils.isNotBlank(str)){
                return JsonUtils.jsonToPojo(str,TbItemDesc.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        if (tbItemDesc != null) {
            //添加缓存
            try {
                jedisClient.set(ITEM_INFO_PRE +":" +itemId +":DESC", JsonUtils.objectToJson(tbItemDesc));
                jedisClient.expire(ITEM_INFO_PRE +":" +itemId +":DESC",ITEM_TIMEOUT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return tbItemDesc;
        }
        return null;
    }

}
