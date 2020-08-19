package cn.ideal.mapper;

import cn.ideal.domain.Account;
import cn.ideal.domain.UserAccount;

import java.util.List;

public interface AccountMapper {

    /**
     * 查询所有账户
     * @return
     */
    List<Account> findAll();

    /**
     * 查询所有账户，并且带有用户名称和地址信息
     * @return
     */
    List<UserAccount> findAllAccount();
}
