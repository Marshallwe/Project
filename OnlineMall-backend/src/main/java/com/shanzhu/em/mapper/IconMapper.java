package com.shanzhu.em.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanzhu.em.entity.Icon;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IconMapper extends BaseMapper<Icon> {

    List<Icon> getIconCategoryMapList();
}
