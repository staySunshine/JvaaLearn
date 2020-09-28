package cn.xie.sb_aop.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    public String getUser(Integer id){
        System.out.println("getUser");
        return  "hello";
    }

    public void deleteUser(Integer id){
        System.out.println("deleteUser");
    }
}
