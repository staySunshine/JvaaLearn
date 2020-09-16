# (二 )SpringBoot起飞之路-入门原理分析

## **(一) 起步依赖原理分析**

## **(1) spring-boot-starter-parent**

在前一篇的入门程序编写时，我们自己通过 Maven 创建一个 SpringBoot 的项目的时候，我们需要在前面使用 `<parent></parent>` 标签对引入一个 spring-boot-starter-parent

这就是 SpringBoot 的父级依赖，它就是一个特殊的 starter ，可以用来提供我们所需 Maven 的一些依赖，同时管理项目的资源过滤

简单看一下，首先按下 ctrl 键点击 spring-boot-starter-parent，

```xml
 <parent>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-parent</artifactId>
     <version>2.2.7.RELEASE</version>
     <relativePath/> <!-- lookup parent from repository -->
 </parent>
```

进去之后，就是一些资源引入的内容，还有一些插件，接着又看到了一个父级标签，我们继续点进去看一看

```xml
 <parent>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-dependencies</artifactId>
   <version>2.2.7.RELEASE</version>
   <relativePath>../../spring-boot-dependencies</relativePath>
 </parent>
```

到此为止，就看不到别的父级标签了，来观察一下这个文件，纵观一下，这里就是管理 SpringBoot 应用里面所有依赖版本的地方

- `properties` 标签中配置了一些版本信息
- `dependencys` 标签内就是各种依赖
- `plugins` 标签内就是一些涉及的插件

从前在 Spring 中的开发，我们总会考虑不同依赖版本之间的兼容冲突问题，而在这里，SpringBoot 就会帮我们根据我们选定的 SpringBoot 版本，选择出最适合的依赖们，这一点就舒服了~

## **(2) spring-boot-starter-web**

接着我们再次回到 pom.xml 中，还有一个与我们过去开发不同的东西，那就是 spring-boot-starter-web，在前面第一篇的文章中初始化组件时，我们就选择了 Spring Web 组件，自然这个依赖就是用来实现 Web 功能的

进入 spring-boot-starter-web

```xml
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
 </dependency>
```

下面节选了一部分 xml 配置（依赖是全的），看到这里，也就能解释为什么 spring-boot-starter-web 这个依赖可以实现 Web 的开发，本质上就是对支持 Web 开发的 spring-webmvc、spring-webmvc等坐标进行了 “整合” 也就是将依赖传递了

```xml
 <?xml version="1.0" encoding="UTF-8"?>
 <project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
   <modelVersion>4.0.0</modelVersion>
   <parent>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starters</artifactId>
     <version>2.2.7.RELEASE</version>
   </parent>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
   <version>2.2.7.RELEASE</version>
   <name>Spring Boot Web Starter</name>
     ......
 <dependencies>
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter</artifactId>
       <version>2.2.7.RELEASE</version>
       <scope>compile</scope>
     </dependency>
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-json</artifactId>
       <version>2.2.7.RELEASE</version>
       <scope>compile</scope>
     </dependency>
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-tomcat</artifactId>
       <version>2.2.7.RELEASE</version>
       <scope>compile</scope>
     </dependency>
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-validation</artifactId>
       <version>2.2.7.RELEASE</version>
       <scope>compile</scope>
       <exclusions>
         <exclusion>
           <artifactId>tomcat-embed-el</artifactId>
           <groupId>org.apache.tomcat.embed</groupId>
         </exclusion>
       </exclusions>
     </dependency>
     <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-web</artifactId>
       <version>5.2.6.RELEASE</version>
       <scope>compile</scope>
     </dependency>
     <dependency>
       <groupId>org.springframework</groupId>
       <artifactId>spring-webmvc</artifactId>
       <version>5.2.6.RELEASE</version>
       <scope>compile</scope>
     </dependency>
   </dependencies>
 </project>
```

## **(3) 这就是启动器**

