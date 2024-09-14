package cn.odboy.modules.system.domain.vo;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;


@Data
public class RoleQueryCriteria {

    private String blurry;

    private List<Timestamp> createTime;

    private Long offset;

    private Long size;
}
