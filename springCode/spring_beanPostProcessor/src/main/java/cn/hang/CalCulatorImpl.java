package cn.hang;

import cn.hang.Calculator;
import org.springframework.stereotype.Component;

@Component
public class CalCulatorImpl implements Calculator {
    public void add(int a, int b) {
        System.out.println(a+b);
    }
}
