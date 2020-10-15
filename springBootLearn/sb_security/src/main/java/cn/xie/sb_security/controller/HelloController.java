package cn.xie.sb_security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    private String hello(){
        return "hello security";
    }
    @GetMapping("/admin/hello")
    private String admin(){
        return "hello admin";
    }
    @GetMapping("/user/hello")
    private String user(){
        return "hello user";
    }
}
