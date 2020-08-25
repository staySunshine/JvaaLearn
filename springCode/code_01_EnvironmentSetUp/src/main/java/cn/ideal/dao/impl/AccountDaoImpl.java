package cn.ideal.dao.impl;

import cn.ideal.dao.AccountDao;

/**
 * 账户持久层实现类
 */
public class AccountDaoImpl implements AccountDao {

//    public void addAccount() {
//        System.out.println("添加用户成功！");
//    }

    //定义一个类成员
    private int i = 1;

    public void addAccount() {
        System.out.println("添加用户成功！");
        System.out.println(i);
        i++;
    }
}