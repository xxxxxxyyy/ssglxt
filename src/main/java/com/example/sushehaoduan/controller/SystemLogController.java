package com.example.sushehaoduan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sushehaoduan.mapper.SystemLogMapper;
import com.example.sushehaoduan.pojo.Announcement;
import com.example.sushehaoduan.pojo.SystemLog;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class SystemLogController {
    @Autowired
    SystemLogMapper systemLogMapper;
    @GetMapping("/systemLogByKeyword")
    @Operation(summary = "全字段模糊查询系统日志")
    public IPage findByKeyword(@RequestParam("keyword") String keyword,
                               @RequestParam("pageNum") Integer pageNum,
                               @RequestParam("pageSize") Integer pageSize) {
        QueryWrapper<SystemLog> queryWrapper = new QueryWrapper<>();
        Page<SystemLog> page = new Page<>(pageNum, pageSize);

        queryWrapper.like("lid", keyword)
                .or()
                .like("date", keyword)
                .or()
                .like("uid", keyword)
                .or()
                .like("content", keyword)
                .orderByAsc("lid");

        page.addOrder(OrderItem.asc("lid")); // 添加升序条件

        IPage ipage = systemLogMapper.selectPage(page, queryWrapper);
        System.out.println(pageSize);
        return ipage;
    }
    @DeleteMapping("/systemLogs/{lid}")
    @Operation(summary = "删除系统日志的接口")
    public int deleteSystemLog(@PathVariable("lid") String id) {
        return systemLogMapper.deleteById(id);
    }
    @PutMapping("/systemLogs/{lid}")
    @Operation(summary = "修改系统日志的接口")
    public SystemLog updateSystemLog(@PathVariable("lid") String id, @RequestBody SystemLog systemLog) {
        SystemLog existingSystemLog = systemLogMapper.selectById(id);
        if (existingSystemLog != null) {
            systemLog.setLid(id);
            systemLogMapper.updateById(systemLog);
            return systemLog;
        } else {
            return null;
        }
    }
    @PostMapping("/systemLogs")
    @Operation(summary = "添加系统日志的接口")
    public SystemLog createSystemLog(@RequestBody SystemLog systemLog) {
        systemLogMapper.insert(systemLog);
        return systemLog;
    }
    @GetMapping("/systemLogs")
    @Operation(summary = "获取系统日志列表的接口")
    public List<SystemLog> getSystemLogs() {
        return systemLogMapper.selectList(null);
    }
    @GetMapping("/systemLog/findByPage")
    @Operation(summary = "实现日志分页的接口")
    public IPage getUserList(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize) {
        Page<SystemLog> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("lid"); // 根据id字段降序排序
        page.addOrder(OrderItem.desc("lid")); // 添加降序排序条件
        IPage ipage = systemLogMapper.selectPage(page,null);
        return ipage;
    }
}
