package com.tanhua.domain.vo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    private Long counts; // 总记录数
    private Long pagesize;// 每页大小
    private Long pages;// 总页数
    private Long page;// 页码
    private List<T> items = Collections.emptyList();

    public static PageResult pageResult(Long page,Long pagesize,List items,Long total){
        return PageResult.builder().counts(total).items(items).page(page).pagesize(pagesize)
                .pages(total/pagesize+(total%pagesize==0?0:1)).build();
    }

}