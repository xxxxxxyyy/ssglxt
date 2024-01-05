package com.example.sushehaoduan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sushehaoduan.mapper.DormitoryMapper;
import com.example.sushehaoduan.mapper.EquipmentMaintenanceMapper;
import com.example.sushehaoduan.pojo.Announcement;
import com.example.sushehaoduan.pojo.Dormitory;
import com.example.sushehaoduan.pojo.EquipmentMaintenance;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class EquipmentMaintenanceController {
    @Autowired
    EquipmentMaintenanceMapper equipmentMaintenanceMapper;
    @Autowired
    DormitoryMapper dormitoryMapper;
    @GetMapping("/equipmentMaintenanceByKeyword")
    @Operation(summary = "全字段模糊查询设备维护记录")
    public IPage findByKeyword(@RequestParam("keyword") String keyword,
                               @RequestParam("pageNum") Integer pageNum,
                               @RequestParam("pageSize") Integer pageSize) {
        QueryWrapper<EquipmentMaintenance> queryWrapper = new QueryWrapper<>();
        Page<EquipmentMaintenance> page = new Page<>(pageNum, pageSize);

        queryWrapper.like("emid", keyword)
                .or()
                .like("did", keyword)
                .or()
                .like("date", keyword)
                .or()
                .like("sid", keyword)
                .or()
                .like("test", keyword)
                .or()
                .like("status", keyword)
                .orderByAsc("emid");

        page.addOrder(OrderItem.asc("emid")); // 添加升序条件
        IPage ipage = equipmentMaintenanceMapper.selectPage(page, queryWrapper);
        System.out.println(pageSize);
        return ipage;
    }
    @GetMapping("/equipmentMaintenances")
    @Operation(summary = "获取设备维护记录列表的接口")
    public List<EquipmentMaintenance> getEquipmentMaintenances() {
        return equipmentMaintenanceMapper.selectList(null);
    }
    @GetMapping("/equipmentMaintenance/findByPage")
    @Operation(summary = "实现维修记录分页的接口")
    public IPage getUserList(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize) {
        Page<EquipmentMaintenance> page = new Page<>(pageNum, pageSize);
        QueryWrapper<EquipmentMaintenance> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("emid"); // 根据id字段降序排序
        page.addOrder(OrderItem.desc("emid")); // 添加降序排序条件
        IPage ipage = equipmentMaintenanceMapper.selectPage(page,null);
        return ipage;
    }
    @PostMapping("/equipmentMaintenances")
    @Operation(summary = "添加设备维护记录的接口")
    public EquipmentMaintenance createEquipmentMaintenance(@RequestBody EquipmentMaintenance equipmentMaintenance) {
        Dormitory dormitory = dormitoryMapper.selectById(equipmentMaintenance.getDid());
        // 处理设备维护记录对宿舍信息的影响，类似于之前的处理方式
        dormitory.setState(equipmentMaintenance.getTest());
        dormitoryMapper.updateById(dormitory);
        equipmentMaintenanceMapper.insert(equipmentMaintenance);
        return equipmentMaintenance;
    }

    @PutMapping("/equipmentMaintenances/{emid}")
    @Operation(summary = "修改设备维护记录的接口")
    public EquipmentMaintenance updateEquipmentMaintenance(@PathVariable("emid") String id, @RequestBody EquipmentMaintenance equipmentMaintenance) {
        EquipmentMaintenance existingEquipmentMaintenance = equipmentMaintenanceMapper.selectById(id);
        if (existingEquipmentMaintenance != null) {
            equipmentMaintenance.setEmid(id);
            equipmentMaintenanceMapper.updateById(equipmentMaintenance);
            return equipmentMaintenance;
        } else {
            return null;
        }
    }

    @DeleteMapping("/equipmentMaintenances/{emid}")
    @Operation(summary = "删除设备维护记录的接口")
    public int deleteEquipmentMaintenance(@PathVariable("emid") String id) {
        // 处理删除设备维护记录对宿舍信息的影响，类似于之前的处理方式

        return equipmentMaintenanceMapper.deleteById(id);
    }

}
