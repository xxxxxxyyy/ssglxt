package com.example.sushehaoduan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
@TableName("announcement")
@Data
public class Announcement {
    @TableId(value = "aid", type = IdType.AUTO)
    // 公告ID
    private String aid;
    // 管理员ID（外键）
    private String mid;
    // 发布时间
    private String date;
    // 公告内容
    private String content;

    private String title;

    // 构造方法、getter和setter等省略
}

