package com.example.sushehaoduan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user")
@Data
public class User {
    @TableId(value = "uid", type = IdType.AUTO)
    private String uid;
    private String passwd;
    private int authority;

    // 构造方法、getter和setter等省略
}
