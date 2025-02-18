package com.algo.data.dto.codeforces;

import lombok.Data;

import java.util.List;

@Data
public class CfProblemDto {
    private Long contestId;
    private String index;
    private String name;
    private String type;
    private Integer rating;
    private List<String> tags;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CfProblemDto other) {
            return other.contestId.equals(this.contestId) && other.name.equals(this.name);
        }
        return false;
    }
}
