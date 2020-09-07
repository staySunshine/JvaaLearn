package cn.ideal.dao;

import cn.ideal.damain.Account;

import java.util.List;

public interface AccountDao {

    void addAccount(Account account);

    void deleteAccount(Integer accountId);

    void updateAccount(Account account);

    List<Account> findAllAccount();

    Account findAccountById(Integer accountId);
}
