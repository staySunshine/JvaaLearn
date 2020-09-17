# Spring基础(2)：放弃XML，走向注解

今天我将会带大家复习Spring的基础，大致流程是：

- 复习XML方式开发
- 通过逐步暴露XML的弊端，引出Spring注解
- 最终完全舍弃XML，采用Spring注解开发

之所以推荐注解开发，原因有两点：

- XML配置太繁琐了
- 掌握Spring注解开发有助于后期学习SpringBoot

主要内容：

- IOC与DI
- Spring的3种编程风格与2种注入方式
- 1️⃣XML配置开发：<bean>描述依赖关系
- 自动装配：让<bean>职责单一化
- 2️⃣XML+注解：XML+<context:component-scan>+@Component
- @Autowired的小秘密
- 2️⃣JavaConfig+注解：@Configuration+@ComponentScan+@Component
- 3️⃣JavaConfig方式：@Configuration+@Bean
- 大乱斗：@ImportResource、@Component、@Bean

---

## IOC与DI

 大家不妨将IOC理解成一种思想，而DI是实现该思想的一种具体方式。Spring被称为IOC容器，它实现IOC的方式除了DI（Dependency Inject，依赖注入），其实还有DL（Dependency Look，依赖查找）。由于我们平时很少用到DL，所以这里只讨论DI（依赖注入）。 

![1600327948708](Spring基础(2)：放弃XML，走向注解.assets/1600327948708.png)

### Spring依赖注入的做法

首先，提供一些配置信息（比如XML）来描述类与类之间的关系，然后由IOC容器（Spring Context）去解析这些配置信息，继而维护好对象之间的关系。

```xml
<!-- 配置信息：在XML中定义Bean -->
<bean id="person" class="com.bravo.annotation.Person">
    <property name="car" ref="car"></property>
</bean>


<bean id="car" class="com.bravo.annotation.Car"></bean>
```

 其次，还有一个很重要的前提是，除了配置信息，对象之间**也要**体现依赖关系。 

```java
public class Person {
    // Person类中声明了Car，表示Person依赖Car
    private Car car;
    // 由于上面XML使用了<property>标签，表示setter方法注入，所以必须提供setter方法
    public void setCar(Car car) {
        this.car = car;
    }
}
```

总结起来就是：

- 编写配置信息描述类与类之间的关系（XML/注解/Configuration配置类均可）
- 对象之间的依赖关系必须在类中定义好（一般是把依赖的对象作为成员变量）
- Spring会按照配置信息的指示，通过构造方法或者setter方法完成依赖注入

![1600328029661](Spring基础(2)：放弃XML，走向注解.assets/1600328029661.png)

---

## Spring的3种编程风格与2种注入方式

按照Spring官方文档的说法，Spring的容器配置方式可以分为3种：

- Schema-based Container Configuration（XML配置）
- Annotation-based Container Configuration（注解）
- Java-based Container Configuration（@Configuration配置类）

Spring支持的2种注入方式：

- 构造方法注入
- setter方法注入

在Spring4之前，Spring还支持接口注入（很少用），这里不提及。

（这个分类还是有问题，后面分析源码时再解释）

大家必须要明确，所谓3种编程风格和2种注入方式到底指什么，之间又有什么联系？

>  Q：Spring注入的是什么？
> A：是Bean。
> Q：这些Bean怎么来的？
> A：IOC容器里的。 

所以，所谓的3种编程风格其实指的是“将Bean交给Spring管理的3种方式”，可以理解为IOC，而2种注入方式即DI，是建立在IOC的基础上的。也就是说Spring的DI（依赖注入）其实是以IOC容器为前提。

![1600328120894](Spring基础(2)：放弃XML，走向注解.assets/1600328120894.png)

## 1️⃣XML配置开发：\<bean>描述依赖关系

### setter方法注入

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

 配置信息（setter方法注入） 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">


    <!-- 在xml中描述类与类的配置信息 -->
    <bean id="person" class="com.bravo.xml.Person">
        <!-- property标签表示，让Spring通过setter方法注入-->
        <property name="car" ref="car"></property>
    </bean>


    <bean id="car" class="com.bravo.xml.Car"></bean>


