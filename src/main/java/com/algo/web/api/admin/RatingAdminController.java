package com.algo.web.api.admin;

import com.algo.data.query.RatingUserQuery;
import com.algo.data.vo.RatingUserVo;
import com.algo.service.RatingService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Log
@RequestMapping("/admin/api/rating")
public class RatingAdminController {
    @Resource
    private RatingService ratingService;

    @GetMapping("/get")
    public Page<RatingUserVo> getRatings(
            @RequestParam(value = "current", defaultValue = "0", required = false) Integer current,
            @RequestParam(value = "size", defaultValue = "15", required = false) Integer size,
            RatingUserQuery ratingUserQuery
    ) {
        Page<RatingUserVo> page = new Page<>(current, size);
        return ratingService.getRatingUsers(page, ratingUserQuery);
    }

    @PostMapping("/update")
    public void update(@RequestBody RatingUserVo userData) {
        try {
            ratingService.updateRating(userData);
        } catch (Exception e) {
            log.warning(e.getMessage());
        }

    }

    @DeleteMapping("/delete")
    public void delete(String realName) {
        try {
            ratingService.deleteRating(realName);
        } catch (Exception e) {
            log.warning(e.getMessage());
        }

    }

    @PostMapping("/create")
    public void create(RatingUserVo ratingUserVo) {
        try {
            ratingService.save(ratingUserVo);
        } catch (Exception e) {
            log.warning(e.getMessage());
        }

    }

    @PostMapping("/upload")
    public ModelAndView upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ModelAndView("redirect:/admin/api/rating/upload");
        }
        ratingService.upload(file);
        return new ModelAndView("redirect:calendar");
    }
}
