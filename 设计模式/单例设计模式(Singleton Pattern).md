# Java设计模式—单例设计模式(Singleton Pattern)完全解析

通过本篇博客你将学到以下内容

①什么是设计模式

②为什么会有单例设计模式即它的用处,以及它解决了什么问题

③怎样实现单例,即它的设计思想是什么

④单例模式有哪些写法

⑤单例模式在面试中要注意哪些事项

1、什么是设计模式？

  首先我们来看第一个问题什么是设计模式？在百度百科中它的定义是这样的： 设计模式（Design pattern）是一套被反复使用、多数人知晓的、经过分类编目的、代码设计经验的总结。(百度百科)

  其实设计模式是人们实践的产物，在初期的开发过程中好多人发现再进行重复的代码书写，那些开发大牛们就不断总结、抽取最终得到了大家的认可于是就产生了设计模式，其实设计模式的种类可以分为23种左右，今天主要和大家一起学习一下单例设计模式，因为这种设计模式是使用的最多的设计模式。在以后的文章中会给大家带来其他模式的讨论。



2、为什么会有单例设计模式？

 我们都知道单例模式是在开发中用的最多的一种设计模式，那么究竟为什么会有单例设计模式呢？对于这个问题相信有很多会写单例的人都会有个这个疑问。在这里先说一下单例的用途，然后举一个例子大家就会明白为什么会有单例了。单例模式主要是为了避免因为创建了多个实例造成资源的浪费，且多个实例由于多次调用容易导致结果出现错误，而使用单例模式能够保证整个应用中有且只有一个实例。从其名字中我们就可以看出所谓单例，就是单个实例也就是说它可以解决的问题是：可以保证一个类在内存中的对象的唯一性，在一些常用的工具类、线程池、缓存，数据库，账户登录系统、配置文件等程序中可能只允许我们创建一个对象，一方面如果创建多个对象可能引起程序的错误，另一方面创建多个对象也造成资源的浪费。在这种基础之上单例设计模式就产生了因为使用单例能够保证整个应用中有且只有一个实例，看到这大家可能有些疑惑，没关系，我们来举一个例子，相信看完后你就会非常明白，为什么会有单例。



3、单例模式的设计思想

在上面我们说到现在解决问题的关键就是保证在应用中只有一个对象就行了，那么怎么保证只有一个对象呢？

其实只需要三步就可以保证对象的唯一性

(1)不允许其他程序用new对象。

  因为new就是开辟新的空间，在这里更改数据只是更改的所创建的对象的数据，如果可以new的话，每一次new都产生一个对象，这样肯定保证不了对象的唯一性。

(2)在该类中创建对象
  因为不允许其他程序new对象，所以这里的对象需要在本类中new出来

(3)对外提供一个可以让其他程序获取该对象的方法

  因为对象是在本类中创建的，所以需要提供一个方法让其它的类获取这个对象。

那么这三步怎么用代码实现呢？将上述三步转换成代码描述是这样的

(1)私有化该类的构造函数
(2)通过new在本类中创建一个本类对象
(3)定义一个公有的方法，将在该类中所创建的对象返回



4、单例模式的写法

  经过3中的分析我们理解了单例所解决的问题以及它的实现思想，接着来看看它的实现代码，单例模式的写法大的方面可以分为5种五种①懒汉式②饿汉式③双重校验锁④静态内部类⑤枚举。接下来我们就一起来看看这几种单例设计模式的代码实现，以及它们的优缺点

4.1单例模式的饿汉式[可用]

```java
public class Singleton {
 
	private static Singleton instance=new Singleton();
	private Singleton(){};
	public static Singleton getInstance(){
		return instance;
	}
}
```

访问方式

```java
Singleton instance = Singleton.getInstance();
```

得到这个实例后就可以访问这个类中的方法了。



优点：从它的实现中我们可以看到，这种方式的实现比较简单，在类加载的时候就完成了实例化，避免了线程的同步问题。

