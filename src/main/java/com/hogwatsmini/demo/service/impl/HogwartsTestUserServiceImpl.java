package com.hogwatsmini.demo.service.impl;

import com.hogwatsmini.demo.common.ResultDto;
import com.hogwatsmini.demo.dao.HogwartsTestUserMapper;
import com.hogwatsmini.demo.dto.UserDto;
import com.hogwatsmini.demo.entity.HogwartsTestUser;
import com.hogwatsmini.demo.service.HogwartsTestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class HogwartsTestUserServiceImpl implements HogwartsTestUserService {

    @Autowired
    private HogwartsTestUserMapper hogwartsTestUserMapper;

    @Override
    public ResultDto login(@RequestBody UserDto userDto){
        System.out.println("UserDto.getName()"+userDto.getUserName());
        System.out.println("UserDto.getPwd()"+userDto.getPassword());
        HogwartsTestUser query=new HogwartsTestUser();
        query.setUserName(userDto.getUserName());
        query.setPassword(userDto.getPassword());

        HogwartsTestUser result=hogwartsTestUserMapper.selectOne(query);
        if(Objects.isNull(result)){
            return ResultDto.fail("用户名不存在或密码错误");
        }

        UserDto resp = new UserDto();
        resp.setUserName(userDto.getUserName());
        resp.setToken(result.getId()+"");

        return ResultDto.success("成功",result);

    }

    @Override
    public ResultDto<HogwartsTestUser> save(HogwartsTestUser hogwartsTestUser) {
        hogwartsTestUser.setCreateTime(new Date());
        hogwartsTestUser.setUpdateTime(new Date());

        HogwartsTestUser query=new HogwartsTestUser();
        query.setUserName(hogwartsTestUser.getUserName());

        HogwartsTestUser result=hogwartsTestUserMapper.selectOne(query);
        if(Objects.nonNull(result)){
            return ResultDto.fail("用户名已存在");
        }

        hogwartsTestUserMapper.insertUseGeneratedKeys(hogwartsTestUser);
        return ResultDto.success("成功",hogwartsTestUser);

    }

    @Override
    public ResultDto<HogwartsTestUser> update(HogwartsTestUser hogwartsTestUser) {
        hogwartsTestUser.setCreateTime(new Date());
        hogwartsTestUser.setUpdateTime(new Date());

        hogwartsTestUserMapper.updateByPrimaryKey(hogwartsTestUser);
//        hogwartsTestUserMapper.updateUserDemo(hogwartsTestUser.getUserName(),hogwartsTestUser.getPassword(),hogwartsTestUser.getEmail(), hogwartsTestUser.getId());
        return ResultDto.success("成功",hogwartsTestUser);
    }

    @Override
    public ResultDto<List<HogwartsTestUser>> getByName(HogwartsTestUser hogwartsTestUser) {
//        List<HogwartsTestUser> hogwartsTestUserList = hogwartsTestUserMapper.getByName(hogwartsTestUser.getUserName(),hogwartsTestUser.getId());
        List<HogwartsTestUser> hogwartsTestUserList = hogwartsTestUserMapper.select(hogwartsTestUser);
        return ResultDto.success("成功",hogwartsTestUserList);    }

    @Override
    public ResultDto<List<HogwartsTestUser>> delete(Integer userId) {
        HogwartsTestUser hogwartsTestUser = new HogwartsTestUser();
        hogwartsTestUser.setId(userId);
        hogwartsTestUserMapper.delete(hogwartsTestUser);
        return ResultDto.success("删除成功");
    }

}
