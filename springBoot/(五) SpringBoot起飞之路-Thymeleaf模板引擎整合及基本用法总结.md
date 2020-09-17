# (五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结

## **(一) 模板引擎引入**

## **(1) 开发方式**

在往常的开发中，一旦涉及到一个完整的前后端项目，有两种办法：

- 一种就是前后端分离，也就是说，约定好接口，通过异步方式，以 Json 字符串作为内容传递，后台进行一些业务逻辑，前台负责界面，以及通过 js 等进行数据的处理以及页面的美化。
- 还有一种方式就是模板引擎的方式，这种方式也没什么太新奇的，你可以简单的理解为 JSP 的那种模式

现在来说，前后端分离开始更加流行，但是很多旧的项目，或者自己一个人写东西，我感觉使用模板引擎也是非常不错的选择，还有时候去找一些后台的开源模板，有一些也都用了Thymeleaf， 何况出于学习的态度，学哪种技术都是可以的

针对第二种方式继续说一下，JSP 大家应该是熟悉的，比如前端传来一个 html 的页面，我们就会去将这个页面改写为 JSP 页面，我们可以用 JSP 比较容易的实现数据的显示，那么为什么不继续用 JSP 而要用别的模板引擎呢？

注：Thymeleaf 和 Freemarker 等各有特点，用熟悉后，可能会对另一种的使用方式感觉很别扭，没必要争论哪种更好，自己喜欢就行

## **(2) 为什么用模板引擎**

以 Springboot 来说，官方就不推荐使用 JSP ，来看一下官方的解释

**Springboot 2.2.7**

地址：`https://docs.spring.io/spring-boot/docs/2.2.7.RELEASE/reference/html/spring-boot-features.html#boot-features-jsp-limitations`

### **Springboot：7.1.10. Template Engines**

As well as REST web services, you can also use Spring MVC to serve dynamic HTML content. Spring MVC supports a variety of templating technologies, including Thymeleaf, FreeMarker, and JSPs. Also, many other templating engines include their own Spring MVC integrations.

Spring Boot includes auto-configuration support for the following templating engines:

FreeMarker、Groovy、Thymeleaf、Mustache

> If possible, JSPs should be avoided. There are several known limitations when using them with embedded servlet containers.

上面一段话，总的来说，就是告诉我们，如果我们想要动态的展示HTML内容，就可以用一些模板的技术，例如 FreeMarker、Groovy、Thymeleaf、Mustache

最后有一句话说道点子上了，Springboot 建议我们尽可能的规避 JSP，并且提供了一些说法

### **Springboot：7.4.5. JSP Limitations**

When running a Spring Boot application that uses an embedded servlet container (and is packaged as an executable archive), there are some limitations in the JSP support.

- With Jetty and Tomcat, it should work if you use war packaging. An executable war will work when launched with java -jar, and will also be deployable to any standard container. JSPs are not supported when using an executable jar.
- Undertow does not support JSPs.
- Creating a custom error.jsp page does not override the default view for error handling. Custom error pages should be used instead.

第二点是说，Undertow 支持 JSP，而第三点，则是关于error错误页面的覆盖问题

第二点是说，Undertow 支持 JSP，而第三点，则是关于error错误页面的覆盖问题

前两点都不是我想说的，**我们主要看第一点：当你把一个使用了 JSP 的项目打 war 包放到了 tomcat 或者其他容器中是可以运行的，当然你使用 java -jar 也是可以运行的，但是如果你直接执行 jar 包是不可以的**

也就是说，**打包的类型决定了JSP可不可以被正常解析使用**，同时 SpringBoot 中 Tomcat 是嵌入式的，而 SpringBoot 程序往往又是脱离容器独立运行的，如果用 JSP ，就需要额外的地址去存生成的 Servlet ，可能会存在一定的安全问题，所**默认是不支持jsp的**

## **(3) SpringBoot推荐Thymeleaf吗？**

网络上的文章都在传，SpringBoot 官方推荐 Thymeleaf，我看了一下 SpringBoot 2.2.7 版本的文档（看了下貌似2.3.0也挂成GA了，这部分没啥改变）我自己是没找到一个关于推荐确切的说法

### **Springboot：7.1.10. Template Engines**

