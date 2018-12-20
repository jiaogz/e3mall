package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;

import java.util.List;

public interface ContentCategoryService {

    List<EasyUITreeNode> getContentCategoryList(long parentId);

    E3Result saveContentCategroy(Long parentId,String name);

    E3Result updateContentCategroy(Long id, String name);
}
