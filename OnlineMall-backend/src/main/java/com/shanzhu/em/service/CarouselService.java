package com.shanzhu.em.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.em.entity.Carousel;
import com.shanzhu.em.mapper.CarouselMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CarouselService extends ServiceImpl<CarouselMapper, Carousel> {

    private final CarouselMapper carouselMapper;


    public List<Carousel> findAllCarousel() {
        return carouselMapper.findAllCarousel();
    }
}
