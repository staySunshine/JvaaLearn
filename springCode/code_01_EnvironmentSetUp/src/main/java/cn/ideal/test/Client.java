package cn.ideal.test;

import cn.ideal.dao.AccountDao;
import cn.ideal.daomain.BeanFactory;
import cn.ideal.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Client {
    /*
    public static void main(String[] args) {
        AccountService as = new AccountServiceImpl();
        as.addAccount();
    }
     */
    /*
    public static void main(String[] args) {
        AccountService as = (AccountService) BeanFactory.getBean("accountService");
        as.addAccount();

        for(int i = 0; i < 4; i++){
            as = (AccountService)BeanFactory.getBean("accountService");
            System.out.println(as);
        }
    }*/
    public static void main(String[] args) {
        //获取核心容器
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");
        //根据id后去Bean对象,下面两种方式都可以
        AccountService as = (AccountService)ac.getBean("accountService");
        System.out.println("222");
        AccountDao ad = ac.getBean("accountDao", AccountDao.class);
        System.out.println(as);
        System.out.println(ad);
    }
}