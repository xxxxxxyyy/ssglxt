package com.example.sushehaoduan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("administrator")
@Data
public class Administrator {
    @TableId(value = "mid", type = IdType.AUTO)
    private String mid;
    private String name;
    private String tel;

    // 构造方法、getter和setter等省略
}

