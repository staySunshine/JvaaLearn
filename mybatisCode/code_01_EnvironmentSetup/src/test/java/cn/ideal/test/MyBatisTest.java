package cn.ideal.test;

import cn.ideal.domain.QueryUserVo;
import cn.ideal.domain.User;
import cn.ideal.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
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
        sqlSession.commit();
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
        user.setId(3);
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
        user.setId(5);
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
        userMapper.deleteUser(5);
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
    /**
     * 根据条件查询
     * @throws Exception
     */
    @Test
    public void testFindByCondition() throws Exception {
        User user = new User();
        user.setUsername("李华");
        user.setGender("男");

        List<User> users = userMapper.findUserByCondition(user);
        for (User u : users) {
            System.out.println(u);
        }
    }

    /**
     * 根据QueryUserVo中提供的id集合，查询用户信息
     * @throws Exception
     */
    @Test
    public void testfindUserInIds() throws Exception{
        QueryUserVo vo = new QueryUserVo();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        vo.setIds(list);

        List<User> users = userMapper.findUserInIds(vo);
        for (User u : users){
            System.out.println(u);
        }
    }

    public static void main(String[] args) throws Exception {
        /*
        //读取配置文件
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        //创建SqlSessionFactory工厂
        SqlSessionFactoryBuilder factoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = factoryBuilder.build(in);
        //使用工厂生产SqlSession对象
        SqlSession session = factory.openSession();
        //使用SqlSession创建Mapper接口的代理对象
        UserMapper userMapper = session.getMapper(UserMapper.class);
        //使用代理对象执行方法
        System.out.println("hello ");
        List<User> users = userMapper.findAllUserInfo();
        for (User user : users) {
            System.out.println(user);
        }
        //释放资源
        session.close();
        in.close();*/
    }
}

