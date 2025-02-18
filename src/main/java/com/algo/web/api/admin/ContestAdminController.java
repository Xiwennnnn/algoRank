package com.algo.web.api.admin;

import com.algo.data.dao.ContestDo;
import com.algo.data.dto.ContestDto;
import com.algo.data.vo.ContestVo;
import com.algo.service.ContestService;
import com.algo.service.link.ContestLink;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/api/contest")
public class ContestAdminController {
    private static final Logger log = LoggerFactory.getLogger(ContestAdminController.class);
    @Resource
    ContestService contestService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteContest(Long id) {
        int res = contestService.deleteContestById(id);
        if (res == 1) return ResponseEntity.ok("success");
        else return ResponseEntity.ok("failed");
    }

    @GetMapping("/getContestById")
    public ContestDo getContestById(Long id) {
        return contestService.getContestById(id);
    }

    @PostMapping("/updateOrSaveContest")
    public ModelAndView updateOrSaveContest(ContestDo contest) {
        log.info(contest.toString());
        contestService.saveOrUpdateContest(List.of(ContestDto.fromDo(contest)));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("success");
    }

    @GetMapping("/page")
    public Page<ContestDo> getContestPage(@RequestParam(value = "platform", required = false) String[] platform,
                                          @RequestParam(value = "isOver", required = false, defaultValue = "false") boolean isOver,
                                          @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
                                          @RequestParam(value = "type", required = false, defaultValue = "1") int type)
    {
        Page<ContestDo> pages = contestService.getContestDos(isOver ? 2 : 1, currentPage, pageSize, type, platform);
        pages.setRecords(pages.getRecords().stream().sorted().toList());
        return pages;
    }
}