- 在Springboot——Spring Boot Features —— 7.1.10. Template Engines 中有提到，SpringBoot 提供了关于Thymeleaf 的自动装配的支持，但是同样的也提供对 FreeMarker、Groovy、Mustache 自动装配的支持，此处并没有看到推荐某一款的说法

### **SpringMVC：1.10.1. Thymeleaf**

Thymeleaf is a modern server-side Java template engine that emphasizes natural HTML templates that can be previewed in a browser by double-clicking, which is very helpful for independent work on UI templates (for example, by a designer) without the need for a running server. If you want to replace JSPs, Thymeleaf offers one of the most extensive set of features to make such a transition easier. Thymeleaf is actively developed and maintained. For a more complete introduction, see the Thymeleaf project home page.

### **SpringMVC：1.10.2. FreeMarker**

Apache FreeMarker is a template engine for generating any kind of text output from HTML to email and others. The Spring Framework has built-in integration for using Spring MVC with FreeMarker templates.

- 再看一下 SpringMVC 5.2.6.RELEASE 的文档，关于模板也没有提到支持或者推荐，而对于几种常见的例如 Thymeleaf、FreeMarker 等作出了一定的说明

所以，我个人觉得，SpringBoot 也只是提供了几种替代 JSP 的方案，并没有指定的推荐什么，当然了如果是我自己没注意到，大家也可以留言和我交流分享啊哈~

## **(4) 此部分小结**

说了一大堆，就一个结论，SpringBoot 中不推荐 JSP 是肯定的，但是也没有指定推荐什么引擎模板，大家可以根据需要自行选择（FreeMarker、Groovy、Thymeleaf、Mustache）

## **(二) JSP 真的有点麻烦**

如果，你真的想要在 SpringBoot 中使用 JSP，通过一些额外的配置，也是可以的，一起感受一下

## **(1) 导入依赖**

初始化的时候，我选了基本的 web 还涉及到热部署的 devtools ，简单体验一下只勾选 web 也成

spring-boot-starter-web，已经内置了，spring-boot-starter-tomcat，其下有tomcat-embed-core、tomcat-embed-el 等，前者包含servlet-api和内置 servlet 容器，后者为el表达式包，所以我们体验的时候，只需要引入 jstl 以及与编译jsp相关的 tomcat-embed-jasper 就可以了

```xml
 <dependencies>
     <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-web</artifactId>
     </dependency>
 
     <dependency>
         <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-devtools</artifactId>
         <scope>runtime</scope>
         <optional>true</optional>
     </dependency>
     <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-test</artifactId>
         <scope>test</scope>
         <exclusions>
             <exclusion>
                 <groupId>org.junit.vintage</groupId>
                 <artifactId>junit-vintage-engine</artifactId>
             </exclusion>
         </exclusions>
      </dependency>
 
     <!-- jsp begin-->
     <dependency>
         <groupId>javax.servlet</groupId>
         <artifactId>jstl</artifactId>
     </dependency>
 
     <dependency>
         <groupId>org.apache.tomcat.embed</groupId>
         <artifactId>tomcat-embed-jasper</artifactId>
         <scope>provided</scope>
     </dependency>
     <!-- jsp end-->
 </dependencies>
```

## **(2) 编写后台代码**

**实体类**

首先创建一个 User实体，简单写三个成员

```java
 public class User {
     private String username;
     private Integer age;
     private String address;
     ...... get set toString 方法
 }
```

**控制层**

这段代码够浅显了，返回这个 List 前台遍历就行了

```java
 @Controller
 @RequestMapping("/user")
 public class UserController {
 
     @RequestMapping("/list")
     public String list(Model model) {
         System.out.println("查询所有");
 
         User u1 = new User();
         u1.setUsername("Steven");
         u1.setAge(20);
         u1.setAddress("北京");
 
         User u2 = new User();
         u2.setUsername("Jack");
         u2.setAge(30);
         u2.setAddress("上海");
 
         List<User> users = new ArrayList<User>();
 
         users.add(u1);
         users.add(u2);
 
         model.addAttribute("users", users);
         return "list";
     }
 }
```

## **(3) 编写 JSP**

首先在 main 文件夹下，创建webapp文件夹，其下创建 WEB-INF 文件夹用来存放jsp文件（我在其下又多创建了一个pages文件夹，看个人习惯就可），而静态文件夹仍然放在resources下的静态资源文件夹例如 static public 等

位置：`src/main/webapp/WEB-INF/pages/list.jsp`

