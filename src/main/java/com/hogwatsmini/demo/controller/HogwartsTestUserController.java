package com.hogwatsmini.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.hogwatsmini.demo.common.ResultDto;
import com.hogwatsmini.demo.common.ServiceException;
import com.hogwatsmini.demo.dto.AddHogwartsTestUser;
import com.hogwatsmini.demo.dto.BuildJobDto;
import com.hogwatsmini.demo.dto.UpdateHogwartsTestUser;
import com.hogwatsmini.demo.dto.UserDto;
import com.hogwatsmini.demo.entity.HogwartsTestUser;
import com.hogwatsmini.demo.service.HogwartsTestUserService;
import com.hogwatsmini.demo.util.JenkinsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Api(tags="霍格沃兹测试学院-测试任务管理")
@RestController
@Slf4j@RequestMapping("HogwartsTestUser")

public class HogwartsTestUserController {
    @Autowired
    private HogwartsTestUserService hogwartsTestUserService;

    @Value("${Hogwarts.key1}")
    private String Hogwartskey1;

    @ApiOperation("登录接口")
    // @RequestMapping(value = "login",method = RequestMethod.POST)
    @PostMapping("login")
    public ResultDto<UserDto> login(@RequestBody UserDto userDto){
        ResultDto result=hogwartsTestUserService.login(userDto);
        return result;
    }
    @ApiOperation("用户注册")
    @PostMapping("register")
    public ResultDto<HogwartsTestUser> register(@RequestBody AddHogwartsTestUser addHogwartsTestUser){
        HogwartsTestUser hogwartsTestUser=new HogwartsTestUser();
        BeanUtils.copyProperties(addHogwartsTestUser,hogwartsTestUser);
        if(StringUtil.isEmpty(addHogwartsTestUser.getUserName())){
            return ResultDto.fail("用户名不能为空");
        }
        if(StringUtil.isEmpty(addHogwartsTestUser.getPassword())){
            return ResultDto.fail("密码不能为空");
        }


        log.info("用户注册 请求参数"+JSONObject.toJSONString(hogwartsTestUser));
        return hogwartsTestUserService.save(hogwartsTestUser);
    }

    @ApiOperation("用户信息修改接口")
    @PutMapping()
    public ResultDto updateUserInfo(@RequestBody UpdateHogwartsTestUser updateHogwartsTestUser){
        HogwartsTestUser hogwartsTestUser=new HogwartsTestUser();
        BeanUtils.copyProperties(updateHogwartsTestUser,hogwartsTestUser);
        if(StringUtil.isEmpty(updateHogwartsTestUser.getUserName())){
            return ResultDto.fail("用户名不能为空");
        }
        if(StringUtil.isEmpty(updateHogwartsTestUser.getPassword())){
            return ResultDto.fail("密码不能为空");
        }


        log.info("用户注册 请求参数"+JSONObject.toJSONString(hogwartsTestUser));
        return hogwartsTestUserService.update(hogwartsTestUser);
    }

    @ApiOperation("根据用户id删除用户信息")
    @DeleteMapping("{userId}")
    public ResultDto delete(@PathVariable("userId") Integer userId){
        System.out.println("根据用户id删除用户信息 "+userId);
        return hogwartsTestUserService.delete(userId);
    }
    @RequestMapping(value = "byId/{userId}/{id}",method = RequestMethod.GET)
    public String getById(@PathVariable("userId") Long userId,@PathVariable("id") long id){
        System.out.println("userId"+userId);
        System.out.println("id"+id);
        return "成功 "+"userId="+userId+"id="+id;
    }
    //@RequestMapping(value = "byId",method = RequestMethod.GET)
    @GetMapping("byId")
    public String getById2(@RequestParam(value="userId",required = false) Long userId,@RequestParam("id") long id){
        System.out.println("userId"+userId);
        System.out.println("id"+id);
        return "成功 "+"userId"+userId+"id"+id;
    }
    @ApiOperation("根据用户名查询")
//    @GetMapping("byName")
     @RequestMapping(value = "byName",method = RequestMethod.GET)
    public ResultDto<List<HogwartsTestUser>>  getByName(@RequestParam(value="userId",required = false) Integer userId, @RequestParam(value="userName",required = false) String userName){
        HogwartsTestUser hogwartsTestUser=new HogwartsTestUser();
        System.out.println(userId);
        hogwartsTestUser.setId(userId);
        hogwartsTestUser.setUserName(userName);

        log.info("根据用户名模糊查询 入参"+JSONObject.toJSONString(hogwartsTestUser));
        return hogwartsTestUserService.getByName(hogwartsTestUser);
    }
    @ApiOperation("调用jenkins构建job")
    @PutMapping("buildJob")
//    @RequestMapping(value = "byName",method = RequestMethod.GET)
    public ResultDto  buildJob(@RequestBody BuildJobDto buildJobDto) throws IOException, URISyntaxException {

        log.info("调用jenkins构建job 请求入参"+JSONObject.toJSONString(buildJobDto));
        JenkinsUtil.buildJob(buildJobDto.getJobName(),buildJobDto.getUserId(),buildJobDto.getRemark(),buildJobDto.getTestCommand());
        return ResultDto.success("成功");

    }
}
