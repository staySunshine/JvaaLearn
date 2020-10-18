##  **Spring Boot 的配置文件 application.properties** 

### 位置问题

1. 当前项目根目录下的 config 目录下
2. 当前项目的根目录下
3. resources 目录下的 config 目录下
4. resources 目录下

![image-20201015205144461](Spring Boot Focus.assets/image-20201015205144461.png)

### 普通的属性注入

 Spring Boot 中，默认会自动加载 application.properties 文件 

 例如，现在定义一个 Book 类： 

```java
public class Book {
    private Long id;
    private String name;
    private String author;
    //省略 getter/setter
}
```

 然后，在 application.properties 文件中定义属性 

```properties
book.name=三国演义
book.author=罗贯中
book.id=1
```

 按照传统的方式（Spring中的方式），可以直接通过 @Value 注解将这些属性注入到 Book 对象中： 

```java
@Component
public class Book {
    @Value("${book.id}")
    private Long id;
    @Value("${book.name}")
    private String name;
    @Value("${book.author}")
    private String author;
    //省略getter/setter
}
```

一般来说，我们在 application.properties 文件中主要存放系统配置，这种自定义配置不建议放在该文件中，可以自定义 properties 文件来存在自定义配置。

例如在 resources 目录下，自定义 book.properties 文件，内容如下：

```properties
book.name=三国演义
book.author=罗贯中
book.id=1
```

 如果是在 Java 配置中，可以通过 @PropertySource 来引入配置： 

```java
@Component
@PropertySource("classpath:book.properties")
public class Book {
    @Value("${book.id}")
    private Long id;
    @Value("${book.name}")
    private String name;
    @Value("${book.author}")
    private String author;
    //getter/setter
}
```

### 类型安全的属性注入

```java
@Component
@PropertySource("classpath:book.properties")
@ConfigurationProperties(prefix = "book")
public class Book {
    private Long id;
    private String name;
    private String author;
    //省略getter/setter
}
```

 引入 @ConfigurationProperties(prefix = “book”) 注解，并且配置了属性的前缀，此时会自动将 Spring 容器中对应的数据注入到对象对应的属性中，就不用通过 @Value 注解挨个注入了 

##  **Spring Boot中的yaml配置** 

#### 狡兔三窟

application.yaml在Spring Boot中可以写在四个不同的位置，分别是如下位置：

1. 项目根目录下的config目录中
2. 项目根目录下
3. classpath下的config目录中
4. classpath目录下

### 数组注入

 yaml也支持数组注入，例如 

```yaml
my:
  servers:
	- dev.example.com
	- another.example.com
```

 这段数据可以绑定到一个带Bean的数组中： 

```java
@ConfigurationProperties(prefix="my")
@Component
public class Config {

	private List<String> servers = new ArrayList<String>();

	public List<String> getServers() {
		return this.servers;
	}
}
```

 在集合中存储对象,例如下面这种： 

```yaml
redis:
  redisConfigs:
    - host: 192.168.66.128
      port: 6379
    - host: 192.168.66.129
      port: 6380
```

 这个可以被注入到如下类中： 

```java
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisCluster {
    private List<SingleRedisConfig> redisConfigs;
	//省略getter/setter
}
```

### 优缺点

 不同于properties文件的无序，yaml配置是有序的，这一点在有些配置中是非常有用的，例如在Spring Cloud Zuul的配置中，当我们配置代理规则时，顺序就显得尤为重要了。当然yaml配置也不是万能的，例如，yaml配置目前不支持@PropertySource注解。 

##  **Spring Boot 中的静态资源** 

### 整体规划

在 Spring Boot 中，默认情况下，一共有5个位置可以放静态资源，五个路径分别是如下5个：

1. classpath:/META-INF/resources/
2. classpath:/resources/
3. classpath:/static/
4. classpath:/public/
5. /

### 自定义配置

#### application.properties

```
spring.resources.static-locations=classpath:/
spring.mvc.static-path-pattern=/**
```

 第一行配置表示定义资源位置，第二行配置表示定义请求 URL 规则。以上文的配置为例，如果我们这样定义了，表示可以将静态资源放在 resources目录下的任意地方，我们访问的时候当然也需要写完整的路径，例如在resources/static目录下有一张名为1.png 的图片，那么访问路径就是 `http://localhost:8080/static/1.png` ,注意此时的static不能省略。 

#### Java 代码定义

```java
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/aaa/");
    }
}
```

##  **SpringMVC 中 @ControllerAdvice 注解的三种使用场景** 

@ControllerAdvice ，这是一个增强的 Controller。使用这个 Controller ，可以实现三个方面的功能：

1. 全局异常处理
2. 全局数据绑定
3. 全局数据预处理

### 全局异常处理

 使用 @ControllerAdvice 实现全局异常处理，只需要定义类，添加该注解即可定义方式如下： 

```java
@ControllerAdvice
public class MyGlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ModelAndView customException(Exception e) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", e.getMessage());
        mv.setViewName("myerror");
        return mv;
    }
}
```

 @ExceptionHandler 注解用来指明异常的处理类型，即如果这里指定为 NullpointerException，则数组越界异常就不会进到这个方法中来。 

### 全局数据绑定

 全局数据绑定功能可以用来做一些初始化的数据操作，我们可以将一些公共的数据定义在添加了 @ControllerAdvice 注解的类中，这样，在每一个 Controller 的接口中，就都能够访问导致这些数据。 

 使用步骤，首先定义全局数据，如下： 

