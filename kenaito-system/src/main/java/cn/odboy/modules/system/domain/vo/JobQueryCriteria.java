package cn.odboy.modules.system.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;


@Data
@NoArgsConstructor
public class JobQueryCriteria {

    private String name;

    private Boolean enabled;

    private List<Timestamp> createTime;
}