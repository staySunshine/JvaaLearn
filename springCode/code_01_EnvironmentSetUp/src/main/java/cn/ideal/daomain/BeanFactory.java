package cn.ideal.daomain;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/*
public class BeanFactory {
    //定义一个Properties对象
    private static Properties properties;
    //使用静态代码块为Properties对象赋值
    static {
        try{
            //实例化对象
            properties = new Properties();
            //获取properties文件的流对象
            InputStream in = BeanFactory.class.getClassLoader().getResourceAsStream("bean.properties");
            properties.load(in);
        }catch (Exception e){
            throw  new ExceptionInInitializerError("初始化properties失败");
        }
    }
    public static Object getBean(String beanName){
        Object bean = null;

        try {
            //根据key获取value
            String beanPath = properties.getProperty(beanName);
            bean = Class.forName(beanPath).newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return bean;
    }
}
*/

public class BeanFactory {
    //定义一个Properties对象
    private static Properties properties;
    //定义Map，作为存放对象的容器
    private static Map<String, Object> beans;

    //使用静态代码块为Properties对象赋值
    static {
        try {
            //实例化对象
            properties = new Properties();
            //获取properties文件的流对象
            InputStream in = BeanFactory.class.getClassLoader().getResourceAsStream("bean.properties");
            properties.load(in);
            //实例化容器
            beans = new HashMap<String, Object>();
            //取出所有key
            Enumeration keys = properties.keys();
            //遍历枚举
            while (keys.hasMoreElements()) {
                String key = keys.nextElement().toString();
                //根据获取到的key获取对应value
                String beanPath = properties.getProperty(key);
                //反射创对象
                Object obj = Class.forName(beanPath).newInstance();
                beans.put(key, obj);
            }

        } catch (Exception e) {
            throw new ExceptionInInitializerError("初始化properties失败");
        }
    }

    public static Object getBean(String beanName) {
        return beans.get(beanName);
    }
}