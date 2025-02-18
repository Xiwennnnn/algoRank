package com.algo.service.impl;

import com.algo.bot.data.ErrorMsgEnum;
import com.algo.crawler.rating.CfRatingCrawler;
import com.algo.crawler.rating.LcRatingCrawler;
import com.algo.data.common.MajorEnum;
import com.algo.data.dao.AlgoUserDo;
import com.algo.data.dao.CfRatingDo;
import com.algo.data.dao.LcRatingDo;
import com.algo.data.query.CfRatingQuery;
import com.algo.data.query.LcRatingQuery;
import com.algo.data.query.RatingUserQuery;
import com.algo.data.vo.CfRatingVo;
import com.algo.data.vo.LcRatingVo;
import com.algo.data.mapper.AlgoUserMapper;
import com.algo.data.mapper.CfRatingMapper;
import com.algo.data.mapper.LcRatingMapper;
import com.algo.data.vo.RatingUserVo;
import com.algo.exception.HttpRequestWrongException;
import com.algo.exception.RatingCrawlerWrongException;
import com.algo.exception.UserModifyException;
import com.algo.service.RatingService;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import jakarta.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
public class RatingServiceImpl implements RatingService {
    @Resource
    private AlgoUserMapper algoUserMapper;
    @Resource
    private LcRatingMapper lcRatingMapper;
    @Resource
    private CfRatingMapper cfRatingMapper;
    @Resource
    private CfRatingCrawler cfRatingCrawler;
    @Resource
    private LcRatingCrawler lcRatingCrawler;

    public RatingUserVo getUserVoByRealName(String realName) {
        QueryWrapper<RatingUserVo> wrapper = new QueryWrapper<>();
        wrapper.eq("real_name", realName);
        List<RatingUserVo> res = algoUserMapper.getRatingUsers(new Page<>(1, 1), wrapper).getRecords();
        if (res.isEmpty()) return null;
        return res.get(0);
    }


    @Override
    public AlgoUserDo getByRealName(String realName) {
        return algoUserMapper.selectOne(Wrappers.<AlgoUserDo>lambdaQuery().eq(AlgoUserDo::getRealName, realName));
    }

    @Override
    public AlgoUserDo getByQQ(Long qq) {
        return algoUserMapper.selectOne(Wrappers.<AlgoUserDo>lambdaQuery().eq(AlgoUserDo::getQq, qq));
    }

    @Override
    @Cacheable(
            cacheNames = "lcRatingCache",
            key = "#lcRatingQuery.getName() + '_' + #lcRatingQuery.getMajor() + '_' + #lcRatingQuery.getGrade()"
    )
    public List<LcRatingVo> getLcRatings(LcRatingQuery lcRatingQuery) {
        QueryWrapper<LcRatingDo> wrapper = new QueryWrapper<>();
        if (lcRatingQuery.getName()!= null) {
            wrapper.like("name", lcRatingQuery.getName());
        }
        if (lcRatingQuery.getMajor() != null) {
            if ( lcRatingQuery.getMajor().equals("其他")) {
                List<String> majors = MajorEnum.getAllMajorNames().stream().filter(m -> !m.equals("其他")).toList();
                wrapper.notIn("major", majors);
            } else {
                wrapper.eq("major", lcRatingQuery.getMajor());
            }
        }
        if (lcRatingQuery.getGrade() != null) {
            wrapper.eq("grade", lcRatingQuery.getGrade());
        }
        return lcRatingMapper.getLcRatingVOs(wrapper);
    }

    @Override
    @Cacheable(
            cacheNames = "cfRatingCache",
            key = "#cfRatingQuery.getName() + '_' + #cfRatingQuery.getMajor() + '_' + #cfRatingQuery.getGrade()"
    )
    public List<CfRatingVo> getCfRatings(CfRatingQuery cfRatingQuery) {
        QueryWrapper<CfRatingDo> wrapper = new QueryWrapper<>();
        if (cfRatingQuery.getName()!= null) {
            wrapper.like("name", cfRatingQuery.getName());
        }
        if (cfRatingQuery.getMajor() != null) {
            if (cfRatingQuery.getMajor().equals("其他")) {
                List<String> majors = MajorEnum.getAllMajorNames().stream().filter(m -> !m.equals("其他")).toList();
                wrapper.notIn("major", majors);
            } else {
                wrapper.eq("major", cfRatingQuery.getMajor());
            }
        }
        if (cfRatingQuery.getGrade() != null) {
            wrapper.eq("grade", cfRatingQuery.getGrade());
        }
        return cfRatingMapper.getCfRatingVOs(wrapper);
    }

