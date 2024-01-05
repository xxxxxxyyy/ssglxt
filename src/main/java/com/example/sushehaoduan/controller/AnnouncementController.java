package com.example.sushehaoduan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sushehaoduan.mapper.AnnouncementMapper;
import com.example.sushehaoduan.mapper.UserMapper;
import com.example.sushehaoduan.pojo.Announcement;
import com.example.sushehaoduan.pojo.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@CrossOrigin
public class AnnouncementController {
    @Autowired
    AnnouncementMapper announcementMapper;
    @GetMapping("/announcement")
    @Operation(summary = "实现获取公告列表的接口")
    public List<Announcement> getUsers(){
        return announcementMapper.selectList(null);
    }

    @DeleteMapping("/announcementlist")
    @Operation(summary = "通过aids实现查找的接口")
    public List<Announcement> delUsers(@RequestBody List<String> values) {
        List<Announcement> list =new ArrayList<>();
        for (String id : values) {
            if(announcementMapper.selectById(id)!=null){
                list.add(announcementMapper.selectById(id));
            }
        }
        return list;
    }

    @PostMapping("/announcement")
    @Operation(summary = "实现添加公告的接口")
    public Announcement createUser(@RequestBody Announcement user) {
        announcementMapper.insert(user);
        return user;
    }
    @PutMapping("/announcement/{aid}")
    @Operation(summary = "实现修改公告的接口")
    public Announcement updateUser(@PathVariable("aid") String id, @RequestBody Announcement user) {
        Announcement existingUser = announcementMapper.selectById(id);
        if (existingUser != null) {
            user.setAid(id);
            announcementMapper.updateById(user);
            return user;
        } else {
            return null;
        }
    }
    @DeleteMapping("/announcement/{aid}")
    @Operation(summary = "实现删除公告的接口")
    public int delUser(@PathVariable("aid") String id) {
        return announcementMapper.deleteById(id);
    }
    @DeleteMapping("/announcements")
    @Operation(summary = "实现批量删除公告的接口")
    public int delUsers1(@RequestBody List<String> ids) {
        int deletedCount = 0;
        for (String id : ids) {
            deletedCount += announcementMapper.deleteById(id);
        }
        return deletedCount;
    }
    @GetMapping("/announcementByKeyword")
    @Operation(summary = "全字段模糊查询公告")
    public IPage findByKeyword(@RequestParam("keyword") String keyword,
                               @RequestParam("pageNum") Integer pageNum,
                               @RequestParam("pageSize") Integer pageSize) {
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        Page<Announcement> page = new Page<>(pageNum, pageSize);

        queryWrapper.like("aid", keyword)
                .or()
                .like("mid", keyword)
                .or()
                .like("date", keyword)
                .or()
                .like("content", keyword)
                .orderByAsc("aid");

        page.addOrder(OrderItem.asc("aid")); // 添加升序条件

        IPage ipage = announcementMapper.selectPage(page, queryWrapper);
        System.out.println(pageSize);
        return ipage;
    }

    /* @GetMapping("/userByName/{name}")
     @Operation(summary = "模糊查询姓名")
     public List<User> findByName(@PathVariable String name) {
         QueryWrapper<User> queryWrapper = new QueryWrapper<>();
         queryWrapper.like("name", name);
         return userMapper.selectList(queryWrapper);
     }*/
    @GetMapping("/announcement/findByPage")
    @Operation(summary = "实现公告分页的接口")
    public IPage getUserList(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize) {
        Page<Announcement> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("aid"); // 根据id字段降序排序
        page.addOrder(OrderItem.desc("aid")); // 添加降序排序条件
        IPage ipage = announcementMapper.selectPage(page,null);
        return ipage;
    }

    @GetMapping("/generateAid")
    @Operation(summary = "返回新的aid")
    public String generateEid() {
        // 获取当前日期和时间
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String naid;
        while(true){
            // 创建一个Random对象
            Random random = new Random();
            // 生成一个3位随机数
            int randomNumber = random.nextInt(900) + 100; // 生成[0, 899]之间的随机数，然后加上100
            String newAid = dateFormat.format(currentDate) + randomNumber;
            if(announcementMapper.selectById(newAid)==null){
                naid=newAid;
                break;
            }
        }
        // 根据需求自定义生成新eid的逻辑
        // 例如，将格式化后的日期与唯一计数器连接起来
        return naid;
    }
  /*  @PostMapping("/user/login")
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
    }*/
}
