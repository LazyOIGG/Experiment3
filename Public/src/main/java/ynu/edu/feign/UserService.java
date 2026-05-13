package ynu.edu.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ynu.edu.entity.User;

@FeignClient("provider-service")
public interface UserService {

    // GET：根据用户ID查询用户
    @GetMapping("/user/getUserById/{userId}")
    User getUserById(@PathVariable("userId") Integer userId);

    // POST：新增用户
    @PostMapping("/user/addUser")
    String addUser(@RequestBody User user);

    // PUT：修改用户信息（全量更新）
    @PutMapping("/user/updateUser")
    String updateUser(@RequestBody User user);

    // DELETE：根据用户ID删除用户
    @DeleteMapping("/user/deleteUser/{userId}")
    String deleteUser(@PathVariable("userId") Integer userId);
}