下面也就是一个遍历到表格的 Demo

```jsp
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <!DOCTYPE html>
 <html>
 <head>
     <meta charset="UTF-8">
     <title>list all</title>
 </head>
 <body>
     <table border="1">
         <c:forEach items="${users}" var="user">
             <tr>
                 <td>${user.username}</td>
                 <td>${user.age}</td>
                 <td>${user.address}</td>
             </tr>
         </c:forEach>
     </table>
 </body>
 </html>
```

## **(4) 修改配置**

上面做完了，如果不配置的话，这些页面根本是找不到的，所以需要配置 JSP 映射路径和后缀

```properties
 spring.mvc.view.prefix=/WEB-INF/pages/
 spring.mvc.view.suffix=.jsp
```

## **(5) 测试**

**直接运行启动类访问**

可以访问得到

![image-20200917234218811]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917234218811.png)

**打成 jar 运行访问**

Maven Projects --> Lifecycle --> clean --> package

![image-20200917234253602]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917234253602.png)

我们的 JSP 页面访问不到了

![image-20200917234323168]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917234323168.png)

## **(6) 多模块项目404问题**

主要的原因是无法找到正确的路径，所以需要将启动的Working directory设置为模块工作文件夹`$MODULE_WORKING_DIR$`，

设置方式：打开 Run/Dedug Configurations，然后修改

![image-20200917234418607]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917234418607.png)

关于 JSP 我自己也没怎么在 SpringBoot 中用过，也没细研究过，之前看到有一个篇文章有总结过关于 JSP 的坑，大家有兴趣或许可以了解一下

```
https://juejin.im/post/5ad21eb5f265da23945feb62
```

## **(三) 来试试 Thymeleaf**

## **(1) 简单评价**

### **A：优点**

首先，**配置很简单**，SpringBoot 对于 Thymeleaf 在内的几种模板引擎，都提供了**自动装配**的支持，所以简**单的引入依赖就可以快速使用**

其次，Thymeleaf "很优雅" ，因为绑定或者控制的时候，都是以**属性的方式**，所以 HTML 的原有结构，没有被破坏掉，这样，就有了一个其他模板引擎所没有的优点：**可以被浏览器正常渲染**，也就是说，直接就可以访问（例如 JSP 是没有办法脱离容器直接访问的）

### **B：缺点**

标签检查很严格，有时候会发疯....

表达式和标签种类繁多 {} 花括号前面不同的符号代表不同的意思，例如`${...}` 变量表达式 、 `*{...}` 选择表达式

如果习惯了 freemarker 这种类型的写法，写 Thymeleaf 会感觉很麻烦，因为两者的书写角度或者说思路是不同的

### **C：关于性能**

关于性能，在 3.x 后 Thymeleaf 已经有了很大的提升，但是我查过一下数据，貌似仍不一定有那么理想，不过就我个人而言，我一个后端狗写页面，一堆乱七八糟的 js、css 各种增大开销，Thymeleaf 带来的一些影响，貌似与我和没有很大关系（菜是原罪）



## **(2) 引入Thymeleaf**

用之前，肯定得引入相关的依赖对吧，可以去 Thymeleaf 官网，或者 Springboot 的官网，这里我更加推荐后者

**Thymeleaf 官网**：`https://www.thymeleaf.org`

**Springboot 官网**：

官网在 2.3.0 的版本中，你现在去看还是没有更出来Pom的，不过可以去看 2.2.7 的

**VersionNameDescriptionPom2.3.0**`spring-boot-starter-thymeleaf`Starter for building MVC web applications using Thymeleaf views暂未提供**2.2.7**`spring-boot-starter-thymeleaf`Starter for building MVC web applications using Thymeleaf viewsPom

**2.2.7 Pom**

```xml
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter</artifactId>
 </dependency>
 
 <dependency>
     <groupId>org.thymeleaf</groupId>
     <artifactId>thymeleaf-spring5</artifactId>
 </dependency>
 
 <dependency>
     <groupId>org.thymeleaf.extras</groupId>
     <artifactId>thymeleaf-extras-java8time</artifactId>
 </dependency>
```

这样引入是一种方式，但是这些相关的东西，Springboot 都已经帮我们打包好了，开箱即用

所以最终的引入方式就是：

### **A：Pom 增加依赖**

```xml
 <!--thymeleaf-->
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-thymeleaf</artifactId>
 </dependency>
```

