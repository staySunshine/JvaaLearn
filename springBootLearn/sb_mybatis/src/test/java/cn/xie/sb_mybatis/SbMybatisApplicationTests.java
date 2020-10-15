package cn.xie.sb_mybatis;

import cn.xie.sb_mybatis.bean.User;
import cn.xie.sb_mybatis.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SbMybatisApplicationTests {

    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() {
        List<User> userList = userMapper.getAllUser();
        System.out.println(userList);
    }

}
