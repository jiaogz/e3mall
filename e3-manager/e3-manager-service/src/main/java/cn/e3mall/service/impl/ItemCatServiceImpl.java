package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import java.util.ArrayList;
import java.util.List;
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<EasyUITreeNode> getCatList(Long parentId) {

        System.out.println("服务层 "+parentId);
        TbItemCatExample tbItemCatExample = new TbItemCatExample();

        Criteria criteria = tbItemCatExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> tbItemCats = tbItemCatMapper.selectByExample(tbItemCatExample);

        List<EasyUITreeNode> easyUITreeNodes = new ArrayList<>();

        for (TbItemCat tbItemCat: tbItemCats) {

            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();

            easyUITreeNode.setId(tbItemCat.getId());
            easyUITreeNode.setText(tbItemCat.getName());
            easyUITreeNode.setState(tbItemCat.getIsParent() ? "closed":"open");

            easyUITreeNodes.add(easyUITreeNode);
        }

        return easyUITreeNodes;
    }
}
