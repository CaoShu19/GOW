package com.gow.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gow.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : Str2ke
 * @date : 2023/3/7 下午1:10
 * @Desc :
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