```java
@ControllerAdvice
public class MyGlobalExceptionHandler {
    @ModelAttribute(name = "md")
    public Map<String,Object> mydata() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("age", 99);
        map.put("gender", "男");
        return map;
    }
}
```

 使用 @ModelAttribute 注解标记该方法的返回数据是一个全局数据，默认情况下，这个全局数据的 key 就是返回的变量名，value 就是方法返回值，当然开发者可以通过 @ModelAttribute 注解的 name 属性去重新指定 key。 

 定义完成后，在任何一个Controller 的接口中，都可以获取到这里定义的数据： 

```java
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(Model model) {
        Map<String, Object> map = model.asMap();
        System.out.println(map);
        int i = 1 / 0;
        return "hello controller advice";
    }
}
```

 @ModelAttribute的各个属性值主要是用于其在接口参数上进行标注时使用的，如果是作为方法注解，其name或value属性则指定的是返回值的名称。如下是使用@ModelAttribute进行方法标注的一个例子： 

```java
@ControllerAdvice(basePackages = "mvc")
public class SpringControllerAdvice {
  @ModelAttribute(value = "message")
  public String globalModelAttribute() {
    System.out.println("global model attribute.");
    return "this is from model attribute";
  }
}
```

 这里需要注意的是，该方法提供了一个String类型的返回值，而@ModelAttribute中指定了该属性名称为message，这样在Controller层就可以接收该参数了，如下是Controller层的代码： 

```java
@Controller
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @RequestMapping(value = "/detail", method = RequestMethod.GET)
  public ModelAndView detail(@RequestParam("id") long id, 
       @ModelAttribute("message") String message) {
    System.out.println(message);
    ModelAndView view = new ModelAndView("user");
    User user = userService.detail(id);
    view.addObject("user", user);
    return view;
  }
}
```

### 全局数据预处理

 考虑我有两个实体类，Book 和 Author，分别定义如下： 

```java
public class Book {
    private String name;
    private Long price;
    //getter/setter
}
public class Author {
    private String name;
    private Integer age;
    //getter/setter
}
```

 此时，如果我定义一个数据添加接口，如下： 

```java
@PostMapping("/book")
public void addBook(Book book, Author author) {
    System.out.println(book);
    System.out.println(author);
}
```

这个时候，添加操作就会有问题，因为两个实体类都有一个 name 属性，从前端传递时 ，无法区分。此时，通过 @ControllerAdvice 的全局数据预处理可以解决这个问题

解决步骤如下:

 1.给接口中的变量取别名 

```java
@PostMapping("/book")
public void addBook(@ModelAttribute("b") Book book, @ModelAttribute("a") Author author) {
    System.out.println(book);
    System.out.println(author);
}
```

 2.进行请求数据预处理
在 @ControllerAdvice 标记的类中添加如下代码: 

```java
@InitBinder("b")
public void b(WebDataBinder binder) {
    binder.setFieldDefaultPrefix("b.");
}
@InitBinder("a")
public void a(WebDataBinder binder) {
    binder.setFieldDefaultPrefix("a.");
}
```

@InitBinder(“b”) 注解表示该方法用来处理和Book和相关的参数,在方法中,给参数添加一个 b 前缀,即请求参数要有b前缀.

3.发送请求

请求发送时,通过给不同对象的参数添加不同的前缀,可以实现参数的区分.

![1602745218905](Spring Boot Focus.assets/1602745218905.png)

##  **Spring Boot中通过CORS解决跨域问题** 

### 同源策略

 同源策略是由Netscape提出的一个著名的安全策略，它是浏览器最核心也最基本的安全功能，现在所有支持JavaScript的浏览器都会使用这个策略。所谓同源是指协议、域名以及端口要相同。 

### 实践

 使用CORS可以在前端代码不做任何修改的情况下，实现跨域，那么接下来看看在provider中如何配置。首先可以通过@CrossOrigin注解配置某一个方法接受某一个域的请求，如下： 

```java
@RestController
public class HelloController {
    @CrossOrigin(value = "http://localhost:8081")
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @CrossOrigin(value = "http://localhost:8081")
    @PostMapping("/hello")
    public String hello2() {
        return "post hello";
    }
}
```

 还可以通过全局配置一次性解决这个问题，全局配置只需要在配置类中重写addCorsMappings方法即可，如下： 

```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedOrigins("http://localhost:8081")
        .allowedMethods("*")
        .allowedHeaders("*");
    }
}
```

 `/**`表示本应用的所有方法都会去处理跨域请求，allowedMethods表示允许通过的请求数，allowedHeaders则表示允许的请求头。经过这样的配置之后，就不必在每个方法上单独配置跨域了。 

##  **Spring Boot 定义系统启动任务** 

###   基础 web 项目的解决方案 

在 Servlet/Jsp 项目中，如果涉及到系统任务，例如在项目启动阶段要做一些数据初始化操作，这些操作有一个共同的特点，只在项目启动时进行，以后都不再执行，这里，容易想到web基础中的三大组件（ Servlet、Filter、Listener ）之一 Listener ，这种情况下，一般定义一个 ServletContextListener，然后就可以监听到项目启动和销毁，进而做出相应的数据初始化和销毁操作，例如下面这样： 

```java
public class MyListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //在这里做数据初始化操作
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //在这里做数据备份操作
    }
}
```

