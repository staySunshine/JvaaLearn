package cn.ideal.dao;

import cn.ideal.domain.Account;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDao {
    /**
     * \查询所有
     * @return
     */
    @Select("SELECT * FROM account")
    public List<Account> findAll();

    /**
     * 添加账户
     * @param account
     */
    @Select("INSERT INTO account (name,balance) VALUES (#{name},#{balance})")
    public void addAccount(Account account);
}
