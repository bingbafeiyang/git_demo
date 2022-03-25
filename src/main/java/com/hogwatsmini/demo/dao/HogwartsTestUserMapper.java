package com.hogwatsmini.demo.dao;

import com.hogwatsmini.demo.common.MysqlExtensionMapper;
import com.hogwatsmini.demo.entity.HogwartsTestUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //语义级别的声明bean
public interface HogwartsTestUserMapper extends MysqlExtensionMapper<HogwartsTestUser> {
    int updateUserDemo(@Param("username") String username,@Param("password") String password,@Param("email") String email,@Param("id") Integer id);

    List<HogwartsTestUser> getByName(@Param("userName") String userName, @Param("id") Integer id);

}