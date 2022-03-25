package com.hogwatsmini.demo.service.impl;


import com.hogwatsmini.demo.common.Constants;
import com.hogwatsmini.demo.common.PageTableRequest;
import com.hogwatsmini.demo.common.PageTableResponse;
import com.hogwatsmini.demo.common.ResultDto;
import com.hogwatsmini.demo.dao.HogwartsTestCaseMapper;
import com.hogwatsmini.demo.entity.HogwartsTestCase;
import com.hogwatsmini.demo.service.HogwartsTestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class HogwartsTestCaseServiceImpl implements HogwartsTestCaseService {

    @Autowired
    private HogwartsTestCaseMapper hogwartsTestCaseMapper;

    /**
     * 列表查询
     * @param request
     * @return
     */

    @Override
    public ResultDto<PageTableResponse<HogwartsTestCase>> list(PageTableRequest request) {
        Map params=request.getParams();
        Integer pageNum = request.getPageNum();
        Integer pageSize = request.getPageSize();
        //查询总的数据量
        int count=hogwartsTestCaseMapper.count(params);
        //分页数据
        List<HogwartsTestCase> hogwartsTestCaseList =hogwartsTestCaseMapper
                .list(params,(pageNum-1)*pageSize,pageSize);

        PageTableResponse<HogwartsTestCase> pageTableResponse= new PageTableResponse<>();
        pageTableResponse.setRecordsTotal(count);
        pageTableResponse.setData(hogwartsTestCaseList);
        return ResultDto.success("成功",pageTableResponse);

    }

    /**
     * 保存
     * @param hogwartsTestCase
     * @return
     */
    @Override
    public ResultDto<HogwartsTestCase> save(HogwartsTestCase hogwartsTestCase) {
        hogwartsTestCase.setCreateTime(new Date());
        hogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ONE);
        hogwartsTestCase.setUpdateTime(new Date());
        hogwartsTestCaseMapper.insertUseGeneratedKeys(hogwartsTestCase);
        return ResultDto.success("成功",hogwartsTestCase);
    }

    /**
     * 更新
     * @param hogwartsTestCase
     * @return
     */
    @Override
    public ResultDto<HogwartsTestCase> update(HogwartsTestCase hogwartsTestCase) {
        HogwartsTestCase query= new HogwartsTestCase();
        query.setCreateUserId(hogwartsTestCase.getCreateUserId());
        query.setId(hogwartsTestCase.getId());
        query.setDelFlag(Constants.DEL_FLAG_ONE);
        HogwartsTestCase result= hogwartsTestCaseMapper.selectOne(query);
        if(Objects.isNull(result)){
            return ResultDto.fail("未查到测试用例数据");
        }
        hogwartsTestCase.setUpdateTime(new Date());
        hogwartsTestCaseMapper.updateByPrimaryKeySelective(hogwartsTestCase);
        return ResultDto.success("成功",hogwartsTestCase);    }

    @Override
    public ResultDto getById(Integer caseId) {

        HogwartsTestCase query= new HogwartsTestCase();
        query.setId(caseId);
        query.setDelFlag(Constants.DEL_FLAG_ONE);
        HogwartsTestCase result= hogwartsTestCaseMapper.selectOne(query);
        if(Objects.isNull(result)){
            return ResultDto.fail("未查到测试用例数据");
        }

        return ResultDto.success("成功",result);
    }

    @Override
    public String getDataById(Integer caseId) {
        HogwartsTestCase query= new HogwartsTestCase();
        query.setId(caseId);
        query.setDelFlag(Constants.DEL_FLAG_ONE);
        HogwartsTestCase result= hogwartsTestCaseMapper.selectOne(query);
        if(Objects.isNull(result)){
            return "无";
        }

        return result.getCaseData();
    }

    /**
     * 删除
     * @param caseId
     * @return
     */

    @Override
    public ResultDto delete(Integer caseId) {
        ResultDto<HogwartsTestCase> resultDto=getById(caseId);
        if(resultDto.getResultCode()==0){
            return resultDto;
        }
        HogwartsTestCase hogwartsTestCase=resultDto.getData();
        hogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ZERO);
        hogwartsTestCase.setUpdateTime(new Date() );
        hogwartsTestCaseMapper.updateByPrimaryKeySelective(hogwartsTestCase);


        return ResultDto.success("成功");
    }
}
