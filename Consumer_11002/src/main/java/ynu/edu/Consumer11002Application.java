package ynu.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Consumer11002Application {
    public static void main(String[] args) {
        SpringApplication.run(Consumer11002Application.class, args);
    }
}
