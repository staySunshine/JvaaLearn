# **03-Java并发编程**

- 第一部分：并发编程
  - \1. 线程状态转换
    - [新建（New）](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#新建new)
    - [可运行（Runnable）](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#可运行runnable)
    - [阻塞（Blocking）](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#阻塞blocking)
    - [无限期等待（Waiting）](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#无限期等待waiting)
    - [限期等待（Timed Waiting）](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#限期等待timed-waiting)
    - [死亡（Terminated）](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#死亡terminated)
  - \2. Java实现多线程的方式及三种方式的区别
    - [实现 Runnable 接口](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#实现-runnable-接口)
    - [实现 Callable 接口](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#实现-callable-接口)
    - [继承 Thread 类](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#继承-thread-类)
    - [实现接口 VS 继承 Thread](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#实现接口-vs-继承-thread)
    - [三种方式的区别](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#三种方式的区别)
  - \3. 基础线程机制
    - [Executor](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#executor)
    - [Daemon（守护线程）](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#daemon守护线程)
    - [sleep()](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#sleep)
    - [yield()](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#yield)
    - [线程阻塞](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#线程阻塞)
  - \4. 中断
    - [InterruptedException](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#interruptedexception)
    - [interrupted()](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#interrupted)
    - [Executor 的中断操作](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#executor-的中断操作)
  - \5. 互斥同步
    - [synchronized](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#synchronized)
    - [ReentrantLock](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#reentrantlock)
    - [synchronized 和 ReentrantLock 比较](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#synchronized-和-reentrantlock-比较)
    - [synchronized与lock的区别，使用场景。看过synchronized的源码没？](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#synchronized与lock的区别使用场景看过synchronized的源码没)
    - 什么是CAS
      - [入门例子](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#入门例子)
      - [Compare And Swap](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#compare-and-swap)
    - [什么是乐观锁和悲观锁](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#什么是乐观锁和悲观锁)
    - [Synchronized（对象锁）和Static Synchronized（类锁）区别](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#synchronized对象锁和static-synchronized类锁区别)
  - \6. 线程之间的协作
    - [join()](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#join)
    - [wait() notify() notifyAll()](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#wait-notify-notifyall)
    - [await() signal() signalAll()](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#await-signal-signalall)
    - [sleep和wait有什么区别](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#sleep和wait有什么区别)
  - \7. J.U.C - AQS
    - [CountdownLatch](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#countdownlatch)
    - [CyclicBarrier](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#cyclicbarrier)
    - [Semaphore](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#semaphore)
    - [总结](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#总结)
  - \8. J.U.C - 其它组件
    - [FutureTask](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#futuretask)
    - [BlockingQueue](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#blockingqueue)
    - [ForkJoin](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#forkjoin)
  - [9. 线程不安全示例](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#9-线程不安全示例)
  - \10. Java 内存模型（JMM）
    - [主内存与工作内存](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#主内存与工作内存)
    - [内存间交互操作](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#内存间交互操作)
    - 内存模型三大特性
      - [1. 原子性](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#1-原子性)
      - [2. 可见性](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#2-可见性)
      - [3. 有序性](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#3-有序性)
    - 指令重排序
      - [数据依赖性](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#数据依赖性)
      - [as-if-serial语义](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#as-if-serial语义)
      - [程序顺序规则](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#程序顺序规则)
      - [重排序对多线程的影响](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#重排序对多线程的影响)
    - 先行发生原则（happens-before）
      - [1. 单一线程原则](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#1-单一线程原则)
      - [2. 管程锁定规则](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#2-管程锁定规则)
      - [3. volatile 变量规则](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#3-volatile-变量规则)
      - [4. 线程启动规则](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#4-线程启动规则)
      - [5. 线程加入规则](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#5-线程加入规则)
      - [6. 线程中断规则](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#6-线程中断规则)
      - [7. 对象终结规则](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#7-对象终结规则)
      - [8. 传递性](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#8-传递性)
  - \11. 线程安全
    - [线程安全定义](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#线程安全定义)
    - 线程安全分类
      - [1. 不可变](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#1-不可变)
      - [2. 绝对线程安全](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#2-绝对线程安全)
      - [3. 相对线程安全](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#3-相对线程安全)
      - [4. 线程兼容](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#4-线程兼容)
      - [5. 线程对立](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#5-线程对立)
    - 线程安全的实现方法
      - [1. 阻塞同步（互斥同步）](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#1-阻塞同步互斥同步)
      - [2. 非阻塞同步](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#2-非阻塞同步)
      - \3. 无同步方案
        - [（一）可重入代码（Reentrant Code）](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#一可重入代码reentrant-code)
        - [（二）栈封闭](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#二栈封闭)
        - [（三）线程本地存储（Thread Local Storage）](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#三线程本地存储thread-local-storage)
  - \12. 锁优化
    - [自旋锁](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#自旋锁)
    - [锁消除](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#锁消除)
    - [锁粗化](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#锁粗化)
    - [轻量级锁](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#轻量级锁)
    - [偏向锁](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#偏向锁)
  - [13. 多线程开发良好的实践](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#13-多线程开发良好的实践)
  - \14. 线程池实现原理
    - [并发队列](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#并发队列)
    - [线程池概念](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#线程池概念)
    - [Executor类图](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#executor类图)
    - [线程池工作原理](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#线程池工作原理)
    - 初始化线程池
      - [初始化方法](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#初始化方法)
    - 常用方法
      - [execute与submit的区别](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#execute与submit的区别)
      - [shutDown与shutDownNow的区别](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#shutdown与shutdownnow的区别)
    - [内部实现](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#内部实现)
    - [线程池的状态](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#线程池的状态)
    - [线程池其他常用方法](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#线程池其他常用方法)
    - [如何合理设置线程池的大小](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#如何合理设置线程池的大小)
- 第二部分：面试指南
  - [1. volatile 与 synchronized 的区别](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#1-volatile-与-synchronized-的区别)
  - [2. 什么是线程池？如果让你设计一个动态大小的线程池，如何设计，应该有哪些方法？线程池创建的方式？](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#2-什么是线程池如果让你设计一个动态大小的线程池如何设计应该有哪些方法线程池创建的方式)
  - \3. 什么是并发和并行
    - [并发](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#并发)
    - [并行](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#并行)
  - \4. 什么是线程安全
    - [非线程安全!=不安全？](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#非线程安全不安全)
    - [线程安全十万个为什么？](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#线程安全十万个为什么)
  - [5. volatile 关键字的如何保证内存可见性](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#5-volatile-关键字的如何保证内存可见性)
  - [5. 什么是线程？线程和进程有什么区别？为什么要使用多线程](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#5-什么是线程线程和进程有什么区别为什么要使用多线程)
  - [6. 多线程共用一个数据变量需要注意什么？](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#6-多线程共用一个数据变量需要注意什么)
  - \7. 内存泄漏与内存溢出
    - [Java内存回收机制](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#java内存回收机制)
    - Java内存泄露引起原因
      - [静态集合类](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#静态集合类)
      - [监听器](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#监听器)
      - [各种连接](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#各种连接)
      - [内部类和外部模块等的引用](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#内部类和外部模块等的引用)
      - [单例模式](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#单例模式)
  - [8. 如何减少线程上下文切换](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#8-如何减少线程上下文切换)
  - \9. 线程间通信和进程间通信
    - [线程间通信](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#线程间通信)
    - [进程间通信](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#进程间通信)
  - \10. 什么是同步和异步，阻塞和非阻塞？
    - [同步](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#同步)
    - [异步](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#异步)
    - [阻塞](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#阻塞)
    - [非阻塞](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#非阻塞)
  - \11. Java中的锁
    - [一个简单的锁](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#一个简单的锁)
    - [锁的可重入性](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#锁的可重入性)
    - [锁的公平性](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#锁的公平性)
    - [在 finally 语句中调用 unlock()](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#在-finally-语句中调用-unlock)
  - [12. 并发包(J.U.C)下面，都用过什么](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#12-并发包juc下面都用过什么)
  - [13. 从volatile说到,i++原子操作,线程安全问题](https://github.com/frank-lam/fullstack-tutorial/blob/master/notes/JavaArchitecture/03-Java并发编程.md#13-从volatile说到i原子操作线程安全问题)

## 第一部分：并发编程

### 1. 线程状态转换

![1599449820916](03-Java并发编程.assets/1599449820916.png)

![1599449841999](03-Java并发编程.assets/1599449841999.png)

### 新建（New）

创建后尚未启动。

### 可运行（Runnable）

可能正在运行，也可能正在等待 CPU 时间片。

包含了操作系统线程状态中的 运行（Running ） 和 就绪（Ready）。

### 阻塞（Blocking）

这个状态下，是在多个线程有同步操作的场景，比如正在等待另一个线程的 synchronized 块的执行释放，或者可重入的 synchronized 块里别人调用 wait() 方法，也就是线程在等待进入临界区。

阻塞可以分为：等待阻塞，同步阻塞，其他阻塞

### 无限期等待（Waiting）

等待其它线程显式地唤醒，否则不会被分配 CPU 时间片。

| 进入方法                                   | 退出方法                             |
| ------------------------------------------ | ------------------------------------ |
| 没有设置 Timeout 参数的 Object.wait() 方法 | Object.notify() / Object.notifyAll() |
| 没有设置 Timeout 参数的 Thread.join() 方法 | 被调用的线程执行完毕                 |
| LockSupport.park() 方法                    | -                                    |

### 限期等待（Timed Waiting）

无需等待其它线程显式地唤醒，在一定时间之后会被系统自动唤醒。

调用 Thread.sleep() 方法使线程进入限期等待状态时，常常用 “**使一个线程睡眠**” 进行描述。

调用 Object.wait() 方法使线程进入限期等待或者无限期等待时，常常用 “**挂起一个线程**” 进行描述。

**睡眠和挂起**是用来描述**行为**，而**阻塞**和等待用来描述**状态**。

阻塞和等待的区别在于，阻塞是被动的，它是在等待获取一个排它锁。而等待是主动的，通过调用 Thread.sleep() 和 Object.wait() 等方法进入。

| 进入方法                                 | 退出方法                                        |
| ---------------------------------------- | ----------------------------------------------- |
| Thread.sleep() 方法                      | 时间结束                                        |
| 设置了 Timeout 参数的 Object.wait() 方法 | 时间结束 / Object.notify() / Object.notifyAll() |
| 设置了 Timeout 参数的 Thread.join() 方法 | 时间结束 / 被调用的线程执行完毕                 |
| LockSupport.parkNanos() 方法             | -                                               |
| LockSupport.parkUntil() 方法             | -                                               |

### 死亡（Terminated）

- 线程因为 run 方法正常退出而自然死亡
- 因为一个没有捕获的异常终止了 run 方法而意外死亡

### 2. Java实现多线程的方式及三种方式的区别

有三种使用线程的方法：

- 实现 Runnable 接口；
- 实现 Callable 接口；
- 继承 Thread 类。

 实现 Runnable 和 Callable 接口的类只能当做一个可以在线程中运行的任务，不是真正意义上的线程，因此最后还需要通过 Thread 来调用。可以说任务是通过线程驱动从而执行的。 

#### 实现 Runnable 接口

需要实现 run() 方法。

通过 Thread 调用 start() 方法来启动线程。

```java
public class MyRunnable implements Runnable {
    public void run() {
        // ...
    }
}

```

```java
public static void main(String[] args) {
    MyRunnable instance = new MyRunnable();
    Thread thread = new Thread(instance);
    thread.start();
}
```



#### 实现 Callable 接口

与 Runnable 相比，Callable 可以有返回值，返回值通过 FutureTask 进行封装。

```java
public class MyCallable implements Callable<Integer> {
    public Integer call() {
        return 123;
    }
}
```

```java
public static void main(String[] args) throws ExecutionException, InterruptedException {
    MyCallable mc = new MyCallable();
    FutureTask<Integer> ft = new FutureTask<>(mc);
    Thread thread = new Thread(ft);
    thread.start();
    System.out.println(ft.get());
}
```

#### 继承 Thread 类

 同样也是需要实现 run() 方法，因为 Thread 类也实现了 Runable 接口。 

```java
public class MyThread extends Thread {
    public void run() {
        // ...
    }
}
```

```java
public static void main(String[] args) {
    MyThread mt = new MyThread();
    mt.start();
}
```

#### 实现接口 VS 继承 Thread

实现接口会更好一些，因为：

- Java 不支持多重继承，因此继承了 Thread 类就无法继承其它类，但是可以实现多个接口；
- 类可能只要求可执行就行，继承整个 Thread 类开销过大。

#### 三种方式的区别

- 实现 Runnable 接口可以避免 Java 单继承特性而带来的局限；增强程序的健壮性，代码能够被多个线程共享，代码与数据是独立的；适合多个相同程序代码的线程区处理同一资源的情况。
- 继承 Thread 类和实现 Runnable 方法启动线程都是使用 start() 方法，然后 JVM 虚拟机将此线程放到就绪队列中，如果有处理机可用，则执行 run() 方法。
- 实现 Callable 接口要实现 call() 方法，并且线程执行完毕后会有返回值。其他的两种都是重写 run() 方法，没有返回值。