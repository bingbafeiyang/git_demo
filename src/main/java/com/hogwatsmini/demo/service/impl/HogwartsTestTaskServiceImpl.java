package com.hogwatsmini.demo.service.impl;


import com.hogwatsmini.demo.common.Constants;
import com.hogwatsmini.demo.common.PageTableRequest;
import com.hogwatsmini.demo.common.PageTableResponse;
import com.hogwatsmini.demo.common.ResultDto;
import com.hogwatsmini.demo.dao.HogwartsTestCaseMapper;
import com.hogwatsmini.demo.dao.HogwartsTestTaskCaseRelMapper;
import com.hogwatsmini.demo.dao.HogwartsTestTaskMapper;
import com.hogwatsmini.demo.dao.HogwartsTestUserMapper;
import com.hogwatsmini.demo.dto.AddHogwartsTestTask;
import com.hogwatsmini.demo.dto.TestTaskDto;
import com.hogwatsmini.demo.entity.HogwartsTestCase;
import com.hogwatsmini.demo.entity.HogwartsTestTask;
import com.hogwatsmini.demo.entity.HogwartsTestTaskCaseRel;
import com.hogwatsmini.demo.service.HogwartsTestTaskService;
import com.hogwatsmini.demo.util.StrUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class HogwartsTestTaskServiceImpl implements HogwartsTestTaskService {

    @Autowired
    private HogwartsTestTaskMapper hogwartsTestTaskMapper;

    @Autowired
    private HogwartsTestCaseMapper hogwartsTestCaseMapper;

    @Autowired
    private HogwartsTestTaskCaseRelMapper hogwartsTestTaskCaseRelMapper;

    @Autowired
    private HogwartsTestUserMapper hogwartsTestUserMapper;

    @Value("${jenkins.url}")
    private String jenkinsUrl;
    @Value("${jenkins.username}")
    private String jenkinsUserName;
    @Value("${jenkins.password}")
    private String jenkinsPassword;
    @Value("${jenkins.casetype}")
    private Integer jenkinsCaseType;
    @Value("${jenkins.casesuffix}")
    private String jenkinsCaseSuffix;
    @Value("${jenkins.testcommand}")
    private String jenkinsTestCommand;
    /**
     * 列表查询
     * @param request
     * @return
     */

    @Override
    public ResultDto<PageTableResponse<HogwartsTestTask>> list(PageTableRequest request) {
        Map<String,Object> params=request.getParams();
        Integer pageNum = request.getPageNum();
        Integer pageSize = request.getPageSize();
        //查询总的数据量
        Integer recordsTotal=hogwartsTestTaskMapper.count(params);
        //分页数据
        List<HogwartsTestTask> hogwartsTestTaskList =hogwartsTestTaskMapper
                .list(params,(pageNum-1)*pageSize,pageSize);

        PageTableResponse<HogwartsTestTask> pageTableResponse= new PageTableResponse<>();
        pageTableResponse.setRecordsTotal(recordsTotal);
        pageTableResponse.setData(hogwartsTestTaskList);
        return ResultDto.success("成功",pageTableResponse);

    }

    @Override
    public ResultDto<HogwartsTestTask> save(TestTaskDto testTaskDto, Integer taskType) {
        List<Integer> caseIdList=testTaskDto.getCaseIdList();
        AddHogwartsTestTask testTask=testTaskDto.getTestTask();
        String ids= StrUtil.list2Ids(caseIdList);
        StringBuilder testCommand =new StringBuilder();
        List<HogwartsTestCase> hogwartsTestCaseList=hogwartsTestCaseMapper.selectByIds(ids);
        makeTestCommand(testCommand,hogwartsTestCaseList);

        HogwartsTestTask hogwartsTestTask=new HogwartsTestTask();
        hogwartsTestTask.setStatus(Constants.STATUS_ONE);
        hogwartsTestTask.setCreateUserId(testTask.getCreateUserId());
        hogwartsTestTask.setTestCommand(testCommand.toString());
        hogwartsTestTask.setCaseCount(caseIdList.size());
        hogwartsTestTask.setName(testTask.getName());
        hogwartsTestTask.setCreateTime(new Date());
        hogwartsTestTask.setUpdateTime(new Date());
        hogwartsTestTask.setTaskType(1);
        hogwartsTestTask.setRemark(testTask.getRemark());
        hogwartsTestTask.setTestJenkinsId(1);
        hogwartsTestTaskMapper.insert(hogwartsTestTask);
        List<HogwartsTestTaskCaseRel> hogwartsTestTaskCaseRelList=
                new ArrayList<>();
        for(Integer caseId:caseIdList){
            HogwartsTestTaskCaseRel hogwartsTestTaskCaseRel=new HogwartsTestTaskCaseRel();
            hogwartsTestTaskCaseRel.setCaseId(caseId);
            hogwartsTestTaskCaseRel.setTaskId(hogwartsTestTask.getId());
            hogwartsTestTaskCaseRel.setCreateTime(new Date());
            hogwartsTestTaskCaseRel.setUpdateTime(new Date());
            hogwartsTestTaskCaseRel.setCreateUserId(testTask.getCreateUserId());
            hogwartsTestTaskCaseRelList.add(hogwartsTestTaskCaseRel);
        }
        hogwartsTestTaskCaseRelMapper.insertList(hogwartsTestTaskCaseRelList);



        return ResultDto.success("成功",hogwartsTestTask);
    }

    private void makeTestCommand(StringBuilder testCommand,List<HogwartsTestCase> hogwartsTestCaseList) {
        testCommand.append("pwd");
        testCommand.append("\n");
        String sysCommand = jenkinsTestCommand + " --alluredir=${WORKSPACE}/target/allure-results";

        if(jenkinsCaseType==1){
            for(HogwartsTestCase hogwartsTestCase:hogwartsTestCaseList){
                testCommand.append(sysCommand).append(" ");
                testCommand.append(hogwartsTestCase.getCaseData()).append("\n");
            }

        }
        if(jenkinsCaseType==2){
            for (HogwartsTestCase hogwartsTestCase:hogwartsTestCaseList){
                makeCurlCommand(testCommand, hogwartsTestCase, jenkinsCaseSuffix);
                testCommand.append("\n");

                testCommand.append(sysCommand).append(" ");

                testCommand.append(hogwartsTestCase.getCaseName())
                        .append(".")
                        .append(jenkinsCaseSuffix)
                        .append(" || true")
                        .append("\n");
            }
            }
            testCommand.append("\n");
        }

    /**
     *  拼装下载文件的curl命令
     * @param testCommand
     * @param hogwartsTestCase
     * @param commandRunCaseSuffix
     */
    private void makeCurlCommand(StringBuilder testCommand, HogwartsTestCase hogwartsTestCase, String commandRunCaseSuffix) {

        //通过curl命令获取测试数据并保存为文件
        testCommand.append("curl ")
                .append("-o ");

        String caseName = hogwartsTestCase.getCaseName();

        if(StringUtils.isEmpty(caseName)){
            caseName = "测试用例无测试名称";
        }

        testCommand.append(caseName)
                .append(".")
                .append(commandRunCaseSuffix)
                .append(" ${aitestBaseUrl}/testCase/data/")
                .append(hogwartsTestCase.getId())
                .append(" -H \"token: ${token}\" ");

        //本行命令执行失败，继续运行下面的命令行
        testCommand.append(" || true");

        testCommand.append("\n");
    }


    @Override
    public ResultDto<HogwartsTestTask> updateStatus(HogwartsTestTask hogwartsTestTask) {

        HogwartsTestTask queryHogwartsTestTask= new HogwartsTestTask();
        queryHogwartsTestTask.setCreateUserId(hogwartsTestTask.getCreateUserId());
        queryHogwartsTestTask.setId(hogwartsTestTask.getId());
        HogwartsTestTask result= hogwartsTestTaskMapper.selectOne(queryHogwartsTestTask);
        //如果为空，则提示
        if(Objects.isNull(result)){
            return ResultDto.fail("未查到测试任务信息");
        }
        //如果任务已经完成，则不重复修改
        if(Constants.STATUS_THREE.equals(result.getStatus())){
            return ResultDto.fail("测试任务已完成，无需修改");
        }
        result.setUpdateTime(new Date());
        //仅状态为已完成时修改
        if(Constants.STATUS_THREE.equals(hogwartsTestTask.getStatus())){
            result.setBuildUrl(hogwartsTestTask.getBuildUrl());
            result.setStatus(Constants.STATUS_THREE);
            hogwartsTestTaskMapper.updateByPrimaryKey(result);
        }
        return ResultDto.success("成功修改任务状态");
    }

    /**
     * 更新
     * @param hogwartsTestTask
     * @return
     */
    @Override
    public ResultDto<HogwartsTestTask> update(HogwartsTestTask hogwartsTestTask) {
        HogwartsTestTask query= new HogwartsTestTask();
        query.setCreateUserId(hogwartsTestTask.getCreateUserId());
        query.setId(hogwartsTestTask.getId());
        HogwartsTestTask result= hogwartsTestTaskMapper.selectOne(query);
        if(Objects.isNull(result)){
            return ResultDto.fail("未查到测试任务信息");
        }
        hogwartsTestTask.setUpdateTime(new Date());
        hogwartsTestTaskMapper.updateByPrimaryKeySelective(hogwartsTestTask);
        return ResultDto.success("成功",hogwartsTestTask);
    }

    @Override
    public ResultDto<HogwartsTestTask> getById(Integer taskId,Integer createUserId) {

        HogwartsTestTask query= new HogwartsTestTask();
        query.setId(taskId);
        HogwartsTestTask result= hogwartsTestTaskMapper.selectOne(query);
        if(Objects.isNull(result)){
            return ResultDto.fail("未查到测试任务数据");
        }

        return ResultDto.success("成功",result);
    }



    /**
     * 删除
     * @param taskId
     * @param createUserId
     * @return
     */
    @Override
    public ResultDto<HogwartsTestTask> delete(Integer taskId, Integer createUserId) {

        ResultDto<HogwartsTestTask> resultDto=getById(taskId,createUserId);
        if(resultDto.getResultCode()==0){
            return resultDto;
        }
        HogwartsTestTask hogwartsTestTask=resultDto.getData();
        hogwartsTestTask.setUpdateTime(new Date() );
        hogwartsTestTaskMapper.deleteByPrimaryKey(taskId);


        return ResultDto.success("成功");
    }

}
