package com.hogwatsmini.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.hogwatsmini.demo.common.PageTableRequest;
import com.hogwatsmini.demo.common.PageTableResponse;
import com.hogwatsmini.demo.common.ResultDto;
import com.hogwatsmini.demo.dto.*;
import com.hogwatsmini.demo.entity.HogwartsTestCase;
import com.hogwatsmini.demo.entity.HogwartsTestTask;
import com.hogwatsmini.demo.service.HogwartsTestCaseService;
import com.hogwatsmini.demo.service.HogwartsTestTaskService;
import com.hogwatsmini.demo.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.alibaba.fastjson.JSONObject.*;

@Api(tags="霍格沃兹测试学院-测试任务管理")
@RestController
@Slf4j@RequestMapping("/task")
public class HogwartsTestTaskController {
    @Autowired
    private HogwartsTestTaskService hogwartsTestTaskService;

    @Value("${jenkins.callbackurl}")
    private String jenkinsCallbackUrl;

    /**
     *
     * @param request
     * @param testTaskDto
     * @return
     */
    @ApiOperation(value = "添加测试任务")
    @PostMapping
    public ResultDto<HogwartsTestTask> save(HttpServletRequest request, @RequestBody TestTaskDto testTaskDto){
        log.info("添加测试任务-入参= "+JSONObject.toJSONString(testTaskDto));
        if(Objects.isNull(testTaskDto)){
            return ResultDto.fail("测试任务入参不能为空");
        }
        AddHogwartsTestTask addHogwartsTestTask=testTaskDto.getTestTask();
        if(Objects.isNull(addHogwartsTestTask)){
            return ResultDto.fail("测试任务不能为空");
        }
        if(Objects.isNull(addHogwartsTestTask.getName())){
            return ResultDto.fail("测试任务名称不能为空");
        }
        List<Integer> caseIdList=testTaskDto.getCaseIdList();
        if(Objects.isNull(caseIdList)){
            return ResultDto.fail("测试用例不能为空");
        }
        Integer userId = StrUtil.getUserId(request);
        addHogwartsTestTask.setCreateUserId(userId);
        ResultDto<HogwartsTestTask> resultDto = hogwartsTestTaskService.save(testTaskDto,1);
        return resultDto;

    }



    @ApiOperation("列表查询")
    @GetMapping("/list")
    public ResultDto<PageTableResponse<HogwartsTestTask>> list(HttpServletRequest request
             ,PageTableRequest pageTableRequest){

        Integer userId = StrUtil.getUserId(request);

        Map params = pageTableRequest.getParams();
        if(Objects.isNull(params)){
            params = new HashMap();
        }
        params.put("createUserId",userId);
        pageTableRequest.setParams(params);
        ResultDto<PageTableResponse<HogwartsTestTask>> responseResultDto =
                hogwartsTestTaskService.list(pageTableRequest);
        return responseResultDto;
    }
    @ApiOperation("修改测试任务")
    @PutMapping()
    public ResultDto<HogwartsTestTask> update(HttpServletRequest request
            ,@RequestBody UpdateHogwartsTestTask updateHogwartsTestTask){
        log.info("修改测试任务-入参= "+ JSONObject.toJSONString(updateHogwartsTestTask));
        if(Objects.isNull(updateHogwartsTestTask)){
            return ResultDto.fail("测试任务信息不能为空");
        }
        Integer taskId=updateHogwartsTestTask.getId();
        String name= updateHogwartsTestTask.getName();

        if(Objects.isNull(taskId)){
            return ResultDto.fail("任务id不能为空");
        }
        if(StringUtil.isEmpty(name)){
            return ResultDto.fail("任务名称不能为空");
        }
        HogwartsTestTask hogwartsTestTask=new HogwartsTestTask();
        BeanUtils.copyProperties(updateHogwartsTestTask,hogwartsTestTask);
        Integer userId=StrUtil.getUserId(request);
        hogwartsTestTask.setCreateUserId(userId);
        return hogwartsTestTaskService.update(hogwartsTestTask);
    }

    @ApiOperation("修改测试任务状态")
    @PutMapping("status")
    public ResultDto<HogwartsTestTask> updateStatus(HttpServletRequest request
            , @RequestBody UpdateHogwartsTestTaskStatusDto updateHogwartsTestTaskStatusDto){
        log.info("修改测试任务状态码-入参= "+ JSONObject.toJSONString(updateHogwartsTestTaskStatusDto));
        if(Objects.isNull(updateHogwartsTestTaskStatusDto)){
            return ResultDto.fail("测试任务信息不能为空");
        }
        Integer taskId=updateHogwartsTestTaskStatusDto.getTaskId();
        String buildUrl= updateHogwartsTestTaskStatusDto.getBuildUrl();
        Integer status=updateHogwartsTestTaskStatusDto.getStatus();

        if(Objects.isNull(taskId)){
            return ResultDto.fail("任务id不能为空");
        }
        if(StringUtil.isEmpty(buildUrl)){
            return ResultDto.fail("任务构建地址不能为空");
        }
        if(Objects.isNull(status)){
            return  ResultDto.fail("任务状态码不能为空");

        }
        HogwartsTestTask hogwartsTestTask=new HogwartsTestTask();
        hogwartsTestTask.setId(taskId);
        hogwartsTestTask.setBuildUrl(buildUrl);
        hogwartsTestTask.setStatus(status);

        Integer userId=StrUtil.getUserId(request);
        hogwartsTestTask.setCreateUserId(userId);
        return hogwartsTestTaskService.updateStatus(hogwartsTestTask);
    }

    @ApiOperation("根据任务id查询任务信息")
    @GetMapping("{taskId}")
    public ResultDto<HogwartsTestTask> getById(HttpServletRequest request,@PathVariable("taskId") Integer taskId){
        System.out.println("根据任务id查询任务信息 "+taskId);
        if (Objects.isNull(taskId)){
            return ResultDto.fail("任务id不能为空");
        }
        Integer userId=StrUtil.getUserId(request);
        ResultDto<HogwartsTestTask> resultDto=hogwartsTestTaskService.getById(userId,taskId);
        return resultDto;
    }

    /**
     *
     * @param request
     * @param taskId
     * @return
     */
    @ApiOperation("根据id删除用户信息")
    @DeleteMapping("/{taskId}")
    public ResultDto delete(HttpServletRequest request,@PathVariable("taskId") Integer taskId){


        log.info("根据用例id删除用例信息 "+taskId);
        if(Objects.isNull(taskId)){
            return ResultDto.fail("任务Id不能为空");
        }
        Integer userId = StrUtil.getUserId(request);

        return hogwartsTestTaskService.delete(taskId,userId);
    }

}
