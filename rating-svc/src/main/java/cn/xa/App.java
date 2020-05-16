package cn.xa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"cn.xa.collaboration", "cn.xa.tracking", "cn.xa.task"})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
