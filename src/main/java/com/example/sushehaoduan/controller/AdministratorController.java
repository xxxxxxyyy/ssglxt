package com.example.sushehaoduan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sushehaoduan.mapper.AdministratorMapper;
import com.example.sushehaoduan.mapper.UserMapper;
import com.example.sushehaoduan.pojo.Administrator;
import com.example.sushehaoduan.pojo.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class AdministratorController {
    @Autowired
    AdministratorMapper administratorMapper;
    @GetMapping("/administrator")
    @Operation(summary = "实现获取管理员列表的接口")
    public List<Administrator> getUsers(){
        return administratorMapper.selectList(null);
    }
    @PostMapping("/administrator")
    @Operation(summary = "实现添加管理员的接口")
    public Administrator createUser(@RequestBody Administrator user) {
        String id=user.getMid();
        User user1= new User();
        user1.setUid(id);
        user1.setPasswd("123");
        userMapper.insert(user1);
        administratorMapper.insert(user);
        return user;
    }
    @Autowired
    UserMapper userMapper;
    @PutMapping("/administrator/{mid}")
    @Operation(summary = "实现修改管理员的接口")
    public Administrator updateUser(@PathVariable("mid") String id, @RequestBody Administrator user) {

        Administrator existingUser = administratorMapper.selectById(id);
        if (existingUser != null) {
            user.setMid(id);
            administratorMapper.updateById(user);
            return user;
        } else {
            return null;
        }
    }
    @DeleteMapping("/administrator/{mid}")
    @Operation(summary = "实现删除管理员的接口")
    public int delUser(@PathVariable("mid") String id) {
        return administratorMapper.deleteById(id);
    }
    @DeleteMapping("/administrators")
    @Operation(summary = "实现批量删除管理员的接口")
    public int delUsers(@RequestBody List<String> ids) {
        int deletedCount = 0;
        for (String id : ids) {
            deletedCount += administratorMapper.deleteById(id);
        }
        return deletedCount;
    }
    /* @GetMapping("/userByName/{name}")
     @Operation(summary = "模糊查询姓名")
     public List<User> findByName(@PathVariable String name) {
         QueryWrapper<User> queryWrapper = new QueryWrapper<>();
         queryWrapper.like("name", name);
         return userMapper.selectList(queryWrapper);
     }*/
    @GetMapping("/managerByName")
    @Operation(summary = "全字段模糊查询管理员")
    public IPage findByKeyword(@RequestParam("keyword") String keyword,
                               @RequestParam("pageNum") Integer pageNum,
                               @RequestParam("pageSize") Integer pageSize) {
        QueryWrapper<Administrator> queryWrapper = new QueryWrapper<>();
        Page<Administrator> page = new Page<>(pageNum, pageSize);

        queryWrapper.like("mid", keyword)
                .or()
                .like("name", keyword)
                .or()
                .like("tel", keyword)
                .orderByAsc("mid");

        page.addOrder(OrderItem.asc("mid")); // 添加升序条件

        IPage ipage = administratorMapper.selectPage(page, queryWrapper);
        System.out.println(pageSize);
        return ipage;
    }

    @GetMapping("/administrator/findByPage")
    @Operation(summary = "实现管理员分页的接口")
    public IPage getUserList(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize) {
        Page<Administrator> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Administrator> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("mid"); // 根据id字段降序排序
        page.addOrder(OrderItem.desc("mid")); // 添加降序排序条件
        IPage ipage = administratorMapper.selectPage(page,null);
        return ipage;
    }
  /*  @PostMapping("/administrator/login")
    public ResponseEntity<String> login(@RequestParam("uid") int nid, @RequestParam("passwd") String password) {
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
    public boolean check(@RequestParam("uid") int nid) {
        // 根据用户名从数据库中查询用户是否存在
        User personnel1 = userMapper.selectById(nid);
        return personnel1 != null;
    }*/
}
