package com.student.vo;

import com.student.dao.entity.StDorm;
import com.student.dao.entity.StReviewGrade;
import com.student.dao.entity.StUser;
import lombok.Data;

@Data
public class QueryBox {
    private int current;
    private long userType;
    private StUser stUser;
    private StDorm stDorm;
    private StReviewGrade stReviewGrade;
}
