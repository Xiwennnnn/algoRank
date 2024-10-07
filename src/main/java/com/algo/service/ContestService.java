package com.algo.service;

import com.algo.data.dto.ContestDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ContestService {
    public List<ContestDto> getContests();

    public List<ContestDto> getAcmContests();

    public List<ContestDto> getOiContests();

    public void saveOrUpdateContest(List<ContestDto> contests);

    public Integer deleteOverlapContests();

    public Integer updateStatus();
}