### CommandLineRunner

 使用 CommandLineRunner 时，首先自定义 MyCommandLineRunner1 并且实现 CommandLineRunner 接口： 

```java
@Component
@Order(100)
public class MyCommandLineRunner1 implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
    }
}
```

关于这段代码，我做如下解释：

1. 首先通过 @Compoent 注解将 MyCommandLineRunner1 注册为Spring容器中的一个 Bean。
2. 添加 @Order注解，表示这个启动任务的执行优先级，因为在一个项目中，启动任务可能有多个，所以需要有一个排序。@Order 注解中，数字越小，优先级越大，默认情况下，优先级的值为 Integer.MAX_VALUE，表示优先级最低。
3. 在 run 方法中，写启动任务的核心逻辑，当项目启动时，run方法会被自动执行。
4. run 方法的参数，来自于项目的启动参数，即项目入口类中，main方法的参数会被传到这里。

### ApplicationRunner

ApplicationRunner 和 CommandLineRunner 功能一致，用法也基本一致，唯一的区别主要体现在对参数的处理上，ApplicationRunner 可以接收更多类型的参数（ApplicationRunner 除了可以接收 CommandLineRunner 的参数之外，还可以接收 key/value形式的参数）。

使用 ApplicationRunner ，自定义类实现 ApplicationRunner 接口即可，组件注册以及组件优先级的配置都和 CommandLineRunner 一致，如下：

```java
@Component
@Order(98)
public class MyApplicationRunner1 implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> nonOptionArgs = args.getNonOptionArgs();
        System.out.println("MyApplicationRunner1>>>"+nonOptionArgs);
        Set<String> optionNames = args.getOptionNames();
        for (String key : optionNames) {
            System.out.println("MyApplicationRunner1>>>"+key + ":" + args.getOptionValues(key));
        }
        String[] sourceArgs = args.getSourceArgs();
        System.out.println("MyApplicationRunner1>>>"+Arrays.toString(sourceArgs));
    }
}
```

当项目启动时，这里的 run 方法就会被自动执行，关于 run 方法的参数 ApplicationArguments ，我说如下几点：

1. args.getNonOptionArgs();可以用来获取命令行中的无key参数（和CommandLineRunner一样）。
2. args.getOptionNames();可以用来获取所有key/value形式的参数的key。
3. args.getOptionValues(key));可以根据key获取key/value 形式的参数的value。
4. args.getSourceArgs(); 则表示获取命令行中的所有参数。

##  **Spring Boot 中实现定时任务的两种方式** 

有两中方案，一种是使用 Spring 自带的定时任务处理器 @Scheduled 注解，另一种就是使用第三方框架 Quartz。

### @Scheduled

 直接创建一个 Spring Boot 项目，并且添加 web 依赖 `spring-boot-starter-web`，项目创建成功后，添加 `@EnableScheduling` 注解，开启定时任务： 

```java
@SpringBootApplication
@EnableScheduling
public class ScheduledApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduledApplication.class, args);
    }

}
```

 接下来配置定时任务： 

```java
  @Scheduled(fixedRate = 2000)
    public void fixedRate() {
        System.out.println("fixedRate>>>"+new Date());    
    }
    @Scheduled(fixedDelay = 2000)
    public void fixedDelay() {
        System.out.println("fixedDelay>>>"+new Date());
    }
    @Scheduled(initialDelay = 2000,fixedDelay = 2000)
    public void initialDelay() {
        System.out.println("initialDelay>>>"+new Date());
    }
```

1. 首先使用 @Scheduled 注解开启一个定时任务。
2. fixedRate 表示任务执行之间的时间间隔，具体是指两次任务的开始时间间隔，即第二次任务开始时，第一次任务可能还没结束。
3. fixedDelay 表示任务执行之间的时间间隔，具体是指本次任务结束到下次任务开始之间的时间间隔。
4. initialDelay 表示首次任务启动的延迟时间。
5. 所有时间的单位都是毫秒。

上面这是一个基本用法，除了这几个基本属性之外，@Scheduled 注解也支持 cron 表达式，使用 cron 表达式，可以非常丰富的描述定时任务的时间。cron 表达式格式如下：

> [秒] [分] [小时] [日] [月] [周] [年]

具体取值如下：

| 序号 | 说明 | 是否必填 | 允许填写的值    | 允许的通配符 |
| :--- | :--- | :------- | :-------------- | :----------- |
| 1    | 秒   | 是       | 0-59            | - * /        |
| 2    | 分   | 是       | 0-59            | - * /        |
| 3    | 时   | 是       | 0-23            | - * /        |
| 4    | 日   | 是       | 1-31            | - * ? / L W  |
| 5    | 月   | 是       | 1-12 or JAN-DEC | - * /        |
| 6    | 周   | 是       | 1-7 or SUN-SAT  | - * ? / L #  |
| 7    | 年   | 否       | 1970-2099       | - * /        |

**这一块需要大家注意的是，月份中的日期和星期可能会起冲突，因此在配置时这两个得有一个是 `?`**

**通配符含义：**

