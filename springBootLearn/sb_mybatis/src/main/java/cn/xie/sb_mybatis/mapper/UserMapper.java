package cn.xie.sb_mybatis.mapper;

import cn.xie.sb_mybatis.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//@Mapper
public interface UserMapper {
    List<User> getAllUser();
}
