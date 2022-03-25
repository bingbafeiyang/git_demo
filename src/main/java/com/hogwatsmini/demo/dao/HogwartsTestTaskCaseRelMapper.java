package com.hogwatsmini.demo.dao;

import com.hogwatsmini.demo.common.MysqlExtensionMapper;
import com.hogwatsmini.demo.dto.HogwartsTestTaskCaseRelDetailDto;
import com.hogwatsmini.demo.entity.HogwartsTestTaskCaseRel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface HogwartsTestTaskCaseRelMapper extends MysqlExtensionMapper<HogwartsTestTaskCaseRel> {
//    List<HogwartsTestTaskCaseRelDetailDto> listDetail (@Param("params")Map<String ,Object> params,
//                                                       @Param("pageSize") Integer pageSize);
}