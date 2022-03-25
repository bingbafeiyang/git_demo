package com.hogwatsmini.demo.common;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MysqlExtensionMapper<T> extends Mapper<T>, MySqlMapper<T>, IdsMapper<T> {

}
