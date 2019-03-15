package cn.e3mall.cart.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;

import java.util.List;

public interface CartService {
    E3Result addCart(long userId,long itemId,int num);
    E3Result margeCartList(long userId, List<TbItem> cartList);
    List<TbItem> showCartList(long userId);
    E3Result updateCartNum(long userId,long itemId,int num);
    E3Result deletCart(long userId,long ItemId);
    E3Result cleanCartAll(long userId);
}
