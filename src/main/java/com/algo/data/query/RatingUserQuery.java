package com.algo.data.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingUserQuery {
    private String name;
    private String grade;
    private String major;
}
