package com.algo.web.api;

import com.algo.data.dto.ContestDto;
import com.algo.data.vo.ContestVo;
import com.algo.service.ContestService;
import com.algo.service.link.ContestLink;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ContestController {
    @Resource
    private ContestService service;

    @GetMapping("/api/contest")
    public List<ContestDto> getContests(@RequestParam(value = "status", required = false, defaultValue = "0") int status) {
        return service.getContests(status);
    }

    @GetMapping("/api/contest/acm")
    public List<ContestDto> getAcmContests(@RequestParam(value = "status", required = false, defaultValue = "0") int status) {
        return service.getAcmContests(status);
    }

    @GetMapping("/api/contest/oi")
    public List<ContestDto> getOiContests(@RequestParam(value = "status", required = false, defaultValue = "0") int status) {
        return service.getOiContests(status);
    }

    @GetMapping("/page/contest")
    public Page<ContestVo> getContestPage(@RequestParam(value = "platform", required = false) String[] platform,
                                           @RequestParam(value = "isOver", required = false, defaultValue = "false") boolean isOver,
                                           @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                                          @RequestParam(value = "type", required = false, defaultValue = "1") int type)
    {
        Page<ContestDto> contests = service.getContests(isOver ? 2 : 1, currentPage, pageSize, type, platform);
        List<ContestVo> contestVos = contests.getRecords().stream().sorted().map(ContestLink::convert).collect(Collectors.toList());
        if (isOver) Collections.reverse(contestVos);
        return new Page<ContestVo>(contests.getCurrent(), contests.getSize(), contests.getTotal()).setRecords(contestVos);
    }
}