### **B：初始化时勾选相关组件**

![image-20200917234502290]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917234502290.png)

## **(3) 模板页面存放位置**

引入了依赖之后，先确定一下页面给放哪里，前面演示的 JSP 好不折腾，又是创建 webapp WEB-INF ，又是配置，而 **Thymeleaf 等模板，则可以直接将页面放到自动生成的 templates 文件夹中就可以了**

具体路径为：`src -- main -- resources -- template`

如果想了解一下具体原因：可以去看一下 ThymeleafProperties 这个配置类，前后缀都已经指定好了

```java
 @ConfigurationProperties(prefix = "spring.thymeleaf")
 public class ThymeleafProperties {
 
    private static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;
 
    public static final String DEFAULT_PREFIX = "classpath:/templates/";
 
    public static final String DEFAULT_SUFFIX = ".html";
 
    // 是否在呈现模板之前检查模板是否存在
    private boolean checkTemplate = true;
     
    // 是否检查模板位置是否存在
    private boolean checkTemplateLocation = true;
 
    // 在构建URL时以查看名称作为前缀
    private String prefix = DEFAULT_PREFIX;
 
    // 在构建URL时附加到视图名称的后缀
    private String suffix = DEFAULT_SUFFIX;
 
    // 要应用于模板的模板模式
    private String mode = "HTML";
 
    // 模板文件编码
    private Charset encoding = DEFAULT_ENCODING;
```

## **(4) 入门程序体验**

**1、编写 Controller**

```java
@Controller
public class TestController {
    @RequestMapping("test")
    public String test(Model model){
        String hello = "Hello Thymeleaf";
        model.addAttribute("hello",hello);
        return "test.html";
    }
}
```

**2、编写页面**

页面其实可以看到就是一个普通的 html 页面，有几个不同的地方就是，上方引入了一个命名空间的约束，使用了 th:xxx 这样的语法 例如，th:text，然后 `${hello}` 引入了控制层带来的 hello

接受到的数据这一行上面的注释可以不加，不过 `${hello}` 应该会报红，不过也可以运行，具体原因看下面

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8">
  <title>Test</title>
</head>
<body>
  <h2>测试成功</h2>
  <!--/*@thymesVar id="hello" type="java.lang.String"*/-->
  <p th:text="'接收到的数据: ' + ${hello}"></p>
</body>
</html>
```

**3、测试结果**

![image-20200917234557711]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917234557711.png)

## **(5) 标签报红**

```html
<p th:text="'接收到的数据: ' + ${hello}"></p>
```

例如上面这么一行代码，如果不加任何处理，`${hello}` 必然是要报红的，其下会有一个红色波浪线

原因：后端 model 中虽然添加了数据，但是由于并没有运行程序，前端的文件也不知道，所以这个时候就会有红色波浪线存在了，其实正常运行是没有问题的，就是看起来很烦

解决方式有三种：

- 自动补全快捷键，自动写出上面的注释，然后自己写入类型

```html
<!--/*@thymesVar id="hello" type="java.lang.String"*/-->
```

- `<!DOCTYPE html>` 下加上 `<!--suppress ALL-->` 抑制所有警告，不过需要的每个 HTML 都得加
- 在 IDEA 中，进行一个全局的忽略

![image-20200917234623857]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917234623857.png)

## **(6) Thymeleaf 特点及说明**

经过了一个简单的例程，很显然，大家也有一定的感觉了，而在入门程序之前，我们已经说过一些其优缺点呀，但是说实话也没啥特殊的感觉，我们正好循着这个例子，给大家简单说一说其特点还有一些注意的点

1、首先，我们谈到了，Thymeleaf **"很优雅"** ，因为绑定或者控制的时候，都是以**属性的方式**，例如下面这段

```html
 <p th:text="'接收到的数据: ' + ${hello}"></p>
