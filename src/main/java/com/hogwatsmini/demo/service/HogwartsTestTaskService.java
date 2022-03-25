package com.hogwatsmini.demo.service;

import com.hogwatsmini.demo.common.PageTableRequest;
import com.hogwatsmini.demo.common.PageTableResponse;
import com.hogwatsmini.demo.common.ResultDto;
import com.hogwatsmini.demo.dto.TestTaskDto;
import com.hogwatsmini.demo.entity.HogwartsTestTask;

public interface HogwartsTestTaskService {

    /**
     * 列表查询
     * @param request
     * @return
     */
    ResultDto<PageTableResponse<HogwartsTestTask>> list(PageTableRequest request);

    /**
     * 新增测试任务
     * @param testTaskDto
     * @param taskType
     * @return
     */
    ResultDto<HogwartsTestTask> save(TestTaskDto testTaskDto, Integer taskType);

    /**
     * 修改任务状态
     * @param hogwartsTestTask
     * @return
     */
    ResultDto<HogwartsTestTask> updateStatus(HogwartsTestTask hogwartsTestTask);
    /**
     * 更新
     * @param hogwartsTestTask
     * @return
     */
    ResultDto<HogwartsTestTask> update(HogwartsTestTask hogwartsTestTask);

    /**
     * 根据id查询
     * @param createUserId
     * @param taskId
     * @return
     */
    ResultDto<HogwartsTestTask> getById(Integer createUserId,Integer taskId);


    /**
     * 删除测试任务信息
     * @param taskId
     * @param createUserId
     * @return
     */

    ResultDto<HogwartsTestTask> delete(Integer taskId, Integer createUserId);

}

