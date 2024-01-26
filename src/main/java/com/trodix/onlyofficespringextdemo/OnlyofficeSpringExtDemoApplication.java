package com.trodix.onlyofficespringextdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.trodix", "com.trodix.duckcloud.connectors.onlyoffice"})
public class OnlyofficeSpringExtDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlyofficeSpringExtDemoApplication.class, args);
    }

}
