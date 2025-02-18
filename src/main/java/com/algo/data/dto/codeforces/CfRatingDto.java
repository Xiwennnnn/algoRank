package com.algo.data.dto.codeforces;

import com.algo.data.dto.RatingDto;
import lombok.Data;

@Data
public class CfRatingDto extends RatingDto {
    private String rank;
    private Integer maxRating;
    private String avatar;
    private Long registrationTimeSeconds;

    @Override
    public String toString() {
        return "CfRatingDto{" +
                "rank='" + rank + '\'' +
                ", highestRating=" + maxRating +
                ", userName='" + userName + '\'' +
                ", rating=" + rating +
                ", avatar='" + avatar + '\'' +
                ", registrationTimeSeconds=" + registrationTimeSeconds +
                '}';
    }
}
