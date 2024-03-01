package com.example.seckilldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;





//@SpringBootApplication
//@MapperScan("com.examples.seckilldemo.pojo")
//public class SeckillDemoApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(SeckillDemoApplication.class, args);
//    }
//
//}
@SpringBootApplication
@MapperScan("com.example.seckilldemo.mapper")
//@EnableSwagger2
public class SeckillDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillDemoApplication.class, args);
    }

}
