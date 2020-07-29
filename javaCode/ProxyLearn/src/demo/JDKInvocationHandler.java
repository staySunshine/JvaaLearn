package demo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKInvocationHandler implements InvocationHandler {

    private Object target;

    public JDKInvocationHandler(Object target){
        this.target = target;
    }

    /**
     * 创建代理实例
     * @return
     * @throws Throwable
     */
    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread()
                .getContextClassLoader(), this.target.getClass()
                .getInterfaces(), this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("method:" + method.getName() + " is invoked!");
        return method.invoke(target, args);
    }
}