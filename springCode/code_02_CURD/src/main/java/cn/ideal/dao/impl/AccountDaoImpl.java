package cn.ideal.dao.impl;

import cn.ideal.damain.Account;
import cn.ideal.dao.AccountDao;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.List;

public class AccountDaoImpl implements AccountDao {

    private QueryRunner runner;

    public void setRunner(QueryRunner runner) {
        this.runner = runner;
    }

    public void addAccount(Account account) {
        try {
            runner.update("insert into account(name,balance)values(?,?)", account.getName(), account.getBalance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAccount(Account account) {
        try {
            runner.update("update account set name=?,balance=? where id=?", account.getName(), account.getBalance(), account.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAccount(Integer accountId) {
        try {
            runner.update("delete from account where id=?", accountId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Account> findAllAccount() {
        try {
            return runner.query("select * from account", new BeanListHandler<Account>(Account.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Account findAccountById(Integer accountId) {
        try {
            return runner.query("select * from account where id = ? ", new BeanHandler<Account>(Account.class), accountId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
