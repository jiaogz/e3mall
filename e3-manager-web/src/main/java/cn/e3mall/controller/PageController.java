package cn.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面处理
 */
@Controller
public class PageController {


    @RequestMapping("/")
    public String showindex(){
        return "index";
    }

    @RequestMapping("/{page}")
    public String page(@PathVariable String page){
        return null;
    }

}
