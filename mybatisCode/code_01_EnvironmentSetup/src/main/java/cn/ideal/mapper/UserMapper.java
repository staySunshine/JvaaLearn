package cn.ideal.mapper;

import cn.ideal.domain.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    /**
     * 查询所有用户信息
     * @return
     */
    //无注解
    //List<User> findAllUserInfo();
    @Select("select * from user")
    List<User> findAllUserInfo();
}