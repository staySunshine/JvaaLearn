package cn.xie.sb_yaml;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SbYamlApplicationTests {

    @Autowired
    RedisCluster redisCluster;
    @Test
    void contextLoads() {
        System.out.println(redisCluster);
    }

}
