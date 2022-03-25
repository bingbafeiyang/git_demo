package com.hogwatsmini.demo.util;

import com.hogwatsmini.demo.common.Constants;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class StrUtil {
    public static Integer getUserId(HttpServletRequest request){
        String userIdStr =request.getHeader(Constants.TOKEN);
        Integer userId=null;
        try {
            userId = Integer.parseInt(userIdStr);
        }catch (Exception e){
            throw new RuntimeException("用户id必须为数字");
        }
        return userId;
    }
    public static String list2Ids ( List<Integer> caseIdList){
        return caseIdList.toString().replace("[","").replace("]","");
    }



}
