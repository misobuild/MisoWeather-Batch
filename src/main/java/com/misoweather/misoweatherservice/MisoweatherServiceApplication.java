package com.misoweather.misoweatherservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MisoweatherServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MisoweatherServiceApplication.class, args);
    }

}