</bean
```

 Person（这里偷懒，把后面要讲的构造器注入的准备工作也做了，对运行结果不影响） 

```java
public class Person {


    // Person依赖Car
    private Car car;


    // 无参构造
    public Person(){}


    // 有参构造
    public Person(Car car){
        this.car = car;
        System.out.println("通过构造方法注入...");
    }


    // setter方法
    public void setCar(Car car) {
        this.car = car;
        System.out.println("通过setter方法注入...");
    }


    @Override
    public String toString() {
        return "Person{" +
                "car=" + car +
                '}';
}
```

Car

```java
public class Car {
}
```

 Test 

```java
public class Test {
    public static void main(String[] args) {
        // 由于是XML配置方式，对应的Spring容器是ClassPathXmlApplicationContext,传入配置文件告知Spring去哪读取配置信息
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-context.xml");
        // 从容器中获取Person
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);
    }
}
```

 目录结构 

![1600328233853](Spring基础(2)：放弃XML，走向注解.assets/1600328233853.png)

 测试结果 

![1600328258204](Spring基础(2)：放弃XML，走向注解.assets/1600328258204.png)

 						由于XML中配置依赖信息时，使用了property标签，所以Spring会调用setter方法注入 

### 构造方法注入

接下来，我们试一下构造方法注入：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">


    <!-- 在xml中描述类与类的配置信息 -->
    <bean id="person" class="com.bravo.xml.Person">
        <!-- constructor-arg标签表示，让Spring通过构造方法注入-->
        <constructor-arg ref="car"></constructor-arg>
    </bean>
    <bean id="car" class="com.bravo.xml.Car"></bean>
    
</beans>
```

 测试结果 

![1600328421429](Spring基础(2)：放弃XML，走向注解.assets/1600328421429.png)

至此，我们把XML配置下2种注入方式都实验过了，它们的区别是：

- XML配置<property> + 对象提供对应的setter方法
- XML配置<constructor-arg> + 对象提供对应的构造方法

改变XML配置的同时，需要对象提供对应的方法支持。如果你用了<property>，却没有在类中提供setter方法，则会报错。

---

## 自动装配：让\<bean>职责单一化

我们会发现<bean>这个标签，其实承载着两个作用：

- 定义bean，告诉Spring哪个Bean需要交给它管理（放入容器）
- 维护bean与bean之间的依赖关系

接下来我们思考这样一个问题：

对于Person类

```java
public class Person {
    // Person依赖Car
    private Car car;


    public void setCar(Car car) {
        this.car = car;
    }
}
```

上面代码其实已经很好地描述了Person和Car的依赖关系，此时在XML中继续用<property>或者<constructor-arg>反而成了累赘：

- 既然类结构本身包含了依赖信息，<bean>再用<property>等去描述就显得多余了
- 如果类结构变动，我们还需要额外维护<bean>的依赖信息，很麻烦。比如Person新增了一个shoes字段，那么<bean>又要写一个<property>表示shoes

所以，最好的做法是把让<bean>标签职责单一化，让它只负责定义bean，把bean与bean的依赖关系转交给类自身维护（有这个字段就说明有依赖）。

既然菜鸡的我们能想到，那么Spring肯定也想到了，于是它提出了“自动装配”的概念。很多人一听到自动装配，脑子里只有@Autowired。不算错，但其实XML也支持自动装配，而且真要论先来后到的话，肯定还是XML的自动装配在前。

XML实现自动装配可以分为两种：全局、局部。

### **全局自动装配（XML根标签末尾加default-autowire配置）**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd"
        default-autowire="byName">

    <!-- 在xml中只定义bean，无需配置依赖关系 -->
    <bean id="person" class="com.bravo.xml.Person"></bean>
    <bean id="car" class="com.bravo.xml.Car"></bean>
  
</beans>
```

所谓全局，就是在XML根标签末尾再加一个配置default-autowire="byName"，那么在此XML中配置的每一个<bean>都遵守这个自动装配模式，可选值有4个：

- byName
- byType
- constructor
- no

![1600328391909](Spring基础(2)：放弃XML，走向注解.assets/1600328391909.png)

 测试结果 

![1600328457861](Spring基础(2)：放弃XML，走向注解.assets/1600328457861.png)

 **局部自动装配（每一个单独设置autowire）** 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- 在xml中只定义bean，无需配置依赖关系 -->
    <bean id="person" class="com.bravo.xml.Person" autowire="byName"></bean>
    <bean id="car" class="com.bravo.xml.Car"></bean>

</bean
```

 测试结果 

