# (一) SpringBoot起飞之路-HelloWorld

**(一) 初始 SpringBoot**

## **(1) 学习之路**

开发一个 Web 项目，从入门的 JavaWeb，也就是 Servlet + Tomcat 的那一套，学完了这部分，大部分人就会去接触一些框架，像SSM、SSH（少），再接着就会去接触 SpringBoot 等等，随着新框架的出现，亦或者统一框架的版本大更新，我们也需要不断的学习，其实像我平时做的很多东西，说白了也就是简单或者复杂的增删改查，根据需求掺杂着很多业务逻辑，就例如，一个简单的登陆大家在学习中应该做过N个版本了吧

当然，也有一些朋友，直接跳过了 Spring 先看 SpringBoot，然后再回头去学，不过看个人哈，我自己不太习惯， SpringBoot 背后帮你做了太多事情了，由俭入奢易，由奢入俭难，换做我，就没有回去学习的欲望了（卑微码农）

**说明：**

- 如果暂时还不想看这些繁文缛节，真正的 SpringBoot 项目创建的具体实现在 "IDEA 快速创建SpringBoot项目" 标题下
- 本来想着把一些常用的入门知识写成一篇文章再发出来，后来感觉移动端篇幅太长，阅读体验太差了，就打算分成几篇来发
- 当然对于各位大佬来说，这根本也算不了什么，权当一篇工具文来看啦，不喜勿愤哈 ~

## **(2) 框架发展之路**

到此看来，暂且可以粗浅的将 Java 企业级应用的开发 分为如下三个过程：Javaweb --> Spring --> SpringBoot

### **A：为什么用框架？**

有了 JavaWeb 为什么还要用 Spring 等框架呢，我在之前的文章也说过，这些框架可以大大的减少开发的成本，却又能高效的实现一些需求，给大家摘一段，以前我写的一些浅薄的看法：

> 不扯什么太专业的名词，咱们就讲点大白话，大家应该都听过 **“框架是一个半成品”** ，这句话没毛病，框架就是封装了很多很多的代码，然后你只需要直接调用它提供给你的 API 就能直接使用它的功能
> 当然框架的最初意愿当然都是为了简化开发，帮助开发者快速的完成项目需求，说的确切一点，就是框架中结合了很多的**设计模式**，可以让你 **“动态”** 的开发，将代码实现了通用性，一般自己写的简单的代码，都涉及太多的 “硬编码” 问题了 ，而如果自己去写这些设计模式又太复杂了

### **B：为什么用 SpringBoot**

Spring 帮助人们能够相对高效的快速搭建出一个企业级应用，但是配置却是相对繁琐的，在 2.5 的版本之后，引入了基于注解的组件扫描，这已经使得XML的少了很多，3.0版本以后可以使用@Configuration定义配置类，XML 也可以全部去掉了，也可以叫做纯注解开发，不过现在很多人还是习惯 XML + 注解的方式开发

- Spring 虽然在 XML配置 在一定程度上得到了消除，但是这些配置说实话是有一些麻烦的，不可避免的就需要花费一些时间在配置上面
- 再者，依赖的坑，真的一把鼻涕一把泪，不同版本的依赖疯狂冲突不兼容，总让我发疯

而 SpringBoot ，就是在这种背景下被开发出来的框架，它在很好的解决了我们存在的问题，能让我们开发更加容易，下面我们就来认识一下它

## **(2) SpringBoot 真香**

### **A：如何解决过去的问题**

SpringBoot，根据Spring等过去的一些问题，提出了**约定优于配置的思想**，默认的进行了很多设置，在背后帮我们做了很多事情，使得开发者可以使用少量的配置就可以快速构建项目，或者集成一些第三方的东西，开发人员就不需要为了配置而过分的花费心思，专心写业务逻辑就可以了，这使得开发的效率大幅度提升

### **B：基本概念**

SpringBoot 也不需要理解为一个新框架，它不为了替代 Spring 而产生，它主要为了简化 Spring 的开发，其默认帮我们配置了很多框架，使得我们可以不再为了那些框架的配置过于费神，使得上手变得更加容易

SpringBoot 的官方说明：

> Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".
> We take an opinionated view of the Spring platform and third-party libraries so you can get started with minimum fuss. Most Spring Boot applications need minimal Spring configuration

### **C：特点**

- 为基于Spring的开发提供更快的入门体验
- 直接上手，没有冗余代码生成，也没有XML配置
- 内嵌式的容器极大地简化了Web项目
- 旨在简化 Spring，非替代Spring

SpringBoot 的官方说明：

> Features

- Create stand-alone Spring applications
- Embed Tomcat, Jetty or Undertow directly (no need to deploy WAR files)
- Provide opinionated 'starter' dependencies to simplify your build configuration
- Automatically configure Spring and 3rd party libraries whenever possible
- Provide production-ready features such as metrics, health checks, and externalized configuration
- Absolutely no code generation and no requirement for XML configuration



### **D：核心功能**

### **起步依赖**

- 起步依赖本质上是一个Maven项目对象模型（Project Object Model，POM），定义了对其他库的传递依赖，这些东西加在一起即支持某项功能
- 简单的说，起步依赖就是将具备某种功能的坐标打包到一起，并提供一些默认的功能，你只需要告诉Spring Boot需要什么功能，它就能引入需要的库

### **自动配置**

- 针对很多Spring应用程序常见的应用功能，Spring Boot能自动提供相关配置
- 同时Spring Boot的自动配置是一个运行时（更准确地说，是应用程序启动时）的过程，考虑了众多因素，才决定Spring配置应该用哪个，不该用哪个

当经过简单的学习后，再回来看看这些话，就知道 SpringBoot 有多么强了，不过我们做不了巨人，不如我们就站在巨人的肩膀上学习！ 起飞！！！

## **(二) Hello SpringBoot**

## **(1) 初次体验（Maven创建）**

提前说明：此方法，以后基本不会用的，只是为了第二点和第三点做铺垫，从而更好的明白，SpringBoot 的简便、自动

创建一个空工程，我们下面的几种演示方式，就分别创 mudule 就行了，自己弄得话直接创在 Project 也是一样的

![image-20200916203326516]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916203326516.png)

### **A：创建 Maven Module**

创建一个 Module，选择 Maven 工程，别勾选我们以前常用的 web 骨架，直接创建就行了

![image-20200916203349829]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916203349829.png)

填好这些基本的值

> GroupID 是项目组织唯一的标识符，一般来说可以设置的与包结构一致，也就是 main 目录里java 的目录结构，可以设置为域名的倒序，当然这不是强制的，例如我设置为 cn.ideal
> ArtifactID 就是项目的唯一的标识符，一般设置为项目的名称
> 正是通过这两个值，形成了一个 “坐标” ，能保证项目的唯一性

![image-20200916203419086]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916203419086.png)

这步也没啥好说的，大家都很熟悉

![image-20200916203443706]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916203443706.png)

### **B：添加起步依赖**

根据 SpringBoot 的要求，我们要进行简单的测试还需要添加其起步的依赖

- 项目要继承SpringBoot的起步依赖 spring-boot-starter-parent
- 为了集成SpringMVC进行Controller的开发，所以项目要导入web的启动依赖 spring-boot-starter-web

```xml
 <?xml version="1.0" encoding="UTF-8"?>
 <project xmlns="http://maven.apache.org/POM/4.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
     <modelVersion>4.0.0</modelVersion>
 
     <parent>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-parent</artifactId>
         <version>2.2.7.RELEASE</version>
     </parent>
 
     <groupId>cn.ideal</groupId>
     <artifactId>springboot_01_start</artifactId>
     <version>1.0-SNAPSHOT</version>
 
     <dependencies>
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-web</artifactId>
         </dependency>
     </dependencies>
 </project>
```

### **C：编写 SpringBoot 启动类**

我们的包结构现在是 cn.ideal 我们就在此目录下创建一个类叫做 MySpringBootApplication (名字不强制要求)

写入下列内容，别忘记注解，暂时只需要知道，这个启动类是很关键的，具体的解释会在后面几篇文章写

```java
 package cn.ideal;
 
 import org.springframework.boot.SpringApplication;
 import org.springframework.boot.autoconfigure.SpringBootApplication;
 
 @SpringBootApplication
 public class MySpringBootApplication {
     public static void main(String[] args) {
         SpringApplication.run(MySpringBootApplication.class);
     }
 }
```

