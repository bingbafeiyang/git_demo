package com.hogwatsmini.demo.service;

import com.hogwatsmini.demo.common.ResultDto;
import com.hogwatsmini.demo.dto.UserDto;
import com.hogwatsmini.demo.entity.HogwartsTestUser;

import java.util.List;

public interface   HogwartsTestUserService {

    ResultDto login(UserDto userDto);
    //保存用户注册信息
    ResultDto<HogwartsTestUser> save(HogwartsTestUser hogwartsTestUser);
    //更新
    ResultDto<HogwartsTestUser> update(HogwartsTestUser hogwartsTestUser);

    //根据用户id或名称查询
    ResultDto<List<HogwartsTestUser>>  getByName(HogwartsTestUser hogwartsTestUser);

    //根据用户id删除
    ResultDto<List<HogwartsTestUser>>  delete(Integer userId);

}