    @Override
    @Cacheable(
            key = "#page.current + '_' + #page.size + '_' + #lcRatingQuery.getName() + '_' + #lcRatingQuery.getMajor() + '_' + #lcRatingQuery.getGrade()",
            cacheNames = "lcRatingPageCache"
    )
    public Page<LcRatingVo> getLcRatings(Page<LcRatingVo> page, LcRatingQuery lcRatingQuery) {
        QueryWrapper<LcRatingDo> wrapper = new QueryWrapper<>();
        if (lcRatingQuery.getName()!= null) {
            wrapper.like("real_name", lcRatingQuery.getName());
        }
        if (lcRatingQuery.getMajor() != null) {
            if (lcRatingQuery.getMajor().equals("其他")) {
                List<String> majors = MajorEnum.getAllMajorNames().stream().filter(m -> !m.equals("其他")).toList();
                wrapper.notIn("major", majors);
            } else {
                wrapper.eq("major", lcRatingQuery.getMajor());
            }
        }
        if (lcRatingQuery.getGrade() != null) {
            wrapper.eq("grade", lcRatingQuery.getGrade());
        }
        return lcRatingMapper.getLcRatingVOs(page, wrapper);
    }

    @Override
    @Cacheable(
            key = "#page.current + '_' + #page.size + '_' + #cfRatingQuery.getName() + '_' + #cfRatingQuery.getMajor() + '_' + #cfRatingQuery.getGrade()",
            cacheNames = "cfRatingPageCache"
    )
    public Page<CfRatingVo> getCfRatings(Page<CfRatingVo> page, CfRatingQuery cfRatingQuery) {
        QueryWrapper<CfRatingDo> wrapper = new QueryWrapper<>();
        if (cfRatingQuery.getName()!= null) {
            wrapper.like("real_name", cfRatingQuery.getName());
        }
        if (cfRatingQuery.getMajor() != null) {
            if (cfRatingQuery.getMajor().equals("其他")) {
                List<String> majors = MajorEnum.getAllMajorNames().stream().filter(m -> !m.equals("其他")).toList();
                wrapper.notIn("major", majors);
            } else {
                wrapper.eq("major", cfRatingQuery.getMajor());
            }
        }
        if (cfRatingQuery.getGrade() != null) {
            wrapper.eq("grade", cfRatingQuery.getGrade());
        }
        return cfRatingMapper.getCfRatingVOs(page, wrapper);
    }

    @Override
    public Page<RatingUserVo> getRatingUsers(Page<RatingUserVo> page, RatingUserQuery query) {
        QueryWrapper<RatingUserVo> wrapper = new QueryWrapper<>();
        if (query.getName() != null) {
            wrapper.like("real_name", query.getName());
        }
        if (query.getMajor() != null) {
            if (query.getMajor().equals("其他")) {
                List<String> majors = MajorEnum.getAllMajorNames().stream().filter(m -> !m.equals("其他")).toList();
                wrapper.notIn("major", majors);
            } else {
                wrapper.eq("major", query.getMajor());
            }
        }
        if (query.getGrade() != null) {
            wrapper.eq("grade", query.getGrade());
        }
        return algoUserMapper.getRatingUsers(page, wrapper);
    }

    @Override
    @CacheEvict(
            cacheNames = {"cfRatingPageCache", "lcRatingPageCache", "cfRatingCache", "lcRatingCache"}
            , allEntries = true
    )
    public void deleteRating(String realName) throws UserModifyException {
        AlgoUserDo userDo = algoUserMapper.selectOne(Wrappers.<AlgoUserDo>query().eq("real_name", realName));
        if (userDo == null) {
            throw new RuntimeException("用户【" + realName + "】已经不存在");
        }
        if (userDo.getCfId() != null) {
            cfRatingMapper.deleteById(userDo.getCfId());
        }
        if (userDo.getLcId() != null) {
            lcRatingMapper.deleteById(userDo.getLcId());
        }
        algoUserMapper.delete(Wrappers.<AlgoUserDo>query().eq("real_name", realName));
    }

