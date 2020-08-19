package cn.ideal.test;

import cn.ideal.domain.Account;
import cn.ideal.domain.User;
import cn.ideal.domain.UserAccount;
import cn.ideal.mapper.AccountMapper;
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
    private AccountMapper accountMapper;
    @Before
    public  void init() throws Exception{
        //读取配置文件
        inputStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        //创建SqlSessionFactory工厂
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);

        //使用工厂生产SqlSession对象
        sqlSession = factory.openSession();
        //使用SqlSession创建Mapper接口的代理对象
        accountMapper = sqlSession.getMapper(AccountMapper.class);
    }
    @After
    public void destroy() throws Exception{
        sqlSession.commit();
        sqlSession.close();
        inputStream.close();
    }

    /**
     * 测试查询所有
     */
    /*
    @Test
    public void testFindAll(){
        List<Account> accounts = accountMapper.findAll();
        for (Account account : accounts){
            System.out.println(account);
        }
    }*/

    /**
     * 查询所有账户，并且带有用户名称和地址信息
     * @return
     */
    @Test
    public void testFindAllAccount(){
        List<UserAccount> uas = accountMapper.findAllAccount();
        for (UserAccount ua : uas ){
            System.out.println(ua);
        }
    }
    /**
     * 测试查询所有
     */
    @Test
    public void testFindAll(){
        List<Account> accounts = accountMapper.findAll();
        for (Account account : accounts){
            System.out.println("--------------------------");
            System.out.println(account);
            System.out.println(account.getUser());
        }
    }
    public static void main(String[] args) throws Exception {

    }
}

