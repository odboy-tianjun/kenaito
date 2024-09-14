package cn.odboy.modules.system.domain.vo;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;


@Data
public class DeptQueryCriteria {

    private List<Long> ids;

    private String name;

    private Boolean enabled;

    private Long pid;

    private Boolean pidIsNull;

    private List<Timestamp> createTime;
}