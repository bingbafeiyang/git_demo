package com.hogwatsmini.demo.dto;

import com.hogwatsmini.demo.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@ApiModel(value = "修改测试任务状态对象")
@Data
public class UpdateHogwartsTestTaskStatusDto extends BaseEntityNew {
    /**
     * ID
     */
    @ApiModelProperty(value = "任务主键",required = true)
    private Integer taskId;


    /**
     * Jenkins的构建url
     */
    @ApiModelProperty(value = "构建地址",required = true)
    private String buildUrl;

    /**
     * 状态 0 无效 1 新建 2 执行中 3 执行完成
     */
    @ApiModelProperty(value = "任务状态码 0 无效 1 新建 2 执行中 3 执行完成",required = true)
    private Integer status;

}