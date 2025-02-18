package com.algo.web.page;

import com.algo.data.dto.ContestDto;
import com.algo.data.vo.ContestVo;
import com.algo.service.link.ContestLink;
import com.algo.web.api.ContestController;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class CalendarController {
    @Resource
    ContestController contestController;
    @RequestMapping(value = {"/calendar", "/"})
    public String calendar(Model model) {
        List<ContestDto> contests = contestController.getAcmContests(1);
        List<ContestVo> contestVos =  contests.stream().map(ContestLink::convert).collect(Collectors.toList());
        model.addAttribute("contests", contestVos);
        return "calendar";
    }

    @RequestMapping(value = "/global")
    public String global(Model model) {
        return "iron-globe";
    }
}
