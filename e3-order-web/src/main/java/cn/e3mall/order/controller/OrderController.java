package cn.e3mall.order.controller;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单服务
 */
@Controller
public class OrderController {

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    /**
     * 展示订单页面
     * @param request
     * @return
     */
    @RequestMapping("order/order-cart")
    public String showOrderCart(HttpServletRequest request){
        //1.获取用户信息
        TbUser user = (TbUser) request.getAttribute("user");
        //2.根据用户ID查询购物车列表
        List<TbItem> cartList = cartService.showCartList(user.getId());
        request.setAttribute("cartList",cartList);
        //设置用户地址、支付方式等
        //3.返回逻辑视图
        return "order-cart";
    }

    /**
     * 创建订单
     * @param orderInfo
     * @param request
     * @return 跳转到支付页面
     */
    @RequestMapping(value = "/order/create",method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo,HttpServletRequest request){
        //获取用户信息
        TbUser user = (TbUser) request.getAttribute("user");
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());
        //调用服务创建订单
        E3Result e3Result = orderService.creatOrder(orderInfo);
        //订单创建成果，清空购物车列表
        if (e3Result.getStatus() == 200) {
            cartService.cleanCartAll(user.getId());
        }
        //向页面返回订单ID，金额信息
        request.setAttribute("orderId",e3Result.getData());
        request.setAttribute("payment",orderInfo.getPayment());

        //返回支付页面
        return "success";
    }

}
