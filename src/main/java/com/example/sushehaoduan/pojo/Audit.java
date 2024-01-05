package com.example.sushehaoduan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@TableName("audit")
@Data
public class Audit {
    @TableId(value = "aid", type = IdType.AUTO)
    private String aid;

    private String sid;

    private String type;

    private String content;

    private String state;

    private String text;

    // 省略构造函数、getter和setter等方法
}
