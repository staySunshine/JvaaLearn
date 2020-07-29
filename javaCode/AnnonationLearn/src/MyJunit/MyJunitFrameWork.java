package MyJunit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MyJunitFrameWork {
    public static void main(String[] args) throws Exception {
        // 1.先找到测试类的字节码：EmployeeDAOTest
        Class clazz = EmployeeDAOTest.class;
        Object obj = clazz.newInstance();
        // 2.获取EmployeeDAOTest类中所有的公共方法
        Method[] methods = clazz.getMethods();
		/* 3.迭代出每一个Method对象
                     判断哪些方法上使用了@MyBefore/@MyAfter/@MyTest注解
                */
        List<Method> mybeforeList = new ArrayList<>();
        List<Method> myAfterList = new ArrayList<>();
        List<Method> myTestList = new ArrayList<>();
        for (Method method : methods) {
            if(method.isAnnotationPresent(MyBefore.class)){
                //存储使用了@MyBefore注解的方法对象
                mybeforeList.add(method);
            }else if(method.isAnnotationPresent(MyTest.class)){
                //存储使用了@MyTest注解的方法对象
                myTestList.add(method);
            }else if(method.isAnnotationPresent(MyAfter.class)){
                //存储使用了@MyAfter注解的方法对象
                myAfterList.add(method);
            }
        }

        // 执行方法测试
        for (Method testMethod : myTestList) {
            // 先执行@MyBefore的方法
            for (Method beforeMethod : mybeforeList) {
                beforeMethod.invoke(obj);
            }
            // 测试方法
            testMethod.invoke(obj);
            // 最后执行@MyAfter的方法
            for (Method afterMethod : myAfterList) {
                afterMethod.invoke(obj);
            }
        }
    }
}