# 超全面 spring 复习总结笔记

# 目录

1. 环境搭建
2. 入门
3. 配置详解
4. AOP
5. JDBCTemplate
6. 事务
7. 整合 Web 和 Junit

# 正文

## 1. 环境搭建

这里介绍的是 Spring Framew2. 入门

### IOC

#### 定义

控制反转（Inverse of  Control）：对象在被创建的时候，由一个调控系统内所有对象的外界实体将其所依赖的对象的引用传递给它。
这里是指将对象创建的任务交给 Spring 来做。

#### Demo

com/ittianyu/spring/a_ioc/applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="UserDao" class="com.ittianyu.spring.a_base.z_common.dao.impl.UserDaoImpl"/>

</beans>
```

com.ittianyu.spring.a_base.a_ioc.TestClass

```java
public class TestClass {

    @Test
    public void helloIOC() {
        String xmlPath = "com/ittianyu/spring/a_ioc/applicationContext.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);

        // 通过 spring 创建 dao
        UserDao userDao = (UserDao) applicationContext.getBean("UserDao");
        userDao.save(new User("hello spring !"));
    }
    
}
```

### DI

#### 定义

依赖注入（Dependency Injection，简称DI）：在程序运行过程中，客户类不直接实例化具体服务类实例，而是客户类的运行上下文环境或专门组件负责实例化服务类，然后将其注入到客户类中，保证客户类的正常运行。
 在这里指的是 要创建的对象中的成员变量由 Spring 进行 set。

#### Demo

com/ittianyu/spring/b_di/applicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="UserService" class="com.ittianyu.spring.a_base.z_common.service.impl.UserServiceImpl">
        <!-- 依赖注入 DI：在创建 service 时，需要设置 service 所依赖的 dao 实现 -->
        <property name="userDao" ref="UserDao"/>
    </bean>
    <bean id="UserDao" class="com.ittianyu.spring.a_base.z_common.dao.impl.UserDaoImpl"/>

</beans>
```

com.ittianyu.spring.a_base.b_di.TestClass

```java
public class TestClass {

    @Test
    public void helloDI() {
        String xmlPath = "com/ittianyu/spring/b_di/applicationContext.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);

        // 通过 spring 创建 service
        UserService userService = (UserService) applicationContext.getBean("UserService");
        userService.addUser(new User("hello spring !"));
    }

}
```

## 3. 配置详解

### 实例化方式

默认构造、静态工厂、实例工厂

#### 默认构造

通过默认构造方法实例化对象

```xml
<bean id="" class="">
```

#### 静态工厂

通过工厂的静态方法实例化对象

```xml
<bean id=""  class="工厂全类名"  factory-method="静态方法">
```

#### 工厂

通过工厂的普通方法实例化对象

```xml
<!-- 1. 创建工厂实例 -->
<bean id="myBeanFactory" class="com.xxx.MyBeanFactory"></bean>
<!-- 2. 通过工厂实例创建对象 userservice
    * factory-bean 指定工厂实例
    * factory-method 指定创建方法
-->
<bean id="userService" factory-bean="myBeanFactory"
    factory-method="createService"></bean>
```

### Bean 种类

- 普通 bean：通过默认构造直接创建的 bean
- FactoryBean：具有工厂生产对象能力的 bean，只能生成特定的对象。
   bean 必须使用 FactoryBean接口，此接口提供方法 `getObject()` 用于获得特定 bean。
   `<bean id="" class="FB">`
   先创建FB实例，使用调用 getObject() 方法，并返回方法的返回值

```java
FB fb = new FB();
return fb.getObject();
```

BeanFactory 和 FactoryBean 对比？

- BeanFactory：工厂，用于生成任意bean。
- FactoryBean：特殊bean，用于生成另一个特定的bean。    例如：ProxyFactoryBean ，此工厂bean用于生产代理。<bean id="" class="....ProxyFactoryBean"> 获得代理对象实例。AOP使用

### 作用域

| 类别          | 说明                                                         |
| ------------- | ------------------------------------------------------------ |
| singleton     | Spring IOC 容器中仅存在一份 bean 实例                        |
| prototype     | 每次都创建新的实例                                           |
| request       | 一次 http 请求创建一个新的 bean，仅适用于 WebApplicationContext |
| session       | 一个 session 对应一个 bean 实例，仅适用于 WebApplicationContext |
| globalSession | 一般用于 Portlet 应用环境，作用域仅适用于 WebApplicationContext 环境 |

scope 为作用域

```xml
<bean id="" class=""  scope="">
```

### 生命周期

#### 初始化和销毁

目标方法执行前后执行的方法

```xml
<bean id="" class="" init-method="初始化方法"  destroy-method="销毁方法">
```

例子：

com.ittianyu.spring.a_base.e_lifecycle.UserServiceImpl

