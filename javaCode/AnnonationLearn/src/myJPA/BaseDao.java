package myJPA;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

public class BaseDao<T> {

    private static BasicDataSource datasource = new BasicDataSource();

    //静态代码块,设置连接数据库的参数
    static{
        datasource.setDriverClassName("com.mysql.jdbc.Driver");
        datasource.setUrl("jdbc:mysql://localhost:3306/test");
        datasource.setUsername("root");
        datasource.setPassword("123456");
    }

    //得到jdbcTemplate
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);
    //泛型参数的Class对象
    private Class<T> beanClass;

    public BaseDao() {
		/*this指代子类
                  通过子类得到子类传给父类的泛型Class对象，假设是User.class
                */
        beanClass = (Class) ((ParameterizedType) this.getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    public void add(T bean) {
        //得到User对象的所有字段
        Field[] declaredFields = beanClass.getDeclaredFields();

        //拼接sql语句，表名直接用POJO的类名
        //所以创建表时，请注意写成User，而不是t_user
        String sql = "insert into "
                + beanClass.getSimpleName() + " values(";
        for (int i = 0; i < declaredFields.length; i++) {
            sql += "?";
            if (i < declaredFields.length - 1) {
                sql += ",";
            }
        }
        sql += ")";

        //获得bean字段的值（要插入的记录）
        ArrayList<Object> paramList = new ArrayList<>();
        for (int i = 0; i < declaredFields.length; i++) {
            try {
                declaredFields[i].setAccessible(true);
                Object o = declaredFields[i].get(bean);
                paramList.add(o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        int size = paramList.size();
        Object[] params = paramList.toArray(new Object[size]);

        //传入sql语句模板和模板所需的参数，插入User
        int num = jdbcTemplate.update(sql, params);
        System.out.println(num);
    }
}
