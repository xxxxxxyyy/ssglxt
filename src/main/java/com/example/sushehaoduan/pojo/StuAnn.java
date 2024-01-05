package com.example.sushehaoduan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("stuann")
@Data
public class StuAnn {
    @TableId(value = "said", type = IdType.AUTO)
    private String said;

    private String sid;

    private String aid;

    private String state;
}
