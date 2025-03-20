package com.example.spring.service;


import com.example.spring.entity.Good;

import java.util.List;


public interface RecommendService {


    List<Good> recommendGoods(Long userId);
}

