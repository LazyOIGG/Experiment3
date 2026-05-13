package ynu.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ynu.edu.rule.RandomLoadBalancerConfig;

@SpringBootApplication
@EnableFeignClients
@LoadBalancerClient(name = "provider-service", configuration = RandomLoadBalancerConfig.class)
public class Consumer11001Application {
    public static void main(String[] args) {
        SpringApplication.run(Consumer11001Application.class, args);
    }
}
