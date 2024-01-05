package com.example.sushehaoduan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sushehaoduan.mapper.AnnouncementMapper;
import com.example.sushehaoduan.mapper.StudentMapper;
import com.example.sushehaoduan.mapper.VisitorMapper;
import com.example.sushehaoduan.pojo.Announcement;
import com.example.sushehaoduan.pojo.Student;
import com.example.sushehaoduan.pojo.User;
import com.example.sushehaoduan.pojo.Visitor;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;

@RestController
@CrossOrigin
public class VisitorController {
    @Autowired
    VisitorMapper visitorMapper;
    @Autowired
    StudentMapper studentMapper;
    @GetMapping("/visitor")
    @Operation(summary = "实现获取访客列表的接口")
    public List<Visitor> getUsers(){
        return visitorMapper.selectList(null);
    }
    @PostMapping("/visitor")
    @Operation(summary = "实现添加访客信息的接口")
    public Visitor createUser(@RequestBody Visitor user) {
        visitorMapper.insert(user);
        return user;
    }
    @PutMapping("/visitor/{vid}")
    @Operation(summary = "实现修改访客的接口")
    public Visitor updateUser(@PathVariable("vid") String id, @RequestBody Visitor user) {
        Visitor existingUser = visitorMapper.selectById(id);
        if (existingUser != null) {
            user.setVid(id);
            visitorMapper.updateById(user);
            return user;
        } else {
            return null;
        }
    }
    @DeleteMapping("/visitor/{vid}")
    @Operation(summary = "实现删除访客记录的接口")
    public int delUser(@PathVariable("vid") String id) {
        return visitorMapper.deleteById(id);
    }
    @DeleteMapping("/visitors")
    @Operation(summary = "实现批量删除访客记录的接口")
    public int delUsers(@RequestBody List<String> ids) {
        int deletedCount = 0;
        for (String id : ids) {
            deletedCount += visitorMapper.deleteById(id);
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
    @GetMapping("/visitorByName")
    @Operation(summary = "全字段模糊查询访客")
    public IPage findByKeyword(@RequestParam("keyword") String keyword,
                               @RequestParam("pageNum") Integer pageNum,
                               @RequestParam("pageSize") Integer pageSize) {
        QueryWrapper<Visitor> queryWrapper = new QueryWrapper<>();
        Page<Visitor> page = new Page<>(pageNum, pageSize);

        queryWrapper.like("vid", keyword)
                .or()
                .like("sid", keyword)
                .or()
                .like("name", keyword)
                .or()
                .like("date", keyword)
                .or()
                .like("ties", keyword)
                .or()
                .like("aim", keyword)
                .orderByAsc("vid");

        page.addOrder(OrderItem.asc("vid")); // 添加升序条件

        IPage ipage = visitorMapper.selectPage(page, queryWrapper);
        System.out.println(pageSize);
        return ipage;
    }

    @GetMapping("/visitor/findByPage")
    @Operation(summary = "实现访客记录分页的接口")
    public IPage getUserList(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize) {
        Page<Visitor> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Visitor> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("vid"); // 根据id字段降序排序
        page.addOrder(OrderItem.desc("vid")); // 添加降序排序条件
        IPage ipage = visitorMapper.selectPage(page,null);
        return ipage;
    }
    @GetMapping("/vistorvid")
    @Operation(summary = "返回新的vid")
    public String generateEid() {
        // 获取当前日期和时间
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String nvid;
        while(true){
            // 创建一个Random对象
            Random random = new Random();
            // 生成一个3位随机数
            int randomNumber = random.nextInt(900) + 100; // 生成[0, 899]之间的随机数，然后加上100
            String newVid = dateFormat.format(currentDate) + randomNumber;
            if(visitorMapper.selectById(newVid)==null){
                nvid=newVid;
                break;
            }
        }
        // 根据需求自定义生成新eid的逻辑
        // 例如，将格式化后的日期与唯一计数器连接起来
        return nvid;
    }
    @GetMapping("/visitor/{year}")
    @Operation(summary = "实现返回访客数量")
    public Map<String, List<Integer>> backl(@PathVariable("year") String year) {
        QueryWrapper<Visitor> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("DATE_FORMAT(date, '%Y') = {0}", year);
        List<Visitor> visitorList = visitorMapper.selectList(queryWrapper);

        Map<Integer, Integer> maleMonthlyCounts = new HashMap<>();
        Map<Integer, Integer> femaleMonthlyCounts = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            maleMonthlyCounts.put(month, 0);
            femaleMonthlyCounts.put(month, 0);
        }

        for (Visitor visitor : visitorList) {
            LocalDateTime dateTime = visitor.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            int month = dateTime.getMonthValue();

            if (dateTime.getYear() == Integer.parseInt(year)) {
                String sex = studentMapper.selectById(visitor.getSid()).getSex();
                Map<Integer, Integer> monthlyCounts = (sex.equals("男")) ? maleMonthlyCounts : femaleMonthlyCounts;
                monthlyCounts.put(month, monthlyCounts.get(month) + 1);
            }
        }

        Map<String, List<Integer>> result = new HashMap<>();
        result.put("male", getMonthlyCountsList(maleMonthlyCounts));
        result.put("female", getMonthlyCountsList(femaleMonthlyCounts));
        return result;
    }

    private List<Integer> getMonthlyCountsList(Map<Integer, Integer> monthlyCounts) {
        List<Integer> monthlyCountsList = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            monthlyCountsList.add(monthlyCounts.get(month));
        }

        return monthlyCountsList;
    }

}
