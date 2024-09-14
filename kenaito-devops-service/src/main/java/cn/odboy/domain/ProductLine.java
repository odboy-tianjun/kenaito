package cn.odboy.domain;

import cn.odboy.base.MyLogicEntity;
import cn.odboy.infra.validate.NotEmptyList;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 * 产品线
 * </p>
 *
 * @author odboy
 * @since 2024-09-13
 */
@Getter
@Setter
@TableName("devops_product_line")
public class ProductLine extends MyLogicEntity {
    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 产品名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 描述
     */
    @TableField("`description`")
    private String description;

    /**
     * 父产品线
     */
    @TableField("pid")
    private Long pid;

    @Data
    public static class CreateArgs {
        @NotBlank(message = "产品线名称必填")
        private String name;
        @NotBlank(message = "描述必填")
        private String description;
        private Long pid;
        // 扩展属性
        @NotEmptyList(required = true, message = "请选择管理员")
        private List<Long> adminList;
        @NotEmptyList(required = true, message = "请选择PE")
        private List<Long> peList;
    }

    @Data
    public static class ModifyArgs {
        @NotNull(message = "必填")
        private Long id;
        @NotBlank(message = "产品线名称必填")
        private String name;
        @NotBlank(message = "描述必填")
        private String description;
        private Long pid;
        // 扩展属性
        @NotEmptyList(required = true, message = "请选择管理员")
        private List<Long> adminList;
        @NotEmptyList(required = true, message = "请选择PE")
        private List<Long> peList;
    }

    @Data
    public static class RemoveByIdsArgs {
        @NotEmptyList(required = true, message = "必填")
        private List<Long> ids;
    }

    @Data
    public static class TreeNode {
        private Long id;
        private Long pid;
        private String name;
        private List<TreeNode> children;
    }
}