- `?` 表示不指定值，即不关心某个字段的取值时使用。需要注意的是，月份中的日期和星期可能会起冲突，因此在配置时这两个得有一个是 `?`
- `*` 表示所有值，例如:在秒的字段上设置 `*`,表示每一秒都会触发
- `,` 用来分开多个值，例如在周字段上设置 “MON,WED,FRI” 表示周一，周三和周五触发
- `-` 表示区间，例如在秒上设置 “10-12”,表示 10,11,12秒都会触发
- `/` 用于递增触发，如在秒上面设置”5/15” 表示从5秒开始，每增15秒触发(5,20,35,50)
- `#` 序号(表示每月的第几个周几)，例如在周字段上设置”6#3”表示在每月的第三个周六，(用 在母亲节和父亲节再合适不过了)
- 周字段的设置，若使用英文字母是不区分大小写的 ，即 MON 与mon相同
- `L` 表示最后的意思。在日字段设置上，表示当月的最后一天(依据当前月份，如果是二月还会自动判断是否是润年), 在周字段上表示星期六，相当于”7”或”SAT”（注意周日算是第一天）。如果在”L”前加上数字，则表示该数据的最后一个。例如在周字段上设置”6L”这样的格式,则表示”本月最后一个星期五”
- `W` 表示离指定日期的最近工作日(周一至周五)，例如在日字段上设置”15W”，表示离每月15号最近的那个工作日触发。如果15号正好是周六，则找最近的周五(14号)触发, 如果15号是周未，则找最近的下周一(16号)触发，如果15号正好在工作日(周一至周五)，则就在该天触发。如果指定格式为 “1W”,它则表示每月1号往后最近的工作日触发。如果1号正是周六，则将在3号下周一触发。(注，”W”前只能设置具体的数字,不允许区间”-“)
- `L` 和 `W` 可以一组合使用。如果在日字段上设置”LW”,则表示在本月的最后一个工作日触发(一般指发工资 )

 例如，在 @Scheduled 注解中来一个简单的 cron 表达式，每隔5秒触发一次，如下： 

```java
@Scheduled(cron = "0/5 * * * * *")
public void cron() {
    System.out.println(new Date());
}
```

### Quartz

一般在项目中，除非定时任务涉及到的业务实在是太简单，使用 @Scheduled 注解来解决定时任务，否则大部分情况可能都是使用 Quartz 来做定时任务。在 Spring Boot 中使用 Quartz ，只需要在创建项目时，添加 Quartz 依赖即可：

![1602751327648](Spring Boot Focus.assets/1602751327648.png)

 项目创建完成后，也需要添加开启定时任务的注解： 

```java
@SpringBootApplication
@EnableScheduling
public class QuartzApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuartzApplication.class, args);
    }
}
```

Quartz 在使用过程中，有两个关键概念，一个是JobDetail（要做的事情），另一个是触发器（什么时候做），要定义 JobDetail，需要先定义 Job，Job 的定义有两种方式：

第一种方式，直接定义一个Bean：

```java
@Component
public class MyJob1 {
    public void sayHello() {
        System.out.println("MyJob1>>>"+new Date());
    }
}
```

关于这种定义方式说两点：

1. 首先将这个 Job 注册到 Spring 容器中。
2. 这种定义方式有一个缺陷，就是无法传参。

第二种定义方式，则是继承 QuartzJobBean 并实现默认的方法：

```java
public class MyJob2 extends QuartzJobBean {
    HelloService helloService;
    public HelloService getHelloService() {
        return helloService;
    }
    public void setHelloService(HelloService helloService) {
        this.helloService = helloService;
    }
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        helloService.sayHello();
    }
}
public class HelloService {
    public void sayHello() {
        System.out.println("hello service >>>"+new Date());
    }
}
```

和第1种方式相比，这种方式支持传参，任务启动时，executeInternal 方法将会被执行。

Job 有了之后，接下来创建类，配置 JobDetail 和 Trigger 触发器，如下：

```java
@Configuration
public class QuartzConfig {
    @Bean
    MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean() {
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();
        bean.setTargetBeanName("myJob1");
        bean.setTargetMethod("sayHello");
        return bean;
    }
    @Bean
    JobDetailFactoryBean jobDetailFactoryBean() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setJobClass(MyJob2.class);
        JobDataMap map = new JobDataMap();
        map.put("helloService", helloService());
        bean.setJobDataMap(map);
        return bean;
    }
    @Bean
    SimpleTriggerFactoryBean simpleTriggerFactoryBean() {
        SimpleTriggerFactoryBean bean = new SimpleTriggerFactoryBean();
        bean.setStartTime(new Date());
        bean.setRepeatCount(5);
        bean.setJobDetail(methodInvokingJobDetailFactoryBean().getObject());
        bean.setRepeatInterval(3000);
        return bean;
    }
    @Bean
    CronTriggerFactoryBean cronTrigger() {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setCronExpression("0/10 * * * * ?");
        bean.setJobDetail(jobDetailFactoryBean().getObject());
        return bean;
    }
    @Bean
    SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setTriggers(cronTrigger().getObject(), simpleTriggerFactoryBean().getObject());
        return bean;
    }
    @Bean
    HelloService helloService() {
        return new HelloService();
    }
}
```

关于这个配置说如下几点：

1. JobDetail 的配置有两种方式：MethodInvokingJobDetailFactoryBean 和 JobDetailFactoryBean 。
2. 使用 MethodInvokingJobDetailFactoryBean 可以配置目标 Bean 的名字和目标方法的名字，这种方式不支持传参。
3. 使用 JobDetailFactoryBean 可以配置 JobDetail ，任务类继承自 QuartzJobBean ，这种方式支持传参，将参数封装在 JobDataMap 中进行传递。
4. Trigger 是指触发器，Quartz 中定义了多个触发器，这里向大家展示其中两种的用法，SimpleTrigger 和 CronTrigger 。
5. SimpleTrigger 有点类似于前面说的 @Scheduled 的基本用法。
6. CronTrigger 则有点类似于 @Scheduled 中 cron 表达式的用法。

