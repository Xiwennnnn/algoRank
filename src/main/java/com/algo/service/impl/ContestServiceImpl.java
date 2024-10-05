package com.algo.service.impl;

import com.algo.data.dto.ContestDto;
import com.algo.service.ContestService;
import com.algo.task.ContestManager;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContestServiceImpl implements ContestService {
    @Resource
    private ContestManager manager;

    @Override
    public List<ContestDto> getContests() {
        return manager.getContests();
    }

    @Override
    public List<ContestDto> getAcmContests() {
        return manager.getAcmContests();
    }

    @Override
    public List<ContestDto> getOiContests() {
        return manager.getOiContests();
    }
}