我们从一个入门程序讨论到了 spring-boot-starter-web，**在 SpringBoot 中所有的功能场景都被抽取出来**，定义成了很多很多**启动器**，通过这些启动器，开发人员就能按照自己的需求，**选择不同的启动器进行配**合，极为**快速的就能搭建起一个想要的项目运行框架环境**，启动器命名规则如下：

- **springboot-boot-starter-xxx**

在举几个例子，体验一下，以后的文章，在用的时候，会给出具体的说明

spring-boot-starter-test：支持常规的测试依赖，包括JUnit、Hamcrest、Mockito以及spring-test模块

spring-boot-starter-web：支持全栈式 Web 开发，包括 Tomcat 和 spring-webmvc

spring-boot-starter-redis：支持 Redis 键值存储数据库

## **(二) SpringBoot 启动类分析**

除了依赖，还有一个与我们从前 Spring 的开发不一样的点就是，SpringBoot 多了一个启动类，在入门程序的编写的时候，我只提到，这个启动类是必须存在的，看似简单的一个类，实则也很玄妙，下面我们来一起分析一下

```java
 package cn.ideal;
 
 import org.springframework.boot.SpringApplication;
 import org.springframework.boot.autoconfigure.SpringBootApplication;
 
 @SpringBootApplication
 public class SpringbootApplication {
     public static void main(String[] args) {
         SpringApplication.run(Springboot02QuickstartApplication.class, args);
     }    
 }
```

## **(1) @SpringbootApplication 注解**

首先最显眼的也就是在类上的 @SpringbootApplication 注解了，我们 ctrl 点击进入

```java
 @Target(ElementType.TYPE)
 @Retention(RetentionPolicy.RUNTIME)
 @Documented
 @Inherited
 @SpringBootConfiguration
 @EnableAutoConfiguration
 @ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
       @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
 public @interface SpringBootApplication {
     ......
 }
```

从上面的代码可以看到，这里又加注着很多注解，但是其中比较核心的有三个：**@SpringBootConfiguration、@EnableAutoConfiguration、@ComponentScan**

为什么这么说呢，大家可以把这三个注解加到我们之前的启动类上，然后执行一下，简单的看呢，其实也是可以达到之前的效果的，下面，我们就主要讲解一下这三个注解

```java
 package cn.ideal;
 
 import org.springframework.boot.SpringApplication;
 import org.springframework.boot.SpringBootConfiguration;
 import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
 import org.springframework.context.annotation.ComponentScan;
 
 @SpringBootConfiguration
 @EnableAutoConfiguration
 @ComponentScan
 public class SpringbootApplication {
     public static void main(String[] args) {
         SpringApplication.run(Springboot02QuickstartApplication.class, args);
     }
 }
```

### **A：@SpringBootConfiguration**

点击进去，我们可以看到 @SpringBootConfiguration 注解本质上，还是用了 Spring 的 @Configuration 注解

```java
@Configuration
public @interface SpringBootConfiguration {...}
```

在点 @Configuration 进去还可以看到下述内容

```java
@Component
public @interface Configuration {...}
```

这两个组件大家应该还是很熟悉的

- **@Configuration**：指定当前类是 spring 的一个配置类，相当于 XML中的 applicationContext.xml 文件
- **@Component** ：说明这个类也是交给Spring管理，也能看出启动类本身也是Spring中的一个组件而已，用来启动应用

### **B：@ComponentScan**

@ComponentScan的功能其实是自动扫描并加载符合条件的组件（比如 @Component 和 @Controller 等）或者bean定义，并把这些bean定义加载到IoC容器中，在 XML 中相当于：

```xml
<!--开启扫描-->
<context:component-scan base-package="cn.ideal"></context:component-scan>
```

我们可以通过 basePackages 属性来指定 @ComponentScan 自动扫描的范围，且和这个注解中value属性的作用是一致

如果不指定，**则默认Spring框架实现会从声明 @ComponentScan 所在类的 package 进行扫描**，由于默认是不指定的，所以这也是我们把启动类放在与 controller service 等包同级的位置下

我们还可以看到指定了两个 Filter ，简单提一下他们两个

```java
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
      @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
```

