package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆处理
 */
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Value("${TOKEN_NAME}")
    private String TOKEN_NAME;

    /**
     * 跳转到登陆页
     * @return
     */
    @RequestMapping("/page/login")
    public String showLogin(){
        return "login";
    }

    /**
     * 登陆
     * @return
     */
    @RequestMapping("/user/login")
    @ResponseBody
    public E3Result Login(String username, String password, HttpServletRequest request,HttpServletResponse response){
        E3Result e3Result = loginService.Login(username, password);
        if (e3Result.getStatus() == 200) {
            //将token保存到cookie中
            String token = e3Result.getData().toString();
            CookieUtils.setCookie(request,response,TOKEN_NAME,token);
        }
        return e3Result;
    }

}
