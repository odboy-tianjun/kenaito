package ${package}.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.odboy.base.MyObject;
<#if hasTimestamp>
    import java.sql.Timestamp;
</#if>
<#if hasBigDecimal>
    import java.math.BigDecimal;
</#if>
import java.io.Serializable;
<#if auto>
    import com.baomidou.mybatisplus.annotation.IdType;
</#if>
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* @description /
* @author ${author}
* @date ${date}
**/
@Data
@TableName("${tableName}")
public class ${className} extends MyObject {
<#if columns??>
    <#list columns as column>

        <#if column.columnKey = 'PRI'>
            @TableId(value = "${column.columnName}"<#if auto>, type = IdType.AUTO</#if>)
        </#if>
        <#if column.istNotNull && column.columnKey != 'PRI'>
            <#if column.columnType = 'String'>
                @NotBlank
            <#else>
                @NotNull
            </#if>
        </#if>
        <#if column.remark != ''>
            @ApiModelProperty(value = "${column.remark}")
        <#else>
            @ApiModelProperty(value = "${column.changeColumnName}")
        </#if>
        private ${column.columnType} ${column.changeColumnName};
    </#list>
</#if>

public void copy(${className} source){
BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
}
}
