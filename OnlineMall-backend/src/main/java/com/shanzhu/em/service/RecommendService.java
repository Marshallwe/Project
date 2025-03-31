package com.shanzhu.em.service;

import com.shanzhu.em.entity.Good;

import java.util.List;


public interface RecommendService {


    List<Good> recommendGoods(Long userId);
}

  