package com.algo.service;

import com.algo.data.dao.ContestDo;
import com.algo.data.dto.ContestDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ContestService {
    /**
     * 获取比赛列表
     * @param status - 0 是全部比赛, 1 是还未结束的比赛, 2 是已经结束的比赛
     * @return
     */
    public List<ContestDto> getContests(int status);

    public Integer deleteContestById(Long id);

    public Page<ContestDo> getContestDos(int status, int pageNum, int pageSize, int type, String[] platforms);

    /**
     *
     * @param status - 0 是全部比赛, 1 是还未结束的比赛, 2 是已经结束的比赛
     * @param pageNum
     * @param pageSize
     * @param type 0 是全部比赛, 1 是ACM比赛, 2 是OI比赛
     * @return
     */
    public Page<ContestDto> getContests(int status, int pageNum, int pageSize, int type, String[] platforms);

    /**
     * 获取ACM比赛列表
     * @param status - 0 是全部比赛, 1 是还未结束的比赛, 2 是已经结束的比赛
     * @return
     */
    public List<ContestDto> getAcmContests(int status);
    /**
     * 获取OI比赛列表
     * @param status - 0 是全部比赛, 1 是还未结束的比赛, 2 是已经结束的比赛
     * @return
     */
    public List<ContestDto> getOiContests(int status);

    public void saveOrUpdateContest(List<ContestDto> contests);

    public Integer deleteOverlapContests();

    public Integer update(ContestDto contestDto);

    public Integer updateById(ContestDo contestDo);

    public Integer updateStatus();

    public ContestDo getContestById(Long id);
}
