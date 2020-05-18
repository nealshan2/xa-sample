package cn.xa;

import org.apache.servicecomb.saga.omega.spring.EnableOmega;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableOmega
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

    }
}
