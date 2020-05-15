package cn.xa.rating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"cn.xa.spec", "cn.xa.job"})
public class RfeSvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(RfeSvcApplication.class, args);
    }

}
