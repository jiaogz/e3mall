package cn.e3mall.controller;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

import java.util.List;

/**
 * 商品管理Controller
 * <p>Title: ItemController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 *
 * @version 1.0
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemCatService itemCatService;

    /**
     * 根据商品ID查询
     * @param itemId
     * @return
     */
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId) {
        TbItem tbItem = itemService.getItemById(itemId);
        return tbItem;
    }

    /**
     * 分页查询商品列表
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        EasyUIDataGridResult result = itemService.getTbItemlList(page, rows);
        return result;
    }

    /**
     * 商品类目
     * @param parentId
     * @return
     */
    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUITreeNode> getItemCat(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        List<EasyUITreeNode> catList = itemCatService.getCatList(parentId);

        return catList;
    }

    /**
     * 新增商品
     * @param tbItem 商品信息
     * @param desc 商品描述
     * @return
     */
    @RequestMapping("/item/save")
    @ResponseBody
    public E3Result saveItem(TbItem tbItem, String desc){

        E3Result e3Result = itemService.saveItem(tbItem, desc);

        return e3Result;
    }

    /**
     * 删除商品
     * @param ids
     * @return
     */
    @RequestMapping("/rest/item/delete")
    @ResponseBody
    public E3Result deleItemByID(long ids){

        E3Result e3Result = itemService.deleItemByID(ids);
        return e3Result;
    }


}
