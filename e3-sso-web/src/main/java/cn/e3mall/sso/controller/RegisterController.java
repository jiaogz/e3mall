package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 注册功能
 */
@Controller
public class RegisterController {

    @Autowired
    private RegisterUserService registerUserService;

    @RequestMapping("/page/register")
    public String register(){
        return "register";
    }

    /**
     * 校验用户是否存在
     * @param param
     * @param type
     * @return
     */
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkUserDate(@PathVariable String param,@PathVariable Integer type ){
        return registerUserService.checkUserData(param,type);
    }

    @RequestMapping("/page/register")
    @ResponseBody
    public E3Result registerUser(TbUser user) {
        return registerUserService.registerUser(user);
    }

}
