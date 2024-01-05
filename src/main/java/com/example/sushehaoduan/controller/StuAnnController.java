package com.example.sushehaoduan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sushehaoduan.mapper.StuAnnMapper;
import com.example.sushehaoduan.pojo.Announcement;
import com.example.sushehaoduan.pojo.Audit;
import com.example.sushehaoduan.pojo.StuAnn;
import com.example.sushehaoduan.pojo.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class StuAnnController {
    @Autowired
    StuAnnMapper stuAnnMapper;

    @PostMapping("/stuann/{aid}")
    @Operation(summary = "实现添加学生公告的接口")
    public void createUser(@PathVariable String aid,@RequestBody List<String> sids) {
        System.out.println("sids");
        System.out.println(sids);
        StuAnn stuAnn = new StuAnn();
        for(String sid:sids){
            stuAnn.setSaid(sid+aid);
            stuAnn.setState("未读");
            stuAnn.setAid(aid);
            stuAnn.setSid(sid);
            stuAnnMapper.insert(stuAnn);
        }
    }

    @PutMapping("/stuann/{said}")
    @Operation(summary = "实现修改已读状态的接口")
    public StuAnn updateUser(@PathVariable("said") String id) {
        StuAnn existingUser = stuAnnMapper.selectById(id);
        if (existingUser != null) {
            if(existingUser.getState().equals("未读")){
                existingUser.setState("已读");
                stuAnnMapper.updateById(existingUser);
                return existingUser;
            }else {
                existingUser.setState("未读");
                stuAnnMapper.updateById(existingUser);
                return existingUser;
            }
        } else {
            return null;
        }
    }
    @GetMapping("/stuann/{sid}/{state}")
    @Operation(summary = "实现获取自己学号公告记录的接口")
    public List<String> getUsers(@PathVariable String sid,@PathVariable String state){
        List<String> list1 =new ArrayList<>();
        QueryWrapper<StuAnn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid",sid);
        queryWrapper.eq("state",state);
        List<StuAnn> list2 =stuAnnMapper.selectList(queryWrapper);
        for (StuAnn stuAnn : list2) {
            list1.add(stuAnn.getAid());
        }
        return list1;
    }
    @GetMapping("/stuann/findByPage")
    @Operation(summary = "实现学号公告记录分页的接口")
    public IPage getUserList(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize) {
        Page<StuAnn> page = new Page<>(pageNum, pageSize);
        QueryWrapper<StuAnn> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("said"); // 根据id字段降序排序
        page.addOrder(OrderItem.desc("said")); // 添加降序排序条件
        IPage ipage = stuAnnMapper.selectPage(page,null);
        return ipage;
    }
}
