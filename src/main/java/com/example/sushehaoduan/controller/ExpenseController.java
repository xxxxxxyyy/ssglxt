package com.example.sushehaoduan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sushehaoduan.mapper.AnnouncementMapper;
import com.example.sushehaoduan.mapper.DormitoryMapper;
import com.example.sushehaoduan.mapper.ExpenseMapper;
import com.example.sushehaoduan.mapper.StudentMapper;
import com.example.sushehaoduan.pojo.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@CrossOrigin
public class ExpenseController {
    @Autowired
    ExpenseMapper expenseMapper;
    @Autowired
    DormitoryMapper dormitoryMapper;
    @Autowired
    StudentMapper studentMapper;
    @GetMapping("/expense")
    @Operation(summary = "实现获取缴费记录列表的接口")
    public List<Expense> getUsers(){
        return expenseMapper.selectList(null);
    }
    @PostMapping("/expense")
    @Operation(summary = "实现添加缴费记录的接口")
    public Expense createUser(@RequestBody Expense user) {
        Student student=studentMapper.selectById(user.getSid());
        Dormitory dormitory=dormitoryMapper.selectById(student.getDid());
        System.out.println(user.getSum()+dormitory.getCost());
        dormitory.setCost(user.getSum()+dormitory.getCost());
        dormitoryMapper.updateById(dormitory);
        expenseMapper.insert(user);
        return user;
    }
    @PutMapping("/expense/{eid}")
    @Operation(summary = "实现修改缴费记录的接口")
    public Expense updateUser(@PathVariable("eid") String id, @RequestBody Expense user) {
        Expense existingUser = expenseMapper.selectById(id);
        Student student=studentMapper.selectById(user.getSid());
        Dormitory dormitory=dormitoryMapper.selectById(student.getDid());
        if (existingUser != null) {
            double cost0=dormitory.getCost();
            double cost1=existingUser.getSum();
            double cost2=cost0-cost1+user.getSum();
            user.setEid(id);
            expenseMapper.updateById(user);
            dormitory.setCost(cost2);
            dormitoryMapper.updateById(dormitory);
            System.out.println(cost2);
            return user;
        } else {
            return null;
        }
    }
    @DeleteMapping("/expense/{eid}")
    @Operation(summary = "实现删除缴费记录的接口")
    public int delUser(@PathVariable("eid") String id) {

        Expense expense=expenseMapper.selectById(id);
        Student student=studentMapper.selectById(expense.getSid());
        Dormitory dormitory=dormitoryMapper.selectById(student.getDid());
        System.out.println(dormitory.getCost()-expense.getSum());
        dormitory.setCost(dormitory.getCost()-expense.getSum());
        dormitoryMapper.updateById(dormitory);
        expense.setSum(-1*expense.getSum());
        expense.setState(1);
        expenseMapper.updateById(expense);
        return 0;
    }
    @DeleteMapping("/expenses")
    @Operation(summary = "实现批量删除缴费记录的接口")
    public int delUsers(@RequestBody List<String> ids) {
        /*int deletedCount = 0;*/
        for (String id : ids) {
            delUser(id);
            /*deletedCount += expenseMapper.deleteById(id);*/
        }
        return 0;
    }
    @GetMapping("/expenseByName")
    @Operation(summary = "全字段模糊查询费用")
    public IPage findByKeyword(@RequestParam("keyword") String keyword,
                               @RequestParam("pageNum") Integer pageNum,
                               @RequestParam("pageSize") Integer pageSize) {
        QueryWrapper<Expense> queryWrapper = new QueryWrapper<>();
        Page<Expense> page = new Page<>(pageNum, pageSize);

        queryWrapper.like("eid", keyword)
                .or()
                .like("sid", keyword)
                .or()
                .like("type", keyword)
                .or()
                .like("date", keyword)
                .or()
                .like("sum", keyword)
                .orderByAsc("eid");

        page.addOrder(OrderItem.asc("eid")); // 添加升序条件

        IPage ipage = expenseMapper.selectPage(page, queryWrapper);
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
    @GetMapping("/expense/findByPage")
    @Operation(summary = "实现缴费分页的接口")
    public IPage getUserList(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize) {
        Page<Expense> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Expense> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("eid"); // 根据id字段降序排序
        page.addOrder(OrderItem.desc("eid")); // 添加降序排序条件
        IPage ipage = expenseMapper.selectPage(page,null);
        return ipage;
    }

    @GetMapping("/expense/findByPage1/{sid}")
    @Operation(summary = "实现个人缴费分页的接口")
    public IPage getUserList1(@PathVariable String sid,
                              @RequestParam("pageNum") Integer pageNum,
                              @RequestParam("pageSize") Integer pageSize) {
        System.out.println(sid);
        System.out.println("ssdfsdffs");
        Page<Expense> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Expense> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sid",sid);
        queryWrapper.orderByDesc("eid"); // 根据id字段降序排序
        page.addOrder(OrderItem.desc("eid")); // 添加降序排序条件
        IPage ipage = expenseMapper.selectPage(page,queryWrapper);
        return ipage;
    }

    @GetMapping("/generateEid")
    @Operation(summary = "返回新的eid")
    public String generateEid() {
        // 获取当前日期和时间
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String neid;
        while(true){
            // 创建一个Random对象
            Random random = new Random();
            // 生成一个3位随机数
            int randomNumber = random.nextInt(900) + 100; // 生成[0, 899]之间的随机数，然后加上100
            String newEid = dateFormat.format(currentDate) + randomNumber;
            if(expenseMapper.selectById(newEid)==null){
                neid=newEid;
                break;
            }
        }
        // 根据需求自定义生成新eid的逻辑
        // 例如，将格式化后的日期与唯一计数器连接起来
        return neid;
    }
    @GetMapping("/expense/{year}")
    @Operation(summary = "每个月份男女缴费金额")
    public Map<String, List<Double>> getMonthlyExpense(@PathVariable("year") String year) {
        QueryWrapper<Expense> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("sum",0);
        queryWrapper.apply("DATE_FORMAT(date, '%Y') = {0}", year);
        List<Expense> expenseList = expenseMapper.selectList(queryWrapper);

        Map<Integer, List<Double>> maleMonthlyExpenses = new HashMap<>();
        Map<Integer, List<Double>> femaleMonthlyExpenses = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            maleMonthlyExpenses.put(month, new ArrayList<>());
            femaleMonthlyExpenses.put(month, new ArrayList<>());
        }

        for (Expense expense : expenseList) {
            LocalDateTime dateTime = expense.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            int month = dateTime.getMonthValue();

            if (dateTime.getYear() == Integer.parseInt(year)) {
                String sex = studentMapper.selectById(expense.getSid()).getSex();
                Map<Integer, List<Double>> monthlyExpenses = (sex.equals("男")) ? maleMonthlyExpenses : femaleMonthlyExpenses;
                monthlyExpenses.get(month).add(expense.getSum());
            }
        }

        Map<String, List<Double>> result = new HashMap<>();
        result.put("male", getMonthlyExpensesList(maleMonthlyExpenses));
        result.put("female", getMonthlyExpensesList(femaleMonthlyExpenses));

        return result;
    }

    private List<Double> getMonthlyExpensesList(Map<Integer, List<Double>> monthlyExpenses) {
        List<Double> monthlyExpensesList = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            List<Double> sums = monthlyExpenses.get(month);
            double totalSum = (sums != null) ? sums.stream().mapToDouble(Double::doubleValue).sum() : 0.0;
            monthlyExpensesList.add(totalSum);
        }

        return monthlyExpensesList;
    }


}