![1602755322427](Spring Boot Focus.assets/1602755322427.png)

 全部定义完成后，启动 Spring Boot 项目就可以看到定时任务的执行了。 

## Spring Boot 中自定义 SpringMVC 配置

### 概览

首先我们需要明确，跟自定义 SpringMVC 相关的类和注解主要有如下四个：

- WebMvcConfigurerAdapter
- WebMvcConfigurer
- WebMvcConfigurationSupport
- @EnableWebMvc

### WebMvcConfigurerAdapter

我们先来看 WebMvcConfigurerAdapter，这个是在 Spring Boot 1.x 中我们自定义 SpringMVC 时继承的一个抽象类，这个抽象类本身是实现了 WebMvcConfigurer 接口，然后抽象类里边都是空方法，我们来看一下这个类的声明：

```java
public abstract class WebMvcConfigurerAdapter implements WebMvcConfigurer {
    //各种 SpringMVC 配置的方法
}
```

再来看看这个类的注释：

```
/**
 * An implementation of {@link WebMvcConfigurer} with empty methods allowing
 * subclasses to override only the methods they're interested in.
 * @deprecated as of 5.0 {@link WebMvcConfigurer} has default methods (made
 * possible by a Java 8 baseline) and can be implemented directly without the
 * need for this adapter
 */
```

 这段注释关于这个类说的很明白了。同时我们也看到，从 Spring5 开始，由于我们要使用 Java8，而 Java8 中的接口允许存在 default 方法，因此官方建议我们直接实现 WebMvcConfigurer 接口，而不是继承 WebMvcConfigurerAdapter 。

**也就是说，在 Spring Boot 1.x 的时代，如果我们需要自定义 SpringMVC 配置，直接继承 WebMvcConfigurerAdapter 类即可。**

### WebMvcConfigurer

根据上一小节的解释，小伙伴们已经明白了，WebMvcConfigurer 是我们在 Spring Boot 2.x 中实现自定义配置的方案。

WebMvcConfigurer 是一个接口，接口中的方法和 WebMvcConfigurerAdapter 中定义的空方法其实一样，所以用法上来说，基本上没有差别，从 Spring Boot 1.x 切换到 Spring Boot 2.x ，只需要把继承类改成实现接口即可。

### WebMvcConfigurationSupport

WebMvcConfigurationSupport 类本身是没有问题的，我们自定义 SpringMVC 的配置是可以通过继承 WebMvcConfigurationSupport 来实现的。但是继承 WebMvcConfigurationSupport 这种操作我们一般只在 Java 配置的 SSM 项目中使用，Spring Boot 中基本上不会这么写，为什么呢？

Spring Boot 中，SpringMVC 相关的自动化配置是在 WebMvcAutoConfiguration 配置类中实现的，那么我们来看看这个配置类的生效条件：

```java
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@AutoConfigureAfter({ DispatcherServletAutoConfiguration.class, TaskExecutionAutoConfiguration.class,
		ValidationAutoConfiguration.class })
public class WebMvcAutoConfiguration {
}
```

我们从这个类的注解中可以看到，它的生效条件有一条，就是当不存在 WebMvcConfigurationSupport 的实例时，这个自动化配置才会生生效。因此，如果我们在 Spring Boot 中自定义 SpringMVC 配置时选择了继承 WebMvcConfigurationSupport，就会导致 Spring Boot 中 SpringMVC 的自动化配置失效。

**Spring Boot 给我们提供了很多自动化配置，很多时候当我们修改这些配置的时候，并不是要全盘否定 Spring Boot 提供的自动化配置，我们可能只是针对某一个配置做出修改，其他的配置还是按照 Spring Boot 默认的自动化配置来，而继承 WebMvcConfigurationSupport 来实现对 SpringMVC 的配置会导致所有的 SpringMVC 自动化配置失效，因此，一般情况下我们不选择这种方案。**

在 Java 搭建的 SSM 项目中，因为本身就没什么自动化配置，所以我们使用了继承 WebMvcConfigurationSupport。

## @EnableWebMvc

最后还有一个 @EnableWebMvc 注解，这个注解很好理解，它的作用就是启用 WebMvcConfigurationSupport。

加了这个注解，就会自动导入 WebMvcConfigurationSupport，所以在 Spring Boot 中，我们也不建议使用 @EnableWebMvc 注解，因为它一样会导致 Spring Boot 中的 SpringMVC 自动化配置失效。

### 总结

不知道上面的解释小伙伴有没有看懂？我再简单总结一下：

