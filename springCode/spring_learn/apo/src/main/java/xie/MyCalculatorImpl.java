package xie;

import org.springframework.stereotype.Component;

@Component
public class MyCalculatorImpl {
    @Action
    public int add(int a, int b) {
        return a + b;
    }

    public void min(int a, int b) {
        System.out.println(a + "-" + b + "=" + (a - b));
    }
}
