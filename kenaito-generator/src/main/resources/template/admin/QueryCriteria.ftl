package ${package}.domain.vo;

import lombok.Data;
<#if queryHasTimestamp>
    import java.sql.Timestamp;
</#if>
<#if queryHasBigDecimal>
    import java.math.BigDecimal;
</#if>
<#if betweens?? && (betweens?size > 0)>
    import java.util.List;
</#if>

/**
* @author ${author}
* @date ${date}
**/
@Data
public class ${className}QueryCriteria{
<#if queryColumns??>
    <#list queryColumns as column>
        private ${column.columnType} ${column.changeColumnName};
    </#list>
</#if>
<#if betweens??>
    <#list betweens as column>
        private List<${column.columnType}> ${column.changeColumnName};
    </#list>
</#if>
}
