package com.example.sushehaoduan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sushehaoduan.mapper.DormitoryMapper;
import com.example.sushehaoduan.mapper.StudentMapper;
import com.example.sushehaoduan.mapper.ViolationMapper;
import com.example.sushehaoduan.pojo.Dormitory;
import com.example.sushehaoduan.pojo.Student;
import com.example.sushehaoduan.pojo.Violation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@RestController
@CrossOrigin
public class ViolationController {
    @Autowired
    ViolationMapper violationMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    DormitoryMapper dormitoryMapper;
    @GetMapping("/violationByKeyword")
    @Operation(summary = "全字段模糊查询违纪记录")
    public IPage findByKeyword(@RequestParam("keyword") String keyword,
                               @RequestParam("pageNum") Integer pageNum,
                               @RequestParam("pageSize") Integer pageSize) {
        QueryWrapper<Violation> queryWrapper = new QueryWrapper<>();
        Page<Violation> page = new Page<>(pageNum, pageSize);

        queryWrapper.like("vid", keyword)
                .or()
                .like("sid", keyword)
                .or()
                .like("did", keyword)
                .or()
                .like("date", keyword)
                .or()
                .like("description", keyword)
                .or()
                .like("type", keyword)
                .orderByAsc("vid");

        page.addOrder(OrderItem.asc("vid")); // 添加升序条件

        IPage ipage = violationMapper.selectPage(page, queryWrapper);
        System.out.println(pageSize);
        return ipage;
    }
    @PostMapping("/violations")
    @Operation(summary = "添加违纪记录的接口")
    public Violation createViolation(@RequestBody Violation violation) {
        Student student = studentMapper.selectById(violation.getSid());
        Dormitory dormitory = dormitoryMapper.selectById(student.getDid());

        // 处理违纪记录对宿舍或学生信息的影响，类似于ExpenseController中的处理方式

        violationMapper.insert(violation);
        return violation;
    }

    @GetMapping("/violations")
    @Operation(summary = "获取违纪记录列表的接口")
    public List<Violation> getViolations() {
        return violationMapper.selectList(null);
    }
    @PutMapping("/violations/{vid}")
    @Operation(summary = "修改违纪记录的接口")
    public Violation updateViolation(@PathVariable("vid") String id, @RequestBody Violation violation) {
        Violation existingViolation = violationMapper.selectById(id);
        if (existingViolation != null) {
            violation.setVid(id);
            violationMapper.updateById(violation);
            return violation;
        } else {
            return null;
        }
    }

    @DeleteMapping("/violations/{vid}")
    @Operation(summary = "删除违纪记录的接口")
    public int deleteViolation(@PathVariable("vid") String id) {
        // 处理删除违纪记录对宿舍或学生信息的影响，类似于ExpenseController中的处理方式
        return violationMapper.deleteById(id);
    }

    @DeleteMapping("/violations")
    @Operation(summary = "实现批量删除违规记录的接口")
    public int delUsers(@RequestBody List<String> ids) {
        int deletedCount = 0;
        for (String id : ids) {
            deletedCount += violationMapper.deleteById(id);
        }
        return deletedCount;
    }
    @GetMapping("/violationvid")
    @Operation(summary = "返回新的违规vid")
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
            if(violationMapper.selectById(newVid)==null){
                nvid=newVid;
                break;
            }
        }
        // 根据需求自定义生成新eid的逻辑
        // 例如，将格式化后的日期与唯一计数器连接起来
        return nvid;
    }
    @GetMapping("/violation/findByPage")
    @Operation(summary = "实现违规记录分页的接口")
    public IPage getUserList(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize) {
        Page<Violation> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Violation> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("vid"); // 根据id字段升序排序
        page.addOrder(OrderItem.asc("vid")); // 添加升序条件
        IPage ipage = violationMapper.selectPage(page,queryWrapper);
        return ipage;
    }

    @GetMapping("/violation/findByPage1/{sid}")
    @Operation(summary = "实现个人违规记录分页的接口")
    public IPage getUserList1(@PathVariable String sid,
                                @RequestParam("pageNum") Integer pageNum,
                                @RequestParam("pageSize") Integer pageSize) {
        Page<Violation> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Violation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid",sid);
        queryWrapper.orderByAsc("vid"); // 根据id字段升序排序
        page.addOrder(OrderItem.asc("vid")); // 添加升序条件
        IPage ipage = violationMapper.selectPage(page,queryWrapper);
        return ipage;
    }
    @GetMapping("/violation/{year}")
    @Operation(summary = "实现返回违规数量")
    public Map<String, List<Integer>> backl(@PathVariable("year") String year) {
        QueryWrapper<Violation> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("DATE_FORMAT(date, '%Y') = {0}", year);
        List<Violation> violationList = violationMapper.selectList(queryWrapper);

        Map<Integer, List<Integer>> maleMonthlyCounts = new HashMap<>();
        Map<Integer, List<Integer>> femaleMonthlyCounts = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            maleMonthlyCounts.put(month, new ArrayList<>());
            femaleMonthlyCounts.put(month, new ArrayList<>());
        }

        for (Violation violation : violationList) {
            LocalDateTime dateTime = violation.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            int month = dateTime.getMonthValue();

            if (dateTime.getYear() == Integer.parseInt(year)) {
                String sex = studentMapper.selectById(violation.getSid()).getSex();
                Map<Integer, List<Integer>> monthlyCounts = (sex.equals("男")) ? maleMonthlyCounts : femaleMonthlyCounts;

                monthlyCounts.get(month).add(1);
            }
        }

        Map<String, List<Integer>> result = new HashMap<>();
        result.put("male", getMonthlyCountsList(maleMonthlyCounts));
        result.put("female", getMonthlyCountsList(femaleMonthlyCounts));

        return result;
    }

    private List<Integer> getMonthlyCountsList(Map<Integer, List<Integer>> monthlyCounts) {
        List<Integer> monthlyCountsList = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            List<Integer> counts = monthlyCounts.get(month);
            int totalCount = (counts != null) ? counts.size() : 0;
            monthlyCountsList.add(totalCount);
        }

        return monthlyCountsList;
    }

}
