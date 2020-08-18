package cn.ideal.test;

import cn.ideal.domain.User;
import cn.ideal.mapper.UserMapper;
import cn.ideal.mybatis.io.Resources;
import cn.ideal.mybatis.sqlsession.SqlSession;
import cn.ideal.mybatis.sqlsession.SqlSessionFactory;
import cn.ideal.mybatis.sqlsession.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class MyBatisTest {
    private InputStream inputStream;
    private SqlSession sqlSession;
    private UserMapper userMapper;
    @Before
    public  void init() throws Exception{
        //读取配置文件
        inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        //创建SqlSessionFactory工厂
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);

        //使用工厂生产SqlSession对象
        sqlSession = factory.openSession();
        //使用SqlSession创建Mapper接口的代理对象
        userMapper = sqlSession.getMapper(UserMapper.class);
    }
    @After
    public void destroy() throws Exception{
        sqlSession.close();
        inputStream.close();
    }
    /**
     * 测试新增用户
     * @throws Exception
     */
    /*
    @Test
    public void testAddUser() throws Exception{
        User user = new User();
        user.setUsername("增加");
        user.setTelephone("12266668888");
        user.setBirthday(new Date());
        user.setGender("女");
        user.setAddress("成都");

        //执行方法
        userMapper.addUser(user);

    }*/

    @Test
    public void testAddUser() throws Exception{
        User user = new User();
        user.setUsername("增加");
        user.setTelephone("12266660000");
        user.setBirthday(new Date());
        user.setGender("男");
        user.setAddress("珠海");
        System.out.println("执行插入前" + user);
        //执行方法
        userMapper.addUser(user);
        System.out.println("执行插入后" + user);
    }

    /**
     * 测试新增用户
     * @throws Exception
     */
    @Test
    public void testUpdateUser() throws Exception{
        User user = new User();
        user.setId(17);
        user.setUsername("修改");
        user.setTelephone("18899999999");
        user.setBirthday(new Date());
        user.setGender("女");
        user.setAddress("广州");

        //执行方法
        userMapper.updateUser(user);
    }

    /**
     * 测试删除用户
     * @throws Exception
     */
    @Test
    public void testDeleteUser() throws Exception{
        //执行方法
        userMapper.deleteUser(17);
    }

    /**
     * 测试模糊查询
     * @throws Exception
     */
    @Test
    public void testFindByName() throws Exception{
        List<User> users = userMapper.findByName("%张%");
        for (User user : users){
            System.out.println(user);
        }
    }
    public static void main(String[] args) throws Exception {

    }
}