缺点：由于在类加载的时候就实例化了，所以没有达到Lazy Loading(懒加载)的效果，也就是说可能我没有用到这个实例，但是它也会加载，会造成内存的浪费(但是这个浪费可以忽略，所以这种方式也是推荐使用的)。

4.2单例模式的饿汉式变换写法[可用]

```java
public class Singleton{
 
	private static Singleton instance = null;
	
	static {
		instance = new Singleton();
	}
 
	private Singleton() {};
 
	public static Singleton getInstance() {
		return instance;
	}
}
```

访问方式：

```java
Singleton instance = Singleton.getInstance();
```

得到这个实例后就可以访问这个类中的方法了。

可以看到上面的代码是按照在2中分析的那三步来实现的，这中写法被称为饿汉式，因为它在类创建的时候就已经实例化了对象。其实4.2和4.1只是写法有点不同，都是在类初始化时创建对象的，它的优缺点和4.1一样，可以归为一种写法。

4.3单例模式的懒汉式[线程不安全，不可用]

```java
public class Singleton {
 
	private static Singleton instance=null;
	
	private Singleton() {};
	
	public static Singleton getInstance(){
		
		if(instance==null){
			instance=new Singleton();
		}
		return instance;
	}
}
```

这种方式是在调用getInstance方法的时候才创建对象的，所以它比较懒因此被称为懒汉式。

在上述两种写法中懒汉式其实是存在线程安全问题的，喜欢刨根问题的同学可能会问，存在怎样的线程安全问题？怎样导致这种问题的？好，我们来说一下什么情况下这种写法会有问题。在运行过程中可能存在这么一种情况：有多个线程去调用getInstance方法来获取Singleton的实例，那么就有可能发生这样一种情况当第一个线程在执行if(instance==null)这个语句时，此时instance是为null的进入语句。在还没有执行instance=new Singleton()时(此时instance是为null的)第二个线程也进入if(instance==null)这个语句，因为之前进入这个语句的线程中还没有执行instance=new Singleton()，所以它会执行instance=new Singleton()来实例化Singleton对象，因为第二个线程也进入了if语句所以它也会实例化Singleton对象。这样就导致了实例化了两个Singleton对象。所以单例模式的懒汉式是存在线程安全问题的，既然它存在问题，那么可能有解决这个问题的方法，那么究竟怎么解决呢？对这种问题可能很多人会想到加锁于是出现了下面这种写法。

```java
public class Singleton {
 
	private static Singleton instance=null;
	
	private Singleton() {};
	
	public static synchronized Singleton getInstance(){
		
		if(instance==null){
			instance=new Singleton();
		}
		return instance;
	}
}
```

缺点：效率太低了，每个线程在想获得类的实例时候，执行getInstance()方法都要进行同步。而其实这个方法只执行一次实例化代码就够了，后面的想获得该类实例，直接return就行了。方法进行同步效率太低要改进。

4.5单例模式懒汉式[线程不安全，不可用]

对于上述缺陷的改进可能有的人会想到如下的代码

```java
public class Singleton7 {
 
	private static Singleton instance=null;
	
	public static Singleton getInstance() {
		if (instance == null) {
			synchronized (Singleton.class) {
				instance = new Singleton();
			}
		}
		return instance;
	}
}
```

其实这种写法跟4.3一样是线程不安全的，当一个线程还没有实例化Singleton时另一个线程执行到if(instance==null)这个判断语句时就会进入if语句，虽然加了锁，但是等到第一个线程执行完instance=new Singleton()跳出这个锁时，另一个进入if语句的线程同样会实例化另外一个Singleton对象，线程不安全的原理跟4.3类似。因此这种改进方式并不可行，经过大神们一步一步的探索，写出了懒汉式的双重校验锁。

4.6单例模式懒汉式双重校验锁[推荐用]

```java
public class Singleton {
	/**
	 * 懒汉式变种，属于懒汉式中最好的写法，保证了：延迟加载和线程安全
	 */
	private static Singleton instance=null;
	
	private Singleton() {};
	
	public static Singleton getInstance(){
		 if (instance == null) {  
	          synchronized (Singleton.class) {  
	              if (instance == null) {  
	            	  instance = new Singleton();  
	              }  
	          }  
	      }  
	      return instance;  
	}
}
```

