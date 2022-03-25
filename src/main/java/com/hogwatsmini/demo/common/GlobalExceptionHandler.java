package com.hogwatsmini.demo.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ServiceException.class})
    public ResultDto serviceException(ServiceException se){
        return resultFormat(se);
    }
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler({Exception.class})
    public ResultDto exceptionHandler(Exception e){
        return resultFormat(e);
    }
    @ExceptionHandler({Throwable.class})
    public ResultDto throwableHandler(Throwable t){
        log.error(t.getMessage());
        return ResultDto.fail("系统错误 ");
    }

    public ResultDto resultFormat(Throwable t){
        log.error(t.getMessage());
        String tips="系统繁忙，请稍后重试";
        if(t instanceof ServiceException){
        return ResultDto.fail("业务异常"+tips);
        }
        if(t instanceof Exception){
            return ResultDto.fail("非业务异常"+tips);
        }
        return ResultDto.fail(tips);
    }

}
