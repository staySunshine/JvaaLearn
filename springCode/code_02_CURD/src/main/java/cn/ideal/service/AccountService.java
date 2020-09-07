package cn.ideal.service;

import cn.ideal.damain.Account;

import java.util.List;

public interface AccountService {
    void add(Account account);

    void delete(Integer accpuntId);

    void update(Account account);

    List<Account> findAll();

    Account findById(Integer accountId);
}
