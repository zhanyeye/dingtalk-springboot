package com.softeng.dingtalk.vo;

import com.softeng.dingtalk.entity.Vote;
import lombok.Data;

/**
 * @description:
 * @author: zhanyeye
 * @create: 2020-10-21 10:12
 **/
@Data
public class PollVO {
    private Boolean result;
    private Integer vid;
}