1. Spring Boot 1.x 中，自定义 SpringMVC 配置可以通过继承 WebMvcConfigurerAdapter 来实现。
2. Spring Boot 2.x 中，自定义 SpringMVC 配置可以通过实现 WebMvcConfigurer 接口来完成。
3. 如果在 Spring Boot 中使用继承 WebMvcConfigurationSupport 来实现自定义 SpringMVC 配置，或者在 Spring Boot 中使用了 @EnableWebMvc 注解，都会导致 Spring Boot 中默认的 SpringMVC 自动化配置失效。
4. 在纯 Java 配置的 SSM 环境中，如果我们要自定义 SpringMVC 配置，有两种办法，第一种就是直接继承自 WebMvcConfigurationSupport 来完成 SpringMVC 配置，还有一种方案就是实现 WebMvcConfigurer 接口来完成自定义 SpringMVC 配置，如果使用第二种方式，则需要给 SpringMVC 的配置类上额外添加 @EnableWebMvc 注解，表示启用 WebMvcConfigurationSupport，这样配置才会生效。换句话说，在纯 Java 配置的 SSM 中，如果你需要自定义 SpringMVC 配置，你离不开 WebMvcConfigurationSupport ，所以在这种情况下建议通过继承 WebMvcConfigurationSupport 来实现自动化配置。

## 整合 MyBatis

### 工程创建

首先创建一个基本的 Spring Boot 工程，添加 Web 依赖，MyBatis 依赖以及 MySQL 驱动依赖。

添加Druid依赖，并且锁定MySQL驱动版本：

```XML
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.1.10</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.28</version>
    <scope>runtime</scope>
</dependency>
```

### 基本用法

首先是在 application.properties 中配置数据库的基本信息：

```properties
spring.datasource.url=jdbc:mysql:///test01?useUnicode=true&characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
```

```java
public interface UserMapper2 {
    @Select("select * from user")
    List<User> getAllUsers();

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "u"),
            @Result(property = "address", column = "a")
    })
    @Select("select username as u,address as a,id as id from user where id=#{id}")
    User getUserById(Long id);

    @Select("select * from user where username like concat('%',#{name},'%')")
    List<User> getUsersByName(String name);

    @Insert({"insert into user(username,address) values(#{username},#{address})"})
    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Integer.class)
    Integer addUser(User user);

    @Update("update user set username=#{username},address=#{address} where id=#{id}")
    Integer updateUserById(User user);

    @Delete("delete from user where id=#{id}")
    Integer deleteUserById(Integer id);
}
```

这里是通过全注解的方式来写 SQL，不写 XML 文件。

@Select、@Insert、@Update 以及 @Delete 四个注解分别对应 XML 中的 select、insert、update 以及 delete 标签，@Results 注解类似于 XML 中的 ResultMap 映射文件（getUserById 方法给查询结果的字段取别名主要是向小伙伴们演示下 `@Results` 注解的用法）。

另外使用 @SelectKey 注解可以实现主键回填的功能，即当数据插入成功后，插入成功的数据 id 会赋值到 user 对象的id 属性上。

UserMapper2 创建好之后，还要配置 mapper 扫描，有两种方式，一种是直接在 UserMapper2 上面添加 `@Mapper` 注解，这种方式有一个弊端就是所有的 Mapper 都要手动添加，要是落下一个就会报错，还有一个一劳永逸的办法就是直接在启动类上添加 Mapper 扫描，如下：

```java
@SpringBootApplication
@MapperScan(basePackages = "org.javaboy.mybatis.mapper")
public class MybatisApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisApplication.class, args);
    }
}
```

### mapper 映射

在 XML 中写 SQL，例如创建一个 UserMapper，如下：

```java
public interface UserMapper {
    List<User> getAllUser();

    Integer addUser(User user);

    Integer updateUserById(User user);

    Integer deleteUserById(Integer id);
}
```

然后创建 UserMapper.xml 文件，如下：

```java
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-21-mapper.dtd">
<mapper namespace="org.javaboy.mybatis.mapper.UserMapper">
    <select id="getAllUser" resultType="org.javaboy.mybatis.model.User">
        select * from t_user;
    </select>
    <insert id="addUser" parameterType="org.javaboy.mybatis.model.User">
        insert into user (username,address) values (#{username},#{address});
    </insert>
    <update id="updateUserById" parameterType="org.javaboy.mybatis.model.User">
        update user set username=#{username},address=#{address} where id=#{id}
    </update>
    <delete id="deleteUserById">
        delete from user where id=#{id}
    </delete>
</mapper>
```

将接口中方法对应的 SQL 直接写在 XML 文件中。

那么这个 UserMapper.xml 到底放在哪里呢？有两个位置可以放，第一个是直接放在 UserMapper 所在的包下面：

![image-20201018225718878](Spring Boot Focus.assets/image-20201018225718878.png)

放在这里的 UserMapper.xml 会被自动扫描到，但是有另外一个 Maven 带来的问题，就是 java 目录下的 xml 资源在项目打包时会被忽略掉，所以，如果 UserMapper.xml 放在包下，需要在 pom.xml 文件中再添加如下配置，避免打包时 java 目录下的 XML 文件被自动忽略掉：

```xml
<build>
    <resources>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.xml</include>
            </includes>
        </resource>
        <resource>
            <directory>src/main/resources</directory>
        </resource>
    </resources>
</build>
```

当然，UserMapper.xml 也可以直接放在 resources 目录下，这样就不用担心打包时被忽略了，但是放在 resources 目录下，必须创建和 Mapper 接口包目录相同的目录层级，这样才能确保打包后 XML 和 Mapper 接口又处于在一起，否则 XML 文件将不能被自动扫描，这个时候就需要添加额外配置。例如我在 resources 目录下创建 mapper 目录用来放 mapper 文件，如下：

![image-20201018225846473](Spring Boot Focus.assets/image-20201018225846473.png)

