package com.algo.data.dto.codeforces;

import lombok.Data;

@Data
public class CfStatusDto {
    private Long id;
    private Long contestId;
    private Long creationTimeSeconds;
    private CfProblemDto problem;
    private CfAuthorDto author;
    private String programmingLanguage;
    private String verdict;
    private String testset;
    private Integer passedTestCount;
    private Integer timeConsumedMillis;
    private Long memoryConsumedBytes;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CfStatusDto other) {
            return this.id.equals(other.id);
        }
        return false;
    }
}
