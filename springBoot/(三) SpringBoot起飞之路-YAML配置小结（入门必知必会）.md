# (三) SpringBoot起飞之路-YAML配置小结（入门必知必会）

## **(一) SpringBoot配置文件类型**

在前一篇的入门原理讲解中，我们简单有提到过关于自动配置的一些说明，我们演示的时候，仅仅在 src\main\resources 下的 application.properties 中写入 `server.port=9090` 就可以非常便捷的修改端口号

也就是说，**SpringBoot是基于约定的**，很多值都是默认存在的，如果想要进行一定的修改，我们就需要使用 application.properties或者application.yml（application.yaml）进行配置

properties，我们相对还是熟悉的，在过去 Spring 中的开发我们还算是经常用

这一篇主要讲解 yaml 这一类型

## **(二) yaml 简介**

维基百科贴的简介

> YAML是"YAML Ain't a Markup Language"（YAML不是一种标记语言）的递归缩写。在开发的这种语言时，YAML 的意思其实是："Yet Another Markup Language"（仍是一种标记语言），但为了强调这种语言**以数据做为中心**，而**不是以标记语言为重点**，而用反向缩略语重命名。

维基百科贴的功能介绍

> YAM的语法和其他高级语言类似，并且可以简单表达清单、散列表，标量等数据形态,它使用空白符号缩进和大量依赖外观的特色，特别适合用来表达或编辑数据结构、各种配置文件、倾印调试内容、文件大。尽管它比较适合用来表达层次结构式的数据结构，不过也有精致的语法可以表示关系性的数据其让人最容易上手的特色是巧妙避开各种封闭符号，如：引号、各种括号等，这些符号在嵌套结构时会变得复杂而难以辨认

我们可以先看一个例子：

- XML 配置

```xml
 <server>
     <port>9090<port>
 </server>
```

- Properties 配置

```properties
server.port=9090
```

- Yaml 配置

```yaml
server：
   prot: 9090
```

## **(三) 基础语法**

## **(1) 语法要求**

- value 与冒号之间的空格不能省略，例如 key: value ，':' 后面可是有空格的
- 缩进表示层级关系，左对齐的一列数据为同一层级
- 缩进不能用 Tab，只能用空空格
- 语法中大小写是敏感的

## **(2) 配置普通数据**

普通数据在这里值得是：**数字，布尔值，字符串** 等

语法：

```yaml
 key: value
```

示例：

```yaml
 name: ideal
```

- 普通数据的值直接写在冒号（加空格）后面，通常字符串也不需要加单双引号
- “ ” 双引号会将转义字符，例如 “\n” 当做换行符返回
- ' ' 单引号不会解析转义，'\n' 会被当作字符串返回



## **(3) 配置对象数据、Map数据**

语法：

- 基本写法

```yaml
 key: 
   key1: value1
   key2: value2
```

- 行内写法

```yaml
 key: {key1: value1,key2: value2}
```

示例：

```yaml
 user:
   name: Steven
   age:20
   address: beijing
user: {name:Steven,age: 20,address: beijing}  
```

说明：key1 前面空格个数是没有限定的，虽然默认是两格，但是只要是相同缩进，就代表是同一级别，用默认即可，仅此说明

## **(4) 配置数组、List、Set 数据**

这里用 `-` 代表一个元素，注意 `-` 和 value1 等之间存在一个空格

语法：

- 基本写法

```yaml
key: 
  - value1
  - value2
```

- 行内写法

```yaml
key: [value1,value2]
```

示例：

```yaml
province:
  - guangdong
  - hebei
  - sichuan
province: [guangdong,hebei,sichuan]
```

**补充：当集合中元素为对象的时候，示例如下**

```yaml
user:
  - name: zhangsan
    age: 20
    address: beijing
  - name: lisi
    age: 28
    address: shanghai
  - name: wangwu
    age: 26
    address: shenzhen
```

## **(四) 配置文件与配置类的属性映射**

## **(1) @Value映射**

在Spring中，我们已经介绍过 @Value 注解，我们可以通过 @Value 注解将配置文件中的值映射到一个Spring管理的Bean的字段上

先创建一个简单的 User 类用来演示，就这么三个成员

```java
@Component
public class User {
    private String name;
    private Integer age;
    private String address;
    ......补充 get、toString方法（set可以不写）
}
```

测试都是统一的

```java
@Controller
public class UserController {
    @Autowired
    User user;

    @RequestMapping("/testUser")
    @ResponseBody
    public String testUser(){
       return user.toString();
    }
}
```

现在我们分三种演示一下

### **A：直接赋值**

```java
@Value("Tom")
private String name;

@Value("20")
private Integer age;

@Value("北京")
private String address;
```

### **B：Properties**

application.properties

```properties
user1.name=Jack
user1.age=30
user1.address=北京
```

User

