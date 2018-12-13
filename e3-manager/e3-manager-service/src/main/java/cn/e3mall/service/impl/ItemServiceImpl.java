package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.pojo.TbItemDesc;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

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
	
	@Override
	public TbItem getItemById(long itemId) {
		//根据主键查询
		//TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
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

	    long itemId = IDUtils.genItemId();
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

}
