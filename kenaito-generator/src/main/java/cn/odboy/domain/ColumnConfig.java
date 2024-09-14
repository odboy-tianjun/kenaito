package cn.odboy.domain;

import cn.odboy.base.MyObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * 数据库表列的数据信息
 *
 * @author odboy
 * @date 2024-09-06
 */
@Getter
@Setter
@NoArgsConstructor
@TableName("system_code_column_config")
public class ColumnConfig extends MyObject {
    @ApiModelProperty(value = "ID", hidden = true)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "数据库字段名称")
    private String columnName;

    @ApiModelProperty(value = "数据库字段类型")
    private String columnType;

    @ApiModelProperty(value = "数据库字段键类型")
    private String keyType;

    @ApiModelProperty(value = "字段额外的参数")
    private String extra;

    @ApiModelProperty(value = "数据库字段描述")
    private String remark;

    @ApiModelProperty(value = "是否必填")
    private Boolean notNull;

    @ApiModelProperty(value = "是否在列表显示")
    private Boolean listShow = true;

    @ApiModelProperty(value = "是否表单显示")
    private Boolean formShow = true;

    @ApiModelProperty(value = "表单类型")
    private String formType;

    @ApiModelProperty(value = "查询 1:模糊 2：精确")
    private String queryType;

    @ApiModelProperty(value = "字典名称")
    private String dictName;

    /**
     * 表的数据信息
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public static class QueryPage extends MyObject {

        /**
         * 表名称
         */
        private Object tableName;

        /**
         * 创建日期
         */
        private Object createTime;

        /**
         * 数据库引擎
         */
        private Object engine;

        /**
         * 编码集
         */
        private Object coding;

        /**
         * 备注
         */
        private Object remark;
    }
}
