package cn.ideal.service;

import cn.ideal.domain.Account;

import java.util.List;

public interface AccountService {

    /**
     * \查询所有
     * @return
     */
    List<Account> findAll();

    /**
     * 添加账户
     * @param account
     */
    void addAccount(Account account);
}
