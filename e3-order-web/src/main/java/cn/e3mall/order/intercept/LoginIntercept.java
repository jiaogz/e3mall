package cn.e3mall.order.intercept;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 */
public class LoginIntercept implements HandlerInterceptor {

    @Value("$(SSO_HOST)")
    private String SSO_HOST;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CartService cartService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //1.从cookie中获取token
        String token = CookieUtils.getCookieValue(httpServletRequest, "token");
        //2.判断token是否存在，不存在跳转到SSO登录页面，登录成功后回到该请求URL
        if (StringUtils.isBlank(token)) {
            httpServletResponse.sendRedirect(SSO_HOST+"/page/login?redirect=" + httpServletRequest.getRequestURL());
            return false;
        }
        //3.token存在，调用SSO系统查询用户信息
        E3Result e3Result = tokenService.getUserByToken(token);
        //4.用户信息不存在，跳转到SSO登录页面，登录成功后回到该请求URL
        if (e3Result.getStatus() != 200) {
            httpServletResponse.sendRedirect(SSO_HOST+"/page/login?redirect=" + httpServletRequest.getRequestURL());
            return false;
        }
        //5.获取用户信息，存入request中
        TbUser user = (TbUser) e3Result.getData();
        //6.获取cookie中的购物车列表，合并到服务器端(redis)
        String jsonCartList = CookieUtils.getCookieValue(httpServletRequest, "carts", true);
        if (StringUtils.isNotBlank(jsonCartList)) {
            cartService.margeCartList(user.getId(), JsonUtils.jsonToList(jsonCartList, TbItem.class));
            CookieUtils.deleteCookie(httpServletRequest,httpServletResponse,"carts");
        }

        //7.放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
