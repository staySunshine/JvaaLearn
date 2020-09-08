package cn.ideal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ControllerDemo {
    @RequestMapping(path = "/test")
    public String methodTest(){
        System.out.println("这是Controller测试方法");
        return "testSuccess";
    }
}
