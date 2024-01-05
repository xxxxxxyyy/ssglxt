package com.example.sushehaoduan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sushehaoduan.mapper.DormitoryMapper;
import com.example.sushehaoduan.mapper.StudentMapper;
import com.example.sushehaoduan.mapper.UserMapper;
import com.example.sushehaoduan.pojo.Dormitory;
import com.example.sushehaoduan.pojo.Student;
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
public class StudentController {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    DormitoryMapper dormitoryMapper;
    @GetMapping("/student")
    @Operation(summary = "实现获取学生列表的接口")
    public List<Student> getUsers(){
        return studentMapper.selectList(null);
    }

    @GetMapping("/studentsid")
    @Operation(summary = "实现获取学生列表的接口")
    public List<String> getSids(){
        List<String> list1 = new ArrayList<>();
        List<Student> list2 = studentMapper.selectList(null);
        for(Student s:list2){
            list1.add(s.getSid());
        }
        return list1;
    }


    @PostMapping("/student")
    @Operation(summary = "实现添加学生的接口")
    public Student createUser(@RequestBody Student user) {
        studentMapper.insert(user);
        return user;
    }
    @GetMapping("/student/message/{sid}")
    @Operation(summary = "实现查找学生的接口")
    public Student selectUser(@PathVariable String sid) {
        return studentMapper.selectById(sid);
    }

    @PutMapping("/student/{sid}")
    @Operation(summary = "实现修改学生的接口")
    public Student updateUser(@PathVariable("sid") String id, @RequestBody Student user) {
        Student existingUser = studentMapper.selectById(id);
        if (existingUser != null) {
            user.setSid(id);
            studentMapper.updateById(user);
            return user;
        } else {
            return null;
        }
    }
    @GetMapping("/studentyear")
    @Operation(summary = "实现返回所有学年")
    public List<String> backyear() {
        List<Student> dormitoryList=studentMapper.selectList(null);
        List<String> lclist =new ArrayList<>();
        for (Student dormitory : dormitoryList) {
            if( !lclist.contains(dormitory.getDate())){
                lclist.add(dormitory.getDate());
            }
        }
        return lclist;
    }
    @GetMapping("/student/{year}")
    @Operation(summary = "实现返回人数")
    public List<Integer> backl(@PathVariable("year") String year) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date",year);
        List<Student> dormitoryList=studentMapper.selectList(queryWrapper);
        List<Integer> lclist =new ArrayList<>();
        int i = 0;
        for (Student student : dormitoryList) {
            if(student.getDate().equals(year)&&student.getSex().equals("男")){
                i++;
            }
        }
        lclist.add(dormitoryList.size());//总人数
        lclist.add(i);//男生
        lclist.add(dormitoryList.size()-i);//女生
        return lclist;
    }

    @PutMapping("/studentfp")
    @Operation(summary = "实现给单个学生分配宿舍的接口")
    public Student updateUserFP(@RequestParam("sid") String sid,
                                @RequestParam("did") String did) {
        System.out.println("siddddd");
        System.out.println(sid);
        System.out.println(did);
        Student existingUser = studentMapper.selectById(sid);
        Dormitory dormitory=dormitoryMapper.selectById(did);

        if (existingUser != null) {
            if(existingUser.isState()){
                Dormitory dormitory1=dormitoryMapper.selectById(existingUser.getDid());
                dormitory1.setCount(dormitory1.getCount()-1);
                dormitoryMapper.updateById(dormitory1);
                existingUser.setDid(did);
                studentMapper.updateById(existingUser);
                return existingUser;
            }else {
                existingUser.setDid(did);
                existingUser.setState(true);
                studentMapper.updateById(existingUser);
                dormitory.setCount(dormitory.getCount()+1);
                dormitoryMapper.updateById(dormitory);
                return existingUser;
            }
        } else {
            return null;
        }
    }
    @DeleteMapping("/student/{sid}")
    @Operation(summary = "实现删除学生的接口,入住状态清空")
    public boolean delStudent(@PathVariable("sid") String id) {

        /*Dormitory dormitory=dormitoryMapper.selectById(studentMapper.selectById(id).getDid());
        dormitory.setCount(dormitory.getCount()-1);*/

        Dormitory dormitory1=dormitoryMapper.selectById(studentMapper.selectById(id).getDid());
        Student student1=studentMapper.selectById(id);
        dormitory1.setCount(dormitory1.getCount()-1);
        student1.setDid("00000001");
        student1.setState(false);
        studentMapper.updateById(student1);
        dormitoryMapper.updateById(dormitory1);
        return true;
    }
    @DeleteMapping("/students")
    @Operation(summary = "实现批量删除学生的接口")
    public int delUsers(@RequestBody List<String> values) {
        System.out.println(values);
        System.out.println("values");
        for (String id : values) {
            delStudent(id);
        }
        return 0;
    }
    /* @GetMapping("/studentByName/{name}")
     @Operation(summary = "模糊查询姓名")
     public List<Student> findByName(@PathVariable String name) {
         QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
         queryWrapper.like("name", name);
         return studentMapper.selectList(queryWrapper);
     }*/

    @GetMapping("/studentByKeyword")
    @Operation(summary = "全字段模糊查询")
    public IPage findByKeyword(@RequestParam("keyword") String keyword,
                                       @RequestParam("pageNum") Integer pageNum,
                                       @RequestParam("pageSize") Integer pageSize) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();

        Page<Student> page = new Page<>(pageNum, pageSize);
        queryWrapper.and(qr -> qr.eq("state", true)
                .and(wrapper -> wrapper.like("name", keyword)
                        .or()
                        .like("sid", keyword)
                        .or()
                        .like("tel", keyword)
                        .or()
                        .like("sex", keyword)
                        .or()
                        .like("date", keyword)
                        .or()
                        .like("college", keyword)
                )
        );