![1600328495084](Spring基础(2)：放弃XML，走向注解.assets/1600328495084.png)

小结：

- Spring支持自动装配（全局/局部），把原先<bean>标签的职责单一化，只定义bean，而依赖关系交给类本身维护
- 自动装配共4种，除了no，其他3种各自对应两种注入方式：byName/byType对应setter方法注入，constructor对应构造方法注入 **（请自己动手证明）**

![1600328545712](Spring基础(2)：放弃XML，走向注解.assets/1600328545712.png)

---

## 2️⃣XML+注解：XML+\<context:component-scan>+@Component

原本<bean>标签有两个职责：

- 定义bean
- 描述依赖信息

上面通过自动装配，把依赖信息交给类本身维护，从此<bean>只负责bean定义。

现在，我们想想办法，能不能干脆把bean定义也剥离出来？这样就不需要在XML中写任何<bean>标签了。我早就看<bean>标签不爽了，这么一大坨，要是bean多了，就很臃肿。

怎么做呢？

我们先来回顾一下手上的牌面：

![1600328618157](Spring基础(2)：放弃XML，走向注解.assets/1600328618157.png)

至此，我们已经成功调教Spring帮我们做了自动装配，也就是说IOC和DI中，DI已经实现自动化。我们接下来要考虑的是如何减少IOC配置的工作量。

原先是把<bean>写在XML中，再把XML喂给Spring：

```java
ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-context.xml");
```

既然现在打算消灭XML中的<bean>，则说明即使把XML喂给Spring，它也吃不到bean定义了。所以，必须要告诉Spring去哪可以吃到bean。

我们来看一下，当Spring吃下<bean>时，到底吃了什么：

```xml
<!-- 在xml中只定义bean，无需配置依赖关系 -->
<bean id="person" class="com.bravo.xml.Person" autowire="byName"></bean>
<bean id="car" class="com.bravo.xml.Car"></bean>
```

是的，<bean>只指定了类名和自动装配的模式。也就是说，要定义一个bean，只需要最基本的两样东西：

- 类名
- 装配模式（其实这个也不是必须的，默认no，不自动装配）

而类名其实很好得到，我们自己写的类不就有吗？至于自动装配的模式，也完全可以在类中通过注解指定。于是，我们找到了改造的方向：用带注解的类代替<bean>标签。

![1600328661629](Spring基础(2)：放弃XML，走向注解.assets/1600328661629.png)

![1600328680541](Spring基础(2)：放弃XML，走向注解.assets/1600328680541.png)

 Spring2.5开始提供了一系列注解，比如@Component、@Service等，这些注解都是用来表示bean的。而@Service等注解底层其实还是@Component： 

![1600328700877](Spring基础(2)：放弃XML，走向注解.assets/1600328700877.png)

之所以做一层封装，是为了赋予它特殊的语义：定义Service层的bean。其余的这里不再赘述。总之我们暂时理解为，如果要使用注解表示bean定义，我们能用的只有@Component。



新建annotation包，把Car和Person移过去：

![1600328730310](Spring基础(2)：放弃XML，走向注解.assets/1600328730310.png)

 Person 

```java
@Component //带注解的类，我们希望用这种方式定义bean，并让Spring把它吃进去
public class Person {


    // Person依赖Car
    private Car car;


    @Override
    public String toString() {
        return "Person{" +
                "car=" + car +
                '}';
    }
}
```

 Car 

```java
@Component
public class Car {
}
```

 XML（什么都没有配置，连自动装配模式也没指定，因为不在这里定义bean了） 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">