```

如果你直接在 HTML 中写 `${hello}` 那肯定是会出幺蛾子的，但是 Thymeleaf 这种写法，表达式都写在了自定义属性中，所以在静态环境下表达式的内容会被当做普通字符串，浏览器就不会报错

2、同时看到， HTML 的原有结构，没有被破坏掉，一眼望去还是 HTML 的那般模样

3、根据上面的特点，所以也就使得 Thymeleaf 在静态环境下也可以直接用浏览器运行，在静态环境下 th 指令内的内容不会被识别，但是也不会报错

## **(四) Thymeleaf 基本语法**

## **(1) 引入命名空间约束**

可以看到我们下面都是用 `th:*` 这种形式书写，如果想要正常使用，就必须在 `<head>` 前面引入约束

```html
<html xmlns:th="http://www.thymeleaf.org">
```

上面例程中已经用过了，下面开始正式的说一些常用的语法

## **(2) 变量语法及 th:text**

### **A：举例说明**

一个简单变量去看上面，的入门例程就行了，其实非常简单，下面我们通过对象中的变量取值来看一下

**1、编写实体**

首先创建两个实体，学生类和课程类，在学生类中，引用课程类

```java
public class Student {
    private String name;
    private Integer age;
    private Course course;
    ...... 省略 get set toString
}
public class Course {
    private String name;
    private String teacher;
    ...... 省略 get set toString
}
```

**2、编写 Conteoller**

```java
@Controller
public class TestController {
    @RequestMapping("testStudent")
    public String testStudent(Model model){
        // 两个对象 分别赋值
        Student stu = new Student();
        Course course = new Course();

        course.setName("JavaEE课程");
        course.setTeacher("杰克老师");

        stu.setName("张三");
        stu.setAge(25);
        stu.setCourse(course);

        model.addAttribute("student",stu);
        return "test.html";
    }
}
```

**3、编写页面**

- 我们存入的是一个学生的对象，名为 student 我们通过`${}` 的格式，来获取model中的变量，代码中所用 例如：`student.name` 就是ognl 表达式，可以去了解一下，形式就是 `xxx.属性名`
- 而表达式写在一个名为：`th:text`的标签属性中，叫做指令
- 一般总会出现 `th:xxx` 的形式，这些常见的指令，会在后面把常见的给出，现在用的这个 `th:text` 叫做文本替换，作用就是对表达式或变量求值，然后将结果显示在其被包含的 html 标签体内，替换掉原来的文本
- 所以可以在标签中，写上一些默认值，方便静态的时候对比效果，运行后，那些文本就被后台的数据替换掉了

```html
<p>学生姓名: <span th:text="${student.name}"></span> </p>
<p>学生年龄: <span th:text="${student.age}"></span> </p>
<p>课程名: <span th:text="${student.course.name}"></span> </p>
<p>授课老师: <span th:text="${student.course.teacher}"></span> </p>
```

**4、执行效果**



### **B：补充 th:utext**

还有一个 `th:utext` ，与上面用的 `th:text` 很相似区别就是：

- `th:text` 以纯文本显示且不解析内容里的HTML标签或元素
- `th:utext`则把整个内容当成是HTML来解析并展示，也就是说，例如取到的值为 `<h2>测试</h2>` 会按照二级标题来进行显示

### **C：减少变量书写次数方式**

当我们涉及到的数据过多的时候，我们每写一个就需要写一次 student，我们同样可以将其写成一个自定义的变量

- `th:object="${student}"`获取到值
- 引用时只需要通过 `*{属性名}` 的方式，来获取student中的这些属性

```html
<h5 th:object="${student}">
  <p>学生姓名: <span th:text="*{name}"></span> </p>
  <p>学生年龄: <span th:text="*{age}"></span> </p>
  <p>课程名: <span th:text="*{course.name}"></span> </p>
  <p>授课老师: <span th:text="*{course.teacher}"></span> </p>
