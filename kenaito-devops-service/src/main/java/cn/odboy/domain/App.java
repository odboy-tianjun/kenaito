package cn.odboy.domain;

import cn.odboy.base.MyLogicEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 应用
 * </p>
 *
 * @author odboy
 * @since 2024-09-13
 */
@Getter
@Setter
@TableName("devops_app")
public class App extends MyLogicEntity {
    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 应用名称
     */
    @TableField("app_name")
    private String appName;

    /**
     * 所属产品线
     */
    @TableField("product_line_id")
    private Long productLineId;

    /**
     * 开发语言
     */
    @TableField("`language`")
    private String language;

    /**
     * 应用描述
     */
    @TableField("`description`")
    private String description;

    /**
     * 应用等级
     */
    @TableField("app_level")
    private String appLevel;

    /**
     * git仓库地址
     */
    @TableField("git_repo_url")
    private String gitRepoUrl;
}
