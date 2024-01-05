package com.example.sushehaoduan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sushehaoduan.mapper.DormitoryMapper;
import com.example.sushehaoduan.mapper.UserMapper;
import com.example.sushehaoduan.pojo.Dormitory;
import com.example.sushehaoduan.pojo.Student;
import com.example.sushehaoduan.pojo.User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
@CrossOrigin
public class DormitoryController {
    @Autowired
    DormitoryMapper dormitoryMapper;
    @GetMapping("/dormitory")
    @Operation(summary = "实现获取宿舍列表的接口")
    public List<Dormitory> getUsers(){
        return dormitoryMapper.selectList(null);
    }
    @PostMapping("/dormitory")
    @Operation(summary = "实现添加宿舍的接口")
    public Dormitory createUser(@RequestBody Dormitory dormitory) {
        dormitoryMapper.insert(dormitory);
        return dormitory;
    }
    @GetMapping("/dormitory/message/{did}")
    @Operation(summary = "实现查找宿舍的接口")
    public Dormitory selectUser(@PathVariable String did) {
        return dormitoryMapper.selectById(did);
    }
    @GetMapping("/dormitorylc/{sex}")
    @Operation(summary = "实现返回性别特定的楼")
    public List<String> backl(@PathVariable("sex") String sex) {
        System.out.println("sex");
        System.out.println(sex);
        QueryWrapper<Dormitory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sex",sex);
        List<Dormitory> dormitoryList=dormitoryMapper.selectList(queryWrapper);
        List<String> lclist =new ArrayList<>();
        for (Dormitory dormitory : dormitoryList) {
            if(dormitory.getSex().equals(sex)&& !lclist.contains(dormitory.getBuilding())){
                lclist.add(dormitory.getBuilding());
            }
        }
        return lclist;
    }

    @GetMapping("/dormitorylc1/{building}")
    @Operation(summary = "实现返回楼的所有单元的接口")
    public List<String> backlc(@PathVariable String building) {
        QueryWrapper<Dormitory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("building",building);
        List<Dormitory> dormitoryList=dormitoryMapper.selectList(queryWrapper);
        List<String> lclist =new ArrayList<>();
        for (Dormitory dormitory : dormitoryList) {
            if(dormitory.getBuilding().equals(building)&& !lclist.contains(dormitory.getUnit())){
                lclist.add(dormitory.getUnit());
            }
        }
        return lclist;
    }

