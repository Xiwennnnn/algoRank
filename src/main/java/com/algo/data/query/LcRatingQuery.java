package com.algo.data.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LcRatingQuery {
    private String name;
    private String grade;
    private String major;

    public static LcRatingQuery empty() {
        return new LcRatingQuery();
    }
}