此时在 application.properties 中告诉 mybatis 去哪里扫描 mapper：

```xml
mybatis.mapper-locations=classpath:mapper/*.xml
```

如此配置之后，mapper 就可以正常使用了。**注意这种方式不需要在 pom.xml 文件中配置文件过滤。**

### 原理分析

在 SSM 整合中，开发者需要自己提供两个 Bean，一个SqlSessionFactoryBean ，还有一个是 MapperScannerConfigurer，在 Spring Boot 中，这两个东西虽然不用开发者自己提供了，但是并不意味着这两个 Bean 不需要了，在 `org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration` 类中，我们可以看到 Spring Boot 提供了这两个 Bean，部分源码如下：

```java
@org.springframework.context.annotation.Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MybatisAutoConfiguration implements InitializingBean {

  @Bean
  @ConditionalOnMissingBean
  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
    factory.setDataSource(dataSource);
    return factory.getObject();
  }
  @Bean
  @ConditionalOnMissingBean
  public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    ExecutorType executorType = this.properties.getExecutorType();
    if (executorType != null) {
      return new SqlSessionTemplate(sqlSessionFactory, executorType);
    } else {
      return new SqlSessionTemplate(sqlSessionFactory);
    }
  }
  @org.springframework.context.annotation.Configuration
  @Import({ AutoConfiguredMapperScannerRegistrar.class })
  @ConditionalOnMissingBean(MapperFactoryBean.class)
  public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
      logger.debug("No {} found.", MapperFactoryBean.class.getName());
    }
  }
}
```

从类上的注解可以看出，当当前类路径下存在 SqlSessionFactory、 SqlSessionFactoryBean 以及 DataSource 时，这里的配置才会生效，SqlSessionFactory 和 SqlTemplate 都被提供了。

## 整合 MyBatis 多数据源

### 多数据源配置

首先在 application.properties 中配置数据库基本信息，然后提供两个 DataSource 即可，这里我再把代码贴出来，里边的道理条条框框的，大伙可以参考前面的文章，这里不再赘述。

application.properties 中的配置：

```pro
spring.datasource.one.url=jdbc:mysql:///test01?useUnicode=true&characterEncoding=utf-8
spring.datasource.one.username=root
spring.datasource.one.password=root
spring.datasource.one.type=com.alibaba.druid.pool.DruidDataSource

spring.datasource.two.url=jdbc:mysql:///test02?useUnicode=true&characterEncoding=utf-8
spring.datasource.two.username=root
spring.datasource.two.password=root
spring.datasource.two.type=com.alibaba.druid.pool.DruidDataSource
```

然后再提供两个 DataSource，如下：

```java
@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.one")
    DataSource dsOne() {
        return DruidDataSourceBuilder.create().build();
    }
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.two")
    DataSource dsTwo() {
        return DruidDataSourceBuilder.create().build();
    }
}
```

## MyBatis 配置

接下来则是 MyBatis 的配置，不同于 JdbcTemplate，MyBatis 的配置要稍微麻烦一些，因为要提供两个 Bean，因此这里两个数据源我将在两个类中分开来配置，首先来看第一个数据源的配置：

```java
@Configuration
@MapperScan(basePackages = "org.javaboy.mybatis.mapper1",sqlSessionFactoryRef = "sqlSessionFactory1",sqlSessionTemplateRef = "sqlSessionTemplate1")
public class MyBatisConfigOne {
    @Resource(name = "dsOne")
    DataSource dsOne;

    @Bean
    SqlSessionFactory sqlSessionFactory1() {
        SqlSessionFactory sessionFactory = null;
        try {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dsOne);
            sessionFactory = bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionFactory;
    }
    @Bean
    SqlSessionTemplate sqlSessionTemplate1() {
        return new SqlSessionTemplate(sqlSessionFactory1());
    }
}
```

创建 MyBatisConfigOne 类，首先指明该类是一个配置类，配置类中要扫描的包是 org.javaboy.mybatis.mapper1 ，即该包下的 Mapper 接口将操作 dsOne 中的数据，对应的 SqlSessionFactory 和 SqlSessionTemplate 分别是 sqlSessionFactory1 和 sqlSessionTemplate1，在 MyBatisConfigOne 内部，分别提供 SqlSessionFactory 和 SqlSessionTemplate 即可， SqlSessionFactory 根据 dsOne 创建，然后再根据创建好的SqlSessionFactory 创建一个 SqlSessionTemplate。

这里配置完成后，依据这个配置，再来配置第二个数据源即可：

```java
@Configuration
@MapperScan(basePackages = "org.javaboy.mybatis.mapper2",sqlSessionFactoryRef = "sqlSessionFactory2",sqlSessionTemplateRef = "sqlSessionTemplate2")
public class MyBatisConfigTwo {
    @Resource(name = "dsTwo")
    DataSource dsTwo;

    @Bean
    SqlSessionFactory sqlSessionFactory2() {
        SqlSessionFactory sessionFactory = null;
        try {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dsTwo);
            sessionFactory = bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionFactory;
    }
    @Bean
    SqlSessionTemplate sqlSessionTemplate2() {
        return new SqlSessionTemplate(sqlSessionFactory2());
    }
}
```

这样 MyBatis 多数据源基本上就配置好了，接下来只需要在 org.javaboy.mybatis.mapper1 和 org.javaboy.mybatis.mapper2 包中提供不同的 Mapper，Service 中注入不同的 Mapper 就可以操作不同的数据源。

