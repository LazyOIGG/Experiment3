package ynu.edu.rule;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

public class RandomLoadBalancerConfig {
    @Bean
    ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment env, LoadBalancerClientFactory lbf) {
        String name = env.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new org.springframework.cloud.loadbalancer.core.RandomLoadBalancer(lbf.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
    }
}