```csharp
public class UserServiceImpl implements UserService{
    @Override
    public void addUser() {
        System.out.println("addUser");
    }
    // 在 xml 中配置声明周期方法
    public void onInit() {
        System.out.println("onInit");
    }
    // 销毁方法只有满足 1.容器 close 2. 必须是单例
    public void onDestroy() {
        System.out.println("onDestroy");
    }
}
```

配置：
com/ittianyu/spring/a_base/e_lifecycle/applicationContext.xml

```xml
...
<!-- 注册 初始化和销毁方法 -->
<bean id="UserService" class="com.ittianyu.spring.a_base.e_lifecycle.UserServiceImpl"
      init-method="onInit" destroy-method="onDestroy" />
...
```

测试：
com.ittianyu.spring.a_base.e_lifecycle.TestClass

```java
public class TestClass {
    @Test
    public void test() {
        String xmlPath = "com/ittianyu/spring/e_lifecycle/applicationContext.xml";
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);

        // 通过 spring 生成工厂 调用 工厂方法 生成对象
        UserService userService = applicationContext.getBean("UserService", UserService.class);
        userService.addUser();

        // 关闭容器才会调用 销毁的方法
        applicationContext.close();
    }
}
```

#### BeanPostProcessor

只要实现此接口BeanPostProcessor，并配置此 bean，则在所有初始化方法前执行 `before()`，在初始化方法之后执行 `after()`。

例子：
 com.ittianyu.spring.a_base.e_lifecycle.MyBeanPostProcessor

```java
public class MyBeanPostProcessor implements BeanPostProcessor {

    // 不在 before 使用代理因为 jdk 的代理是面向接口，而 init 和 destroy 方法是 实现类的
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("before");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("after");

        // 在 after 中生成 代理对象，在执行方法前后开启和关闭事务
        Object proxy = Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("开始事务");
                        Object result = method.invoke(bean, args);
                        System.out.println("关闭事务");
                        return result;
                    }
                });

        return proxy;
    }
}
```

com/ittianyu/spring/a_base/e_lifecycle/applicationContext.xml

```xml
...
<!--注册 初始化 前后回调-->
<bean class="com.ittianyu.spring.a_base.e_lifecycle.MyBeanPostProcessor" />
...
```

### 属性注入

#### 注入方式

- 手动装配：一般进行配置信息都采用手动
  - 基于xml装配：构造方法、setter方法
  - 基于注解装配
- 自动装配：struts 和 spring 整合可以自动装配
  - byType：按类型装配
  - byName：按名称装配
  - constructor：构造装配
  - auto： 不确定装配

#### 构造注入

实体类

```kotlin
public class User {
    private Integer id;
    private String username;
    private Integer age;

    public User(Integer id, String username) {
        this.id = id;
        this.username = username;
    }
    // 省略 get set...
}
```

配置

```xml
...
<!--
基于 name 配置构造参数，不常用
-->
<!--    <bean id="user" class="com.ittianyu.spring.a_base.f_xml.a_constructor.User">
    <constructor-arg name="username" value="name"/>
    <constructor-arg name="age" value="1"/>
</bean>-->

<!--
基于 index 和 type 方式
如果不指定 type，可能匹配到多个符合条件的构造方法，默认使用位置在前面的那个
-->
<bean id="User" class="com.ittianyu.spring.a_base.f_xml.a_constructor.User">
    <constructor-arg index="0" type="java.lang.Integer" value="1"/>
    <constructor-arg index="1" type="java.lang.String" value="hello"/>
</bean>
...
```

#### set 注入

实体类省略

```xml
...
<!--
利用 setter 方法进行参数注入
property 的值可以利用 内嵌标签来完成，但一般很少用
-->
<bean id="Person" class="com.ittianyu.spring.a_base.f_xml.b_setter.Person">
    <property name="name" value="Jone" />
    <property name="age">
        <value>18</value>
    </property>
    <property name="oldCar" ref="OldCar"/>
    <property name="newCar">
        <ref bean="NewCar"/>
    </property>
</bean>

<bean id="OldCar" class="com.ittianyu.spring.a_base.f_xml.b_setter.Car">
    <property name="name" value="拖拉机"/>
    <property name="price" value="5000.0"/>
</bean>
<bean id="NewCar" class="com.ittianyu.spring.a_base.f_xml.b_setter.Car">
    <property name="name" value="宾利"/>
    <property name="price" value="5000000.0"/>
</bean>
...
```

#### P

对 "set 注入" 简化，使用：

```xml
<bean p:属性名="普通值"  p:属性名-ref="引用值">
```

替换

```xml
<property name="" value=""/>
```

