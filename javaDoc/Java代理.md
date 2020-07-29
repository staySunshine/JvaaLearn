## Java代理(jdk静态代理、动态代理和cglib动态代理)

代理模式是常用的java设计模式，他的特征是代理类与委托类有同样的接口，代理类主要负责为委托类预处理消息、过滤消息、把消息转发给委托类，以及事后处理消息等。代理类与委托类之间通常会存在关联关系，一个代理类的对象与一个委托类的对象关联，代理类的对象本身并不真正实现服务，而是通过调用委托类的对象的相关方法，来提供特定的服务。 


####  JDK 动态代理 

#####  业务接口（必须有） 

```java
public interface HelloWorldJDKProxy {
    void helloWorld();
}
```

#####  业务实现 

```java
public class HelloWorldJDKProxyImpl implements HelloWorldJDKProxy {
    public void helloWorld() {
        System.out.println("Hello World JDK Proxy!");
    }
}
```

####  代理类 

```java
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

```

#####  JDK动态代理测试 

```java

public class ProxyController {
 
    public static void main(String[] atgs){
 
        /**
         * JDK 动态代理
         */
        HelloWorldJDKProxy jdkProxy = new HelloWorldJDKProxyImpl();
        JDKInvocationHandler handle = new JDKInvocationHandler(jdkProxy);
 
        // 根据目标生成代理对象
        HelloWorldJDKProxy proxy = (HelloWorldJDKProxy)handle.getProxy();
        proxy.helloWorld();
 
    }
}
```

