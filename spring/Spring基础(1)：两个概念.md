# Spring基础(1)：两个概念

主要内容：

- 盲点
- Spring说，万物皆可定义
- 默默付出的后置处理器
- 利用后置处理器返回代理对象

## 盲点

如果你恰好非科班转行且从未独立看过源码，那么你**很可能**至今都不曾注意某两个概念。

你以为我会说IOC和AOP？NO。

看到这里，一部分读者心里一惊：卧槽，说的啥玩意，Spring不就IOC和AOP吗？！这两个都不说，你这篇文章为啥能写这么长？

不错，我就是这么长。其实我要讲的是：

- BeanDefinition
- BeanPostProcessor



大部分人一听到“请你谈谈对Spring的理解”，就会下意识地搬出IOC和AOP两座大山，赶紧糊弄过去。大概是这样的：

**IOC**

所谓的控制反转。通俗地讲，就是把原本需要程序员自己创建和维护的一大堆bean统统交由Spring管理。 

![1600313648668](Spring基础(1)：两个概念.assets/1600313648668.png)

 也就是说，Spring将我们从盘根错节的依赖关系中解放了。当前对象如果需要依赖另一个对象，只要打一个@Autowired注解，Spring就会自动帮你安装上。 

![1600313666530](Spring基础(1)：两个概念.assets/1600313666530.png)

**AOP**

所谓的面向切面编程。通俗地讲，它一般被用来解决一些系统交叉业务的织入，比如日志啦、事务啥的。打个比方，UserService的method1可能要打印日志，BrandService的method2可能也需要。亦即：一个交叉业务就是要切入系统的一个方面。具体用代码展示就是：

![1600313716617](Spring基础(1)：两个概念.assets/1600313716617.png)

 交叉业务的编程问题即为面向切面编程。AOP的目标就是使交叉业务模块化。做法是将切面代码移动到原始方法的周围： 

![1600313737882](Spring基础(1)：两个概念.assets/1600313737882.png)

原先不用AOP时（图一），交叉业务的代码直接硬编码在**方法内部的前后**，而AOP则是把交叉业务写在**方法调用前后**。那么，为什么AOP不把代码也写在方法内部的前后呢？两点原因：

- 首先，这与AOP的底层实现方式有关：动态代理其实就是代理对象调用目标对象的同名方法，并在调用前后加增强代码。

![1600313801194](Spring基础(1)：两个概念.assets/1600313801194.png)

- 其次，这两种最终运行效果是一样的，所以没什么好纠结的。

而所谓的模块化，我个人的理解是将切面代码做成一个可管理的状态。比如日志打印，不再是直接硬编码在方法中的零散语句，而是做成一个切面类，通过通知方法去执行切面代码。

我相信大部分培训班出来的朋友也就言尽于此，讲完上面内容就准备打卡下班了。

怎么说呢，IOC按上面的解释，虽然很浅，但也马马虎虎吧。然而AOP，很多人对它的认识是非常片面的... 这样吧，我问你一个问题，现在我自己写了一个UserController，以及UserServiceImpl implements UserService，并且在UserController中注入Service层对象： 

```java
@Autowired
private UserService userService;
```

那么，这个userService一定是我们写的UserServiceImpl的实例吗？

如果你听不懂我要问什么，说明你对Spring的AOP理解还是太少了。

实际上，Spring依赖注入的对象并不一定是我们自己写的类的实例，也可能是userServiceImpl的代理对象。下面分别演示这两种情况：

- 注入userServiceImpl对象

![1600314228749](Spring基础(1)：两个概念.assets/1600314228749.png)

- 注入userServiceImpl的代理对象（CGLib动态代理）

![1600314255492](Spring基础(1)：两个概念.assets/1600314255492.png)

为什么两次注入的对象不同？

因为第二次我给UserServiceImpl加了@Transactional 注解。

![1600314392434](Spring基础(1)：两个概念.assets/1600314392434.png)

此时Spring读取到这个注解，便知道我们要使用事务。而我们编写的UserService类中并没有包含任何事务相关的代码。如果给你，你会怎么做？

动态代理嘛！

上面说了，InvocationHandler作用有两个：1.衔接调用链， 2.存放增强代码。

