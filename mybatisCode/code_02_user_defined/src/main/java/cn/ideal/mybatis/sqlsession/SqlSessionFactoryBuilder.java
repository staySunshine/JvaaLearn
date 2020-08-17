package cn.ideal.mybatis.sqlsession;

import cn.ideal.mybatis.cfg.Configuration;
import cn.ideal.mybatis.sqlsession.defaults.DefaultSqlSessionFactory;
import cn.ideal.mybatis.utils.XMLConfigBuilder;

import java.io.InputStream;

public class SqlSessionFactoryBuilder {
    public  SqlSessionFactory build(InputStream config){
        Configuration cfg = XMLConfigBuilder.loadConfiguration(config);
        return new DefaultSqlSessionFactory(cfg);
    }
}
