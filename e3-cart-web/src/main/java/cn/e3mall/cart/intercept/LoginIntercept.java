package cn.e3mall.cart.intercept;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 */
public class LoginIntercept implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //1.从cookie中获取token
        String token = CookieUtils.getCookieValue(httpServletRequest, "token");
        //2.没有token放行
        if (StringUtils.isBlank(token)) {
            return true;
        }
        //3.有token调用SSO系统查询用户信息
        E3Result e3Result = tokenService.getUserByToken(token);
        //4.验证用户是否过期，过期放行
        if (e3Result.getStatus()!=200) {
            return true;
        }
        //5.登录状态，添加用户到request中，放行
        httpServletRequest.setAttribute("user",e3Result.getData());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