    public void deleteCfRating(Long id) {
        AlgoUserDo userDo = algoUserMapper.selectOne(Wrappers.<AlgoUserDo>query().eq("cf_id", id));
        cfRatingMapper.deleteById(id);
        if (userDo.getLcId() == null) {
            algoUserMapper.deleteById(userDo.getUserId());
        }
    }

    public void deleteLcRating(Long id) {
        AlgoUserDo userDo = algoUserMapper.selectOne(Wrappers.<AlgoUserDo>query().eq("lc_id", id));
        lcRatingMapper.deleteById(id);
        if (userDo.getCfId() == null) {
            algoUserMapper.deleteById(userDo.getUserId());
        }
    }

    @Override
    public Integer addLcRating(String username, String realName) throws UserModifyException {
        try {
            lcRatingCrawler.crawl(username);
        } catch (RatingCrawlerWrongException e) {
            throw new UserModifyException("Lc用户【" + username + "】不存在，添加失败");
        } catch (HttpRequestWrongException e) {
            throw new UserModifyException(ErrorMsgEnum.NETWORK_ERROR.msg);
        }
        AlgoUserDo userDo = algoUserMapper.selectOne(Wrappers.<AlgoUserDo>query().eq("real_name", realName));
        if (userDo == null) {
            return 0;
        }
        if (username == null) {
            deleteLcRating(userDo.getLcId());
            return 1;
        }
        if (userDo.getLcId() != null) {
            lcRatingMapper.deleteById(userDo.getLcId());
            LcRatingDo lcRatingDo = new LcRatingDo();
            lcRatingDo.setUserName(username);
            lcRatingMapper.insert(lcRatingDo);
            LcRatingDo Do = lcRatingMapper.selectOne(Wrappers.<LcRatingDo>query().eq("user_name", username));
            Long lcId = Do.getLcId();
            userDo.setLcId(lcId);
            algoUserMapper.updateById(userDo);
            return 1;
        }
        LcRatingDo lcRatingDo = new LcRatingDo();
        lcRatingDo.setUserName(username);
        lcRatingMapper.insert(lcRatingDo);
        userDo.setLcId(lcRatingDo.getLcId());
        algoUserMapper.updateById(userDo);
        return 1;
    }

    @Override
    public Integer addCfRating(String username, String realName) throws UserModifyException {
        try {
            cfRatingCrawler.crawl(username);
        } catch (RatingCrawlerWrongException e) {
            throw new UserModifyException("Cf用户【" + username + "】不存在，添加失败");
        } catch (HttpRequestWrongException e) {
            throw new UserModifyException(ErrorMsgEnum.NETWORK_ERROR.msg);
        }
        AlgoUserDo userDo = algoUserMapper.selectOne(Wrappers.<AlgoUserDo>query().eq("real_name", realName));
        if (userDo == null) {
            return 0;
        }
        if (username == null) {
            deleteCfRating(userDo.getCfId());
            return 1;
        }
        if (userDo.getCfId() != null) {
            cfRatingMapper.deleteById(userDo.getCfId());
            CfRatingDo cfRatingDo = new CfRatingDo();
            cfRatingDo.setUserName(username);
            cfRatingMapper.insert(cfRatingDo);
            CfRatingDo Do = cfRatingMapper.selectOne(Wrappers.<CfRatingDo>query().eq("user_name", username));
            Long cfId = Do.getCfId();
            userDo.setCfId(cfId);
            algoUserMapper.updateById(userDo);
            return 1;
        }
        CfRatingDo cfRatingDo = new CfRatingDo();
        cfRatingDo.setUserName(username);
        cfRatingMapper.insert(cfRatingDo);
        userDo.setCfId(cfRatingDo.getCfId());
        algoUserMapper.updateById(userDo);
        return 1;
    }

