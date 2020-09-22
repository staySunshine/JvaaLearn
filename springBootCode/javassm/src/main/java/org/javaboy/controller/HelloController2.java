package org.javaboy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController2 {
    @GetMapping("/hello2")
    public String hello() {
        return "hello";
    }
}