必须添加 p 命名空间

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
```

例子：

```xml
...
<!--
利用 setter 方法进行参数注入
在上面要加入 xmlns:p="http://www.springframework.org/schema/p" （从 beans 行复制修改而来）
使用 p 简化 setter 方法
-->
<bean id="Person" class="com.ittianyu.spring.a_base.f_xml.b_setter.Person"
      p:name="Jone" p:age="18"
      p:oldCar-ref="OldCar" p:newCar-ref="NewCar">
</bean>

<bean id="OldCar" class="com.ittianyu.spring.a_base.f_xml.b_setter.Car"
      p:name="拖拉机" p:price="5000.0">
</bean>
<bean id="NewCar" class="com.ittianyu.spring.a_base.f_xml.b_setter.Car"
      p:name="宾利" p:price="5000000.0">
</bean>
...
```

#### SpEL

对 `<property>` 进行统一编程，所有的内容都使用 value

```xml
<property name="" value="#{表达式}">

#{123}、#{'jack'}： 数字、字符串
#{beanId}：另一个bean引用
#{beanId.propName}：操作数据
#{beanId.toString()}：执行方法
#{T(类).字段|方法}：静态方法或字段
```

例子：

```xml
<!--
利用 setter 方法进行参数注入
使用 SPEL 表达式 #{} 为固定格式
字符串用 ''
数字直接写
名称表示引用
静态方法调用 T(Class).method()
-->
<bean id="Person" class="com.ittianyu.spring.a_base.f_xml.b_setter.Person">
    <property name="name" value="#{'Jone'}" />
    <property name="age" value="#{18}" />
    <property name="oldCar" value="#{OldCar}"/>
    <property name="newCar" value="#{NewCar}"/>
</bean>

<bean id="OldCar" class="com.ittianyu.spring.a_base.f_xml.b_setter.Car">
    <property name="name" value="#{'拖拉机'}"/>
    <property name="price" value="#{T(java.lang.Math).random() * 1000 + 5000}"/>
</bean>
<bean id="NewCar" class="com.ittianyu.spring.a_base.f_xml.b_setter.Car">
    <property name="name" value="#{'宾利'}"/>
    <property name="price" value="#{OldCar.price * 1000}"/>
</bean>
```

#### 集合注入

```xml
<!--
各种集合类型的注入配置
    数组：<array>
    List：<list>
    Set：<set>
    Map：<map>, map 存放键值对，使用 <entry> 描述
    Properties：<props>  <prop key=""></prop>
-->

<bean id="Collection" class="com.ittianyu.spring.a_base.f_xml.e_collection.Collection">
    <property name="array">
        <array>
            <value>1</value>
            <value>2</value>
            <value>3</value>
        </array>
    </property>

    <property name="list">
        <list>
            <value>l1</value>
            <value>l2</value>
            <value>l3</value>
        </list>
    </property>

    <property name="map">
        <map>
            <entry key="1" value="m1"/>
            <entry key="2" value="m2"/>
            <entry key="3" value="m3"/>
        </map>
    </property>

    <property name="set">
        <set>
            <value>s1</value>
            <value>s2</value>
            <value>s3</value>
        </set>
    </property>

    <property name="properties">
        <props>
            <prop key="1">p1</prop>
            <prop key="2">p2</prop>
            <prop key="3">p3</prop>
        </props>
    </property>
</bean>
```

### 注解

#### 类型

1. `@Component` 取代 `<bean class="">`
    `@Component("id")` 取代 `<bean id="" class="">`

2. web开发，提供3个 

   ```
   @Component
   ```

    衍生注解（功能一样）取代 

   ```
   <bean class="">
   ```

   - @Repository：dao 层
   - @Service：service 层
   - @Controller：web 层

3. 依赖注入，给私有字段设置，也可以给 setter 方法设置

   普通值：

   ```
   @Value("")
   ```

   引用值：

   - 方式1：按照【类型】注入
      `@Autowired`
   - 方式2：按照【名称】注入1
      `@Autowired`
      `@Qualifier("名称")`
   - 方式3：按照【名称】注入2
      `@Resource("名称")`

4. 生命周期

   - 初始化：`@PostConstruct`
   - 销毁：`@PreDestroy`

5. 作用域
    `@Scope("prototype")`

#### 配置

需要配置自动扫描才能使注解生效

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context" xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <!--自动扫描包和子包下的所有类注解-->
    <context:component-scan base-package="com.ittianyu.spring.a_base.g_annotation.a_ioc" />

</beans>
```

## 4. AOP

### 简介

#### 定义

面向切面编程，通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术。

#### 优点

利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。

#### 原理

aop底层将采用代理机制进行实现。

- 接口 + 实现类：spring 默认采用 jdk 的动态代理 Proxy。
- 实现类：spring 默认采用 cglib 字节码增强。

#### 术语