用动态代理在InvocationHandler的invoke()中开启关闭事务即可完成事务控制。JDK动态代理实现事务请参考：[bravo1988：浅谈JDK动态代理（下）](https://zhuanlan.zhihu.com/p/63126398)

 但是，上面对IOC和AOP的理解，也仅仅是应用级别，是一个面。仅仅了解到这个程度，对Spring的了解还是非常扁平的，不够立体。

---

## Spring说，万物皆可定义

上帝说，要有光。于是特斯拉搞出了交流电。

Java说，万物皆对象。但是Spring另外搞了BeanDefinition...

什么BeanDefinition呢？其实它是bean定义的一个顶级接口:

![1600314556619](Spring基础(1)：两个概念.assets/1600314556619.png)

哎呀卧槽，啥玩意啊。描述一个bean实例？我咋想起了Class类呢。

其实，两者并没有矛盾。

![1600314592056](Spring基础(1)：两个概念.assets/1600314592056.png)

Class只是描述了一个类有哪些字段、方法，但是无法描述如何实例化这个bean！**如果说，Class类描述了一块猪肉，那么BeanDefinition就是描述如何做红烧肉：**

- 单例吗？
- 是否需要延迟加载？
- 需要调用哪个初始化方法/销毁方法？

>  在容器内部，这些bean定义被表示为BeanDefinition对象，包含以下元数据：
>
> 1.包限定的类名：通常，定义bean的实际实现类。
> 2.Bean行为配置：它声明Bean在容器中的行为(范围、生命周期回调，等等)。
> 3.Bean依赖：对其他Bean的引用。
> 4.对当前Bean的一些设置：例如，池的大小限制或在管理连接池的bean中使用的连接数。
> ——Spring官方文档 

 大部分初学者以为Spring解析<bean/>或者@Bean后，就直接搞了一个bean存到一个大Map中，其实并不是。 

- Spring首先会扫描解析指定位置的所有的类得到Resources（可以理解为读取.Class文件）
- 然后依照TypeFilter和@Conditional注解决定是否将这个类解析为BeanDefinition
- 稍后再把一个个BeanDefinition取出实例化成Bean

 就好比什么呢？你从海里吊了一条鱼，但是你还没想好清蒸还是红烧，那就干脆先晒成鱼干吧。一条咸鱼，其实蕴藏着无限可能，因为它可能会翻身！ 

---

## 默默付出的后置处理器

接下来，我们讨论一下咸鱼如何翻身。

最典型的例子就是AOP。上面AOP的例子中我说过了，如果不加@Transactional，那么Controller层注入的就是普通的userServiceImpl，而加了注解以后返回的实际是代理对象。

为什么Spring要返回代理对象？因为我们压根就没在UserServiceImpl中写任何commit或者rollback等事务相关的代码，但是此时此刻代理对象却能完成事务操作。毫无疑问，这个代理对象已经被Spring加了佐料（事务增强代码）。

那么Spring是何时何地加佐料的呢？说来话长，我们先绕个弯子。

大部分人把Spring比作容器，其实潜意识里是将Spring完全等同于一个Map了。其实，真正存单例对象的Map，只是Spring中很小很小的一部分，仅仅是BeanFactory子类的一个字段，我更习惯称它为“单例池”。

```java
/** Cache of singleton objects: bean name --> bean instance */
private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(256);
```

![1600316488565](Spring基础(1)：两个概念.assets/1600316488565.png)

 这里的ApplicationContext和BeanFactory是接口，实际上都有各自的子类。比如注解驱动开发时，Spring中最关键的就是AnnotationConfigApplicationContext和DefaultListableBeanFactory。 

 所以，很多人把Spring理解成一个大Map，还是太肤浅了。就拿ApplicationContext来讲，它也实现了BeanFactory接口，说明它其实也是一个容器。但是同为容器，与BeanFactory不同的是，ApplicationContext主要用来包含各种各样的组件，而不是存bean： 

![1600316538135](Spring基础(1)：两个概念.assets/1600316538135.png)

那么，Spring是如何给咸鱼加佐料（事务代码的织入）的呢？关键就在于后置处理器。

后置处理器其实可以分好多种，属于Spring的扩展点之一：

![1600316643997](Spring基础(1)：两个概念.assets/1600316643997.png)

前三个BeanFactoryPostProcessor、BeanDefinitionRegistryPostProcessor、BeanPostProcessor都算是后置处理器，这里篇幅有限，暂且先只介绍一下BeanPostProcessor。

![1600316695998](Spring基础(1)：两个概念.assets/1600316695998.png)

 BeanFactoryPostProcessor是用来干预BeanFactory创建的，而BeanPostProcessor是用来干预Bean的实例化。不知道大家有没有试过在普通Bean中注入ApplicationContext实例？你第一时间想到的是： 

```java
@Autowired
ApplicationContext annotationConfigApplicationContext;
```

除了利用Spring本身的IOC容器自动注入以外，你还有别的办法吗？

我们可以让Bean实现ApplicationContextAware接口：

![1600316802484](Spring基础(1)：两个概念.assets/1600316802484.png)

后期，Spring会调用setApplicationContext()方法传入ApplicationContext实例。

> Spring官方文档：
> 一般来说，您应该避免使用它，因为它将代码耦合到Spring中，并且不遵循控制反转样式。

这是我认为Spring最牛逼的地方：代码具有高度的可扩展性，甚至你自己都懵逼，为什么实现了一个接口，这个方法就被莫名其妙调用，还传进了一个对象...

这其实就是后置处理器的工作！

什么意思呢？

也就是说，虽然表面上在我们只要让Bean实现一个接口就能完成ApplicationContext组件的注入，看起来很简单，但是背地里Spring做了很多事情。Spring会在框架的某一处搞个for循环，遍历当前容器中所有的BeanPostProcessor，其中就包括一个叫ApplicationContextAwareProcessor的后置处理器，它的作用是：处理实现了ApplicationContextAware接口的Bean。

上面这句话有点绕，大家停下来多想几遍。

![1600317141748](Spring基础(1)：两个概念.assets/1600317141748.png)

​											 Spring Bean的生命周期，创建过程必然经过BeanPostProcessor 

 要扩展的类（Bean）是不确定的，但是处理扩展类的流程（循环BeanPostProcessor）是写死的。因为一个程序，再怎么高度可扩展，总有一个要定下来吧。也就是说，在这个Bean实例化的某一紧要处，必然要经过很多BeanPostProcessor。但是，BeanPostProcessor也不是谁都处理，有时也会做判断。比如： 

```java
if (bean instanceof Aware) {
    if (bean instanceof EnvironmentAware) {
        ((EnvironmentAware) bean).setEnvironment(this.applicationContext.getEnvironment());
    }
    if (bean instanceof EmbeddedValueResolverAware) {
        ((EmbeddedValueResolverAware) bean).setEmbeddedValueResolver(this.embeddedValueResolver);
    }
    if (bean instanceof ResourceLoaderAware) {
        ((ResourceLoaderAware) bean).setResourceLoader(this.applicationContext);
    }
    if (bean instanceof ApplicationEventPublisherAware) {
        ((ApplicationEventPublisherAware) bean).setApplicationEventPublisher(this.applicationContext);
    }
    if (bean instanceof MessageSourceAware) {
        ((MessageSourceAware) bean).setMessageSource(this.applicationContext);
    }
    if (bean instanceof ApplicationContextAware) {
        ((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
    }
}
```

所以，此时此刻一个类实现ApplicationContextAware接口，有两层含义：

- 作为后置处理器的判断依据，只有你实现了该接口我才处理你
- 提供被后置处理器调用的方法

![1600317301607](Spring基础(1)：两个概念.assets/1600317301607.png)

## 利用后置处理器返回代理对象

大致了解Spring Bean的创建流程后，接下来我们尝试着用BeanPostProcessor返回当前Bean的代理对象。

pom.xml

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>4.3.12.RELEASE</version>
    </dependency>
</dependencies>
```

 AppConfig 

```java
@Configuration //JavaConfig方式，即当前配置类相当于一个applicationContext.xml文件
@ComponentScan //不写路径，则默认扫描当前配置类（AppConfig）所在包及其子包
public class AppConfig {

}
```

 Calculator 

```java
public interface Calculator {
    public void add(int a, int b);
}
```

 CalCulatorImpl 

```java
@Component
public class CalculatorImpl implements Calculator {
    public void add(int a, int b) {
        System.out.println(a+b);
    }
}
```

 后置处理器MyAspectJAutoProxyCreator 

使用步骤：

1. 实现BeanPostProcessor
2. @Component加入Spring容器

```java
@Component
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
```

 测试类 

```java
public class TestPostProcessor {
    public static void main(String[] args) {

        System.out.println("容器启动成功！");
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        //打印当前容器所有BeanDefinition
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }

        System.out.println("============");
        
        //取出Calculator类型的实例，调用add方法
        Calculator calculator = (Calculator) applicationContext.getBean(Calculator.class);
        calculator.add(1, 2);
}
```

 先把MyAspectJAutoProxyCreator的@Component注释掉，此时Spring中没有我们自定义的后置处理器，那么返回的就是CalculatorImpl： 

![1600317689734](Spring基础(1)：两个概念.assets/1600317689734.png)

把@Component加上，此时MyAspectJAutoProxyCreator加入到Spring的BeanPostProcessors中，会拦截到CalculatorImpl，并返回代理对象：

![1600317709869](Spring基础(1)：两个概念.assets/1600317709869.png)