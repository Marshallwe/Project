package com.example.spring.service;
import com.example.spring.entity.Replay;
import java.util.List;

public interface ReplayService {


    List<Replay> findAllById(Integer messageId);


    Integer delete(Integer replayId);


    Integer update(Replay replay);


    Integer add(Replay replay);
}
