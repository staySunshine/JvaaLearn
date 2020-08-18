package cn.ideal.test;

import cn.ideal.domain.User;
import cn.ideal.mapper.UserMapper;
import cn.ideal.mybatis.io.Resources;
import cn.ideal.mybatis.sqlsession.SqlSession;
import cn.ideal.mybatis.sqlsession.SqlSessionFactory;
import cn.ideal.mybatis.sqlsession.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

public class MyBatisTest {
    public static void main(String[] args) throws Exception {
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
        List<User> users = userMapper.findAllUserInfo();
        for (User user : users) {
            System.out.println(user);
        }
        //释放资源
        session.close();
        in.close();
    }
}