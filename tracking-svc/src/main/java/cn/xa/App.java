package cn.xa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"cn.xa.collaboration"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

    }
}
