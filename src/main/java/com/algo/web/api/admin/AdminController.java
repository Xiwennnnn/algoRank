package com.algo.web.api.admin;

import com.algo.data.query.RatingUserQuery;
import com.algo.data.vo.RatingUserVo;
import com.algo.service.RatingService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/api/rating")
public class AdminController {
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
        ratingService.updateRating(userData);
    }

    @DeleteMapping("/delete")
    public void delete(String realName) {
        ratingService.deleteRating(realName);
    }

    @PostMapping("/create")
    public void create(@RequestBody RatingUserVo ratingUserVo) {
        ratingService.addAllRating(ratingUserVo);
    }

    @PostMapping("/upload")
    public void upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) return;
        ratingService.upload(file);
    }
}
