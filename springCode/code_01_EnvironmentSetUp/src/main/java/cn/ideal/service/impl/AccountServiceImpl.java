package cn.ideal.service.impl;

import cn.ideal.dao.AccountDao;
import cn.ideal.dao.impl.AccountDaoImpl;
import cn.ideal.service.AccountService;

/**
 * 账户业务层实现类
 */
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao = new AccountDaoImpl();

    public void addAccount() {
        accountDao.addAccount();
    }
}
