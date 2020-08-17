package cn.ideal.mybatis.sqlsession;

public interface SqlSession {
    /**
     * 根据参数创建一个代理对象
     * @param mapperInterfaceClass mapper的接口字节码
     * @param <T>
     * @return
     */
    <T> T getMapper(Class<T> mapperInterfaceClass);

    /**
     * 释放资源
     */
    void close();
}