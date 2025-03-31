package com.shanzhu.em.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanzhu.em.entity.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper extends BaseMapper<User> {

}
