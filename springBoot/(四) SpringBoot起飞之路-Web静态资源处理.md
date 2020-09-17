# (四) SpringBoot起飞之路-Web静态资源处理

## **(一) 静态资源处理方式**

前面的演示，我们只涉及到了直接返回一些数据，例如字符串等等，但是如果想要真正的去做一个完整的 Web 项目，没有页面以及诸多静态资源（CSS、JS等）怎么能行，按照以往 Spring 的开发来说，我们的 main 下会有一个 webapp文件夹，但是我们现在创建的 SpringBoot 项目却不然，这是因为 SpringBoot 对于静态资源的放置，有自己的一套规定，下面来看一下吧

## **(1) 第一种映射规则**

### **A：规则分析**

首先来看一下 SpringMVC 关于 web 的配置，`ctrl + n` 查找一下 WebMvcAutoConfiguration 这个配置类，找到 addResourceHandlers，看到一项与资源配置有关，**addResourceHandlers**，的简单看一下源码，其实就大致能明白，访此 `/webjars/**` 路径下的内容，就会去 `/META-INF/resources/webjars/` 下去找，

**addResourceHandlers**

```java
 Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
 CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
 if (!registry.hasMappingForPattern("/webjars/**")) {
     
     customizeResourceHandlerRegistration(registry.addResourceHandler("/webjars/**")
         .addResourceLocations("classpath:/META-INF/resources/webjars/")                     .setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
 
 }
```

这是什么意思呢？首先来看一下 webjars 的概念

### **B：Webjars**

以前项目中，如果需要一些静态资源，我们会直接引入文件到项目中，但是 Webjars 是通过 jar 包方式引入静态资源的，来看一下：

去访问一下官网：`https://www.webjars.org`

官网的说明已经告诉我们，WebJars 就是帮助我们把一些 web 库例如 jQuery & Bootstrap 等打包到 jar 中，我们通过依赖就可以快速使用

> WebJars are client-side web libraries (e.g. jQuery & Bootstrap) packaged into JAR (Java Archive) files. Explicitly and easily manage the client-side dependencies in JVM-based web applications Use JVM-based build tools (e.g. Maven, Gradle, sbt, ...) to download your client-side dependencies

下面我们以 jquery 为例使用一下，（为了演示，随便选个版本就行了）：选择 Mavan 的依赖

```xml
 <dependency>
     <groupId>org.webjars</groupId>
     <artifactId>jquery</artifactId>
     <version>3.5.1</version>
 </dependency>
```

引入进 pom 后，我们回顾一下刚才看得源码

访此 `/webjars/**` 路径下的内容，就会去 `/META-INF/resources/webjars/` 下去找

我们找到这个引入的 jquery 依赖，可以看到我们从 Webjars 网站引入的内容，都是符合 Springboot 默认格式要求的，所以下面直接访问一下看一看

![image-20200917223131810]((四) SpringBoot起飞之路-Web静态资源处理.assets/image-20200917223131810.png)

访问的格式就是：`http://localhost:8080/webjars/**` 引入它会自动去 `/META-INF/resources/webjars/` 下面找，所以直接从 jquery这个文件夹开始写就可以了

所以访问路径为：`http://localhost:8080/webjars/jquery/3.5.1/jquery.js`

![image-20200917223200273]((四) SpringBoot起飞之路-Web静态资源处理.assets/image-20200917223200273.png)

## **(2) 第二种映射规则**

### **A：规则分析**

这些第三方的 web 库的问题给出了一种方式，但是说了半天，还没有说自己的页面怎么弄，如果想要使用自己的静态资源又该怎么办呢？

继续看 **addResourceHandlers** 的源码：

```java
 //这里是第一种
 Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
 CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
 if (!registry.hasMappingForPattern("/webjars/**")) {
    customizeResourceHandlerRegistration(registry.addResourceHandler("/webjars/**")
          .addResourceLocations("classpath:/META-INF/resources/webjars/")
          .setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
 }
 
 //这里是第二种
 String staticPathPattern = this.mvcProperties.getStaticPathPattern();
 if (!registry.hasMappingForPattern(staticPathPattern)) {
     
   customizeResourceHandlerRegistration(registry.addResourceHandler(staticPathPattern)       .addResourceLocations(getResourceLocations(this.resourceProperties
      .getStaticLocations()))                                                              .setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
     
 }
```

刚才我们分析完了上面的第一种，接着下面以几乎相同的方式，又定义了一种映射的规则，第一种的方式为：访此 `/webjars/**` 路径下的内容，就会去 `/META-INF/resources/webjars/` 下去找

对照相同位置，我们去看第二种，也就是：访问 `staticPathPattern` 此路径下的内容，就会去 `getResourceLocations(this.resourceProperties .getStaticLocations()` 下去找

