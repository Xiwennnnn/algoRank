package com.algo.service.impl;

import ch.qos.logback.core.util.TimeUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.algo.data.dao.ContestDo;
import com.algo.data.dto.ContestDto;
import com.algo.data.mapper.ContestMapper;
import com.algo.service.ContestService;
import com.algo.task.ContestManager;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ContestServiceImpl implements ContestService {
    @Resource
    private ContestMapper mapper;

    @Override
    public List<ContestDto> getContests() {
        List<ContestDo> contestDos = mapper.selectList(Wrappers.emptyWrapper());
        List<ContestDto> contestDtos = contestDos.stream().map(ContestDto::fromDo).sorted().toList();
        return contestDtos;
    }

    @Override
    public List<ContestDto> getAcmContests() {
        List<ContestDo> acmContests = mapper.selectList(Wrappers.<ContestDo>lambdaQuery().eq(ContestDo::isOiContest, false));
        List<ContestDto> contestDtos = acmContests.stream().map(ContestDto::fromDo).sorted().toList();
        return contestDtos;
    }

    @Override
    public List<ContestDto> getOiContests() {
        List<ContestDo> oiContests = mapper.selectList(Wrappers.<ContestDo>lambdaQuery().eq(ContestDo::isOiContest, true));
        List<ContestDto> contestDtos = oiContests.stream().map(ContestDto::fromDo).sorted().toList();
        return contestDtos;
    }

    @Override
    public void saveOrUpdateContest(List<ContestDto> contests) {
        List<ContestDo> contestDos = contests.stream().map(ContestDto::toDo).toList();
        mapper.insertOrUpdate(contestDos, (sqlSession, contest) -> {
            // 根据link字段判断是否存在相同的比赛
            return sqlSession.selectList("com.algo.data.mapper.ContestMapper.selectByLink", contest.getLink()).isEmpty();

        });
    }

    @Override
    public Integer deleteOverlapContests() {
        Long beginDay = DateUtil.beginOfDay(DateTime.now()).getTime();
        Date date = new Date(beginDay);
        return mapper.delete(Wrappers.<ContestDo>lambdaQuery().le(ContestDo::getEndTime, date));
    }

    @Override
    public Integer updateStatus() {
        UpdateWrapper<ContestDo> updateWrapper = new UpdateWrapper<>();
        String status = "Over";
        updateWrapper.set("status", status);
        updateWrapper.lt("end_time", new Date());
        return mapper.update(updateWrapper);
    }
}
