package com.hogwatsmini.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel(value = "用户登录类",description = "请求类")
@Data
public class UserDto {
    @ApiModelProperty(value = "用户名",example = "hogwarts",required = true)
    private String userName;
    @ApiModelProperty(value = "用户密码",example = "hogwarts123",required = true)
    private String password;

    @ApiModelProperty(hidden = true )
    private String token;


}