</h5>
```



## **(3) 字符串拼接表达式**

上面说完了变量，但是我们还有很多时候，还有一些内容是不希望被当做变量解析的，也就是我们所说的字面值，常见的类型例如：字符串、或者数值等都是这样的，例如字符串 只需要在书写时加上单引号，就可以了，而数字不需要什么处理，直接就可以用，还可以进行算数运算

当然很多时候，我们取到的数据，要配合页面的一些字符串来进行显示，一种做法就是直接在外面写好内容例如

```html
<p>学生姓名: <span th:text="${student.name}"></span> </p>
```

还有一种常见的，就是字符串与表达式的拼接，先说一下普通的方式：

- ① 把上面的 p 标签中的内容移到 th:text 中来，用单引号引入即可

```html
<p> <span th:text=" '学生姓名: ' + ${student.name}"></span> </p>
```

- ② 如果涉及到大量的拼接，使用单引号拼接，书写以及可读上会很混乱，所以，可以进行简化:

```html
<p> <span th:text=" |姓名: ${student.name}|"></span> </p>
```

多提一句，这种方式，在静态环境运行的时候，是没有字符串显示的，只有用 SpringBoot 运行时才可以



## **(4) 运算符**

运算符这一块，我都是照着 Thymeleaf 官方文档 ，第4大节，**Standard Expression Syntax** 中写的，摘了一部分感觉还算常用的，不一定所有的例子我都给了测试，给了一些有代表性的

### **A：算数运算**

**1、支持的运算符**

- 二元操作：+, - , * , / , %
- 一元操作: - （负）

**2、测试代码**

注意：运算符最好放在外面，因为 运算符放在了 {} 内部, 表达式使用的是 ognl 引擎进行计算;，如果运算符放在外部, 那么 表达式使用的是 thymeleaf 引擎进行计算

```html
<p>学生年龄 = <span th:text="${student.age} "></span> </p>
<p>学生年龄 / 2 = <span th:text="${student.age} / 2 "></span> </p>
<p>学生年龄 % 2 = <span th:text="${student.age} % 2 "></span> </p>
```

**3、执行效果**

![image-20200917234703972]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917234703972.png)

### **B：布尔运算**

**1、支持的运算符**

- 一元运算符 : and, or
- 二元运算符 : !, not



### **C：比较运算**

**1、支持的运算符**

- 比较：> , < , >= , <= ( gt , lt , ge , le )
- 等于：== , != ( eq , ne )

**2、说明：**

- `> 和 <` 会被当做标签，所以不能直接使用，可以用括号内的别名代替使用
- `== 和 !=` 除比较数值外，还有类似 equals 的作用

**3、测试代码**

```html
<p>学生姓名: <span th:text="${student.name}"></span> </p>
<p>学生姓名为张三 = <span th:text="${student.name} == '张三'"></span> </p>

<p>学生年龄为 25 = <span th:text="${student.age} == 25 "></span> </p>
<p>学生年龄 % 2 为 0 = <span th:text="${student.age} % 2 == 0 "></span> </p>
```

**4、执行效果**

![image-20200917234728707]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917234728707.png)

### **D：条件运算**

- If-then: (if) ? (then)
- If-then-else: (if) ? (then) : (else)
- Default: (value) ?: (defaultvalue)

**2、测试代码**

```html
<p>学生姓名 = <span th:text="${student.name} == '张三' ? '是张三':'不是张三'"></span> </p>
```

**3、执行效果**

![image-20200917234854035]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917234854035.png)

## **(5) 条件运算再补充**

上面讲条件运算是放到了运算符中，演示了一下三元运算，因为逻辑判断是非常常用的，所以我们再补充一下

### **A：if**

没什么好说的，就是一个简单的判断

**1、测试代码**

```html
<p>学生是否成年: <span th:if="${student.age} >= 18 ">成年</span> </p>
```

只有当年龄 > 18 岁的时候，才会显示成年这两个字，比较简单，就不测试了

### **B：unless**

unless 与 if 是截然相反的两个概念，if 是满足条件则执行，而 unless 是不满足则执行

**1、测试代码**

```html
<p>学生是否成年: <span th:unless="${student.age} >= 18 ">成年</span> </p>
```

自己测试感受一下

### **C：switch**

一个分支语句的语法，也很好理解，注意：`th:case="*"`表示默认，放最后面就可以了

**1、测试代码**

```html
<div th:switch="${student.name}">
  <p th:case="张三">姓名: 张三</p>
  <p th:case="李四">姓名: 李四</p>
  <p th:case="王五">姓名: 王五</p>
  <p th:case="*">姓名: 未找到</p>
