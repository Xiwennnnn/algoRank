package com.algo.service;

import com.algo.data.dto.ContestDto;

import java.util.List;

public interface ContestService {
    public List<ContestDto> getContests();

    public List<ContestDto> getAcmContests();

    public List<ContestDto> getOiContests();
}
