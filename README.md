# Experiment3 — Spring Cloud 微服务韧性测试项目

基于 Spring Cloud + Resilience4j 的微服务韧性（弹性）测试项目，演示断路器、隔离器、限流器在服务消费者端的实战应用。

## 技术栈

| 组件                          | 版本                            |
|-----------------------------|-------------------------------|
| Java                        | 17                             |
| Spring Boot                 | 4.0.3                         |
| Spring Cloud                | 2025.1.1                      |
| Spring Cloud Netflix Eureka | —                             |
| Spring Cloud OpenFeign      | —                             |
| Resilience4j                | (managed by Spring Cloud BOM) |
| JMeter                      | 5.6.3                         |

## 模块架构

```
Experiment3
├── Service_Eureka_19000/19001/19002  ← 注册中心集群 (3节点)
├── Provider_15001/15002/15003        ← 服务提供者 'provider-service' (3实例)
├── Public/                           ← 共享模块 (实体类 + Feign接口)
├── Consumer_11001/                   ← ★ 服务消费者 (韧性测试主体)
├── Consumer_11002/                   ← 服务消费者 (无韧性配置)
└── 测试计划-实验三.jmx               ← JMeter 测试计划
```

## Consumer_11001 — 韧性配置详情

### 依赖

```xml
spring-cloud-starter-circuitbreaker-resilience4j  <!-- 断路器抽象层 -->
resilience4j-spring-boot3                          <!-- @Bulkhead/@RateLimiter 切面 -->
spring-boot-starter-aop                             <!-- AOP 支持 -->
```

### Resilience4j 配置 (application.yml)

#### 断路器 CircuitBreaker

| 实例           | 端点                           | 失败率阈值 | 慢调用率阈值 | 慢调用时长 | 最小调用 | 半开许可 |
|--------------|------------------------------|-------|--------|-------|------|------|
| **breakerA** | `GET /cart/getUser/{userId}` | 30%   | —      | —     | 5    | 3    |
| **breakerB** | `POST /cart/addUser`         | 50%   | 30%    | 2s    | 5    | 3    |

> breakerA 用于测试**失败熔断**：传入非法 userId (≤0) 抛出异常，失败率超 30% 则断路器打开。
> breakerB 用于测试**慢调用熔断**：通过 2s 固定延时模拟慢调用，慢调用比例超 30% 则断路器打开。

#### 隔离器 Bulkhead

| 实例            | 端点                                 | 最大并发 | 最大等待 |
|---------------|------------------------------------|------|------|
| **bulkheadA** | `DELETE /cart/deleteUser/{userId}` | 10   | 20ms |

> 并发请求超过 10 时，后续请求等待最多 20ms 后被拒绝降级。

#### 限流器 RateLimiter

| 实例               | 端点                     | 刷新周期 | 每周期上限 | 超时策略       |
|------------------|------------------------|------|-------|------------|
| **rateLimiterA** | `PUT /cart/updateUser` | 2s   | 5     | 0ms (立即拒绝) |

> 每 2 秒最多放行 5 个请求，超限请求立即被拒绝降级。

### API 端点总览

| 方法     | 路径                          | 韧性注解                         | 降级返回值                     |
|--------|-----------------------------|------------------------------|---------------------------|
| GET    | `/cart/getUser/{userId}`    | `@CircuitBreaker(breakerA)`  | `{"userName":"默认用户",...}` |
| POST   | `/cart/addUser`             | `@CircuitBreaker(breakerB)`  | `"服务暂时不可用，用户添加失败"`        |
| PUT    | `/cart/updateUser`          | `@RateLimiter(rateLimiterA)` | `"服务当前被限流，用户修改失败，请稍后重试"`  |
| DELETE | `/cart/deleteUser/{userId}` | `@Bulkhead(bulkheadA)`       | `"服务当前繁忙，用户删除失败，请稍后重试"`   |

## JMeter 测试计划

测试计划文件：**`测试计划-实验三.jmx`**

### 线程组概览

| 线程组         | 测试目标         | 端点                                      | 并发策略       | 预期结果                      |
|-------------|--------------|-----------------------------------------|------------|---------------------------|
| **1-失败熔断**  | breakerA     | `GET /cart/getUser/-1` (失败) + `/1` (正常) | 20线程×10循环  | 失败率>30% → 断路器打开 → 正常调用也降级 |
| **2-慢调用熔断** | breakerB     | `POST /cart/addUser` (部分+2s延时)          | 20线程×10循环  | 慢调用率>30% → 断路器打开 → 降级返回   |
| **3-隔离器**   | bulkheadA    | `DELETE /cart/deleteUser/${rand}`       | 200线程集合点×1 | 仅10个获批 → 190个隔离降级         |
| **4-限流器**   | rateLimiterA | `PUT /cart/updateUser`                  | 20线程集合点×1  | 仅5个获批 → 15个限流降级           |

### 运行前提

1. 启动 Eureka 注册中心集群 (19000/19001/19002)
2. 启动 Provider 服务实例 (15001/15002/15003)
3. 启动 Consumer_11001 (11001)
4. 使用 JMeter 打开 `测试计划-实验三.jmx`
5. 分别运行各线程组，查看**查看结果树**和**聚合报告**

### 降级响应对照


| 降级类型  | 响应关键字     | 含义               |
|-------|-----------|------------------|
| 失败熔断  | `默认用户`    | 断路器因失败率过高打开      |
| 慢调用熔断 | `服务暂时不可用` | 断路器因慢调用率过高打开     |
| 隔离降级  | `服务当前繁忙`  | 并发超 10 或等待超 20ms |
| 限流降级  | `服务当前被限流` | 2s 内超过 5 个请求     |