</beans>
```

 Test（不变） 

```java
public class Test {
    public static void main(String[] args) {
        // 由于是XML配置方式，对应的Spring容器是ClassPathXmlApplicationContext,传入配置文件告知Spring去哪读取配置信息
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-context.xml");
        // 从容器中获取Person
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);
    }
}
```

 测试结果 

![1600328806038](Spring基础(2)：放弃XML，走向注解.assets/1600328806038.png)

其实很好理解，我们传入了spring-context.xml告诉Spring去哪读取bean定义，但是实际上XML却没有配置任何<bean>，它是不可能把类实例化加入到容器的。

然而我们新定义的bean（@Component）Spring也没吃，怎么回事？

其实主要是因为我们的改变太突然了，Spring以前吃惯了XML中的<bean>，现在突然换成@Component这种注解类，它吃不惯，甚至不知道它能吃！

所以，必须通知Spring：

> 老哥，我们改用注解了，有@Component注解的类就是bean，和以前<bean>一样一样的。

如何通知？只要在XML中配置：

```xml
 <context:component-scan base-package="com.bravo.annotation"/>
```

官方文档对这个标签的解释是：

> The use of \<context:component-scan> implicitly enables the functionality of \<context:annotation-config>. There is usually no need to include the \<context:annotation-config> element when using \<context:component-scan>.

**这个标签的作用相当于什么呢？Spring一口吃下去，发现没有吃到，却吃出了一张小纸条，上面写着：赶紧去找标了@Component注解的类，那是新菜式！**

所以，最终</context:component-scan>标签的作用有两个：

- 扫描：原先我们把写有bean定义的XML文件喂给Spring，现在则让Spring自己去指定路径下扫描bean定义
- 解析：让Spring具备解析注解的功能

所以，XML虽然不用配置<bean>标签，却要配置扫描（需要配置额外的名称空间）：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context-3.0.xsd">


    <context:component-scan base-package="com.bravo.annotation"/>


</beans
```

测试结果：

![1600328891861](Spring基础(2)：放弃XML，走向注解.assets/1600328891861.png)

 又出幺蛾子了，怎么回事呢？我们回想一下XML的bean定义： 

```xml
<!-- 在xml中只定义bean，无需配置依赖关系 -->
<bean id="person" class="com.bravo.xml.Person" autowire="byName"></bean>
<bean id="car" class="com.bravo.xml.Car"></bean>
```

我们设置了autowire属性，告诉Spring按什么方式自动装配。

现在我们改用注解了，@Component只是相当于<bean>标签，却没有指明自动装配的模式。如何在类中告诉Spring我们需要的装配方式呢？

方法有很多种：

- @Autowired（Spring提供的）
- @Resource（JSR-250定义）
- @Inject（JSR-330定义）

这里我们以@Autowired为例：

```java
@Component
public class Person {


    // 用@Autowired告知Spring：请把Car装配进来
    @Autowired
    private Car car;


    @Override
    public String toString() {
        return "Person{" +
                "car=" + car +
                '}';
    }
}
```

测试结果

![1600328962846](Spring基础(2)：放弃XML，走向注解.assets/1600328962846.png)

---

## @Autowired的小秘密

上面我们有惊无险地从用@Component替换了<bean>，并且结识了@Autowired这个超棒的注解，用来完成自动装配。即：

- \<context:component-scan>+@Component彻底解放IOC配置
- @Autowired完成自动装配

但是细心的小伙伴会发现，相较于<bean>中的autowire="byName"，@Autowired虽然装配成功了，却没有显式地指定自动装配的模式。

只有一种解释：它有默认的装配方式。

在探究@Autowire默认的装配模式之前，关于bean的名称，要和大家先交代一下：

```xml
<!-- 在xml中只定义bean，无需配置依赖关系 -->
<bean id="person" class="com.bravo.xml.Person" autowire="byName"></bean>
<bean id="car" class="com.bravo.xml.Car"></bean>
```

在<bean>中，id即为最终bean在Spring容器的名字。

同样的，@Component也提供了给bean命名的方法：

![1600329005237](Spring基础(2)：放弃XML，走向注解.assets/1600329005237.png)

```java
@Component("bravo")
public class Person {


    // 用@Autowired告知Spring：请把Car装配进来
    @Autowired
    private Car car;


    @Override
    public String toString() {
        return "Person{" +
                "car=" + car +
                '}';
    }
}
```

如果不指定，则默认会把类名首字母小写后作为beanName。

铺垫结束，我们开始探究@Autowired到底默认是哪种装配模式：

