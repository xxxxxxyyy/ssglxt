package com.example.sushehaoduan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;



@TableName("visitor")
@Data
public class Visitor {
    @TableId(value = "vid", type = IdType.AUTO)
    // 访客ID
    private String vid;
    // 学生ID（外键）
    private String sid;
    // 访客姓名
    private String name;
    // 访问时间
    private Date date;
    // 访客关系
    private String ties;
    // 访客目的
    private String aim;

    // 构造方法、getter和setter等省略
}

