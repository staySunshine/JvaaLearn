package cn.xie.sb_aop;

import cn.xie.sb_aop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/test1")
    public String getUser(Integer id){
        return  userService.getUser(1);
    }

    @GetMapping("/test2")
    public void deleteUser(Integer id){
        userService.deleteUser(2);
    }
}
