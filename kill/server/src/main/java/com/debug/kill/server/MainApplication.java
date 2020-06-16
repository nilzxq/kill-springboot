package com.debug.kill.server;

import com.debug.kill.api.Main;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

/**
 * @Author nilzxq
 * @Date 2020-06-13 10:24
 */
@SpringBootApplication
@ImportResource(value = {"classpath:spring/spring-jdbc.xml"})
@MapperScan(basePackages = "com.debug.kill.model.mapper")
public class MainApplication extends SpringBootServletInitializer {

    /**
     * 我们的项目是用外置tomcat跑的，所以这里请必须继承这类和重写这个方法，才能启动SpringBoot项目
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MainApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class,args);

    }
}