    @Override
    @CacheEvict(
            cacheNames = {"cfRatingPageCache", "lcRatingPageCache", "cfRatingCache", "lcRatingCache"}
            , allEntries = true
    )
    public void save(RatingUserVo ratingUserVo) throws UserModifyException {
        AlgoUserDo userDo = new AlgoUserDo();
        if (ratingUserVo.getRealName() == null || ratingUserVo.getRealName().isEmpty()) {
            throw new UserModifyException("用户名、年级和专业不能为空");
        }
        if (ratingUserVo.getGrade() == null || ratingUserVo.getGrade().isEmpty()) {
            throw new UserModifyException("用户名、年级和专业不能为空");
        }
        if (ratingUserVo.getMajor() == null || ratingUserVo.getMajor().isEmpty()) {
            throw new UserModifyException("用户名、年级和专业不能为空");
        }
        AlgoUserDo user = algoUserMapper.selectOne(Wrappers.<AlgoUserDo>query().eq("real_name", ratingUserVo.getRealName()));
        String lcUsername = ratingUserVo.getLcUsername();
        String cfUsername = ratingUserVo.getCfUsername();
        if (user != null) {
            throw new UserModifyException("用户【" + ratingUserVo.getRealName() + "】已存在");
        }
        AlgoUserDo existQQ = algoUserMapper.selectOne(Wrappers.<AlgoUserDo>query().eq("qq", ratingUserVo.getQq()));
        if (existQQ != null) {
            throw new UserModifyException("QQ号码【" + ratingUserVo.getQq() + "】已存在");
        }
        userDo.setRealName(ratingUserVo.getRealName());
        userDo.setGrade(ratingUserVo.getGrade());
        userDo.setMajor(ratingUserVo.getMajor());
        userDo.setQq(ratingUserVo.getQq());
        algoUserMapper.insert(userDo);
        if (cfUsername != null && !cfUsername.isEmpty()) addCfRating(cfUsername, ratingUserVo.getRealName());
        if (lcUsername != null && !lcUsername.isEmpty())addLcRating(lcUsername, ratingUserVo.getRealName());
    }

    @Override
    @CacheEvict(
            cacheNames = {"cfRatingPageCache", "lcRatingPageCache", "cfRatingCache", "lcRatingCache"}
            , allEntries = true
    )
    public void updateRating(RatingUserVo ratingUserVo) throws UserModifyException {
        log.warn(ratingUserVo.toString());
        AlgoUserDo userDo = algoUserMapper.selectOne(Wrappers.<AlgoUserDo>query().eq("real_name", ratingUserVo.getRealName()));
        if (userDo == null) {
            throw new UserModifyException("用户【" + ratingUserVo.getRealName() + "】不存在");
        }
        AlgoUserDo existQQ = algoUserMapper.selectOne(Wrappers.<AlgoUserDo>query().eq("qq", ratingUserVo.getQq()));
        if (existQQ != null && !existQQ.getRealName().equals(ratingUserVo.getRealName())) {
            throw new UserModifyException("QQ号码【" + ratingUserVo.getQq() + "】已存在");
        }
        String lcUsername = ratingUserVo.getLcUsername();
        String cfUsername = ratingUserVo.getCfUsername();
        if (ratingUserVo.getLcUsername() != null) addLcRating(lcUsername, ratingUserVo.getRealName());
        if (ratingUserVo.getCfUsername() != null) addCfRating(cfUsername, ratingUserVo.getRealName());
        userDo = algoUserMapper.selectOne(Wrappers.<AlgoUserDo>query().eq("real_name", ratingUserVo.getRealName()));
        if (ratingUserVo.getGrade() != null) userDo.setGrade(ratingUserVo.getGrade());
        if (ratingUserVo.getMajor() != null) userDo.setMajor(ratingUserVo.getMajor());
        algoUserMapper.updateById(userDo);
    }

    @Override
    public void upload(MultipartFile file)  {
        try {
            InputStream is = file.getInputStream();
            AnalysisEventListener<RatingUserVo> listener = new AnalysisEventListener<RatingUserVo>() {
                @Override
                public void invoke(RatingUserVo ratingUserVo, AnalysisContext analysisContext) {
                    System.out.println(ratingUserVo);
                    try {
                        save(ratingUserVo);
                    } catch (Exception e) {
                        log.warn("用户【" + ratingUserVo.getRealName() + "】已存在");
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                }
            };
            ExcelReader er = EasyExcel.read(is, RatingUserVo.class, listener).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            er.read(readSheet);
            er.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