### mapper 创建

org.javaboy.mybatis.mapper1 中的 mapper：

```java
public interface UserMapperOne {
    List<User> getAllUser();
}
```

对应的 XML 文件：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.javaboy.mybatis.mapper1.UserMapperOne">
    <select id="getAllUser" resultType="org.javaboy.mybatis.model.User">
        select * from t_user;
    </select>
</mapper>
```

org.javaboy.mybatis.mapper2 中的 mapper：

```java
public interface UserMapper {
    List<User> getAllUser();
}
```

对应的 XML 文件：

```java
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.javaboy.mybatis.mapper2.UserMapper">
    <select id="getAllUser" resultType="org.javaboy.mybatis.model.User">
        select * from t_user;
    </select>
</mapper>
```

接下来，在 Service 中注入两个不同的 Mapper，不同的 Mapper 将操作不同的数据源。

## Spring Boot 整合 Redis

### 方案一：Spring Data Redis

#### 创建工程

#### 赖：

![image-20201018233301120](Spring Boot Focus.assets/image-20201018233301120.png)

创建成功后，还需要手动引入 commos-pool2 的依赖，因此最终完整的 pom.xml 依赖如下：

```java
<dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-pool2</artifactId>
    </dependency>
```

#### 配置 Redis 信息

接下来配置 Redis 的信息，信息包含两方面，一方面是 Redis 的基本信息，另一方面则是连接池信息:

```pro
spring.redis.database=0
spring.redis.password=123
spring.redis.port=6379
spring.redis.host=192.168.66.128
spring.redis.lettuce.pool.min-idle=5
spring.redis.lettuce.pool.max-idle=10
spring.redis.lettuce.pool.max-active=8
spring.redis.lettuce.pool.max-wait=1ms
spring.redis.lettuce.shutdown-timeout=100ms
```

### 自动配置

当开发者在项目中引入了 Spring Data Redis ，并且配置了 Redis 的基本信息，此时，自动化配置就会生效。

我们从 Spring Boot 中 Redis 的自动化配置类中就可以看出端倪：

```java
@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@Import({ LettuceConnectionConfiguration.class, JedisConnectionConfiguration.class })
public class RedisAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(
                    RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
            RedisTemplate<Object, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(redisConnectionFactory);
            return template;
    }
    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate stringRedisTemplate(
                    RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
            StringRedisTemplate template = new StringRedisTemplate();
            template.setConnectionFactory(redisConnectionFactory);
            return template;
    }
}
```

这个自动化配置类很好理解：

1. 首先标记这个是一个配置类，同时该配置在 RedisOperations 存在的情况下才会生效(即项目中引入了 Spring Data Redis)
2. 然后导入在 application.properties 中配置的属性
3. 然后再导入连接池信息（如果存在的话）
4. 最后，提供了两个 Bean ，RedisTemplate 和 StringRedisTemplate ，其中 StringRedisTemplate 是 RedisTemplate 的子类，两个的方法基本一致，不同之处主要体现在操作的数据类型不同，RedisTemplate 中的两个泛型都是 Object ，意味者存储的 key 和 value 都可以是一个对象，而 StringRedisTemplate 的 两个泛型都是 String ，意味者 StringRedisTemplate 的 key 和 value 都只能是字符串。如果开发者没有提供相关的 Bean ，这两个配置就会生效，否则不会生效。

### 使用

接下来，可以直接在 Service 中注入 StringRedisTemplate 或者 RedisTemplate 来使用：

```java
@Service
public class HelloService {
    @Autowired
    RedisTemplate redisTemplate;
    public void hello() {
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set("k1", "v1");
        Object k1 = ops.get("k1");
        System.out.println(k1);
    }
}
```

Redis 中的数据操作，大体上来说，可以分为两种：

1. 针对 key 的操作，相关的方法就在 RedisTemplate 中
2. 针对具体数据类型的操作，相关的方法需要首先获取对应的数据类型，获取相应数据类型的操作方法是 opsForXXX

调用该方法就可以将数据存储到 Redis 中去了，如下：

![image-20201018233941008](Spring Boot Focus.assets/image-20201018233941008.png)

k1 前面的字符是由于使用了 RedisTemplate 导致的，RedisTemplate 对 key 进行序列化之后的结果。

RedisTemplate 中，key 默认的序列化方案是 JdkSerializationRedisSerializer 。

而在 StringRedisTemplate 中，key 默认的序列化方案是 StringRedisSerializer ，因此，如果使用 StringRedisTemplate ，默认情况下 key 前面不会有前缀。

不过开发者也可以自行修改 RedisTemplate 中的序列化方案，如下:

```java
@Service
public class HelloService {
    @Autowired
    RedisTemplate redisTemplate;
    public void hello() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set("k1", "v1");
        Object k1 = ops.get("k1");
        System.out.println(k1);
    }
}
```

当然也可以直接使用 StringRedisTemplate：

```java
@Service
public class HelloService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    public void hello2() {
        ValueOperations ops = stringRedisTemplate.opsForValue();
        ops.set("k2", "v2");
        Object k1 = ops.get("k2");
        System.out.println(k1);
    }
}
```

另外需要注意 ，Spring Boot 的自动化配置，只能配置单机的 Redis ，如果是 Redis 集群，则所有的东西都需要自己手动配置。

### 方案二：Spring Cache

### 方案三：回归原始时代