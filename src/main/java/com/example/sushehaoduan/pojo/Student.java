package com.example.sushehaoduan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("student")
@Data
public class Student {
    @TableId(value = "sid", type = IdType.AUTO)
    // 学生ID
    private String sid;
    // 姓名
    private String name;
    // 联系方式
    private String tel;
    // 性别
    private String sex;
    // 入学年份
    private String date;
    // 入住状态
    private boolean state;
    // 宿舍ID
    private String did;
    // 床位ID
  /*  private String bed;*/
    // 学院
    private String college;
}