我们顺着线索，继续追过去

```java
String staticPathPattern = this.mvcProperties.getStaticPathPattern();
```

最终找到了这么一条，我们得到了 `/**` 这个路径

```java
 private String staticPathPattern = "/**";
```

我们跳转 `this.resourceProperties.getStaticLocations()`

得到这样一个定义

```text
private String[] staticLocations = CLASSPATH_RESOURCE_LOCATIONS;
```

继续跳转，在ResourceProperties 中又找到了这样一个定义

```java
private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { 
     "classpath:/META-INF/resources/",
     "classpath:/resources/",
     "classpath:/static/",
     "classpath:/public/" 
 };
```

到现在我们对于第二种方式的映射规则其实就清楚了

访问 `/**` 此路径下的内容，就会去 `/METAINF/resources/`，`/resources/`，`/static/`，`/public/` 四者中去找

### **B：classpath 概念补充：**

- src 路径下的文件编译后会被放到 WEB-INF/classes 路径下，所以默认classpath 就是指这里
- 用maven构建一个项目的时候，resources 目录就是默认的 classpath

### **C：测试**

下面测试一下，我们分别将自己定义的一个 js 文件放置于resources文件夹下的 resources、static、public 文件夹下（没有就自己创建，static 是默认有的，现在的新版本直接放在外层的那个 resources 下是不可以的）

经过测试都是可以访问到的

![image-20200917223703900]((四) SpringBoot起飞之路-Web静态资源处理.assets/image-20200917223703900.png)

通过不同测试的比较，还可以得出一个结论：

**访问优先级：resources > static > public**

### **D：自定义资源路径**

如果我们还想要自己指定静态资源的存放路径，一个简单的配置就可以了，例如下面配置就是将路径指定到 resources 的 ideal 和 jsjsjs 文件夹中

```properties
 spring.resources.static-locations=classpath:/ideal/,classpath:/jsjsjs/
```

注意：一旦自定义配置了路径，原来的自动配置就不会生效了

## **(二) 首页文件处理**

继续看 WebMvcAutoConfiguration 这个配置类，找到了一个关于欢迎页面相关的方法 welcomePageHandlerMapping，`this.mvcProperties.getStaticPathPattern()`，代表的是 `/**` 这个路径继续，看到其中又调用了 getWelcomePage()

```java
@Bean
public WelcomePageHandlerMapping welcomePageHandlerMapping(ApplicationContext applicationContext,FormattingConversionService mvcConversionService, ResourceUrlProvider mvcResourceUrlProvider) {
    
	WelcomePageHandlerMapping welcomePageHandlerMapping = new   
	WelcomePageHandlerMapping(neTemplateAvailabilityProviders(applicationContext),   
 	applicationContext,getWelcomePage(),
    this.mvcProperties.getStaticPathPattern());
    
	welcomePageHandlerMapping.setInterceptors(getInterceptors(mvcConversionService, 		mvcResourceUrlProvider));
    
	welcomePageHandlerMapping.setCorsConfigurations(getCorsConfigurations());
    
	return welcomePageHandlerMapping;
}
```

跳转过去，可以看到这么两个方法

locations 的定义是获取到 `resourceProperties.getStaticLocations()`，这也就是刚才我们所探索到的那几个静态资源文件夹，在 getIndexHtml 方法中，又进行了一个拼接，也就是找到 这几个静态资源文件夹下的 index.html

```java
private Optional<Resource> getWelcomePage() {
   String[] locations = getResourceLocations(this.resourceProperties.getStaticLocations());
   return Arrays.stream(locations).map(this::getIndexHtml).filter(this::isReadable).findFirst();
}

private Resource getIndexHtml(String location) {
   return this.resourceLoader.getResource(location + "index.html");
}
```

**结论：静态资源文件夹下面的 index.html 就是默认的欢迎页面**

先不要急，我们把图标文件的处理说完，一起来测试

## **(三) 图标文件处理**

首先要说明一下，在新一些的版本中例如，2.2.x 关于静态资源的 favicon.ico 源码是改动过的

## **(1) 2.2.x 前的版本**

在此之前版本下默认是有一个默认的 favicon.ico 文件的，也就是咱们常说的绿叶子图标，相关的代码在 WebMvcAutoConfiguration 这个配置类中