</div>
```

**2、执行效果**

改了下controller，把学生姓名赋值成了李四，看一下页面的显示

![image-20200917234933955]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917234933955.png)

## **(6) 循环语法**

循环也是非常常用的一种用法，通过 `th:each` 指令，来进行实现，我们下边循着一个小 Demo 学习一下

### **A：演示 Demo**

**1、创建用户类**

```java
public class User {
    private String nickname;
    private Integer age;
    ...... get set toString 方法
}
```

**2、controller 添加方法**

总之就是返回一个用户的 List 集合

```java
@RequestMapping("testUser")
public String testUser(Model model){
    User user1 = new User();
    user1.setNickname("飞翔的企鹅");
    user1.setAge(30);

    User user2 = new User();
    user2.setNickname("伤心小男孩");
    user2.setAge(25);

    List<User> list = new ArrayList<User>();
    list.add(user1);
    list.add(user2);

    model.addAttribute("userList",list);
    return "test.html";
}
```

**3、页面代码**

此处注意：`${userList}` 中的 userLisr 就是我们返回的那个集合，user 是我们遍历到的每一个用户，和 Java 里面的增强 for 感觉是相似的

```html
<table border="1">
  <tr>
    <th>Name</th>
    <th>Age</th>
  </tr>
  <tr th:each="user : ${userList}">
    <td th:text="${user.nickname}">NULL</td>
    <td th:text="${user.age}">0</td>
  </tr>
</table>
```

**4、执行效果**

![image-20200917235012331]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917235012331.png)

### **B：补充说明**

### **① 迭代类型**

关于要被遍历的值，也就例如我们上面的 `${userList}` 实际上有很多种可以接受的类型

- Enumeration，枚举
- Map 集合
- List、数组及其它一切符合数组结果的对象

上面 Demo 的演示，更像 Java 中的增强 for，增强 for 虽然遍历很方便，但是也有比之普通 for 的缺点，那就是没有了例如一些状态量，例如开始结束等等，有了一定的局限，所以 Thymeleaf 给我们提供了 **stat对象**，帮助我们弥补这一点

### **② stat对象的属性**

- index，当前迭代对象的index，从0开始的角标
- count，元素的个数，从1开始
- size，总元素个数
- current，当前遍历到的元素
- even/odd，布尔值，当前循环是否是偶数/奇数，boolean值
- first/last，返回是否为第一或最后，boolean值

**1、测试代码**

```html
<table border="1">
  <tr>
    <th>昵称</th>
    <th>年龄</th>
    <th>index</th>
    <th>count</th>
    <th>size</th>
    <th>current.nickname</th>
    <th>even</th>
    <th>odd</th>
    <th>first</th>
    <th>last</th>
  </tr>
  <tr th:each="user,userStat : ${userList}">
    <td th:text="${user.nickname}">nickname</td>
    <td th:text="${user.age}">age</td>
    <th th:text="${userStat.index}">index</th>
    <th th:text="${userStat.count}">count</th>
    <th th:text="${userStat.size}">size</th>
    <th th:text="${userStat.current.nickname}">current.nickname</th>
    <th th:text="${userStat.even}">even</th>
    <th th:text="${userStat.odd}">odd</th>
    <th th:text="${userStat.first}">first</th>
    <th th:text="${userStat.last}">last</th>
  </tr>
</table>
```

**2、执行效果**

![image-20200917235041600]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917235041600.png)

## **(五) 内置方法**

## **(1) 环境、上下文有关**

Thymeleaf 还提供了一些内置的方法，供我们调用，不过我也不推荐过多的使用下列方法，前端页面中，尽量还是减少逻辑，下面是从官方文档中截的一张图，我下面在表格中选了几个翻译了一下

![image-20200917235109009]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917235109009.png)

**对象作用**`#ctx`获取 Thymeleaf 自己的 Context对象`#requset`是web程序的情况下，用来获取HttpServletRequest对象`#response`是web程序的情况下，用来获取HttpServletReponse对象`#session`是web程序的情况下，用来获取HttpSession对象`#servletContext`是web程序的情况下，用来获取HttpServletContext对象

## **(2) 工具类方法**

还有一些，工具性质的内置对象，方便使用，还是先看下官方的截图，当然了我没截全所有的，有需要可以自己去看一下哈

![image-20200917235142722]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917235142722.png)

**对象作用**`#dates`用来处理时间（java.util.date）的对象`#calendars`处理日历中日期（java.util.calendar） 的对象`#numbers`格式化数字`#strings`处理字符串`#bools`判断布尔值`#arrays`处理数组`#lists`处理 List 集合`#sets`处理 set 集合`#maps`处理 map 集合

## **(3) 演示一下**

**1、编写 controller 方法**

```java
@RequestMapping("testDate")
public String testDate(Model model){
    model.addAttribute("currentTime",new Date());
    return "test.html";
}
```

```html
<p>现在时间: <span th:text="${#dates.format(currentTime,'yyyy-MM-dd hh:mm:ss')}">2020-05-19 00:00:00</span></p>
```

**3、执行效果**

