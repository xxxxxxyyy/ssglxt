package com.example.sushehaoduan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@TableName("expense")
@Data
public class Expense {
    @TableId(value = "eid", type = IdType.AUTO)
    // 费用ID
    private String eid;
    // 学生ID（外键）
    private String sid;
    // 费用类型
    private String type;
    // 缴费时间
    private Date date;
    // 缴费金额
    private double sum;
    // 人员状态
    private int state;
    // 构造方法、getter和setter等省略
}

