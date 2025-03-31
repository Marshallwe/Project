package com.shanzhu.em.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanzhu.em.entity.Carousel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface CarouselMapper extends BaseMapper<Carousel> {

    List<Carousel> findAllCarousel();
}