/*        queryWrapper.eq("state", true)
                .orderByAsc("sid"); // 根据id字段升序排序*/
        page.addOrder(OrderItem.asc("sid")); // 添加升序条件
        IPage ipage = studentMapper.selectPage(page,queryWrapper);
        return ipage;
        /*return studentMapper.selectList(queryWrapper);*/
    }

    @GetMapping("/studentByKeyword1")
    @Operation(summary = "学生未分配宿舍全字段模糊查询")
    public IPage findByKeyword1(@RequestParam("keyword") String keyword,
                               @RequestParam("pageNum") Integer pageNum,
                               @RequestParam("pageSize") Integer pageSize) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();

        Page<Student> page = new Page<>(pageNum, pageSize);
        queryWrapper.and(qr -> qr.eq("state", false)
                .and(wrapper -> wrapper.like("name", keyword)
                        .or()
                        .like("sid", keyword)
                        .or()
                        .like("tel", keyword)
                        .or()
                        .like("sex", keyword)
                        .or()
                        .like("date", keyword)
                        .or()
                        .like("college", keyword)
                )
        );

        queryWrapper.orderByAsc("sid"); // 根据id字段升序排序
        page.addOrder(OrderItem.asc("sid")); // 添加升序条件
        IPage ipage = studentMapper.selectPage(page,queryWrapper);
        return ipage;
        /*return studentMapper.selectList(queryWrapper);*/
    }

    @GetMapping("/student/findByPage")
    @Operation(summary = "实现学生分页的接口")
    public IPage getUserList(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize) {
        Page<Student> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", true)
                .orderByAsc("sid"); // 根据id字段升序排序
        page.addOrder(OrderItem.asc("sid")); // 添加升序条件
        IPage ipage = studentMapper.selectPage(page,queryWrapper);
        return ipage;
    }

    @GetMapping("/student/findByPage1")
    @Operation(summary = "实现学生分页的接口")
    public IPage getUserList1(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize) {
        Page<Student> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", false)
                .orderByAsc("sid"); // 根据id字段升序排序
        page.addOrder(OrderItem.asc("sid")); // 添加升序条件
        IPage ipage = studentMapper.selectPage(page,queryWrapper);
        return ipage;
    }

    @PostMapping("/student/check")
    public boolean check(@RequestParam("sid") int nid) {
        // 根据用户名从数据库中查询用户是否存在
        Student personnel1 = studentMapper.selectById(nid);
        return personnel1 != null;
    }

    @GetMapping("/generateSid/{year}")
    @Operation(summary = "返回新的sid")
    public String generateSid(@PathVariable String year) {
        String sid;
        while(true){
            // 创建一个Random对象
            Random random = new Random();
            // 生成一个3位随机数
            int randomNumber = random.nextInt(10000); // 生成[0, 899]之间的随机数，然后加上100
            String newAid = year + String.format("%04d",randomNumber);
            if(studentMapper.selectById(newAid)==null){
                sid=newAid;
                break;
            }
        }
        // 根据需求自定义生成新eid的逻辑
        // 例如，将格式化后的日期与唯一计数器连接起来
        return sid;
    }

   /* @DeleteMapping("/delstudent/{id}")
    @Operation(summary = "实现删除任务分配的接口")
    public int delPerson(@PathVariable("id") int id) {
        Tasks tasks = tasksMapper.selectById(personMapper.selectById(id).getTaskid());
        Personnel person=personMapper1.selectById(personMapper.selectById(id).getAssigneeid());
        if (tasks != null) {
            String assignee = tasks.getAssignee().replace(personMapper.selectById(id).getAssigneename(), "");
            //tasksMapper.selectById(personMapper.selectById(id).getTaskid()).getName();
            String assignee2 = person.getTasks().replace(tasksMapper.selectById(personMapper.selectById(id).getTaskid()).getName(), "");
            tasks.setAssignee(assignee.trim());
            person.setTasks(assignee2.trim());
            tasksMapper.updateById(tasks);
            personMapper1.updateById(person);
        }

        return personMapper.deleteById(id);
    }
    @DeleteMapping("/assignments")
    @Operation(summary = "实现批量删除任务分配的接口")
    public int delPersons(@RequestBody List<Integer> ids) {
        int deletedCount = 0;
        for (int id : ids) {
            Tasks tasks = tasksMapper.selectById(personMapper.selectById(id).getTaskid());
            Personnel person=personMapper1.selectById(personMapper.selectById(id).getAssigneeid());
            if (tasks != null) {
                String assignee = tasks.getAssignee().replace(personMapper.selectById(id).getAssigneename(), "");
                // tasksMapper.selectById(personMapper.selectById(id).getTaskid()).getName();
                String assignee2 = person.getTasks().replace(tasksMapper.selectById(personMapper.selectById(id).getTaskid()).getName(), "");
                tasks.setAssignee(assignee.trim());
                person.setTasks(assignee2.trim());
                tasksMapper.updateById(tasks);
                personMapper1.updateById(person);
            }
            deletedCount += personMapper.deleteById(id);
        }
        return deletedCount;
    }
    @Autowired
    TasksMapper tasksMapper;
    @GetMapping("/assignment/{taskid}")
    @Operation(summary = "通过任务ID获取另一个表数据的接口")
    public Tasks getTaskById(@PathVariable("taskid") Integer taskId) {
        TaskAssignments assignment = assignmentsMapper.selectOne(new QueryWrapper<TaskAssignments>().eq("taskid", taskId));
        if (assignment != null) {
            Integer assignedTaskId = assignment.getTaskid();
            if (assignedTaskId != null) {
                return tasksMapper.selectById(assignedTaskId);
            }
        }
        return null;
    }

    @PostMapping("/assignment")
    @Operation(summary = "实现添加任务分配的接口")
    public TaskAssignments  createUser(@RequestBody TaskAssignments taskAssignments) {
        taskAssignments.setAssigneename(personMapper1.selectById(taskAssignments.getAssigneeid()).getName());
        taskAssignments.setTaskname(tasksMapper.selectById(taskAssignments.getTaskid()).getName());
        Tasks tasks = tasksMapper.selectById(taskAssignments.getTaskid());
        Personnel personnel=personMapper1.selectById(taskAssignments.getAssigneeid());
        if (tasks != null) {
            personnel.setTasks(personnel.getTasks()+' '+tasksMapper.selectById(taskAssignments.getTaskid()).getName());
            tasks.setAssignee(tasks.getAssignee()+' '+personMapper1.selectById(taskAssignments.getAssigneeid()).getName());
            tasksMapper.updateById(tasks);
            personMapper1.updateById(personnel);
        }
        personMapper.insert(taskAssignments);
        return taskAssignments;
    }
*/
}
