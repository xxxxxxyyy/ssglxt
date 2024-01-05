package com.example.sushehaoduan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sushehaoduan.mapper.AuditMapper;
import com.example.sushehaoduan.mapper.UserMapper;
import com.example.sushehaoduan.pojo.Audit;
import com.example.sushehaoduan.pojo.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@CrossOrigin
public class AuditController {

    @Autowired
    AuditMapper auditMapper;
    @GetMapping("/audit")
    @Operation(summary = "实现获取审核列表的接口")
    public List<Audit> getUsers(){
        return auditMapper.selectList(null);
    }

    @GetMapping("/audit/{eid}/{sid}")
    @Operation(summary = "实现获取审核列表中退费号是否存在的接口")
    public boolean getUsers1(@PathVariable String eid,@PathVariable String sid){
        QueryWrapper<Audit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid",sid);
        queryWrapper.like("content", eid);
        return !auditMapper.selectList(queryWrapper).isEmpty();
    }

    @PostMapping("/audit")
    @Operation(summary = "实现添加审核的接口")
    public Audit createUser(@RequestBody Audit user) {
        auditMapper.insert(user);
        return user;
    }
    @PutMapping("/audit/{aid}")
    @Operation(summary = "实现修改用户的接口")
    public Audit updateUser(@PathVariable("aid") String id, @RequestBody Audit user) {
        Audit existingUser = auditMapper.selectById(id);
        if (existingUser != null) {
            user.setAid(id);
            auditMapper.updateById(user);
            return user;
        } else {
            return null;
        }
    }
    @DeleteMapping("/audit/{aid}")
    @Operation(summary = "实现删除审核的接口")
    public int delUser(@PathVariable("aid") String id) {
        return auditMapper.deleteById(id);
    }
    @DeleteMapping("/audits")
    @Operation(summary = "实现批量删除审核的接口")
    public Integer delUsers(@RequestBody List<String> ids) {
        int deletedCount = 0;
        for (String id : ids) {
            deletedCount += auditMapper.deleteById(id);
        }
        return deletedCount;
    }
    @GetMapping("/auditByName")
    @Operation(summary = "全字段模糊查询审核")
    public IPage findByContent(@RequestParam("keyword") String name,
                               @RequestParam("pageNum") Integer pageNum,
                               @RequestParam("pageSize") Integer pageSize) {
        QueryWrapper<Audit> queryWrapper = new QueryWrapper<>();
        Page<Audit> page = new Page<>(pageNum, pageSize);

        queryWrapper.like("aid", name)
                .or()
                .like("sid", name)
                .or()
                .like("type", name)
                .or()
                .like("content", name)
                .or()
                .like("state", name)
                .or()
                .like("text", name)
                .orderByAsc("aid");
        page.addOrder(OrderItem.asc("aid")); // 添加升序条件
        IPage ipage = auditMapper.selectPage(page, queryWrapper);
        System.out.println(pageSize);
        return ipage;
    }

    @GetMapping("/audit/findByPage")
    @Operation(summary = "实现审核分页的接口")
    public IPage getUserList(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize) {
        Page<Audit> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Audit> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("aid"); // 根据id字段降序排序
        page.addOrder(OrderItem.desc("aid")); // 添加降序排序条件
        IPage ipage = auditMapper.selectPage(page,null);
        return ipage;
    }

    @GetMapping("/audit/findByPage1/{sid}")
    @Operation(summary = "实现个人审核分页的接口")
    public IPage getUserList1(@PathVariable String sid,
                              @RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize) {
        Page<Audit> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Audit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid",sid);
        queryWrapper.orderByDesc("aid"); // 根据id字段降序排序
        page.addOrder(OrderItem.desc("aid")); // 添加降序排序条件
        IPage ipage = auditMapper.selectPage(page,queryWrapper);
        return ipage;
    }

    @GetMapping("/generateAuid")
    @Operation(summary = "返回新的aid")
    public String generateAid() {
        // 获取当前日期和时间
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String newAid;

        while (true) {
            // 创建一个 Random 对象
            Random random = new Random();
            // 生成一个 3 位随机数
            int randomNumber = random.nextInt(900) + 100; // 生成[0, 899]之间的随机数，然后加上100
            String generatedAid = dateFormat.format(currentDate) + randomNumber;
            // 检查数据库中是否已存在相同的 aid
            Audit existingAudit = auditMapper.selectById(generatedAid);
            if (existingAudit == null) {
                newAid = generatedAid;
                break;
            }
        }

        // 根据需求自定义生成新 aid 的逻辑
        // 例如，将格式化后的日期与唯一计数器连接起来
        return newAid;
    }

}

