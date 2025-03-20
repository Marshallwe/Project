package com.example.spring.service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spring.entity.Carousel;
import com.example.spring.mapper.CarouselMapper;
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
