package ynu.edu.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.*;
import ynu.edu.entity.Cart;
import ynu.edu.entity.User;
import ynu.edu.feign.UserService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    private UserService userService;

    // 1. GET：调用UserController的查询用户方法，使用断路器实例breakerA
    @GetMapping("/getUser/{userId}")
    @CircuitBreaker(name = "breakerA", fallbackMethod = "getUserDown")
    public User getUser(@PathVariable("userId") Integer userId) {

        // 如果传入的ID无效，抛出异常
        if (userId <= 0) {
            throw new RuntimeException("Invalid user ID: " + userId);
        }

        String message ="正常调用！";
        System.out.println(message);

        return userService.getUserById(userId);
    }

    // getUser方法的降级方法
    public User getUserDown(Integer userId, Exception exception) {
        exception.printStackTrace();
        System.out.println("获取用户" + userId + "信息的服务当前被熔断，因此方法降级");
        // 返回默认用户对象
        User defaultUser = new User();
        defaultUser.setUserId(userId);
        defaultUser.setUserName("默认用户");
        defaultUser.setUserPassword("默认密码");

        String message ="降级调用！";
        System.out.println(message);

        return defaultUser;
    }

    // 2. POST：调用UserController的新增用户方法，使用断路器实例breakerB
    @PostMapping("/addUser")
    @CircuitBreaker(name = "breakerB", fallbackMethod = "addUserDown")
    public String addUser(@RequestBody User user) {
        String message ="正常调用！";
        System.out.println(message);

        return userService.addUser(user);
    }

    // addUser方法的降级方法
    public String addUserDown(User user, Exception exception) {
        exception.printStackTrace();
        System.out.println("添加用户服务当前被熔断，因此方法降级");

        String message ="降级调用！";
        System.out.println(message);

        return "服务暂时不可用，用户添加失败";
    }

    // 3. PUT：调用UserController的修改用户方法，使用限流器rateLimiterA
    @PutMapping("/updateUser")
    @RateLimiter(name = "rateLimiterA", fallbackMethod = "updateUserDown")
    public String updateUser(@RequestBody User user) {
        System.out.println("正常调用修改用户！");
        return userService.updateUser(user);
    }

    // updateUser方法的限流降级方法
    public String updateUserDown(User user, Throwable throwable) {
        throwable.printStackTrace();
        System.out.println("修改用户服务当前被限流，因此方法降级");
        System.out.println("降级调用！");
        return "服务当前被限流，用户修改失败，请稍后重试";
    }

    // 4. DELETE：调用UserController的删除用户方法，使用隔离器bulkheadA
    @DeleteMapping("/deleteUser/{userId}")
    @Bulkhead(name = "bulkheadA", fallbackMethod = "deleteUserDown")
    public String deleteUser(@PathVariable("userId") Integer userId) {
        System.out.println("正常调用删除用户！");
        return userService.deleteUser(userId);
    }

    // deleteUser方法的隔离降级方法
    public String deleteUserDown(Integer userId, Throwable throwable) {
        throwable.printStackTrace();
        System.out.println("删除用户" + userId + "的服务当前被隔离，因此方法降级");
        System.out.println("降级调用！");
        return "服务当前繁忙，用户删除失败，请稍后重试";
    }

}