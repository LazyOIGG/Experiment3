package ynu.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Eureka19002Application {
    public static void main(String[] args) {
        SpringApplication.run(Eureka19002Application.class, args);
    }
}
