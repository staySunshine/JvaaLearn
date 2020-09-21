package xie;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(JavaConfig.class);
        MyCalculatorImpl myCalculator = ctx.getBean(MyCalculatorImpl.class);
        myCalculator.add(3, 4);
        myCalculator.min(3, 4);
    }
}
