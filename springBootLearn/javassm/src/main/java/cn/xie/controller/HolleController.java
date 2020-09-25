package cn.xie.controller;

import cn.xie.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolleController {
    @Autowired
    HelloService helloService = new HelloService();
    @GetMapping("/hello")
    public String hello(){
        return helloService.sayHello();
    }
}
