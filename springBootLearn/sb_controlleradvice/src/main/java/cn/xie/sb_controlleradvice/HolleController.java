package cn.xie.sb_controlleradvice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
public class HolleController {
    @GetMapping("/hello")
    public String hello(Model model){
        Map<String ,Object> map= model.asMap();
        Set<String> keySet = map.keySet();
        for(String  key:keySet){
            System.out.println(key + ": " + map.get(key));
        }
        return "hello";
    }
}
