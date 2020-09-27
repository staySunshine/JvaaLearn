package cn.xie.sb_controlleradvice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalData {

    @ModelAttribute("info")
    public Map<String,Object> mydata(){
        Map<String ,Object> map = new HashMap<>();
        map.put("001","xie");
        map.put("002","yu");
        map.put("003","hang");
        return map;
    }

    @InitBinder("a")
    public void initA(WebDataBinder binder){
        binder.setFieldDefaultPrefix("a.");
    }

    @InitBinder("b")
    public void initB(WebDataBinder binder){
        binder.setFieldDefaultPrefix("b.");
    }
}
