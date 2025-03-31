package com.shanzhu.em.service;


import com.shanzhu.em.entity.Replay;

import java.util.List;


public interface ReplayService {


    List<Replay> findAllById(Integer messageId);


    Integer delete(Integer replayId);


    Integer update(Replay replay);


    Integer add(Replay replay);
}