![image-20200917235257291]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917235257291.png)

## **(六) 常用标签**

- 标签中只做一个类似提纲目录的用处，更详细的用法还需要进行查阅与实践
  补充：

- - `${...}` : 变量表达式
  - `*{...}` : 选择表达式
  - `#{...}` : 消息 (i18n) 表达式
  - `@{...}` : 链接 (URL) 表达式
  - `~{...}` : 片段表达式

## **(1) th:text**

文本替换：主要用于文本的显示

第一种：

```html
<p th:text="'接收到的数据: ' + ${hello}"></p>
```

第二种：

```html
<p>学生姓名: <span th:text="${student.name}"></span> </p>
```

## **(2) th:utext**

支持 HTML 的文本替换，可以用于富文本编辑器编辑后的内容显示到前端的页面上

> `th:utext`则把整个内容当成是HTML来解析并展示，也就是说，例如取到的值为 `<h2>测试</h2>` 会按照二级标题来进行显示

```html
<p th:utext="'接收到的含有HTML标签数据: ' + ${test}"></p>
```

## **(3) th:if / th:unless**

th:if 用于判断条件，满足则执行，而 th:unless 与前者正好相反

th:if

```html
<p>学生是否成年: <span th:if="${student.age} >= 18 ">成年</span> </p>
```

th:unless

```html
<p>学生是否成年: <span th:unless="${student.age} >= 18 ">成年</span> </p>
```

## **(4) th:switch / th:case**

用于多个同等级判断，即多选一

```html
<div th:switch="${student.name}">
  <p th:case="张三">姓名: 张三</p>
  <p th:case="李四">姓名: 李四</p>
  <p th:case="王五">姓名: 王五</p>
  <p th:case="*">姓名: 未找到</p>
</div>
```

注意：`th:case="*"`表示默认，放最后面就可以了

## **(5) th:each**

用于遍历集合中的对象，和 JSTL 中的 `<c:forEach>` 基本是一致的

除此之外，还能获取到一些状态量 stat ，请翻阅上面关于循环语法的讲解

```html
<table border="1">
  <tr>
    <th>Name</th>
    <th>Age</th>
  </tr>
  <tr th:each="user : ${userList}">
    <td th:text="${user.nickname}">NULL</td>
    <td th:text="${user.age}">0</td>
      
  </tr>
</table>
```

## **(6) th:action**

用于表单的提交时，相当于 `<form>` 标签的action属性。

```html
<form th:action="@{user/login}" method="post"></form>
```

## **(7) th:src**

用于引入例如图片或者 js 等外部资源

```html
<img th:src="@{../images/test.jpg}"/>
或者
<script th:src="@{../static/register.js}"></script>
```

## **(8) th:href**

用于定义超链接,相当于`<a></a>`标签的href属性

传统拼接传递

```html
<a th:href="/showStudent?id=123456&name=张三"></a>
```

带一个参数传递

```html
<a th:href="@{/student/details(studentId=${student.id})}" ></a>
```

## **(9) th:value**

用于属性赋值

```html
<input th:value = "${student.name}" />
```

## **(10) th:object / th:field**

th:object 和 th:field 常搭配之用，用来表单参数绑定，看一个例子就大概明白了

**1、编写实体**

```java
public class LoginUser {
    private String username;
    private String password;
    ......
}
```

**2、编写 controller 方法**

```java
@RequestMapping("/testLogin")
public String testLogin(@ModelAttribute(value = "loginUser")LoginUser loginUser, ModelMap modelMap){

    String username = loginUser.getUsername();
    String password = loginUser.getPassword();

    System.out.println(username);
    System.out.println(password);

    if ("admin".equals(username) && "admin888".equals(password)){
        modelMap.addAttribute("msg","登陆成功");
        return "test.html";
    }
    modelMap.addAttribute("msg","登陆失败");
    return "test.html";
}
```

**3、编写页面**

```html
<form id="login" th:action="@{/testLogin}" th:object="${loginUser}">
  <input type="text" value="" th:field="*{username}"></input>
  <input type="text" value="" th:field="*{password}"></input>
  <input type="submit" value="提交" />
  <span th:text="${msg}"></span>
</form>
```

**4、执行效果**

![image-20200917235426046]((五) SpringBoot起飞之路-Thymeleaf模板引擎整合及基本用法总结.assets/image-20200917235426046.png)