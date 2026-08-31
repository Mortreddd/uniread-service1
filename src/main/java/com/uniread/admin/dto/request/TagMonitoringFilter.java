package com.uniread.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagMonitoringFilter {
    private Integer pageNo = 0;
    private Integer pageSize = 20;
    private String query = "";
}
