package cn.xie.sb_json.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;

@Configuration
public class WebMvcConfig {

//    @Bean
//    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
//        converter.setObjectMapper(objectMapper);
//        return converter;
//    }
//    @Bean
//ObjectMapper objectMapper(){
//    ObjectMapper objectMapper = new ObjectMapper();
//    objectMapper.setDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
//    return objectMapper;
//}
    @Bean
    FastJsonHttpMessageConverter fastJsonHttpMessageConverter(){
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig conf = new FastJsonConfig();
        conf.setDateFormat("yyyy/MM/dd");
        fastJsonHttpMessageConverter.setFastJsonConfig(conf);
        return fastJsonHttpMessageConverter;
    }
}
