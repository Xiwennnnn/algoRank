package com.algo.service.impl;

import ch.qos.logback.core.util.TimeUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.algo.data.dao.ContestDo;
import com.algo.data.dto.ContestDto;
import com.algo.data.mapper.ContestMapper;
import com.algo.service.ContestService;
import com.algo.task.ContestManager;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContestServiceImpl implements ContestService {
    @Resource
    private ContestMapper mapper;

    @Override
    public Integer deleteContestById(Long id) {
        return mapper.deleteById(id);
    }

    @Override
    public Integer updateById(ContestDo contestDo) {
        return mapper.updateById(contestDo);
    }

    @Override
    public ContestDo getContestById(Long id) {
        return mapper.selectById(id);
    }

    public Page<ContestDo> getContestDos(int status, int pageNum, int pageSize, int type, String[] platforms) {
        QueryWrapper<ContestDo> queryWrapper = new QueryWrapper<>();
        if (type == 1) {
            queryWrapper.eq("oi_contest", false);
        } else if (type == 2) {
            queryWrapper.eq("oi_contest", true);
        }
        if (status == 1) {
            queryWrapper.gt("end_time", new Date());
        } else if (status == 2) {
            queryWrapper.lt("end_time", new Date());
        }
        List<String> platformsList = new ArrayList<>();
        if (platforms != null) {
            for (String platform : platforms) {
                if ("codeforces".equalsIgnoreCase(platform)) platform = "Codeforces";
                if ("leetcode".equalsIgnoreCase(platform)) platform = "LeetCode";
                if ("nowcoder".equalsIgnoreCase(platform)) platform = "NowCoder";
                if ("luogu".equalsIgnoreCase(platform)) platform = "Luogu";
                if ("others".equalsIgnoreCase(platform)) platform = "其他";
                platformsList.add(platform);
            }
            queryWrapper.in("oj", platformsList);
            Page<ContestDo> doPage = mapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
            return doPage;
        } else {
            return Page.of(pageNum, pageSize);
        }
    }

    public Page<ContestDto> getContests(int status, int pageNum, int pageSize, int type, String[] platforms) {
        Page<ContestDo> doPage = getContestDos(status, pageNum, pageSize, type, platforms);
        return new Page<ContestDto>(doPage.getCurrent(), doPage.getSize(), doPage.getTotal()).setRecords(doPage.getRecords().stream().sorted().map(ContestDto::fromDo).toList());
    }

    @Override
    public List<ContestDto> getContests(int status) {
        if (status == 0) {
            List<ContestDo> contestDos = mapper.selectList(Wrappers.emptyWrapper());
            return contestDos.stream().map(ContestDto::fromDo).sorted().toList();
        } else if (status == 1) {
            List<ContestDo> notEndContests = mapper.selectList(Wrappers.<ContestDo>lambdaQuery().gt(ContestDo::getEndTime, new Date()));
            return notEndContests.stream().map(ContestDto::fromDo).sorted().toList();
        } else if (status == 2) {
            List<ContestDo> endedContests = mapper.selectList(Wrappers.<ContestDo>lambdaQuery().lt(ContestDo::getEndTime, new Date()));
            return endedContests.stream().map(ContestDto::fromDo).sorted((a, b) -> b.getEndTime().compareTo(a.getEndTime())).toList();
        } else {
            return null;
        }
    }

    @Override
    public List<ContestDto> getAcmContests(int status) {
        if (status == 0) {
            List<ContestDo> acmContests = mapper.selectList(Wrappers.<ContestDo>lambdaQuery().eq(ContestDo::isOiContest, false));
            return acmContests.stream().map(ContestDto::fromDo).sorted().toList();
        } else if (status == 1) {
            List<ContestDo> notEndContests = mapper.selectList(Wrappers.<ContestDo>lambdaQuery().eq(ContestDo::isOiContest, false).gt(ContestDo::getEndTime, new Date()));
            return notEndContests.stream().map(ContestDto::fromDo).sorted().toList();
        } else if (status == 2) {
            List<ContestDo> endedContests = mapper.selectList(Wrappers.<ContestDo>lambdaQuery().eq(ContestDo::isOiContest, false).lt(ContestDo::getEndTime, new Date()));
            return endedContests.stream().map(ContestDto::fromDo).sorted((a, b) -> b.getEndTime().compareTo(a.getEndTime())).toList();
        } else {
            return null;
        }
    }

    @Override
    public List<ContestDto> getOiContests(int status) {
        if (status == 0) {
            List<ContestDo> oiContests = mapper.selectList(Wrappers.<ContestDo>lambdaQuery().eq(ContestDo::isOiContest, true));
            return oiContests.stream().map(ContestDto::fromDo).sorted().toList();
        } else if (status == 1) {
            List<ContestDo> notEndContests = mapper.selectList(Wrappers.<ContestDo>lambdaQuery().eq(ContestDo::isOiContest, true).gt(ContestDo::getEndTime, new Date()));
            return notEndContests.stream().map(ContestDto::fromDo).sorted().toList();
        } else if (status == 2) {
            List<ContestDo> endedContests = mapper.selectList(Wrappers.<ContestDo>lambdaQuery().eq(ContestDo::isOiContest, true).lt(ContestDo::getEndTime, new Date()));
            return endedContests.stream().map(ContestDto::fromDo).sorted((a, b) -> b.getEndTime().compareTo(a.getEndTime())).toList();
        } else {
            return null;
        }
    }

    @Override
    public void saveOrUpdateContest(List<ContestDto> contests) {
        List<ContestDo> contestDos = contests.stream().map(ContestDto::toDo).toList();
        mapper.insertOrUpdate(contestDos, (sqlSession, contest) -> {
            List<ContestDo> existingContests = sqlSession.selectList("com.algo.data.mapper.ContestMapper.selectByLink", contest.getLink());
            if (contest.getId() != null) {
                UpdateWrapper<ContestDo> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("contest_id", contest.getId());
                updateWrapper.set("link", contest.getLink());
                updateWrapper.set("oj", contest.getOj());
                updateWrapper.set("name", contest.getName());
                updateWrapper.set("start_time", contest.getStartTime());
                updateWrapper.set("end_time", contest.getEndTime());
                updateWrapper.set("status", contest.getStatus());
                updateWrapper.set("oi_contest", contest.isOiContest());
                mapper.update(null, updateWrapper);
                return false;
            }
            if (!existingContests.isEmpty()) {
                // 如果存在，返回false表示需要更新
                UpdateWrapper<ContestDo> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("link", contest.getLink());
                updateWrapper.set("oj", contest.getOj());
                updateWrapper.set("name", contest.getName());
                updateWrapper.set("start_time", contest.getStartTime());
                updateWrapper.set("end_time", contest.getEndTime());
                updateWrapper.set("status", contest.getStatus());
                updateWrapper.set("oi_contest", contest.isOiContest());
                mapper.update(null, updateWrapper);
                return false;
            } else {
                return true;
            }
        });
    }

    @Override
    public Integer update(ContestDto contestDto) {
        return mapper.update(
                ContestDto.toDo(contestDto),
                Wrappers.<ContestDo>lambdaQuery().eq(ContestDo::getLink, contestDto.getLink())
        );
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
