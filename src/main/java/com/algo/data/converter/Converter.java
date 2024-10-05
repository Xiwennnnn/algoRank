package com.algo.data.converter;

import com.algo.data.dao.AlgoUserDo;
import com.algo.data.dao.CfRatingDo;
import com.algo.data.dao.LcRatingDo;
import com.algo.data.dto.CfRatingDto;
import com.algo.data.dto.LcRatingDto;
import com.algo.data.vo.CfRatingVo;
import com.algo.data.vo.LcRatingVo;

public class Converter {
    public static LcRatingVo convertAlgoUserToLcRatingVO(AlgoUserDo user) {
//        return LcRatingVo.builder()
//                .lcId(user.getLcId())
//                .realName(user.getRealName())
//                .grade(user.getGrade())
//                .major(user.getMajor())
//                .build();
        LcRatingVo lcRatingVO = new LcRatingVo();
        lcRatingVO.setLcId(user.getLcId());
        lcRatingVO.setRealName(user.getRealName());
        lcRatingVO.setGrade(user.getGrade());
        lcRatingVO.setMajor(user.getMajor());
        return lcRatingVO;
    }

    public static CfRatingVo convertAlgoUserToCfRatingVO(AlgoUserDo user) {
//        return CfRatingVo.builder()
//                .cfId(user.getCfId())
//                .realName(user.getRealName())
//                .grade(user.getGrade())
//                .major(user.getMajor())
//                .build();
        CfRatingVo cfRatingVO = new CfRatingVo();
        cfRatingVO.setCfId(user.getCfId());
        cfRatingVO.setRealName(user.getRealName());
        cfRatingVO.setGrade(user.getGrade());
        cfRatingVO.setMajor(user.getMajor());
        return cfRatingVO;
    }

    public static LcRatingDo convertLcRatingDTOToDO(LcRatingDto lcRatingDTO) {
        LcRatingDo lcRatingDO = new LcRatingDo();
        lcRatingDO.setUserName(lcRatingDTO.getUserName());
        lcRatingDO.setRating(lcRatingDTO.getRating());
        lcRatingDO.setTopPercentage(lcRatingDTO.getTopPercentage());
        lcRatingDO.setGrading(lcRatingDTO.getGrading());
        lcRatingDO.setNationRank(lcRatingDTO.getNationRank());
        return lcRatingDO;
    }

    public static CfRatingDo convertCfRatingDTOToDO(CfRatingDto cfRatingDTO) {
        CfRatingDo cfRatingDO = new CfRatingDo();
        cfRatingDO.setUserName(cfRatingDTO.getUserName());
        cfRatingDO.setRating(cfRatingDTO.getRating());
        cfRatingDO.setCfRank(cfRatingDTO.getRank());
        cfRatingDO.setMaxRating(cfRatingDTO.getMaxRating());
        return cfRatingDO;
    }



    public static void LcRatingVOCollectLcRatingDO(LcRatingVo lcRatingVO, LcRatingDo lcRatingDO) {
        lcRatingVO.setRating(lcRatingDO.getRating());
        lcRatingVO.setTopPercentage(lcRatingDO.getTopPercentage());
        lcRatingVO.setGrading(lcRatingDO.getGrading());
        lcRatingVO.setNationRank(lcRatingDO.getNationRank());
        lcRatingVO.setUserName(lcRatingDO.getUserName());
    }

    public static void CfRatingVOCollectCfRatingDO(CfRatingVo cfRatingVO, CfRatingDo cfRatingDO) {
        cfRatingVO.setRating(cfRatingDO.getRating());
        cfRatingVO.setCfRank(cfRatingDO.getCfRank());
        cfRatingVO.setMaxRating(cfRatingDO.getMaxRating());
        cfRatingVO.setUserName(cfRatingDO.getUserName());
    }
}