- TypeExcludeFilter, 方便用户实现自定义的Filter，用于Spring Test中
- AutoConfigurationExcludeFilter, 忽略@ Configuration和 EnableAutoConfiguration 这样就可以使得，SpringBoot不会错误扫描到自动装配类

### **C：@EnableAutoConfiguration**

这个注解是极其重要的，顾名思义可以看到，它的格式为 @EnableXxxxxx，还有一些类似的注解 @EnableMBeanExport、@EnableScheduling 等等，他们也是相同的开头，拿一个举例

- @EnableScheduling：通过 @Import 将Spring 调度框架相关的bean定义都加载到 IOC容器

@EnableAutoConfiguration 也是这样的，他借助 @Import ，**就可以把所有符合自动配置要求的 bean 定义加载到 IOC 容器**

所以：这个注解的作用就是自动配置，代替了我们很多的配置操作，在 @SpringBootApplication 上加这个注解，也就代表 SpringBoot 自动配置的功能开启了

我们点进去具体看一下

首先同样可以看到很多注解，我们聚焦到这两个

```
@AutoConfigurationPackage` `@Import(AutoConfigurationImportSelector.class)
```

### **① @AutoConfigurationPackage**

我们可以点进去 @AutoConfigurationPackag 这个注解看一下，其下还有一个注解很重要 **@Import(AutoConfigurationPackages.Registrar.class)**

- @Import 就是用来给容器中导入某个组件类
- @Import(AutoConfigurationPackages.Registrar.class) 就是将将Registrar这个组件类导入到 Spring 容器中

进而看一下 Registrar ，其中有一个 registerBeanDefinitions 方法就是导入组件类的实现，作用就是将注解标注的元信息传进去，然后得到对应的包名

```java
@Override
public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
   register(registry, new PackageImport(metadata).getPackageName());
}
```

总结一下：AutoConfigurationPackage注解的作用是将 **添加该注解的类所在的package** 作为 **自动配置package** 进行管理

**根据我们启动类的所在包位置可得，使用 @AutoConfigurationPackage 注解可以将主程序类所在包及所有子包下的组件到扫描到 Spring 容器中**

### **② @Import(AutoConfigurationImportSelector.class)**

回到 EnableAutoConfiguration 下，除了 @AutoConfigurationPackage，最重要的来了，@Import(AutoConfigurationImportSelector.class)，@Import 就不说了，那么 AutoConfigurationImportSelector 究竟做了什么呢？我们进入 AutoConfigurationImportSelector

由于 AutoConfigurationImportSelector 字面意思叫做 自动配置导入选择器，所我们来找一下关于获得配置的相关代码

首先在 getAutoConfigurationEntry 方法中看到这么一行代码：

```java
// 获取全部的配置
List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
```

这些配置是通过 getCandidateConfigurations 获取到的，所我们继续跳转过去

找到 getCandidateConfigurations ，这个方法，其中又调用了 SpringFactoriesLoader 类的静态方法 loadFactoryNames（注意引入的第一个参数）

```java
// 获得候选的配置
protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
   List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),
         getBeanClassLoader());
   Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you "
         + "are using a custom packaging, make sure that file is correct.");
   return configurations;
}

// 在上面被当做参数调用了
// 返回的就是最开始启动自动导入配置文件的注解类；EnableAutoConfiguration
protected Class<?> getSpringFactoriesLoaderFactoryClass() {
	return EnableAutoConfiguration.class;
}
```

继续深入这个方法，我节选了一部分重要的，关键部分我都加了注释

