package com.hogwatsmini.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.hogwatsmini.demo.common.Constants;
import com.hogwatsmini.demo.common.PageTableRequest;
import com.hogwatsmini.demo.common.PageTableResponse;
import com.hogwatsmini.demo.common.ResultDto;
import com.hogwatsmini.demo.dto.*;
import com.hogwatsmini.demo.entity.HogwartsTestCase;
import com.hogwatsmini.demo.entity.HogwartsTestUser;
import com.hogwatsmini.demo.service.HogwartsTestCaseService;
import com.hogwatsmini.demo.service.HogwartsTestUserService;
import com.hogwatsmini.demo.util.JenkinsUtil;
import com.hogwatsmini.demo.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.hogwatsmini.demo.common.Constants.TOKEN;

@Api(tags="霍格沃兹测试学院-用例管理模块")
@RestController
@Slf4j@RequestMapping("testCase")

public class HogwartsTestCaseController {
    @Autowired
    private HogwartsTestCaseService hogwartsTestCaseService;


    @ApiOperation("文件类型用例上传")
    @PostMapping("file")
    public ResultDto<HogwartsTestCase> saveFile(HttpServletRequest request
            ,@RequestParam("caseFile") MultipartFile caseFile
            , AddHogwartsTestCase addHogwartsTestCase) throws IOException {
        if(Objects.isNull(caseFile)){
            return ResultDto.fail("文件为空");
        }
        if(Objects.isNull(addHogwartsTestCase)){
            return ResultDto.fail("参数为空");
        }
        if(StringUtil.isEmpty(addHogwartsTestCase.getCaseName())){
            return ResultDto.fail("用例名称不能为空");
        }

        Integer userId=StrUtil.getUserId(request);

        InputStream inputStream= caseFile.getInputStream();
        String caseData=IOUtils.toString(inputStream,"UTF-8");

        HogwartsTestCase hogwartsTestCase=new HogwartsTestCase();
        hogwartsTestCase.setCaseData(caseData);
        hogwartsTestCase.setCaseName(addHogwartsTestCase.getCaseName());
        hogwartsTestCase.setRemark(addHogwartsTestCase.getRemark());
        hogwartsTestCase.setCreateUserId(userId);
        return hogwartsTestCaseService.save(hogwartsTestCase);
    }

    @ApiOperation("文本类型用例上传")
    @PostMapping("text")
    public ResultDto<HogwartsTestCase> saveText(HttpServletRequest request
            ,@RequestBody AddHogwartsTestCase addHogwartsTestCase){

        if(Objects.isNull(addHogwartsTestCase)){
            return ResultDto.fail("参数为空");
        }
        if(StringUtil.isEmpty(addHogwartsTestCase.getCaseName())){
            return ResultDto.fail("用例名称不能为空");
        }
        if(StringUtil.isEmpty(addHogwartsTestCase.getCaseData())){
            return ResultDto.fail("用例数据不能为空");
        }
        Integer userId=StrUtil.getUserId(request);
        HogwartsTestCase hogwartsTestCase=new HogwartsTestCase();
        BeanUtils.copyProperties(addHogwartsTestCase,hogwartsTestCase);
        hogwartsTestCase.setCreateUserId(userId);
        return hogwartsTestCaseService.save(hogwartsTestCase);
    }

    @ApiOperation("文本类型用例修改")
    @PutMapping()
    public ResultDto<HogwartsTestCase> update(HttpServletRequest request
            ,@RequestBody UpdateHogwartsTestCaseDto updateHogwartsTestCaseDto){

        if(Objects.isNull(updateHogwartsTestCaseDto)){
            return ResultDto.fail("参数为空");
        }
        if(Objects.isNull(updateHogwartsTestCaseDto.getId())){
            return ResultDto.fail("用例id不能为空");
        }
        if(StringUtil.isEmpty(updateHogwartsTestCaseDto.getCaseName())){
            return ResultDto.fail("用例名称不能为空");
        }
        if(StringUtil.isEmpty(updateHogwartsTestCaseDto.getCaseData())){
            return ResultDto.fail("用例数据不能为空");
        }
        Integer userId=StrUtil.getUserId(request);
        HogwartsTestCase hogwartsTestCase=new HogwartsTestCase();
        BeanUtils.copyProperties(updateHogwartsTestCaseDto,hogwartsTestCase);
        hogwartsTestCase.setCreateUserId(userId);
        return hogwartsTestCaseService.update(hogwartsTestCase);
    }

    @ApiOperation("列表查询")
    @GetMapping("list")
    public ResultDto<PageTableResponse<HogwartsTestCase>> list(HttpServletRequest request
             ,PageTableRequest pageTableRequest){

        Integer userId = StrUtil.getUserId(request);

        Map params = pageTableRequest.getParams();
        if(Objects.isNull(params)){
            params = new HashMap();
        }
        params.put("createUserId",userId);
        pageTableRequest.setParams(params);
        ResultDto<PageTableResponse<HogwartsTestCase>> responseResultDto = hogwartsTestCaseService.list(pageTableRequest);
        return responseResultDto;
    }

    @ApiOperation("根据id查询用例原始数据")
    @GetMapping("{caseId}")
    public ResultDto getById(@PathVariable("caseId") Integer caseId){
        System.out.println("根据用例id查询用例信息 "+caseId);
        return hogwartsTestCaseService.getById(caseId);
    }
    @ApiOperation("根据id查询用例原始信息")
    @GetMapping("data/{caseId}")
    public String getDataById(@PathVariable("caseId") Integer caseId){
        System.out.println("根据用例id查询用例信息 "+caseId);
        return hogwartsTestCaseService.getDataById(caseId);
    }
    @ApiOperation("根据id删除用户信息")
    @DeleteMapping("{caseId}")
    public ResultDto delete(@PathVariable("caseId") Integer caseId){


        System.out.println("根据用例id删除用例信息 "+caseId);
        return hogwartsTestCaseService.delete(caseId);
    }

}
