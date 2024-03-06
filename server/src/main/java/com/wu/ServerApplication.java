package com.wu;


import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.HashMap;
import java.util.Map;


@MapperScan("com.wu.mapper")
@SpringBootApplication
public class ServerApplication{

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
