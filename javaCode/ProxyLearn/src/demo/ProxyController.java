package demo;

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