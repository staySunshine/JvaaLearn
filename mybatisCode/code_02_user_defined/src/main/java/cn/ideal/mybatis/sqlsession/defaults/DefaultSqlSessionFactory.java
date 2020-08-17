package cn.ideal.mybatis.sqlsession.defaults;

import cn.ideal.mybatis.cfg.Configuration;
import cn.ideal.mybatis.sqlsession.SqlSession;
import cn.ideal.mybatis.sqlsession.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration cfg;
    public DefaultSqlSessionFactory(Configuration cfg) {
        this.cfg = cfg;
    }

    /**
     * 用于创建一个新的操作数据库对象
     * @return
     */
    public SqlSession openSession() {
        return new DefaultSqlSession(cfg);
    }
}
