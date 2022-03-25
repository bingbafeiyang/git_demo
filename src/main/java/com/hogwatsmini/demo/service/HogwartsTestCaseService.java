package com.hogwatsmini.demo.service;

import com.hogwatsmini.demo.common.PageTableRequest;
import com.hogwatsmini.demo.common.PageTableResponse;
import com.hogwatsmini.demo.common.ResultDto;
import com.hogwatsmini.demo.entity.HogwartsTestCase;

import java.util.List;

public interface HogwartsTestCaseService {

    /**
     * 列表查询
     * @param request
     * @return
     */
    ResultDto<PageTableResponse<HogwartsTestCase>> list(PageTableRequest request);

    /**
     * 保存
     * @param hogwartsTestCase
     * @return
     */
    ResultDto<HogwartsTestCase> save(HogwartsTestCase hogwartsTestCase);

    /**
     * 更新
     * @param hogwartsTestCase
     * @return
     */
    ResultDto<HogwartsTestCase> update(HogwartsTestCase hogwartsTestCase);

    /**
     * 根据id查询
     * @param caseId
     * @return
     */
    ResultDto<List<HogwartsTestCase>>  getById(Integer caseId);
    /**
     * 根据id查询原始数据
     * @param caseId
     * @return
     */
    String  getDataById(Integer caseId);
    /**
     * 删除
     * @param caseId
     * @return
     */
    //根据用户id删除
    ResultDto<List<HogwartsTestCase>>  delete(Integer caseId);

}

