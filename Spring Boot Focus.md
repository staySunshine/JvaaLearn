##  **Spring Boot 的配置文件 application.properties** 

### 位置问题

1. 当前项目根目录下的 config 目录下
2. 当前项目的根目录下
3. resources 目录下的 config 目录下
4. resources 目录下

![1602732769793](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1602732769793.png)

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