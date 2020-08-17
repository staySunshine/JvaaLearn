package cn.ideal.mapper;

import cn.ideal.domain.User;

import java.util.List;
public interface UserMapper {
    /**
     * 查询所有用户信息
     */
    List<User> findAllUserInfo();
}
