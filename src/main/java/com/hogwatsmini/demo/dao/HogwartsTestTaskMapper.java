package com.hogwatsmini.demo.dao;

import com.hogwatsmini.demo.common.MysqlExtensionMapper;
import com.hogwatsmini.demo.entity.HogwartsTestCase;
import com.hogwatsmini.demo.entity.HogwartsTestTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface HogwartsTestTaskMapper extends MysqlExtensionMapper<HogwartsTestTask> {
    /**
     * 统计总数
     * @param params
     * @return
     */
    Integer count(@Param("params") Map<String,Object> params);

    /**
     * 分页数据
     * @param params
     * @param pageNum 页码
     * @param pageSize 每页数据量
     * @return
     */
    List<HogwartsTestTask> list(@Param("params") Map<String,Object> params
            , @Param("pageNum") Integer pageNum
            , @Param("pageSize") Integer pageSize);


}