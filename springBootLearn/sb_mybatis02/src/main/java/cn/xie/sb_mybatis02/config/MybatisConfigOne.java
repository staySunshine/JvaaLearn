package cn.xie.sb_mybatis02.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.sql.DataSource;

@Configuration
public class MybatisConfigOne {
    @Resource(name = "dsOne")
    DataSource dsOne;
}
