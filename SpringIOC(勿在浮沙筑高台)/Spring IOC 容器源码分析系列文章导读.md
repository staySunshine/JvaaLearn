# 配置如下：Spring IOC 容器源码分析系列文章导读

##  1. 简介

Spring 是一个轻量级的企业级应用开发框架，于 2004 年由 [Rod Johnson](https://en.wikipedia.org/wiki/Rod_Johnson_(programmer)) 发布了 1.0 版本。经过十几年的迭代，现在的 Spring 框架已经非常成熟了。Spring 包含了众多模块，包括但不限于 Core、Bean、Context、AOP 和 Web 等。在今天，我们完全可以使用 Spring 所提供的一站式解决方案开发出我们所需要的应用。作为 Java 程序员，我们会经常和 Spring 框架打交道，所以还是很有必要弄懂 Spring 的原理。

本文是 Spring IOC 容器源码分析系列文章的第一篇文章，将会着重介绍 Spring 的一些使用方法和特性，为后续的源码分析文章做铺垫。另外需要特别说明一下，本系列的源码分析文章是基于`Spring 4.3.17.RELEASE`版本编写的，而非最新的`5.0.6.RELEASE`版本。好了，关于简介先说到这里，继续来说说下面的内容。

## 2. 文章编排

![1601009241716](Spring IOC 容器源码分析系列文章导读.assets/1601009241716.png)

## 3. Spring 模块结构

Spring 是分模块开发的，Spring 包含了很多模块，其中最为核心的是 bean 容器相关模块。像 AOP、MVC、Data 等模块都要依赖 bean 容器。这里先看一下 Spring 框架的结构图：

![1601009288643](Spring IOC 容器源码分析系列文章导读.assets/1601009288643.png)

​																		图片来源：Spring 官方文档

从上图中可以看出`Core Container`处于整个框架的最底层（忽略 Test 模块），在其之上有 AOP、Data、Web 等模块。既然 Spring 容器是最核心的部分，那么大家如果要读 Spring 的源码，容器部分必须先弄懂。本篇文章作为 Spring IOC 容器的开篇文章，就来简单介绍一下容器方面的知识。请继续往下看。

##  4. Spring IOC 部分特性介绍

本章将会介绍 IOC 中的部分特性，这些特性均会在后面的源码分析中悉数到场。如果大家不是很熟悉这些特性，这里可以看一下。

###  4.1 alias

alias 的中文意思是“别名”，在 Spring 中，我们可以使用 alias 标签给 bean 起个别名。比如下面的配置：

```xml
<bean id="hello" class="xyz.coolblog.service.Hello">
    <property name="content" value="hello"/>
</bean>
<alias name="hello" alias="alias-hello"/>
<alias name="alias-hello" alias="double-alias-hello"/>
```

 这里我们给`hello`这个 beanName 起了一个别名`alias-hello`，然后又给别名`alias-hello`起了一个别名`double-alias-hello`。我们可以通过这两个别名获取到`hello`这个 bean 实例，比如下面的测试代码： 

```java
public class ApplicationContextTest {

    @Test
    public void testAlias() {
        String configLocation = "application-alias.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocation);
        System.out.println("    alias-hello -> " + applicationContext.getBean("alias-hello"));
        System.out.println("double-alias-hello -> " + applicationContext.getBean("double-alias-hello"));
    }
}
```

 测试结果如下： 

![1601009484218](Spring IOC 容器源码分析系列文章导读.assets/1601009484218.png)

### 4.2 autowire

本小节，我们来了解一下 autowire 这个特性。autowire 即自动注入的意思，通过使用 autowire 特性，我们就不用再显示的配置 bean 之间的依赖了。把依赖的发现和注入都交给 Spring 去处理，省时又省力。autowire 几个可选项，比如 byName、byType 和 constructor 等。autowire 是一个常用特性，相信大家都比较熟悉了，所以本节我们就 byName 为例，快速结束 autowire 特性的介绍。

当 bean 配置中的 autowire = byName 时，Spring 会首先通过反射获取该 bean 所依赖 bean 的名字（beanName），然后再通过调用 BeanFactory.getName(beanName) 方法即可获取对应的依赖实例。autowire = byName 原理大致就是这样，接下来我们来演示一下。

```java
public class Service {

    private Dao mysqlDao;

    private Dao mongoDao;

    // 忽略 getter/setter

    @Override
    public String toString() {
        return super.toString() + "\n\t\t\t\t\t{" +
            "mysqlDao=" + mysqlDao +
            ", mongoDao=" + mongoDao +
            '}';
    }
}

public interface Dao {}
public class MySqlDao implements Dao {}
public class MongoDao implements Dao {}
```

配置如下：

```xml
<bean name="mongoDao" class="xyz.coolblog.autowire.MongoDao"/>
<bean name="mysqlDao" class="xyz.coolblog.autowire.MySqlDao"/>

<!-- 非自动注入，手动配置依赖 -->
<bean name="service-without-autowire" class="xyz.coolblog.autowire.Service" autowire="no">
    <property name="mysqlDao" ref="mysqlDao"/>
    <property name="mongoDao" ref="mongoDao"/>
</bean>

<!-- 通过设置 autowire 属性，我们就不需要像上面那样显式配置依赖了 -->
<bean name="service-with-autowire" class="xyz.coolblog.autowire.Service" autowire="byName"/>
```

 测试代码如下： 

```java
String configLocation = "application-autowire.xml";
ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocation);
System.out.println("service-without-autowire -> " + applicationContext.getBean("service-without-autowire"));
System.out.println("service-with-autowire -> " + applicationContext.getBean("service-with-autowire"));
```

 测试结果如下： 

![1601009647675](Spring IOC 容器源码分析系列文章导读.assets/1601009647675.png)

### 4.3 FactoryBean

 FactoryBean？看起来是不是很像 BeanFactory 孪生兄弟。不错，他们看起来很像，但是他们是不一样的。FactoryBean 是一种工厂 bean，与普通的 bean 不一样，FactoryBean 是一种可以产生 bean 的 bean，好吧说起来很绕嘴。FactoryBean 是一个接口，我们可以实现这个接口。下面演示一下： 

```java
public class HelloFactoryBean implements FactoryBean<Hello> {

    @Override
    public Hello getObject() throws Exception {
        Hello hello = new Hello();
        hello.setContent("hello");
        return hello;
    }

    @Override
    public Class<?> getObjectType() {
        return Hello.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
```

 配置如下： 

```xml

<bean id="helloFactory" class="xyz.coolblog.service.HelloFactoryBean"/>
```

 测试代码如下： 

```java
public class ApplicationContextTest {

    @Test
    public void testFactoryBean() {
        String configLocation = "application-factory-bean.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocation);
        System.out.println("helloFactory -> " + applicationContext.getBean("helloFactory"));
        System.out.println("&helloFactory -> " + applicationContext.getBean("&helloFactory"));
    }
}
```

 测试结果如下： 

![1601010009083](Spring IOC 容器源码分析系列文章导读.assets/1601010009083.png)

 由测试结果可以看到，当我们调用 getBean(“helloFactory”) 时，ApplicationContext 会返回一个 Hello 对象，该对象是 HelloFactoryBean 的 getObject 方法所创建的。如果我们想获取 HelloFactoryBean 本身，则可以在 helloFactory 前加上一个前缀`&`，即`&helloFactory`。 

### 4.4 factory-method

介绍完 FactoryBean，本节再来看看了一个和工厂相关的特性 – factory-method。factory-method 可用于标识静态工厂的工厂方法（工厂方法是静态的），直接举例说明吧：

```java
public class StaticHelloFactory {

    public static Hello getHello() {
        Hello hello = new Hello();
        hello.setContent("created by StaticHelloFactory");
        return hello;
    }
}
```

配置如下：

```xml
<bean id="staticHelloFactory" class="xyz.coolblog.service.StaticHelloFactory" factory-method="getHello"/>
```

 测试代码如下： 

```java
public class ApplicationContextTest {

    @Test
    public void testFactoryMethod() {
        String configLocation = "application-factory-method.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocation);
        System.out.println("staticHelloFactory -> " + applicationContext.getBean("staticHelloFactory"));
    }
}
```

![1601010128691](Spring IOC 容器源码分析系列文章导读.assets/1601010128691.png)

 对于非静态工厂，需要使用 factory-bean 和 factory-method 两个属性配合。关于 factory-bean 这里就不继续说了，留给大家自己去探索吧。 

###  4.5 lookup-method

lookup-method 特性可能大家用的不多（我也没用过），不过它也是个有用的特性。在介绍这个特性前，先介绍一下背景。我们通过 BeanFactory getBean 方法获取 bean 实例时，对于 singleton 类型的 bean，BeanFactory 每次返回的都是同一个 bean。对于 prototype 类型的 bean，BeanFactory 则会返回一个新的 bean。现在考虑这样一种情况，一个 singleton 类型的 bean 中有一个 prototype 类型的成员变量。BeanFactory 在实例化 singleton 类型的 bean 时，会向其注入一个 prototype 类型的实例。但是 singleton 类型的 bean 只会实例化一次，那么它内部的 prototype 类型的成员变量也就不会再被改变。但如果我们每次从 singleton bean 中获取这个 prototype 成员变量时，都想获取一个新的对象。这个时候怎么办？举个例子（该例子源于《Spring 揭秘》一书），我们有一个新闻提供类（NewsProvider），这个类中有一个新闻类（News）成员变量。我们每次调用 getNews 方法都想获取一条新的新闻。这里我们有两种方式实现这个需求，一种方式是让 NewsProvider 类实现 ApplicationContextAware 接口（实现 BeanFactoryAware 接口也是可以的），每次调用 NewsProvider 的 getNews 方法时，都从 ApplicationContext 中获取一个新的 News 实例，返回给调用者。第二种方式就是这里的 lookup-method 了，Spring 会在运行时对 NewsProvider 进行增强，使其 getNews 可以每次都返回一个新的实例。说完了背景和解决方案，接下来就来写点测试代码验证一下。

在演示两种处理方式前，我们先来看看不使用任何处理方式，BeanFactory 所返回的 bean 实例情况。相关类定义如下：

```java
public class News {
    // 仅演示使用，News 类中无成员变量
}

public class NewsProvider {

    private News news;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }
}
```

 配置信息如下： 

```xml
<bean id="news" class="xyz.coolblog.lookupmethod.News" scope="prototype"/>
<bean id="newsProvider" class="xyz.coolblog.lookupmethod.NewsProvider">
    <property name="news" ref="news"/>
</bean>
```

 测试代码如下： 

```java
String configLocation = "application-lookup-method.xml";
ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocation);
NewsProvider newsProvider = (NewsProvider) applicationContext.getBean("newsProvider");
System.out.println(newsProvider.getNews());
System.out.println(newsProvider.getNews());
```

 测试结果如下： 

![1601010257459](Spring IOC 容器源码分析系列文章导读.assets/1601010257459.png)

 从测试结果中可以看出，newsProvider.getNews() 方法两次返回的结果都是一样的，这个是不满足要求的。 

#### 4.5.1 实现 ApplicationContextAware 接口

我们让 NewsProvider 实现 ApplicationContextAware 接口，实现代码如下：

```java
public class NewsProvider implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private News news;

    /** 每次都从 applicationContext 中获取一个新的 bean */
    public News getNews() {
        return applicationContext.getBean("news", News.class);
    }

    public void setNews(News news) {
        this.news = news;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
```

 配置和测试代码同上，测试结果如下： 

![1601010377507](Spring IOC 容器源码分析系列文章导读.assets/1601010377507.png)

 这里两次获取的 news 并就不是同一个 bean 了，满足了我们的需求。 

#### 4.5.2 使用 lookup-method 特性

使用 lookup-method 特性，配置文件需要改一下。如下：

```java
<bean id="news" class="xyz.coolblog.lookupmethod.News" scope="prototype"/>
<bean id="newsProvider" class="xyz.coolblog.lookupmethod.NewsProvider">
    <lookup-method name="getNews" bean="news"/>
</bean>
```

 NewsProvider 的代码沿用 4.5.1 小节之前贴的代码。测试代码稍微变一下，如下： 

```java
String configLocation = "application-lookup-method.xml";
ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocation);
NewsProvider newsProvider = (NewsProvider) applicationContext.getBean("newsProvider");
System.out.println("newsProvider -> " + newsProvider);
System.out.println("news 1 -> " + newsProvider.getNews());
System.out.println("news 2 -> " + newsProvider.getNews());
```

 测试结果如下： 

![1601010466204](Spring IOC 容器源码分析系列文章导读.assets/1601010466204.png)

 从上面的结果可以看出，new1 和 new2 指向了不同的对象。同时，大家注意看 newsProvider，似乎变的很复杂。由此可看出，NewsProvider 被 CGLIB 增强了。 

###  4.6 depends-on

当一个 bean 直接依赖另一个 bean，可以使用 `` 标签进行配置。不过如某个 bean 并不直接依赖于其他 bean，但又需要其他 bean 先实例化好，这个时候就需要使用 depends-on 特性了。depends-on 特性比较简单，就不演示了。仅贴一下配置文件的内容，如下：

这里有两个简单的类，其中 Hello 需要 World 在其之前完成实例化。相关配置如下：

```xml
<bean id="hello" class="xyz.coolblog.depnedson.Hello" depends-on="world"/>
<bean id="world" class="xyz.coolblog.depnedson.World" />
```

### 4.7 BeanPostProcessor

BeanPostProcessor 是 bean 实例化时的后置处理器，包含两个方法，其源码如下：

```java
public interface BeanPostProcessor {
    // bean 初始化前的回调方法
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    // bean 初始化后的回调方法    
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}
```

 BeanPostProcessor 是 Spring 框架的一个扩展点，通过实现 BeanPostProcessor 接口，我们就可插手 bean 实例化的过程。比如大家熟悉的 AOP 就是在 bean 实例后期间将切面逻辑织入 bean 实例中的，AOP 也正是通过 BeanPostProcessor 和 IOC 容器建立起了联系。这里我来演示一下 BeanPostProcessor 的使用方式，如下： 

```java
/**
 * 日志后置处理器，将会在 bean 创建前、后打印日志
 */
public class LoggerBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("Before " + beanName + " Initialization");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("After " + beanName + " Initialization");
        return bean;
    }
}
```

 配置如下： 

```xml
<bean class="xyz.coolblog.beanpostprocessor.LoggerBeanPostProcessor"/>
    
<bean id="hello" class="xyz.coolblog.service.Hello"/>
<bean id="world" class="xyz.coolblog.service.World"/>
```

 测试代码如下： 

```java
public class ApplicationContextTest {

    @Test
    public void testBeanPostProcessor() {
        String configLocation = "application-bean-post-processor.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(configLocation);
    }
}
```

 测试结果如下： 

![1601010654708](Spring IOC 容器源码分析系列文章导读.assets/1601010654708.png)

 与 BeanPostProcessor 类似的还有一个叫 BeanFactoryPostProcessor 拓展点，顾名思义，用户可以通过这个拓展点插手容器启动的过程。不过这个不属于本系列文章范畴，暂时先不细说了。 

### 4.8 BeanFactoryAware

Spring 中定义了一些列的 Aware 接口，比如这里的 BeanFactoryAware，以及 BeanNameAware 和 BeanClassLoaderAware 等等。通过实现这些 Aware 接口，我们可以在运行时获取一些配置信息或者其他一些信息。比如实现 BeanNameAware 接口，我们可以获取 bean 的配置名称（beanName）。通过实现 BeanFactoryAware 接口，我们可以在运行时获取 BeanFactory 实例。关于 Aware 类型接口的使用，可以参考`4.5.1 实现 ApplicationContextAware 接口`一节中的叙述，这里就不演示了。