- byName
- byType
- constructor
- no（已经装配成功，排除）

先来看看是不是byName

```java
@Component
public class Person {


    // 用@Autowired告知Spring：请把Car装配进来
    @Autowired
    private Car myCar;


    @Override
    public String toString() {
        return "Person{" +
                "car=" + myCar +
                '}';
    }
}
```

 测试结果 

![1600329043869](Spring基础(2)：放弃XML，走向注解.assets/1600329043869.png)

Car在Spring中bean的名字应该是car，而我把Person中的Car变量名改为myCar，仍旧注入成功，说明不是byName。

再来看看是不是byType。

这个稍微有点麻烦，因为我需要弄出至少两个同类型的bean。所以我打算把Car变成接口，然后创建Bmw和Benz两个实现类。这个接口只是为了试验，没有实际意义：

 Car 

```java
//接口
public interface Car {
}


//实现类Bmw
@Component
public class Bmw implements Car {
}


//实现类Benz
@Component
public class Benz implements Car {
}
```

 Person 

```java
@Component
public class Person {


    // 用@Autowired告知Spring：请把Car装配进来
    @Autowired
    private Car car;


    @Override
    public String toString() {
        return "Person{" +
                "car=" + car +
                '}';
    }
```

 测试结果 

![1600329114407](Spring基础(2)：放弃XML，走向注解.assets/1600329114407.png)

 				熟悉的配方、熟悉的味道：expected single matching bean but found 2: BMW,benz 

很明显，@Autowired默认采用byType的方式注入，由于当前Spring容器中存在两个Car类型的bean，所以注入时报错了，因为Spring无法替我们决定注入哪一个。

但是，有个神奇的现象是，你如果把变量名改为bmw或者benz，就会注入对应的bean：

```java
@Component
public class Person {

    // 把变量名改为bmw
    @Autowired
    private Car bmw;

    @Override
    public String toString() {
        return "Person{" +
                "car=" + bmw +
                '}';
    }
}
```

![1600329163925](Spring基础(2)：放弃XML，走向注解.assets/1600329163925.png)

**也就是说，@Autowired默认采用byType模式自动装配，如果找到多个同类型的，会根据名字匹配。都不匹配，则会报错。**

当然，有些人可能有强迫症，觉得我Car类型的变量必须叫car，但又想指定注入bmw，怎么办？我们先看看@Autowired能不能指定名字吧：

![1600329199574](Spring基础(2)：放弃XML，走向注解.assets/1600329199574.png)

​				 不能指定名字，因为Autowired只有一个属性：required，表示当前bean是否必须被注入 

 为了弥补@Autowired不能指定名字的缺憾，Spring提供了@Qualifier注解 

```java
@Qualifier("benz")
@Autowired
private Car car;
```

即使Spring容器中有两个Car类型的bean，也只会按名字注入benz。



其他的我就不测了，给个结论就好：

- @Autowired：默认byType，type相同则byName
- @Resource：和@Autowired几乎一样，但不能配合@Qualifier，因为它本身就可以指定beanName。但没有required属性

```java
@Resource(name = "benz")
private Car car;
```

 @Inject：用的很少，不做讨论

---

## 2️⃣JavaConfig+注解：@Configuration+@ComponentScan+@Component

有没有发现，上面标题还是2️⃣？因为接下来要介绍的，还是注解开发。

先复习一下前面两种方式：

- 纯XML（<bean>负责定义bean，Java类负责定义依赖，Spring完成自动装配）

```xml
<!-- 在xml中只定义bean，无需配置依赖关系 -->
<bean id="person" class="com.bravo.xml.Person" autowire="byName"></bean>
<bean id="car" class="com.bravo.xml.Car"></bean>
```

![1600329352958](Spring基础(2)：放弃XML，走向注解.assets/1600329352958.png)

-  注解+XML（@Component+@Autowired，但我们发现注解并不能单独使用，必须要XML中配置开启注解扫描才能生效） 

```xml
 <context:component-scan base-package="com.bravo.annotation"/>
```

![1600329392342](Spring基础(2)：放弃XML，走向注解.assets/1600329392342.png)

