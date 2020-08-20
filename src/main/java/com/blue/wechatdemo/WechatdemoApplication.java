package com.blue.wechatdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.blue")
public class WechatdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WechatdemoApplication.class, args);
	}

}
