package xie;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        MyCalculatorImpl myCalculator = ctx.getBean(MyCalculatorImpl.class);
        myCalculator.add(3, 4);
        myCalculator.min(5, 6);
    }
}
