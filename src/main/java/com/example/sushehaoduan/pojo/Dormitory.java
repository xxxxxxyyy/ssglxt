package com.example.sushehaoduan.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("dormitory")
@Data
public class Dormitory {
    @TableId(value = "did", type = IdType.AUTO)
    private String did;
    private String building;
    private String unit;
    private String floor;
    private String room;
    private int number;
    private int count;
    private String state;
    private double cost;
    private String sex;

    // 构造方法、getter和setter等省略
}