访问方式

```java
Singleton instance = Singleton.getInstance();
```

得到这个实例后就可以访问这个类中的方法了。

Double-Check概念对于多线程开发者来说不会陌生，如代码中所示，我们进行了两次if (instance== null)检查，这样就可以保   证线程安全了。这样，实例化代码只用执行一次，后面再次访问时，判断if (instance== null)，直接return实例化对象。

优点：线程安全；延迟加载；效率较高。

4.7内部类[推荐用]

```java
public class Singleton{
 
	
	private Singleton() {};
	
	private static class SingletonHolder{
		private static Singleton instance=new Singleton();
	} 
	
	public static Singleton getInstance(){
		return SingletonHolder.instance;
	}
}
```

访问方式

```java
Singleton instance = Singleton.getInstance();
```

得到这个实例后就可以访问这个类中的方法了。

  这种方式跟饿汉式方式采用的机制类似，但又有不同。两者都是采用了类装载的机制来保证初始化实例时只有一个线程。不同

的地方在饿汉式方式是只要Singleton类被装载就会实例化，没有Lazy-Loading的作用，而静态内部类方式在Singleton类被装载时

并不会立即实例化，而是在需要实例化时，调用getInstance方法，才会装载SingletonHolder类，从而完成Singleton的实例化。

类的静态属性只会在第一次加载类的时候初始化，所以在这里，JVM帮助我们保证了线程的安全性，在类进行初始化时，别的线程是

无法进入的。

优点：避免了线程不安全，延迟加载，效率高。

4.8枚举[极推荐使用]

```java
public enum SingletonEnum {
	
	 instance; 
	 
	 private SingletonEnum() {}
	 
	 public void method(){
	 }
}
```

访问方式

```java
SingletonEnum.instance.method();
```

可以看到枚举的书写非常简单，访问也很简单在这里SingletonEnum.instance这里的instance即为SingletonEnum类型的引用所以得到它就可以调用枚举中的方法了。

借助JDK1.5中添加的枚举来实现单例模式。不仅能避免多线程同步问题，而且还能防止反序列化重新创建新的对象。可能是因为枚举在JDK1.5中才添加，所以在实际项目开发中，很少见人这么写过，这种方式也是最好的一种方式，如果在开发中JDK满足要求的情况下建议使用这种方式。

5、总结

   在真正的项目开发中一般采用4.1、4.6、4.7、4.8看你最喜欢哪种写法了，一般情况下这几种模式是没有问题的，为了装逼我一般采用4.6这种写法，我们经常用的Android-Universal-Image-Loader这个开源项目也是采用的4.6这种写法，其实最安全的写法是4.8即枚举，它的实现非常简单而且最安全可谓很完美，但是可能是因为只支持JDK1.5吧又或者是因为枚举大家不熟悉所以目前使用的人并不多，但是大家可以尝试下。另外当我们使用反射机制时可能不能保证实例的唯一性，但是枚举始终可以保证唯一性，具体请参考次博客：http://blog.csdn.net/java2000_net/article/details/3983958但是一般情况下很少遇到这种情况。

6、单例模式的在面试中的问题

   单例模式在面试中会常常的被遇到，因为它是考擦一个程序员的基础的扎实程度的，如果说你跟面试官说你做过项目，面试官让你写几个单例设计模式，你写不出来，你觉着面试官会相信吗？在面试时一定要认真准备每一次面试，靠忽悠即使你被录取了，你也很有可能会对这个公司不满意，好了我们言归正传，其实单例设计模式在面试中很少有人会问饿汉式写法，一般都会问单例设计模式的懒汉式的线程安全问题，所以大家一定要充分理解单例模式的线程安全的问题，就这几种模式花点时间，认真学透，面试中遇到任何关于单例模式的问题你都不会害怕是吧。