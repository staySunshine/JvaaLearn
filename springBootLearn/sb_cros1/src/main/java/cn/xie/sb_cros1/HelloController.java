package cn.xie.sb_cros1;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
//    @CrossOrigin(origins = "http://localhost:8081")
    public String hello(){
        return "Hello Cors";
    }
}
