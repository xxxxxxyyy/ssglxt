package com.example.sushehaoduan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("equipmentMaintenance")
@Data
public class EquipmentMaintenance {
    @TableId(value = "emid", type = IdType.AUTO)
    // 记录ID
    private String emid;
    // 宿舍ID（外键）
    private String did;
    // 报修时间
    private Date date;
    // 报修人员ID（学生ID）
    private String sid;
    // 报修内容
    private String test;
    // 报修处理状态
    private String status;

    // 构造方法、getter和setter等省略
}