1. target：目标类，需要被代理的类。例如：UserService
2. Joinpoint：连接点，是指那些可能被拦截到的方法。例如：所有的方法
3. PointCut：切入点，已经被增强的连接点。例如：addUser()
4. advice： 通知/增强，增强代码。例如：after、before
5. Weaving：织入，是指把 advice 应用到 target 来创建代理对象的过程。
6. proxy： 代理类
7. Aspect: 切面，是切入点 pointcut 和通知 advice 的结合
    一个线是一个特殊的面。
    一个切入点和一个通知，组成成一个特殊的面。

#### APO 联盟通知类型

AOP联盟为通知 Advice 定义了 `org.aopalliance.aop.Advice`
 Spring按照通知 Advice 在目标类方法的连接点位置，可以分为5类

- 前置通知 `org.springframework.aop.MethodBeforeAdvice`
- 在目标方法执行前实施增强
- 后置通知 `org.springframework.aop.AfterReturningAdvice`
- 在目标方法执行后实施增强
- 环绕通知 `org.aopalliance.intercept.MethodInterceptor`
- 在目标方法执行前后实施增强
- 异常抛出通知 `org.springframework.aop.ThrowsAdvice`
- 在方法抛出异常后实施增强
- 引介通知 `org.springframework.aop.IntroductionInterceptor`
   在目标类中添加一些新的方法和属性

### Spring APO 编程

#### 切面类

采用环绕通知

```java
public class SpringTimeSpaceAspect implements Aspect, MethodInterceptor {
    private long beforeTime;

    @Override
    public void before() {
        beforeTime = System.currentTimeMillis();
        System.out.println("before:" + beforeTime);
    }

    @Override
    public void after() {
        long afterTime = System.currentTimeMillis();
        System.out.println("after:" + afterTime);
        System.out.println("space:" + (afterTime - beforeTime));
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        before();
        Object result = invocation.proceed();
        after();

        return result;
    }
}
```

#### 配置

1. 导入命名空间 aop

2. 使用 `<aop:config>`进行配置

   ```xml
   proxy-target-class="true" true 表示底层使用 cglib 代理
   <aop:pointcut> 切入点， 从目标对象获得具体方法
   <aop:advisor> 特殊的切面，只有一个通知 和 一个切入点
       advice-ref 通知引用
       pointcut-ref 切入点引用
   ```

3. 切入点表达式

   ```java
   execution(* com.ittianyu.c_spring_aop.*.*(..))
   选择方法 返回值任意   包           类名任意 方法名任意  参数任意
   ```

例如：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop" xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

    <bean id="UserService" class="com.ittianyu.spring.b_aop.service.UserServiceImpl"/>
    <bean id="SpringTimeSpaceAspect" class="com.ittianyu.spring.b_aop.c_spring_proxy.SpringTimeSpaceAspect"/>

    <aop:config>
        <aop:pointcut id="myPointCut" expression="execution(* com.ittianyu.spring.b_aop.service.*.*(..))"/>
        <aop:advisor advice-ref="SpringTimeSpaceAspect" pointcut-ref="myPointCut"/>
    </aop:config>

</beans>
```

### AspectJ

#### 介绍

- AspectJ 是一个基于 Java 语言的 AOP 框架
- Spring2.0 以后新增了对 AspectJ 切点表达式支持
- `@AspectJ` 是 AspectJ1.5 新增功能，通过 JDK5 注解技术，允许直接在 Bean 类中定义切面
   新版本 Spring 框架，建议使用 AspectJ 方式来开发 AOP
- 主要用途：自定义开发

#### 切入点表达式

1. `execution()`用于描述方法 【掌握】

   语法：`execution(修饰符 返回值 包.类.方法名(参数) throws异常)`修饰符，一般省略

   - 修饰符，一般省略
   - 返回值，不能省略
     void 返回没有值
     String 返回值字符串
     \* 任意
   - 包，[省略]
      com.ittianyu.crm            固定包
      com.ittianyu.crm.*.service  crm包下面子包任意 （例如：com.ittianyu.crm.staff.service）
      com.ittianyu.crm..          crm包下面的所有子包（含自己）
      com.ittianyu.crm.*.service..    crm包下面任意子包，固定目录service，service目录任意包
   - 类，[省略]
     UserServiceImpl 指定类
     *Impl 以Impl结尾
     User* 以User开头
     \* 任意
   - 方法名，不能省略
     addUser 固定方法
     add* 以add开头
     *Do 以Do结尾
     \* 任意
   - (参数)
     () 无参
     (int) 一个整型
     (int ,int) 两个
     (..) 参数任意
   - throws ,可省略，一般不写。
   - 综合1
     execution(* com.ittianyu.crm.*.service..*.*(..))
   - 综合2
      <aop:pointcut expression="execution(* com.ittianyu.*WithCommit.*(..)) ||
      execution(* com.ittianyu.*Service.*(..))" id="myPointCut"/>

2. within:匹配包或子包中的方法(了解)
    within(com.ittianyu.aop..*)

3. this:匹配实现接口的代理对象中的方法(了解)
    this(com.ittianyu.aop.user.UserDAO)

4. target:匹配实现接口的目标对象中的方法(了解)
    target(com.ittianyu.aop.user.UserDAO)

5. args:匹配参数格式符合标准的方法(了解)
    args(int,int)

6. bean(id)  对指定的bean所有的方法(了解)
    bean('userServiceId')

#### 通知类型

- before: 前置通知(应用：各种校验)
   在方法执行前执行，如果通知抛出异常，阻止方法运行
- afterReturning: 后置通知(应用：常规数据处理)
   方法正常返回后执行，如果方法中抛出异常，通知无法执行
   必须在方法执行后才执行，所以可以获得方法的返回值。
- around: 环绕通知(应用：十分强大，可以做任何事情)
   方法执行前后分别执行，可以阻止方法的执行
   必须手动执行目标方法
- afterThrowing: 抛出异常通知(应用：包装异常信息)
   方法抛出异常后执行，如果方法没有抛出异常，无法执行
- after: 最终通知(应用：清理现场)
   方法执行完毕后执行，无论方法中是否出现异常

#### 基于 xml Demo

实体类

```java
public class MyAspect {

