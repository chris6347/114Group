package com.tanhua.domain.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwagQuery implements Serializable {

    private Long queryUserId; // 登录用户id
    private String gender;
    private Integer minAge;
    private Integer maxAge;
    private Long page;

}
