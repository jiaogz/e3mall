package cn.e3mall.cart.controller;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车功能实现
 */
@Controller
public class CartController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CartService cartService;
    @Value("${CART_NAME}")
    private String CART_NAME;
    @Value("${CART_ITEM_EXPRIOR}")
    private int CART_ITEM_EXPRIOR;

    /**
     * 添加购物车
     * 1.未登录情况将购物车商品列表存储在cookie中，cookie最大存储量4k，更换机器后不能同步购物车列表
     * 2.登录状态，将购物车列表保存在redis中，使用hash数据结构
     * @param itemId
     * @param num
     * @return 添加商品信息成功页面
     */
    @RequestMapping("/cart/add{itemId}")
    public String addCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
                          HttpServletRequest request, HttpServletResponse response){
        //判断用户是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            //调用服务，保存到redis中
            cartService.addCart(user.getId(),itemId,num);
            return "cartSuccess";
        }

        //从cookie中获取商品列表
        List<TbItem> tbItems = getTbitemByCookie(request);
        //根据商品id判断是否在cookie中
        Boolean falge = false;
        for (TbItem tbItem : tbItems) {
            //在，修改数量
            if (tbItem.getId() == itemId.longValue()) {
                tbItem.setNum(tbItem.getNum()+num);
                falge = true;
                break;
            }
        }

        //不在查询数据库，获取商品信息
        if (!falge) {
            TbItem item = itemService.getItemById(itemId);
            item.setNum(num);
            if (StringUtils.isNotBlank(item.getImage())) {
                item.setImage(item.getImage().split(",")[0]);
            }
            tbItems.add(item);
        }
        //将商品信息写入cookie中
        CookieUtils.setCookie(request,response,CART_NAME,JsonUtils.objectToJson(tbItems),CART_ITEM_EXPRIOR,true);
        //跳转添加成功逻辑视图
        return "cartSuccess";
    }

    /**
     * 展示购物车列表
     * @param request
     * @return
     */
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request,HttpServletResponse response){
        //查询cookie中的商品列表
        List<TbItem> cartList = getTbitemByCookie(request);
        //判断用户是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        //登录，合并购物车列表
        if (user != null) {
            //调用服务，保存到redis中
            cartService.margeCartList(user.getId(),cartList);
            CookieUtils.deleteCookie(request,response,CART_NAME);
            cartList = cartService.showCartList(user.getId());
        }
        request.setAttribute("cartList",cartList);
        return "cart";
    }

    /**
     * 更新购物车数量
     * @param itemId
     * @param num
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/update/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
                                  HttpServletRequest request,HttpServletResponse response){
        //判断用户是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        //登录
        if (user != null) {
            cartService.updateCartNum(user.getId(),itemId,num);
            return E3Result.ok();
        }

        //查询购物车列表
        List<TbItem> cartList = getTbitemByCookie(request);
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()) {
                tbItem.setNum(num);
                break;
            }
        }
        CookieUtils.setCookie(request,response,CART_NAME,JsonUtils.objectToJson(cartList),CART_ITEM_EXPRIOR,true);
        return E3Result.ok();
    }

    /**
     * 删除购物车中的商品
     * @param itemId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCart(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
        //判断用户是否登录
        TbUser user = (TbUser) request.getAttribute("user");
        //登录
        if (user != null) {
            cartService.deletCart(user.getId(),itemId);
            return "redirect:/cart/cart.html";
        }
        List<TbItem> cartList = getTbitemByCookie(request);
        for (TbItem tbItem : cartList) {
            if (tbItem.getId().longValue() ==itemId) {
                cartList.remove(tbItem);
                break;
            }
        }
        CookieUtils.setCookie(request,response,CART_NAME,JsonUtils.objectToJson(cartList),CART_ITEM_EXPRIOR,true);
        return "redirect:/cart/cart.html";
    }
    /**
     * 获取cookie中的商品列表
     * @param request
     * @return
     */
    private List<TbItem> getTbitemByCookie(HttpServletRequest request){
        String json = CookieUtils.getCookieValue(request, CART_NAME,true);
        if (StringUtils.isBlank(json)) {
            return new ArrayList<>();
        }
        return JsonUtils.jsonToList(json,TbItem.class);
    }
}