    public void before(JoinPoint joinPoint) {
        System.out.println("before:" + joinPoint.getSignature().getName());
    }

    public void afterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("after-returning:" + joinPoint.getSignature().getName() + ", result:" + result);
    }

    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around-before:" + joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        System.out.println("around-after:" + joinPoint.getSignature().getName());
        return result;
    }

    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        System.out.println("after-throwing:" + joinPoint.getSignature().getName() + ", exception:" + e.getMessage());
    }

    public void after(JoinPoint joinPoint) {
        System.out.println("after:" + joinPoint.getSignature().getName());
    }
}
```

配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop" xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

    <bean id="UserService" class="com.ittianyu.spring.b_aop.service.UserServiceImpl"/>
    <bean id="MyAspect" class="com.ittianyu.spring.b_aop.d_aspect.a_xml.MyAspect"/>

    <aop:config>
        <aop:aspect ref="MyAspect">
            <aop:pointcut id="myPointCut" expression="execution(* com.ittianyu.spring.b_aop.service.UserServiceImpl.*(..))" />

            <!--前置通知 before
            可选参数 org.aspectj.lang.JoinPoint ，主要用于获取当前执行的方法名
            -->
            <aop:before method="before" pointcut-ref="myPointCut" />

            <!--后置通知 after
            可选参数： org.aspectj.lang.JoinPoint ，主要用于获取当前执行的方法名
                        Object 切入点返回值，参数名需要用属性 returning 指定，比如这里是 result
            -->
            <aop:after-returning method="afterReturning" pointcut-ref="myPointCut" returning="result" />

            <!--环绕通知 around
            方法签名必须如下，要抛出异常
                    public Object around(ProceedingJoinPoint joinPoint) throws Throwable
            中间要执行
                    Object result = joinPoint.proceed();
            最后    return result
            -->
            <aop:around method="around" pointcut-ref="myPointCut" />

            <!--异常通知 after-throwing
            可选参数： org.aspectj.lang.JoinPoint ，主要用于获取当前执行的方法名
                        Throwable 切入点返回值，参数名需要用属性 throwing 指定，比如这里是 e
            -->
            <aop:after-throwing method="afterThrowing" pointcut-ref="myPointCut" throwing="e"/>

            <!--后置通知 after
            可选参数： org.aspectj.lang.JoinPoint ，主要用于获取当前执行的方法名
            -->
            <aop:after method="after" pointcut-ref="myPointCut" />
        </aop:aspect>
    </aop:config>

</beans>
```

测试

```java
public class TestClass {
    @Test
    public void test() {
        String xmlPath = "com/ittianyu/spring/b_aop/d_aspect/a_xml/applicationContext.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);

        UserService userService = (UserService) applicationContext.getBean("UserService");
        userService.add();
        userService.delete();
        userService.update();
    }
}
```

#### 基于注解 Demo

实体类

```java
@Component
@Aspect
public class MyAspect {

    /**
     * <aop:pointcut id="myPointCut" expression="execution(* com.ittianyu.spring.b_aop.service.UserServiceImpl.*(..))" />
     */
    @Pointcut(value = "execution(* com.ittianyu.spring.b_aop.d_aspect.b_annotation.UserServiceImpl.*(..))")
    public void myPointcut() {
    }

    @Before(value = "myPointcut()")
    public void before(JoinPoint joinPoint) {
        System.out.println("before:" + joinPoint.getSignature().getName());
    }

    @AfterReturning(value = "myPointcut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("after-returning:" + joinPoint.getSignature().getName() + ", result:" + result);
    }

    @Around(value = "myPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around-before:" + joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        System.out.println("around-after:" + joinPoint.getSignature().getName());
        return result;
    }

    @AfterThrowing(value = "myPointcut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        System.out.println("after-throwing:" + joinPoint.getSignature().getName() + ", exception:" + e.getMessage());
    }

    @After(value = "myPointcut()")
    public void after(JoinPoint joinPoint) {
        System.out.println("after:" + joinPoint.getSignature().getName());
    }
}
```