### **D：创建 controller**

接着就很熟悉了，我在 cn.ideal 下创建了 controller 包，接着创建了一个

```java
package cn.ideal.controller;
 
 import org.springframework.stereotype.Controller;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.ResponseBody;
 
 @Controller
 public class QuickStartController {
     @RequestMapping("/test")
     @ResponseBody
     public String test(){
         return "springboot 访问测试，起飞，飞飞飞飞 ~ ~ ~";
     }
 }
```

### **E：测试 SpringBoot**

打开刚才创建的 MySpringBootApplication 启动类，然后运行其主函数，控制台会这样一个图案打印出

![image-20200916204353536]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916204353536.png)

同时会有一些信息输出，观察到这样一句 Tomcat started on port(s): 8080 (http) with context path '' 这也就是说我们可以通过 8080 端口访问，同时我们没有设置应用名，也就是直接用端口访问就行了

```text
  .   ____          _            __ _ _
  /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
 ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
  \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
   '  |____| .__|_| |_|_| |_\__, | / / / /
  =========|_|==============|___/=/_/_/_/
  :: Spring Boot ::        (v2.2.7.RELEASE)
 
 2020-05-10 22:11:34.973  INFO 30580 --- [           main] cn.ideal.MySpringBootApplication         : Starting MySpringBootApplication on LAPTOP-5T03DV1G with PID 30580 (F:\develop\IdeaProjects\framework-code\springboot_01_demo\springboot_01_start\target\classes started by abc in F:\develop\IdeaProjects\framework-code\springboot_01_demo)
 2020-05-10 22:11:34.976  INFO 30580 --- [           main] cn.ideal.MySpringBootApplication         : No active profile set, falling back to default profiles: default
 2020-05-10 22:11:35.686  INFO 30580 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
 2020-05-10 22:11:35.693  INFO 30580 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
 2020-05-10 22:11:35.693  INFO 30580 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.34]
 2020-05-10 22:11:35.765  INFO 30580 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
 2020-05-10 22:11:35.766  INFO 30580 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 747 ms
 2020-05-10 22:11:35.884  INFO 30580 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
 2020-05-10 22:11:35.990  INFO 30580 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
 2020-05-10 22:11:35.993  INFO 30580 --- [           main] cn.ideal.MySpringBootApplication         : Started MySpringBootApplication in 1.318 seconds (JVM running for 2.665)
```

直接访问首页是一个白页，因为我们并没有设置 index 页面，所以自然会是这样

![image-20200916204621359]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916204621359.png)

我们请求一下刚才的 controller，我们的字符串就被成功的打印出来了

![image-20200916204641456]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916204641456.png)

## **(2) 官网 Web页面创建项目**

这种方式也是为了第三点做铺垫，使用Spring Initializr 的 Web页面创建项目，

1、打开官网给出的 Spring Initializr 页面 [https://start.spring.io/](https://link.zhihu.com/?target=https%3A//start.spring.io/)

2、把项目信息填写好，我们在第三大点讲

3、点击”Generate Project“ 按钮生成项目，然后下载这个项目

4、解压后，并用IDEA以Maven项目导入

## **(3) IDEA 快速创建SpringBoot项目（※）**

这个是我们比较常用的创建方式

### **A：创建 Spring Initializr Module**

一看这个 URL 是不是一下子就明白了，他默认也是去官网这个网站生成，只不过不需要再导入等等了，直接就可以在 IDEA 生成

![image-20200916205411616]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916205411616.png)

### **B：填写项目信息**

这个部分，首先填入 Group 和 Artifact，然后默认包名会带着我们的项目名，我们删掉就留下一个基本的包结构

关于版本我们都用默认的，当然可以根据自己的选择来改

![image-20200916205518114]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916205518114.png)

### **C：选择初始化组**

这一步，就是让我们你选择初始化的时候，要帮我们加载哪些组件，初学选择 Web 就行了，我在这里同时也勾选了 DevTools ，这是我们后面热部署要用到的，暂时不选也是可以的

![image-20200916205652915]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916205652915.png)

