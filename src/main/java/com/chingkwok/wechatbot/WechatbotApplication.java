package com.chingkwok.wechatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WechatbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatbotApplication.class, args);
    }

}
