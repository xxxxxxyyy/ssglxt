package com.example.sushehaoduan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sushehaoduan.mapper.UserMapper;
import com.example.sushehaoduan.pojo.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    UserMapper userMapper;
    @GetMapping("/user")
    @Operation(summary = "实现获取用户列表的接口")
    public List<User> getUsers(){
        return userMapper.selectList(null);
    }
    @PostMapping("/user")
    @Operation(summary = "实现添加用户的接口")
    public User createUser(@RequestBody User user) {
        userMapper.insert(user);
        return user;
    }
    @PutMapping("/user/{uid}")
    @Operation(summary = "实现修改用户的接口")
    public User updateUser(@PathVariable("uid") String id, @RequestBody User user) {
        User existingUser = userMapper.selectById(id);
        if (existingUser != null) {
            user.setUid(id);
            userMapper.updateById(user);
            return user;
        } else {
            return null;
        }
    }
    @DeleteMapping("/user/{uid}")
    @Operation(summary = "实现删除用户的接口")
    public int delUser(@PathVariable("uid") String id) {
        return userMapper.deleteById(id);
    }
    @DeleteMapping("/users")
    @Operation(summary = "实现批量删除用户的接口")
    public int delUsers(@RequestBody List<Integer> ids) {
        int deletedCount = 0;
        for (int id : ids) {
            deletedCount += userMapper.deleteById(id);
        }
        return deletedCount;
    }
   /* @GetMapping("/announcementByName")
    @Operation(summary = "全字段模糊查询公告")
    public IPage findByContent(@RequestParam("keyword") String name,
                               @RequestParam("pageNum") Integer pageNum,
                               @RequestParam("pageSize") Integer pageSize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Page<User> page = new Page<>(pageNum, pageSize);

        queryWrapper.like("aid", name)
                .or()
                .like("mid", name)
                .or()
                .like("date", name)
                .or()
                .like("content", name)
                .orderByAsc("aid");
        page.addOrder(OrderItem.asc("aid")); // 添加升序条件
        IPage ipage = userMapper.selectPage(page, queryWrapper);
        System.out.println(pageSize);
        return ipage;
    }*/

    /* @GetMapping("/userByName/{name}")
    @Operation(summary = "模糊查询姓名")
    public List<User> findByName(@PathVariable String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        return userMapper.selectList(queryWrapper);
    }*/
    @GetMapping("/user/findByPage")
    @Operation(summary = "实现用户分页的接口")
    public IPage getUserList(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("uid"); // 根据id字段降序排序
        page.addOrder(OrderItem.desc("uid")); // 添加降序排序条件
        IPage ipage = userMapper.selectPage(page,null);
        return ipage;
    }
    @PostMapping("/user/login")
    public ResponseEntity<String> login(@RequestParam("uid") String nid, @RequestParam("passwd") String password) {
        // 根据用户名从数据库中查询用户信息
        User personnel1 = userMapper.selectById(nid);

        // 验证用户是否存在及密码是否匹配
        if (personnel1 != null && personnel1.getPasswd().equals(password)) {
            if(personnel1.getAuthority()==0){
                return ResponseEntity.ok("0");
            }else{
                return ResponseEntity.ok("1");
            }

        }else {
            return ResponseEntity.ok("-1");
        }
    }
    @PostMapping("/user/check")
    public boolean check(@RequestParam("uid") String nid) {
        // 根据用户名从数据库中查询用户是否存在
        System.out.println(nid);
        User personnel1 = userMapper.selectById(nid);
        return personnel1 != null;
    }

}