    @GetMapping("/dormitorylc2")
    @Operation(summary = "实现返回楼的单元的楼层接口")
    public List<String> backlcr(@RequestParam("building") String building,
                                @RequestParam("unit") String unit) {
        QueryWrapper<Dormitory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("building",building);
        List<Dormitory> dormitoryList=dormitoryMapper.selectList(queryWrapper);
        List<String> lclist =new ArrayList<>();
        for (Dormitory dormitory : dormitoryList) {
            if(dormitory.getBuilding().equals(building)&&dormitory.getUnit().equals(unit)&& !lclist.contains(dormitory.getFloor())){
                lclist.add(dormitory.getFloor());
            }
        }
        return lclist;
    }
    @GetMapping("/dormitorylc3")
    @Operation(summary = "实现返回楼的单元的楼层的房间的接口")
    public List<String> backlcrf(@RequestParam("building") String building,
                                @RequestParam("unit") String unit,
                                @RequestParam("floor") String floor) {
        QueryWrapper<Dormitory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("building",building);
        List<Dormitory> dormitoryList=dormitoryMapper.selectList(queryWrapper);
        List<String> lclist =new ArrayList<>();
        for (Dormitory dormitory : dormitoryList) {
            if(dormitory.getBuilding().equals(building)&&dormitory.getUnit().equals(unit)&&dormitory.getFloor().equals(floor)&& !lclist.contains(dormitory.getRoom())){
                lclist.add(dormitory.getRoom());
            }
        }
        return lclist;
    }
    @PutMapping("/dormitory/{id}")
    @Operation(summary = "实现修改宿舍的接口")
    public Dormitory updateUser(@PathVariable("id") String id, @RequestBody Dormitory user) {
        Dormitory existingUser = dormitoryMapper.selectById(id);
        if (existingUser != null) {
            user.setDid(id);
            dormitoryMapper.updateById(user);
            return user;
        } else {
            return null;
        }
    }
    @DeleteMapping("/dormitory/{id}")
    @Operation(summary = "实现删除宿舍的接口")
    public int delUser(@PathVariable("id") String id) {
        return dormitoryMapper.deleteById(id);
    }
    @DeleteMapping("/dormitorys")
    @Operation(summary = "实现批量删除宿舍的接口")
    public int delUsers(@RequestBody List<Integer> ids) {
        int deletedCount = 0;
        for (int id : ids) {
            deletedCount += dormitoryMapper.deleteById(id);
        }
        return deletedCount;
    }
     /*@GetMapping("/dormitoryByName/{name}")
     @Operation(summary = "模糊查询宿舍号")
     public List<Dormitory> findByName(@PathVariable String name) {
         QueryWrapper<Dormitory> queryWrapper = new QueryWrapper<>();
         queryWrapper.like("did", name);
         return dormitoryMapper.selectList(queryWrapper);
     }*/
     @GetMapping("/dormitoryByName")
     @Operation(summary = "全字段模糊查询宿舍号")
     public IPage findByName(@RequestParam("keyword") String keyword,
                             @RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize) {
         QueryWrapper<Dormitory> queryWrapper = new QueryWrapper<>();
         Page<Dormitory> page = new Page<>(pageNum, pageSize);
         queryWrapper.like("did", keyword)
                 .or()
                 .like("building", keyword)
                 .or()
                 .like("unit", keyword)
                 .or()
                 .like("floor", keyword)
                 .or()
                 .like("room", keyword)
                 .or()
                 .like("number", keyword)
                 .or()
                 .like("count", keyword)
                 .or()
                 .like("state", keyword)
                 .or()
                 .like("cost", keyword)
                 .or()
                 .like("sex", keyword)
                 .orderByAsc("did");

         page.addOrder(OrderItem.asc("did")); // 添加升序条件
         IPage ipage = dormitoryMapper.selectPage(page,queryWrapper);
         System.out.println(pageSize);
         return ipage;
         // 添加其他字段的模糊查询条件
         /*return dormitoryMapper.selectList(queryWrapper);*/
     }

    @GetMapping("/dormitory/findByPage")
    @Operation(summary = "实现宿舍分页的接口")
    public IPage getUserList(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize) {
        Page<Dormitory> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Dormitory> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("did", 00000001);
        queryWrapper.orderByDesc("did"); // 根据id字段降序排序
        page.addOrder(OrderItem.desc("did")); // 添加降序排序条件
        IPage ipage = dormitoryMapper.selectPage(page,queryWrapper);
        return ipage;
    }
    @GetMapping("/dormitory/findByPage1/{sex}")
    @Operation(summary = "实现获得一个宿舍id的接口")
    public String getDormitoryWithLowOccupancy(@PathVariable String sex) {
        // 构建查询条件，假设 Dormitory 实体类中有 beds 和 occupants 字段
        QueryWrapper<Dormitory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sex",sex);
        queryWrapper.lt("count", 6); // 查询人数小于床位的宿舍
        // 根据id字段降序排序
        queryWrapper.orderByDesc("did");
        // 查询符合条件的第一个宿舍
        List<Dormitory> dormitorylist = dormitoryMapper.selectList(queryWrapper);
        Dormitory firstDormitory;
     // 检查列表是否非空
        if (!dormitorylist.isEmpty()) {
            // 获取列表中的第一个元素
             firstDormitory = dormitorylist.get(0);

            // 这里可以使用 firstDormitory 进行进一步的处理
        } else {
            // 如果列表为空，可以进行相应的处理，例如返回默认值或抛出异常
            // 这里假设默认值为 null
             firstDormitory = null;
        }
        // 返回宿舍ID，如果没有符合条件的宿舍，则返回空字符串
        System.out.println(firstDormitory.getDid());
        return (firstDormitory != null) ? String.valueOf(firstDormitory.getDid()) : "";
    }
}