```java
@Value("${user1.name}")
private String name;

@Value("${user1.age}")
private Integer age;

@Value("${user1.address}")
private String address;
```

### **C：Yaml**

application.yml

```yaml
user1:
  name: Steven
  age: 20
  address: 北京
```

User

```java
@Value("${user1.name}")
private String name;

@Value("${user1.age}")
private Integer age;

@Value("${user1.address}")
private String address;
```

看一下 yaml 的测试结果

![image-20200917213816804]((三) SpringBoot起飞之路-YAML配置小结（入门必知必会）.assets/image-20200917213816804.png)

### **D：说明**

### **① 配置优先级问题**

properties、yaml、yml，这三种配置文件，如果同时都配置的话，其实三个文件中的配置信息都会生效，但是存在加载优先级问题，后加载的会覆盖先加载的文件，所当三个文件中有配置信息冲突时,虽然加载顺序是yml>yaml>properties，但你看到的实际上是最后出现的，所以优先级是:properties>yaml>yml，

### **② user 命名问题**

上面 `@Value("${user1.name}")` 我全用了 user1，是因为如果 Windows 下 user.name，会获取到当前 Windows 用户的用户名

### **③ application**

记得要在 application.properties 或者 application.yaml 或者 application.yml 中写，因为默认是去找这几个配置文件的，如果配置文件命名不对的话，是找不到的，当然后面会有办法解决

### **④ Properties 乱码问题**

如果以前没有设置的话，中文肯定是会乱码的，下面补充一下设置的方法

将下面的勾选上，然后设置 Properties 编码为 UTF-8

![image-20200917213851038]((三) SpringBoot起飞之路-YAML配置小结（入门必知必会）.assets/image-20200917213851038.png)

## **(2) @ConfigurationProperties映射**

看下的实体类中，使用了这个注解 @ConfigurationProperties(prefix="配置文件中的key的前缀")，它可以将配置文件中的配置自动与实体进行映射

注意：可能会提示一些错误，一般是没有影响的，解决方案放在文章下面，如果无影响，先做就可以了

### **A：配置实体类**

@Value 如果成员比较多的也挺麻烦的，介绍一种更加方便的，这种以后会常用到，所以重写一个复杂一点的实体，演示的全面一些

注意：要使用 @ConfigurationProperties 来实现，就要给其成员添加 set 方法，而前面使用 @Value注解 则不需要也可以

```java
@Component
@ConfigurationProperties(prefix = "student")
public class Student {

    private String name;
    private Integer age;
    private Date birthday;
    private Boolean graduate;
    private Map<String, Integer> score;
    private List<String> subject;
    private User u;
	......补充 get set toString 方法
}
```

Student 类中引用的 User 类

```java
@Component
public class User {
    private String name;
    private Integer age;
    private String address;
    ......补充 get set toString方法
}
```

### **B：yaml 配置**

```yaml
student:
  name: Steven
  age: 20
  birthday: 2020/01/01
  graduate: false
  score: {math: 90, english: 88}
  subject:
    - 高数
    - 大英
    - 大物
  u:
    name: Jack
    age: 30
    address: 北京
```

## **(五) 问题解决方案**

**1、Spring Boot Configuration Annotation Processor not found in classpath错误**

IDEA 提示，springboot配置注解处理器没有找到，也就会给我们地址，让我们去看文档，但是有的文档不一定打得开，大家可以自己去官网找一找

先来看一下官网的说明

> Generating Your Own Metadata by Using the Annotation Processor
> You can easily generate your own configuration metadata file from items annotated with `@ConfigurationProperties` by using the `spring-boot-configuration-processor` jar. The jar includes a Java annotation processor which is invoked as your project is compiled. To use the processor, include a dependency on `spring-boot-configuration-processor`.
> With Maven the dependency should be declared as optional, as shown in the following example:

所以添加注解就可以了

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

**2、Re-run Spring Boot Configuration Annotation Processor to update generated metadata**

一般加好依赖，就会弹出这个吗，这个问题可以忽略，不影响代码执行，应该只是一个提醒，提醒你进行必要的重启或者重新编译，我也没找到完美处理这个问题的方法，实在不顺眼也可以关闭 IDEA 的提示：

- Setting 下搜索 Spring，找到 SpringBoot 取消勾选 **show notification panel**

## **(六) 疑惑占楼**

对于，spring-boot-configuration-processor，网络上有的说法认为是用来引入@PropertySource的， 也有一种说法说是用来使 @ConfigurationProperties 生效的

```java
@Component
@PropertySource(value = "classpath:user.properties")
@ConfigurationProperties(prefix = "usertest")
public class User {
    private String name;
    private Integer age;
    private String address;
    ......补充 get set toString方法
}
```

不过测试一下，不引入 spring-boot-configuration-processor 也能使用这两个注解，一直也没怎么去仔细考虑，都忽略了，如果了解的朋友，可以留言交流一下，以后有了合适的解释我重新更新一下，在此占楼