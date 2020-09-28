package cn.xie.sb_aop.service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogAspect {

    @Pointcut("execution(* cn.xie.sb_aop.service.*.*(..))")
    public void pc1(){}

    @Before("pc1()")
    public void before(JoinPoint jp){
        System.out.println("before " + jp.getSignature().getName());
    }

    @After("pc1()")
    public void after(JoinPoint jp){
        System.out.println("after " + jp.getSignature().getName());
    }

    @AfterReturning(value = "pc1()",returning = "ret")
    public void afterRuturning(JoinPoint jp,Object ret){
        System.out.println("afterRuturning " + jp.getSignature().getName() + "returning" + ret);
    }

    @Around("pc1()")
    public void around(ProceedingJoinPoint pjp) throws Throwable {
        Object ret = pjp.proceed();
        System.out.println("around " + pjp.getSignature().getName() + "returning" + ret);
    }
}
