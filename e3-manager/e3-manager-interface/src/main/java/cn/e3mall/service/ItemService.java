package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;


public interface ItemService {

	TbItem getItemById(long itemId);

	EasyUIDataGridResult getTbItemlList(int page,int rowss);

	E3Result saveItem(TbItem tbItem,String desc);

	E3Result deleItemByID(Long ids);

	TbItemDesc getItemDescById(Long itemId);
}
