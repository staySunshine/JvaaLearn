package cn.ideal.mybatis.sqlsession.defaults;

import cn.ideal.mybatis.cfg.Configuration;
import cn.ideal.mybatis.sqlsession.SqlSession;
import cn.ideal.mybatis.sqlsession.proxy.MapperProxy;
import cn.ideal.mybatis.utils.DataSourceUtil;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

public class DefaultSqlSession implements SqlSession {

    private Configuration cfg;
    private Connection connection;

    public DefaultSqlSession(Configuration cfg) {
        this.cfg = cfg;

        connection = DataSourceUtil.getConnection(cfg);
    }

    /**
     * 用于创建代理对象
     *
     * @param mapperInterfaceClass mapper的接口字节码
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class<T> mapperInterfaceClass) {
        return (T) Proxy.newProxyInstance(mapperInterfaceClass.getClassLoader(),
                new Class[]{mapperInterfaceClass},new MapperProxy(cfg.getMappers(),connection));
    }

    /**
     * 用于释放资源
     */
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}