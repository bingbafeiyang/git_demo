package com.hogwatsmini.demo.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
@ApiModel(value = "用户注册类",description = "请求类")
@Data
public class AddHogwartsTestUser {

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名",example = "hogwarts",required = true)
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码",example = "hogwarts",required = true)
    private String password;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱",example = "123@qq.com",required = true)
    private String email;

    /**
     * 自动生成用例job名称，不为空时表示已经创建job
     */
    @ApiModelProperty(value = "自动生成用例job名称",example = "hogwarts")
    private String autoCreateCaseJobName;

    /**
     * 执行测试job名称 不为空时表示已经创建job
     */
    @ApiModelProperty(value = "执行测试job名称",example = "hogwarts")
    private String startTestJobName;

    /**
     * 默认Jenkins服务器
     */
    @ApiModelProperty(value = "默认Jenkins服务器",example = "hogwarts")
    private Integer defaultJenkinsId;

}