之前我在[注解（上）](https://zhuanlan.zhihu.com/p/60941426)讲过，注解的使用必须包含三步：定义注解、使用注解、解析注解。@Component是Spring**定义**、我们**使用**，也肯定是由Spring**解析**。但是这个解析必须由我们手动开启。这就是\<context:component-scan>标签的意义。

到了这一步我们已经把<bean>标签完全消灭了。但是这种模式有点不伦不类。

你说它叫XML配置开发吧，它又有@Component注解。你说它是注解开发吧，XML中还有一个\<context:component-scan>在那嘚瑟呢。所以如何才能完全消灭XML呢？

究其根本，我们发现无法消灭XML的原因在于：注解的读取和解析必须依赖于\<context:component-scan>标签！因为我们要帮Spring开启注解扫描，不然他不知道去哪读取bean。

既然<bean>标签可以被@Component代替，那么\<context:component-scan>标签应该也能找到对应的注解。

不错！这个注解就是@ComponentScan！如此一来我们就再也不需要spring-context.xml了。

但是转念一想，脊背发凉...ClassPathXmlApplicationContext这个类要求我们必须传一个XML，怎么办？别担心，Spring同样提供了一个注解@Configuration，目的是让我们可以把一个普通的Java类等同于一个XML文件，而这个Java类就是JavaConfig，我们习惯称之为配置类。

新建一个javaconfig包，把annotation包下的所有类移过来，并且新建AppConfig配置类

```java
@Configuration //表示这个Java类充当XML配置文件
@ComponentScan(basePackages = "com.bravo.javaconfig") //相当于XML中的<context:component-scan>标签
public class AppConfig {


}
```

这样，我们就可以把XML删除，用@ComponentScan来开启注解扫描。

目录结构

![1600329442655](Spring基础(2)：放弃XML，走向注解.assets/1600329442655.png)

 准备测试时，发现了大麻烦： 

![1600329468095](Spring基础(2)：放弃XML，走向注解.assets/1600329468095.png)

​						 ClassPathXmlApplicationContext无法接受AppConfig配置类，它只认XML 

所以，用AppConfig配置类替代XML只是我们的一厢情愿吗？

其实是我们选错了实现类。ApplicationContext的子类除了ClassPathXmlApplicationContext，还有一个专门针对注解开发的：AnnotationConfigApplicationContext。

新的Test

```java
public class Test {
    public static void main(String[] args) {
        // AnnotationConfigApplicationContext是Spring用来专门针对注解开发的ApplicationContext子类
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        // 从容器中获取Person
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);
    }
}
```

 测试结果 

![1600329550118](Spring基础(2)：放弃XML，走向注解.assets/1600329550118.png)

 至此，XML已经被我们完全消灭了。

---

## 3️⃣JavaConfig方式：@Configuration+@Bean

严格来说，上面的做法并不是所谓的Java-based Container Configuration（@Configuration配置类）风格。我们虽然用到了@Configuration，但只是为了让Java配置类替代XML，最终消灭XML。这也太大材小用了...本质上，这还是@Component+@Autowired注解开发，只是开启注解扫描的方式从\<context:component-scan>标签变为@ComponentScan。

实际上，真正的Java-based Container Configuration编程风格是这样的：

AppConfig（如果你不扫描@Component，则不需要@ComponentScan）

```java
@Configuration
public class AppConfig {

    //new一个Benz对象，通过@Bean注解告知Spring把这个bean加到容器
    @Bean
    public Car benz(){
       return new Benz();
    }
    
    //new一个Bmw对象，通过@Bean注解告知Spring把这个bean加到容器
    @Bean
    public Car bmw(){
        return new Bmw();
    }
    
    //new一个Person对象，通过@Bean注解告知Spring把这个bean加到容器
    @Bean
    public Person person(){
        Person p = new Person();
        p.setCar(new Benz());
        return p;
    }

}
```

 Benz（去除@Component，那是注解开发方式） 

```java
public class Benz implements Car {
}
```

 Bmw（去除@Component，那是注解开发方式） 

```java
public class Bmw implements Car {
}
```

 Person（去除@Component，那是注解开发方式） 

```java
public class Person {

    private Car car;

    // setter方法。在@Bean场景下，手动调用setter方法设置成员变量
    public void setCar(Car car) {
        this.car = car;
    }
 
    @Override
    public String toString() {
        return "Person{" +
                "car=" + car +
                '}';
    }
}
```

 测试结果 

![1600329614630](Spring基础(2)：放弃XML，走向注解.assets/1600329614630.png)

小结

Java-based Container Configuration编程风格指的是：

- 用@Configuration把一个普通Java类变成配置类，充当XML
- 在配置类中写多个方法，加上@Bean把返回值对象加到Spring容器中
- 把配置类AppConfig喂给AnnotationConfigApplicationContext，让它像解析XML一样解析配置类
- 无需加@Component注解，因为我们可以手动new之后通过@Bean加入容器

---

## 大乱斗：@ImportResource、@Component、@Bean

其实XML、注解、JavaConfig三种方式相互兼容，并不冲突。

- XML的<bean>
- @Component注解和扫描（不论是<context:component-scan>还是@ComponentScan）
- @Configuration与@Bean

为了证实它们确实不冲突，我搞了很变态的，一个项目里三种编程方式混用：

- 两辆车子，bmw和benz交给@Bean（JavaConfig）
- Person交给@Component和@ComponentScan（注解）
- Student交给XML和@ImportResource（XML）

目录结构

![1600329664298](Spring基础(2)：放弃XML，走向注解.assets/1600329664298.png)

 AppConfig 

```java
@Configuration //JavaConfig方式，把当前Java类作为配置类
@ComponentScan(basePackages = "com.bravo.all")//注解方式，开启扫描
@ImportResource("spring-context.xml")//XML方式，导入bean定义
public class AppConfig {


    @Bean
    public Car benz(){
       return new Benz();
    }


    @Bean
    public Car bmw(){
        return new Bmw();
    }

}
```

 Car 

```java
public interface Car {
}
```

 Benz（JavaConfig方式：@Bean加入Spring） 

```java
public class Benz implements Car {
}
```

 Bmw（JavaConfig方式：@Bean加入Spring） 

```java
public class Bmw implements Car {
}
```

 Person（注解方式：@ComponentScan扫描@Component加入Spring） 

```java
@Component
public class Person {


    // 用@Autowired告知Spring：请把Car装配进来
    @Qualifier("benz")
    @Autowired
    private Car car;


    @Override
    public String toString() {
        return "Person{" +
                "car=" + car +
                '}';
    }
}
```

 Student（XML方式：使用\<bean>定义） 

```java
public class Student {
    private Car bmw;


    //由于在下方XML配置中，我选用了byName自动装配，而byName/byType都要提供setter方法
    public void setBmw(Car bmw) {
        this.bmw = bmw;
    }


    @Override
    public String toString() {
        return "Student{" +
                "car=" + bmw +
                '}';
    }
}
```

 spring-context.xml 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- 在xml中描述类与类的配置信息 -->
    <bean id="student" class="com.bravo.all.Student" autowire="byName">
    </bean>

</beans>
```

 Test 

```java
public class Test {
    public static void main(String[] args) {
        // AnnotationConfigApplicationContext是Spring用来专门针对注解开发的ApplicationContext子类
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        // 从容器中获取Person
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);
        // 从容器中获取Student
        Student student = (Student) applicationContext.getBean("student");
        System.out.println(student);
    }
}
```

 测试结果 

![1600329769702](Spring基础(2)：放弃XML，走向注解.assets/1600329769702.png)

通常来说，我们日常开发一般是注解+JavaConfig混用。也就是

- @ComponentScan+@Configuration+@Component+@Bean

---

## 小结

- 纯XML配置开发：没有注解，全部<bean>标签，但也可以配置自动装配

- 注解开发不能单独存在，需要**开启扫描**。自动装配一般用@Autowired

- - XML+注解：XML+****+@Component
  - JavaConfig+注解：@Configuration+**@ComponentScan**+@Component

- JavaConfig方式：@Configuration+@Bean

通常我们都会两两混用，比如XML+注解，或者JavaConfig+注解，但很少三种一起用。

本文目的是让大家知道：

- 3种编程风格：XML、注解、JavaConfig
- 2种注入方式：setter方法、构造方法
- 4种装配模式：byType、byName、constructor、no

 