package com.algo.data.dto.codeforces;

import lombok.Data;

import java.util.List;


@Data
public class CfAuthorDto {
    private Long contestId;
    private List<String> members;
    private String participantType;
    private boolean ghost;
    private Long startTimeSeconds;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CfAuthorDto other) {
            boolean flag = other.contestId.equals(contestId);
            flag &= other.members.size() == members.size();
            for (String member : other.members) {
                flag &= members.contains(member);
            }
            flag &= other.participantType.equals(participantType);
            return flag;
        }
        return false;
    }
}
