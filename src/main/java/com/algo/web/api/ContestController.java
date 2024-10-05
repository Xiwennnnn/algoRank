package com.algo.web.api;

import com.algo.data.dto.ContestDto;
import com.algo.data.vo.ContestVo;
import com.algo.service.ContestService;
import com.algo.service.link.ContestLink;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ContestController {
    @Resource
    private ContestService service;

    @GetMapping
    public List<ContestDto> getContests() {
        return service.getContests();
    }

    @GetMapping("/api/contest/acm")
    public List<ContestDto> getAcmContests() {
        return service.getAcmContests();
    }

    @GetMapping("/api/contest/oi")
    public List<ContestDto> getOiContests() {
        return service.getOiContests();
    }

    @GetMapping("/page/contest/acm")
    public List<ContestVo> getAcmContestPage(@RequestParam(value = "platform", required = false) String[] platform) {
        Set<String> platforms = platform == null? Set.of() : Set.of(platform);
        List<ContestDto> acmContests = service.getAcmContests();
        List<ContestVo> acmContestVos = acmContests.stream().filter(contest -> {
            if(!platforms.contains(contest.getOj().toLowerCase())) {
                return false;
            }
            return true;
        }).map(ContestLink::convert).collect(Collectors.toList());
        System.out.println(acmContestVos);
        return acmContestVos;
    }

    @GetMapping("/page/contest/oi")
    public List<ContestVo> getOiContestPage(@RequestParam(value = "platform", required = false) String[] platform) {
        Set<String> platforms = platform == null? Set.of() : Set.of(platform);
        List<ContestDto> oiContests = service.getOiContests();
        List<ContestVo> oiContestVos = oiContests.stream().filter(contest -> {
            if(!platforms.contains(contest.getOj().toLowerCase())) {
                return false;
            }
            return true;
        }).map(ContestLink::convert).collect(Collectors.toList());
        return oiContestVos;
    }
    @RequestMapping("/page/contest")
    public List<ContestVo> getContest(@RequestParam(value = "platform", required = false) String[] platform) {
        Set<String> platforms = platform == null? Set.of() : Set.of(platform);
        List<ContestDto> contests = service.getContests();
        List<ContestVo> contestVos = contests.stream().filter(contest -> {
            if(!platforms.contains(contest.getOj().toLowerCase())) {
                return false;
            }
            return true;
        }).map(ContestLink::convert).collect(Collectors.toList());

        return contestVos;
    }
}