```java
public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";

public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {
   // 前面传入的参数为EnableAutoConfiguration.class
   // 所以factoryClassName为org.springframework.boot.autoconfigure.EnableAutoConfiguration
   String factoryTypeName = factoryType.getName();
   // 把 factoryClassName传入，返回的是所有spring.factories文件中key为org.springframework.boot.autoconfigure.EnableAutoConfiguration的类路径
   return loadSpringFactories(classLoader).getOrDefault(factoryTypeName, Collections.emptyList());
}

private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
   MultiValueMap<String, String> result = cache.get(classLoader);
   if (result != null) {
      return result;
   }

   try {
      // 找到所有的 "META-INF/spring.factories" 定义在上方
      Enumeration<URL> urls = (classLoader != null ?
            classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
            ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
      result = new LinkedMultiValueMap<>();
      while (urls.hasMoreElements()) {
         URL url = urls.nextElement();
         UrlResource resource = new UrlResource(url);
          // 读取文件内容，properties类似于HashMap，包含了属性的key和value
         Properties properties = PropertiesLoaderUtils.loadProperties(resource);
         for (Map.Entry<?, ?> entry : properties.entrySet()) {
            // 属性文件中用','分割多个value
            String factoryTypeName = ((String) entry.getKey()).trim();
            for (String factoryImplementationName : StringUtils.commaDelimitedListToStringArray((String) entry.getValue())) {
               result.add(factoryTypeName, factoryImplementationName.trim());
            }
         }
      }
      cache.put(classLoader, result);
      return result;
   }
   catch (IOException ex) {
      throw new IllegalArgumentException("Unable to load factories from location [" +
            FACTORIES_RESOURCE_LOCATION + "]", ex);
   }
}
```

到这里了，再怎么找也该知道去找 spring.factories 了，那么这个 **META-INF/spring.factories**文件从哪里找起呢？一般都是默认当前文件下的，所以顺着上面的代码分析找到了源头 package org.springframework.boot.autoconfigure 下

![image-20200916214010316]((二 )SpringBoot起飞之路-入门原理分析.assets/image-20200916214010316.png)

点进去一看，好多以 AutoConfiguration 结尾的配置内容，这就是自动配置的根本，挑选几个简单列出来

```text
......
org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration,\
......
```

随便找几个点进去看一下，可以看到他们每个都是JavaConfig配置类，同时注入了一些Bean，所以可以看出这个 spring.factories 文件就是我们配置的根源内容了

@EnableAutoConfiguration 注解总结：

在 启动类中的 @SpringbootApplication 注解 下引入 @EnableAutoConfiguration 注解，它提供了一种配置查找的功能支持，也就是说，@EnableAutoConfiguration的完整类名org.springframework.boot.autoconfigure.EnableAutoConfiguration 作为查找的Key，获取对应的一组@Configuration类



这些配置类具体是如何做的我们这里不深究，但是要补充一个点，我们首先进入下面这个 Configuration

```text
org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration,\
```

@Configuration(proxyBeanMethods = false) 自然代表这是一个配置类，这也与我们前面的分析一致

要说的是这个注解：@EnableConfigurationProperties(ServerProperties.class)

看名字来说，它是和 Properties 配置有关的，继续点入 ServerProperties

```java
@ConfigurationProperties(prefix = "server", ignoreUnknownFields = true)
public class ServerProperties {

	/**
	 * Server HTTP port.
	 */
	private Integer port;

	/**
	 * Network address to which the server should bind.
	 */
	private InetAddress address
	
	......
}
```

又看到这样一条注解：@ConfigurationProperties(prefix = "server", ignoreUnknownFields = true)

可以看到，这里指定了一个前缀 server，下面还定义了很多东西，例如这里定义了端口还有地址等等

那么，为什么要说这个呢？有什么用呢？

这就与我准备下一篇写的文章有关了，也就是配置相关的，我们可以在这里先体验一下

在项目 resources 的 application.properties 下，我们可以添加这样一条配置：

```properties
server.port=9090
```

![image-20200916214157195]((二 )SpringBoot起飞之路-入门原理分析.assets/image-20200916214157195.png)

就这样一行配置，就把端口号修改好了，是不是很神奇

这就使我们刚才想说的，实际上这里配置的 server.port 其中 server 就是那个 @ConfigurationProperties(prefix = "server", ignoreUnknownFields = true) 配置的前缀，port 就是下面定义的端口，**也就是SpringBoot会将配置文件中以server开始的属性映射到该类的字段中**