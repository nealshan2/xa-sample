package cn.xa.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"cn.xa.collaboration", "cn.xa.tracking"})
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
