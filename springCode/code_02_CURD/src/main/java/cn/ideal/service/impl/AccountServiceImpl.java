package cn.ideal.service.impl;

import cn.ideal.damain.Account;
import cn.ideal.dao.AccountDao;
import cn.ideal.service.AccountService;

import java.util.List;

public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void add(Account account) {
        accountDao.addAccount(account);
    }

    public void delete(Integer accpuntId) {
        accountDao.deleteAccount(accpuntId);
    }

    public void update(Account account) {
        accountDao.updateAccount(account);
    }

    public List<Account> findAll() {
        return accountDao.findAllAccount();
    }

    public Account findById(Integer accountId) {
        return accountDao.findAccountById(accountId);
    }
}
