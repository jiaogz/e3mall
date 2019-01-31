package cn.e3mall.search.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理器
 */
public class GlobalException implements HandlerExceptionResolver {

    private static final Logger log = LoggerFactory.getLogger(GlobalException.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
//        1.控制台打印
        e.printStackTrace();
//        2.记录日志
        log.error("系统异常！！！",e);
//        3.发送邮件短信

//        4.跳转到错误页面
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/exception");
        return modelAndView;
    }
}
