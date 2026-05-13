package ynu.edu.controller;

import org.springframework.web.bind.annotation.*;
import ynu.edu.entity.User;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    // 模拟数据库存储
    private static final Map<Integer, User> userMap = new HashMap<>();

    static {
        // 初始化一些测试数据
        userMap.put(1, new User(1, "小明-from 15002", "123456"));
        userMap.put(2, new User(2, "小红", "654321"));
    }

    // GET：根据用户ID查询用户
    @GetMapping("/getUserById/{userId}")
    public User getUserById(@PathVariable("userId") Integer userId) {
        return userMap.get(userId);
    }

    // POST：新增用户
    @PostMapping("/addUser")
    public String addUser(@RequestBody User user) {
        if (userMap.containsKey(user.getUserId())) {
            return "用户ID已存在，添加失败";
        }
        userMap.put(user.getUserId(), user);
        return "用户添加成功：" + user;
    }

    // PUT：修改用户信息（全量更新）
    @PutMapping("/updateUser")
    public String updateUser(@RequestBody User user) {
        if (!userMap.containsKey(user.getUserId())) {
            return "用户不存在，更新失败";
        }
        userMap.put(user.getUserId(), user);
        return "用户更新成功：" + user;
    }

    // DELETE：根据用户ID删除用户
    @DeleteMapping("/deleteUser/{userId}")
    public String deleteUser(@PathVariable("userId") Integer userId) {
        if (!userMap.containsKey(userId)) {
            return "用户不存在，删除失败";
        }
        userMap.remove(userId);
        return "用户删除成功，用户ID：" + userId;
    }
}