切面类

```java
@Service("UserService")
public class UserServiceImpl implements UserService {
    @Override
    public void add() {
        System.out.println("add");
    }

    @Override
    public String delete() {
        System.out.println("delete");
//        int a = 1 / 0;
        return "result-delete";
    }

    @Override
    public void update() {
        System.out.println("update");
    }
}
```

配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

    <!--自动扫描包下的注解-->
    <context:component-scan base-package="com.ittianyu.spring.b_aop.d_aspect.b_annotation" />
    <!--对 aop 注解进行处理-->
    <aop:aspectj-autoproxy/>

</beans>
```

测试

```java
public class TestClass {
    @Test
    public void test() {
        String xmlPath = "com/ittianyu/spring/b_aop/d_aspect/b_annotation/applicationContext.xml";
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);

        UserService userService = (UserService) applicationContext.getBean("UserService");
        userService.add();
        userService.delete();
        userService.update();
    }
}
```

## 5. JDBCTemplate

Spring 提供用于操作JDBC工具类，类似：DBUtils。
 依赖 连接池DataSource （数据源）

### 基本 API

可以通过代码来设置数据源，但一般都是通过配置文件来注入

```java
//1 创建数据源（连接池） dbcp
BasicDataSource dataSource = new BasicDataSource();
// * 基本4项
dataSource.setDriverClassName("com.mysql.jdbc.Driver");
dataSource.setUrl("jdbc:mysql://localhost:3306/spring_day02");
dataSource.setUsername("root");
dataSource.setPassword("1234");


//2  创建模板
JdbcTemplate jdbcTemplate = new JdbcTemplate();
jdbcTemplate.setDataSource(dataSource);


//3 通过api操作
jdbcTemplate.update("insert into t_user(username,password) values(?,?);", "tom","1");
```

### 配置 DBCP

```xml
<!--配置数据源-->
<bean id="DataSource" class="org.apache.commons.dbcp.BasicDataSource">
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql:///myusers"/>
    <property name="username" value="root"/>
    <property name="password" value="123456"/>
</bean>

<!--配置模版-->
<bean id="JdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    <property name="dataSource" ref="DataSource" />
</bean>

<!--配置 dao-->
<bean id="UserDao" class="com.ittianyu.spring.c_jdbc_template.dao.UserDao">
    <property name="jdbcTemplate" ref="JdbcTemplate"/>
</bean>
```

### 配置 C3P0

```xml
<!--配置数据源-->
<bean id="DataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <property name="driverClass" value="com.mysql.jdbc.Driver"/>
    <property name="jdbcUrl" value="jdbc:mysql:///myusers"/>
    <property name="user" value="root"/>
    <property name="password" value="123456"/>
</bean>

<!--配置模版-->
<bean id="JdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
    <property name="dataSource" ref="DataSource" />
</bean>

<!--配置 dao-->
<bean id="UserDao" class="com.ittianyu.spring.c_jdbc_template.dao.UserDao">
    <property name="jdbcTemplate" ref="JdbcTemplate"/>
</bean>
```

### 配置 properties

properties 配置

```java
jdbc.driverClass=com.mysql.jdbc.Driver
jdbc.jdbcUrl=jdbc:mysql:///myusers
jdbc.user=root
jdbc.password=123456
```

Spring 配置

```xml
<!--导入 properties-->
<context:property-placeholder location="classpath:com/ittianyu/spring/c_jdbc_template/d_properties_config/c3p0.properties" />

<!--配置数据源-->
<bean id="DataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <property name="driverClass" value="${jdbc.driverClass}"/>
    <property name="jdbcUrl" value="${jdbc.jdbcUrl}"/>
    <property name="user" value="${jdbc.user}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>
```

### JdbcDaoSupport

Dao 继承 JdbcDaoSupport 后，继承了设置数据源的方法。
 `public final void setDataSource(DataSource dataSource)`
 然后自动创建 JdbcTemplate。

```java
public class UserDao extends JdbcDaoSupport{

    public void add(User user) {
        String sql = "insert into user (username, password, nick) VALUES (?, ?, ?);";
        Object[] args = new Object[]{user.getUsername(), user.getPassword(), user.getNick()};
        getJdbcTemplate().update(sql, args);
    }
}
```

所以，现在 Spring 配置只需要给 dao 注入数据源即可。

```xml
<!--导入 properties-->
<context:property-placeholder location="classpath:com/ittianyu/spring/c_jdbc_template/d_properties_config/c3p0.properties" />

