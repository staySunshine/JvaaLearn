package cn.xie.sb_properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.security.RunAs;

@SpringBootTest
class SbPropertiesApplicationTests {

    @Autowired
    Book book;
    @Test
    void contextLoads() {
        System.out.println(book);
    }

}
