package cn.ideal.service.impl;

import cn.ideal.dao.AccountDao;
import cn.ideal.domain.Account;
import cn.ideal.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    public List<Account> findAll() {
        System.out.println("这是业务层——查询所有账户方法");
        return accountDao.findAll();
    }

    public void addAccount(Account account) {

        System.out.println("这是业务层——添加账户方法");
        accountDao.addAccount(account);
    }
}