<!--配置数据源-->
<bean id="DataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <property name="driverClass" value="${jdbc.driverClass}"/>
    <property name="jdbcUrl" value="${jdbc.jdbcUrl}"/>
    <property name="user" value="${jdbc.user}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>

<!--配置 dao
    继承了 JdbcDaoSupport，直接注入数据源
-->
<bean id="UserDao" class="com.ittianyu.spring.c_jdbc_template.c_jdbc_dao_support.UserDao">
    <property name="dataSource" ref="DataSource"/>
</bean>
```

## 6. 事务

Spring 中的事务有三个重要的接口。

- PlatformTransactionManager  平台事务管理器，spring要管理事务，必须使用事务管理器，进行事务配置时，必须配置事务管理器。
- TransactionDefinition：事务详情（事务定义、事务属性），spring用于确定事务具体详情，例如：
   隔离级别、是否只读、超时时间 等。
   进行事务配置时，必须配置详情。spring将配置项封装到该对象实例。
- TransactionStatus：事务状态，spring用于记录当前事务运行状态。例如：
   是否有保存点，事务是否完成。
   spring底层根据状态进行相应操作。

### PlatformTransactionManager

#### 常见的事务管理器

- DataSourceTransactionManager  ，jdbc开发时事务管理器，采用JdbcTemplate
- HibernateTransactionManager，hibernate开发时事务管理器，整合hibernate

#### Api

- TransactionStatus getTransaction(TransactionDefinition definition) ，事务管理器 通过“事务详情”，获得“事务状态”，从而管理事务。
- void commit(TransactionStatus status)  根据状态提交
- void rollback(TransactionStatus status) 根据状态回滚

### TransactionStatus

事务状态

```java
public interface TransactionStatus extends SavepointManager, Flushable {

    /**
     * 是否是新事务
     */
    boolean isNewTransaction();

    /**
     * 是否有保存点
     */
    boolean hasSavepoint();

    /**
     * 设置回滚
     */
    void setRollbackOnly();

    /**
     * 是否回滚
     */
    boolean isRollbackOnly();

    /**
     * 刷新
     */
    @Override
    void flush();

    /**
     * 是否已完成
     */
    boolean isCompleted();
}
```

### TransactionDefinition

事务详情

```java
public interface TransactionDefinition {
    /**
     * 传播行为
     */
    int getPropagationBehavior();

    /**
     * 隔离级别
     */
    int getIsolationLevel();

    /**
     * 超时时间
     */
    int getTimeout();

    /**
     * 是否只读
     */
    boolean isReadOnly();

    /**
     * 配置事务详情名称。一般方法名称。例如：save、add 等
     */
    String getName();
}
```

#### 传播行为

在两个业务之间如何共享事务。有以下取值：

- PROPAGATION_REQUIRED , required , 必须  【默认值】
   支持当前事务，A如果有事务，B将使用该事务。
   如果A没有事务，B将创建一个新的事务。
- PROPAGATION_SUPPORTS ，supports ，支持
   支持当前事务，A如果有事务，B将使用该事务。
   如果A没有事务，B将以非事务执行。
- PROPAGATION_MANDATORY，mandatory ，强制
   支持当前事务，A如果有事务，B将使用该事务。
   如果A没有事务，B将抛异常。
- PROPAGATION_REQUIRES_NEW ， requires_new ，必须新的
   如果A有事务，将A的事务挂起，B创建一个新的事务
   如果A没有事务，B创建一个新的事务
- PROPAGATION_NOT_SUPPORTED ，not_supported ,不支持
   如果A有事务，将A的事务挂起，B将以非事务执行
   如果A没有事务，B将以非事务执行
- PROPAGATION_NEVER ，never，从不
   如果A有事务，B将抛异常
   如果A没有事务，B将以非事务执行
- PROPAGATION_NESTED ，nested ，嵌套
   A和B底层采用保存点机制，形成嵌套事务。

常用的是：PROPAGATION_REQUIRED、PROPAGATION_REQUIRES_NEW、PROPAGATION_NESTED

### 转账 Demo

#### 表

```java
create table account(
  id int primary key auto_increment,
  username varchar(50),
  money int
);
insert into account(username,money) values('jack','10000');
insert into account(username,money) values('rose','10000');
```

#### Dao

```java
public class AccountDaoImpl extends JdbcDaoSupport implements AccountDao {
    @Override
    public void out(String outer, Integer money) {
        String sql = "UPDATE account SET money = money - ? WHERE username = ?";
        Object[] args = new Object[]{money, outer};
        getJdbcTemplate().update(sql, args);
    }

