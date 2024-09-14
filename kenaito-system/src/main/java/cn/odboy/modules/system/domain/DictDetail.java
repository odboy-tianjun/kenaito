package cn.odboy.modules.system.domain;

import cn.odboy.base.MyEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 字典详情
 */
@Getter
@Setter
@TableName("system_dict_detail")
public class DictDetail extends MyEntity implements Serializable {

    @NotNull(groups = Update.class)
    @ApiModelProperty(value = "ID", hidden = true)
    @TableId(value = "detail_id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "dict_id")
    @ApiModelProperty(hidden = true)
    private Long dictId;

    @TableField(exist = false)
    private Dict dict;

    @ApiModelProperty(value = "字典标签")
    private String label;

    @ApiModelProperty(value = "字典值")
    private String value;

    @ApiModelProperty(value = "排序")
    private Integer dictSort = 999;
}