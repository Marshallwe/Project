package com.example.spring.service.impl;


import com.example.spring.entity.Replay;
import com.example.spring.mapper.ReplayMapper;
import com.example.spring.service.ReplayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ReplayServiceImpl implements ReplayService {

    private final ReplayMapper replayMapper;

    @Override
    public List<Replay> findAllById(Integer messageId) {
        return replayMapper.findAllById(messageId);
    }


    @Override
    public Integer delete(Integer replayId) {
        return replayMapper.delete(replayId);
    }


    @Override
    public Integer update(Replay replay) {
        return replayMapper.update(replay);
    }

    @Override
    public Integer add(Replay replay) {
        return replayMapper.add(replay);
    }
}
