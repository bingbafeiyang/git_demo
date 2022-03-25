package com.hogwatsmini.demo.common;

import io.swagger.annotations.ApiModelProperty;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;

public class ResultDto<T> implements Serializable {
    private static final long serialVersionUID=-7472879865481412372L;
    @ApiModelProperty(value = "返回结果码 1成功，0失败",required = true,example = "1",allowableValues = "1,0")
    private Integer resultCode;

    @ApiModelProperty(value = "提示信息",example = "成功",allowableValues = "成功,失败")
    private String message="";

    @ApiModelProperty(value = "响应结果数据")
    private T data=null;

    public Integer getResultCode(){
        return resultCode;
    }
    public static ResultDto newInstance(){//使用方法创建resultdto对象
        return new ResultDto();
    }
    //设置为成功状态
    public void setAsSuccess(){
        this.resultCode=1;
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message=message;
    }
    public static ResultDto success(String message){
        ResultDto resultDto=new ResultDto();
        resultDto.setAsSuccess();
        resultDto.setMessage(message);
        return resultDto;
    }
    public static <T> ResultDto<T> success(String message,T data){//重载的方法
        ResultDto<T> resultDto=new ResultDto<>();
        resultDto.setAsSuccess();
        resultDto.setMessage(message);
        resultDto.setData(data);
        return resultDto;

    }
    //设置为失败状态
    public void setAsFailure(){
        this.resultCode=0;
    }
    public static ResultDto fail(String message){
        ResultDto resultDto=new ResultDto();
        resultDto.setAsFailure();
        resultDto.setMessage(message);
        return resultDto;

    }
    public static <T> ResultDto<T> fail(String message,T data){
        ResultDto<T> resultDto=new ResultDto<>();
        resultDto.setAsFailure();
        resultDto.setMessage(message);
        resultDto.setData(data);
        return resultDto;
    }
    public T getData(){
        return data;
    }
    public void setData(T data){
        this.data=data;
    }

    public static void main(String[] args) {
        ResultDto.success("密码不对",new HashMap<>());
    }



}