```java
@Configuration
		@ConditionalOnProperty(value = "spring.mvc.favicon.enabled", matchIfMissing = true)
		public static class FaviconConfiguration implements ResourceLoaderAware {

			private final ResourceProperties resourceProperties;

			private ResourceLoader resourceLoader;

			public FaviconConfiguration(ResourceProperties resourceProperties) {
				this.resourceProperties = resourceProperties;
			}

			@Override
			public void setResourceLoader(ResourceLoader resourceLoader) {
				this.resourceLoader = resourceLoader;
			}

			@Bean
			public SimpleUrlHandlerMapping faviconHandlerMapping() {
				SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
				mapping.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
				mapping.setUrlMap(Collections.singletonMap("**/favicon.ico", faviconRequestHandler()));
				return mapping;
			}

			@Bean
			public ResourceHttpRequestHandler faviconRequestHandler() {
				ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
				requestHandler.setLocations(resolveFaviconLocations());
				return requestHandler;
			}

			private List<Resource> resolveFaviconLocations() {
				String[] staticLocations = getResourceLocations(this.resourceProperties.getStaticLocations());
				List<Resource> locations = new ArrayList<>(staticLocations.length + 1);
				Arrays.stream(staticLocations).map(this.resourceLoader::getResource).forEach(locations::add);
				locations.add(new ClassPathResource("/"));
				return Collections.unmodifiableList(locations);
			}

		}
```

可以看到只要配置 spring.mvc.favicon.enabled 就可以选择性的决定是否使用它默认的绿叶子图标，例如配置为 false 关闭默认图标

**application.properties**

```properties
spring.mvc.favicon.enabled=false
```

如果想要使用自己定制的图标，可以将文件命名为 favicon.ico 然后放置于静态资源文件夹下，就可以了

## **(2) 2.2.x 版本**

关于图标文件的处理，较新的版本做过一些改动，所以在 WebMvcAutoConfiguration 这个配置类中已经找不到关于 icon 相关的内容了，我们去 Github 看一下其改动

首先定位到这个类

![image-20200917223859801]((四) SpringBoot起飞之路-Web静态资源处理.assets/image-20200917223859801.png)

然后跳转到其 History，看到了 2019年8月21日和22日的两个相关改动

![image-20200917223940945]((四) SpringBoot起飞之路-Web静态资源处理.assets/image-20200917223940945.png)

- 21日：先把静态资源文件夹下的优先级提高到类路径前
- 22日：从类路径根目录删除默认的favicon和对服务的支持，也就是说，不提供默认的ico文件了

## **(3) 开发者说明**

我们可以继续去看一下相关的 Issues ，看一下开发者为什么这么做，下面我只截取了重要的三段

如果想看完整的可以访问：`https://github.com/spring-projects/spring-boot/issues/17925`

> **vpavic commented on 21 Aug 2019**
> The default favicon served by Spring Boot could be classified as information leakage, in a similar manner like Server HTTP header (see #4730) and exception error attribute (see #7872) were.
> I'd consider removing the default favicon as applications that don't provide custom favicon will inevitably leak info about being powered by Spring Boot.



> **wilkinsona commented on 21 Aug 2019**
> This is quite tempting. While we have a configuration property (spring.mvc.favicon.enabled), it's enabled by default. The docs also do not seem to mention that a default favicon will be served so some people may not be aware of the out-of-the-box behaviour.
> If an application developer cares about the favicon they will provide their own. If they don't care about it I doubt there's much difference to them between serving a default icon and serving nothing.



> **wilkinsona commented on 21 Aug 2019**
> Another benefit of removing the default favicon is that we could then also remove the spring.mvc.favicon.enabled property. It's a benefit as the property is confusing. Setting it to false does not, as the property's name might suggest, disable serving of a favicon.ico completely. It only disables serving a favicon.ico from the root of the classpath. A favicon.ico that's placed in one of the static resource locations will still be served.



> **wilkinsona commented on 21 Aug 2019**
> We've decided to remove the default favicon.ico file, the resource handler configuration, and the property. Users who were placing a custom favicon.ico in the root of the classpath should move it to a static resource location or configure their own resource mapping.

大家也可以自己翻译，我简单总结一下：

- vpavic 认为在 Spring Boot **提供默认的 Favicon 可能会导致网站信息泄露**，如果用户不进行自定义的图标的设置，Spring Boot 就会用默认的绿叶子，那就会导致泄露网站的开发框架
- wilkinsona 认为正好顺便**把 spring.mvc.favicon.enabled 这个属性也删掉**，**因为设置此值为 false 不是禁用图标，而仅仅是禁用默认的绿叶子罢了**，而且想要设置图标的开发者，自然会关心，不在乎图标设置的开发者，可能会有与那些设置了图标的有一些较大的区别，或者出现一些不确定的因素
- 因此，在Spring Boot2.2.x中，将默认的favicon.ico移除，同时也不再提供上述application.properties中的属性配置
- 所以想设置图标只需要将图标文件 favicon.ico 放在静态资源文件夹下或者自己配置映射就可以了

## **(四) 执行测试**

执行以下，可以看到主页和图标就都生效了

![image-20200917224059563]((四) SpringBoot起飞之路-Web静态资源处理.assets/image-20200917224059563.png)