package com.example.sushehaoduan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("systemlog")
@Data
public class SystemLog {
    @TableId(value = "lid", type = IdType.AUTO)
    // 日志ID
    private String lid;
    // 操作时间
    private String date;
    // 操作用户ID（外键）
    private String uid;
    // 操作内容
    private String content;

    // 构造方法、getter和setter等省略
}

