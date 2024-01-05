package com.example.sushehaoduan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


@TableName("violation")
@Data
public class Violation {
    @TableId(value = "vid", type = IdType.AUTO)
    // 记录ID
    private String vid;
    // 学生ID（外键）
    private String sid;
    // 宿舍ID（外键）
    private String did;
    // 违规时间
    private Date date;
    // 违规行为描述
    private String description;
    // 处罚方式
    private String type;
    // 用户状态
    private int state;

    // 构造方法、getter和setter等省略
}