    @Override
    public void in(String inner, Integer money) {
        String sql = "UPDATE account SET money = money + ? WHERE username = ?";
        Object[] args = new Object[]{money, inner};
        getJdbcTemplate().update(sql, args);
    }
}
```

#### Service

```java
public class AccountServiceImpl implements AccountService {
    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void transfer(String outer, String inner, Integer money) {

        accountDao.out(outer, money);
//        int i = 1 / 0;
        accountDao.in(inner, money);

    }
}
```

#### xml 配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
        http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">

    <!--配置数据源-->
    <bean id="DataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="com.mysql.jdbc.Driver"/>
        <property name="jdbcUrl" value="jdbc:mysql:///db_transaction"/>
        <property name="user" value="root"/>
        <property name="password" value="123456"/>
    </bean>

    <!--配置 dao
        继承了 JdbcDaoSupport，直接注入数据源
    -->
    <bean id="AccountDao" class="com.ittianyu.spring.d_tx.c_auto.dao.AccountDaoImpl">
        <property name="dataSource" ref="DataSource"/>
    </bean>

    <!--service -->
    <bean id="AccountService" class="com.ittianyu.spring.d_tx.c_auto.service.AccountServiceImpl">
        <property name="accountDao" ref="AccountDao"/>
    </bean>

    <!--tx manager-->
    <bean id="TransactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="DataSource"/>
    </bean>

    <!--配置通知-->
    <tx:advice id="Advice" transaction-manager="TransactionManager">
        <tx:attributes>
            <!--配置 要使用事务的 方法名，传播途径，隔离级别 -->
            <tx:method name="transfer" isolation="DEFAULT" propagation="REQUIRED"/>
        </tx:attributes>
    </tx:advice>

    <aop:config>
        <!--应用通知-->
        <aop:advisor advice-ref="Advice" pointcut="execution(* com.ittianyu.spring.d_tx.c_auto.service..*.*(..))" />
    </aop:config>

</beans>
```

#### 注解配置

Service 注解

```java
@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
public class AccountServiceImpl implements AccountService {
    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void transfer(String outer, String inner, Integer money) {

        accountDao.out(outer, money);
//        int i = 1 / 0;
        accountDao.in(inner, money);

    }
}
```

xml 配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">

    <!--配置数据源-->
    <bean id="DataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="com.mysql.jdbc.Driver"/>
        <property name="jdbcUrl" value="jdbc:mysql:///db_transaction"/>
        <property name="user" value="root"/>
        <property name="password" value="123456"/>
    </bean>

    <!--配置 dao
        继承了 JdbcDaoSupport，直接注入数据源
    -->
    <bean id="AccountDao" class="com.ittianyu.spring.d_tx.d_annotation.dao.AccountDaoImpl">
        <property name="dataSource" ref="DataSource"/>
    </bean>

    <!--service -->
    <bean id="AccountService" class="com.ittianyu.spring.d_tx.d_annotation.service.AccountServiceImpl">
        <property name="accountDao" ref="AccountDao"/>
    </bean>

    <!--tx manager-->
    <bean id="TransactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="DataSource"/>
    </bean>

    <!--
    自动扫描事务注解
    -->
    <tx:annotation-driven transaction-manager="TransactionManager" />

</beans>
```

#### 测试

```java
@Test
public void test() {
    String xmlPath = "com/ittianyu/spring/d_tx/d_annotation/applicationContext.xml";
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);

    AccountService accountService = applicationContext.getBean("AccountService", AccountService.class);
    accountService.transfer("zs", "ls", 100);
}
```

## 7. 整合 Web 和 Junit

### Junit

#### 导包

需要导入 spring-test 包，环境搭建列表里面已经包含了。

#### 目的

1. 让 Junit 通知 Spring 加载配置文件
2. 让 Spring 容器自动进行注入

#### 配置

@ContextConfiguration 用于指定配置的位置
 @Autowired 指定需要注入的对象

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:com/ittianyu/spring/e_junit/applicationContext.xml")
public class TestClass {

    @Autowired
    private AccountService accountService;

    @Test
    public void test() {
        accountService.transfer("zs", "ls", 100);
    }
}
```

### Web

#### 导包

需要导入 spring-web 包，环境搭建列表里面已经包含了。

#### 配置

在 web.xml 中配置 spring listener

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <!-- 修改默认的配置文件位置 -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:com/ittianyu/spring/f_web/applicationContext.xml</param-value>
    </context-param>

    <!--配置 spring  -->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

</web-app>
```

#### 手动获取 Spring 容器

```java
// 方式一，直接 map 中获取
ApplicationContext applicationContext1 = (ApplicationContext) getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
// 方式二，通过工具类获取
ApplicationContext applicationContext2 = WebApplicationContextUtils.getWebApplicationContext(getServletContext());

AccountService accountService = applicationContext2.getBean("AccountService", AccountService.class);
accountService.transfer("zs", "ls", 100);
```