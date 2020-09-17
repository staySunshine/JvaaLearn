package cn.hang;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

//@Component
public class MyAspectJAutoProxyCreator implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        final Object obj = bean;
        //如果当前经过BeanPostProcessors的Bean是Calculator类型，我们就返回它的代理对象
        if (bean instanceof Calculator) {
            Object proxyObj = Proxy.newProxyInstance(
                    this.getClass().getClassLoader(),
                    bean.getClass().getInterfaces(),
                    new InvocationHandler() {
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            System.out.println("开始计算....");
                            Object result = method.invoke(obj, args);
                            System.out.println("结束计算...");
                            return result;
                        }
                    }
            );
            return proxyObj;
        }
        //否则返回本身
        return obj;
    }
}