![image-20200916205708814]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916205708814.png)

继续确定位置和名字

![image-20200916205733829]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916205733829.png)

到这里，我们的项目就创建好了

### **D：项目生成了什么**

通过上面步骤完成了基础项目的创建。就会自动生成以下文件

- 程序的主启动类，他是根据项目名来自动命名的，例如 Springboot02QuickstartApplication
- 一个 测试类，Springboot02QuickstartApplicationTests
- 一个 application.properties 配置文件
- 一个 pom.xml，同时根据我们前面所选的初始化组件，帮我们自动的导入了一些依赖

可以看到，我们启动类以及pom都不需要我们自己创建了，省了很多功夫

下面给出自动生成的 pom.xml，我都加了一点注释

```xml
 <?xml version="1.0" encoding="UTF-8"?>
 <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
     <modelVersion>4.0.0</modelVersion>
     <parent>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-parent</artifactId>
         <version>2.2.7.RELEASE</version>
         <relativePath/> <!-- lookup parent from repository -->
     </parent>
     <groupId>cn.ideal</groupId>
     <artifactId>springboot_02_quickstart</artifactId>
     <version>0.0.1-SNAPSHOT</version>
     <name>springboot_02_quickstart</name>
     <description>Demo project for Spring Boot</description>
 
     <properties>
         <java.version>1.8</java.version>
     </properties>
 
     <dependencies>
         <dependency>
             <!-- web相关 -->
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-web</artifactId>
         </dependency>
 
         <dependency>
             <!-- 热部署相关 -->
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-devtools</artifactId>
             <scope>runtime</scope>
             <optional>true</optional>
         </dependency>
         <dependency>
             <!-- springboot单元测试相关-->
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-test</artifactId>
             <scope>test</scope>
             <!-- 剔除依赖 -->
             <exclusions>
                 <exclusion>
                     <groupId>org.junit.vintage</groupId>
                     <artifactId>junit-vintage-engine</artifactId>
                 </exclusion>
             </exclusions>
         </dependency>
     </dependencies>
 
     <build>
         <plugins>
             <plugin>
                 <!-- 打包插件 -->
                 <groupId>org.springframework.boot</groupId>
                 <artifactId>spring-boot-maven-plugin</artifactId>
             </plugin>
         </plugins>
     </build>
 
 </project>
```

## **(三) SpringBoot 工程热部署**

继续推荐两个比较常用的东西，首先是热部署

我们在开发中经常会修改类、页面等等，每次修改后就需要重新启动，很麻烦，浪费时间，我们可以在 pom.xml 中添加如下配置就可以实现不重启使得代码生效，我们称之为热部署

## **(1) 添加依赖**

下面是热部署的依赖，如果我们在初始化组件的时候就选择 **devtools** 就不需要自己添加依赖了

```xml
 <!--热部署配置-->
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-devtools</artifactId>
 </dependency>
```

配置完还不行，因为默认 IDEA 是不会自动编译的，所以我们还需要修改一些设置

## **(2) 修改配置**

在设置中，找到编译相关的，然后将Build project automatically 勾选上

![image-20200916210235492]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916210235492.png)

接着组合键 Shift+Ctrl+Alt+/，选择Registry

![image-20200916210525825]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916210525825.png)

在下面找到，complier.automake.allow.when.app.running 然后勾选，接着CLOSE退出

![image-20200916210605802]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916210605802.png)

重新启动服务器，就可以看到效果了，当我们对类等进行修改后，代码也可以直接生效了

## **(四) 项目打成Jar包**

在上面我们存在这样一个jar包，怎么使用呢？

```xml
 <plugin>
     <!-- 打包插件 -->
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-maven-plugin</artifactId>
 </plugin>
```

在右侧我们可以选择 clean 然后 package 进行打包，当控制台显示 BUILD SUCCESS 后，target下就会多出一个jar包，例如我的 springboot_02_quickstart-0.0.1-SNAPSHOT.jar

![image-20200916210745735]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916210745735.png)

成功后，我们可以指向到 target 下看一下效果，运行成功了，浏览器也可以访问

![image-20200916211813689]((一) SpringBoot起飞之路-HelloWorld.assets/image-20200916211813689.png)