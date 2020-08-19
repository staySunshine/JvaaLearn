package cn.ideal.mapper;

import cn.ideal.domain.QueryUserVo;
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
    /**
     * 查询所有用户信息
     */
    List<User> findAllUserInfo();

    /**
     * 增加用户
     * @param user
     */
    void addUser(User user);

    /**
     * 更新用户
     * @param user
     */
    void updateUser(User user);

    /**
     * 删除用户
     * @param uid
     */
    void deleteUser(Integer uid);

    /**
     * 通过姓名模糊查询
     * @param username
     * @return
     */
    List<User> findByName(String username);

    /**
     * 根据条件查询
     * @return
     */
    List<User> findUserByCondition(User user);

    /**
     * 根据QueryUserVo中提供的id集合，查询用户信息
     * @param vo
     * @return
     */
    List<User> findUserInIds(QueryUserVo vo);
}