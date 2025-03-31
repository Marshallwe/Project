package com.shanzhu.em.service.impl;

import com.shanzhu.em.entity.Replay;
import com.shanzhu.em.mapper.ReplayMapper;
import com.shanzhu.em.service.ReplayService;
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
