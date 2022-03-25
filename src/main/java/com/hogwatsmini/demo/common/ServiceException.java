package com.hogwatsmini.demo.common;

public class ServiceException extends  RuntimeException { //类名无限制，需要继承RE
    private static final long serialVersionUID = 1L;//指定版本UID
    private String message; //定义私有属性：提示信息
    @Override
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public ServiceException(final String message,Throwable th){//构造方法 提示信息，具体异常
        super(message,th);//把提示信息和具体异常抛给父类
        this.message = message;
    }
    public ServiceException(final String message) {//另一构造方法
        this.message = message;
    }
    public static void throwEx(String message){//便捷方法
        throw new ServiceException(message);
    }

}
