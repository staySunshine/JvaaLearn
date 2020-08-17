package cn.ideal.mybatis.sqlsession;

public interface SqlSessionFactory {
    /**
     * 用来打开一个新的SqlSession对象
     * @return
     */
    SqlSession openSession();
}
