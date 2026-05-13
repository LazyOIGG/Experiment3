package ynu.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Eureka19000Application {
    public static void main(String[] args) {
        SpringApplication.run(Eureka19000Application.class, args);
    }
}
