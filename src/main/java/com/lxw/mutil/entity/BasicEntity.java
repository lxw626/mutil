package com.lxw.mutil.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lixiewen
 * @create 2019-12-03 14:32
 */
@Data
@Accessors(chain = true)
public class BasicEntity {
    private Integer currentPage;
    private Integer pageSize;
    private String sortInfo;
}
