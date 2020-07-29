# Tomcat

##### 访问192.168.112.1:8080/demo1/index.html

![1595574058862](E:\Learn\javaLearn\javaDoc\Tomcat.assets\1595574058862.png)

![1595574128202](E:\Learn\javaLearn\javaDoc\Tomcat.assets\1595574128202.png)

![1595574167948](E:\Learn\javaLearn\javaDoc\Tomcat.assets\1595574167948.png)

#####  **我们用Java开发的Web应用只是一个半成品，类似于一个插件**，而服务器则像一个收发器 

![1595574199041](E:\Learn\javaLearn\javaDoc\Tomcat.assets\1595574199041.png)

#### **Tomcat架构**

#####  Tomcat目录 

![1595574391922](E:\Learn\javaLearn\javaDoc\Tomcat.assets\1595574391922.png)

#####  Tomcat的架构 

![1595574429153](E:\Learn\javaLearn\javaDoc\Tomcat.assets\1595574429153.png)

#####  Tomcat的一个配置文件(Server.xml) 

![1595574570418](E:\Learn\javaLearn\javaDoc\Tomcat.assets\1595574570418.png)

![1595574586452](E:\Learn\javaLearn\javaDoc\Tomcat.assets\1595574586452.png)

#####  xml里的配置 解释 

- Server.xml文件中的配置结构和Tomcat的架构是一一对应的。根目录是<Server>，代表服务器，<Server>下面有且仅有1个<Service>，代表服务。

- <Service>下有两个<Connector>，代表连接（需要的话可以再加）。
  其实这个Connector就是我们在上面讨论百度服务器时画过的端口。大家可以看到Tomcat默认配置了两个端口，一个是HTTP/1.1协议的，一个是AJP/1.3协议（我也不知道是啥）。前者专门处理HTTP请求。

- 当我们在浏览器输入"http://localhost:8080/demo/index.html"时，浏览器是以HTTP协议发送的，当这个请求到了服务器后，会被识别为HTTP类型，于是服务器就找来专门处理HTTP的Connector，它的默认端口正是上门Server.xml配置的8080。

- 与Connector平级的还有个<Engine>（Tomcat引擎），也就是说<Service>有两个孩子，小儿子是<Connector>，大儿子是<Engine>。Connector的作用说穿了就是监听端口，如果用户访问地址是“localhost:8080/xx/xx”，那就由监听8080端口的Connector负责，如果是"[https://www.baidu.com](https://link.zhihu.com/?target=https%3A//www.baidu.com/)"，那么就是443端口处理。其实Connector也不处理实际业务，它只是个孩子。但**它会负责把客人（请求）带到哥哥Engine那，然后Engine会处理。

- <Engine>下面有个Host，代表主机。