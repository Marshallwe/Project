package com.example.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spring.entity.Icon;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IconMapper extends BaseMapper<Icon> {

    List<Icon> getIconCategoryMapList();
}
