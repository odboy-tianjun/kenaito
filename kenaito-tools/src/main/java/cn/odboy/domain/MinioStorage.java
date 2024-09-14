package cn.odboy.domain;

import cn.odboy.base.MyEntity;
import cn.odboy.base.MyObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Minio存储
 *
 * @author odboy
 * @date 2024-09-06
 */
@Getter
@Setter
@TableName("system_tool_minio_storage")
public class MinioStorage extends MyEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("file_name")
    @ApiModelProperty(value = "完整文件名称")
    private String fileName;

    @TableField("suffix")
    @ApiModelProperty(value = "文件后缀")
    private String suffix;

    @TableField("file_type")
    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @TableField("file_size")
    @ApiModelProperty(value = "文件大小(字节)")
    private Long fileSize;

    @TableField("obj_name")
    private String objName;

    @TableField("obj_tag")
    private String objTag;

    @TableField("obj_version_id")
    private String objVersionId;

    @TableField("obj_bucket")
    private String objBucket;

    @TableField("obj_region")
    private String objRegion;

    @Data
    public static class QueryArgs {
        private String blurry;
        private List<Timestamp> createTime;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class QueryPage extends MyObject {
        private Long id;
        private String fileName;
        private String suffix;
        private String fileType;
        private Long fileSize;
        private String fileSizeDesc;
        private String objBucket;
        private String objName;
        private String createBy;
        private Date createTime;

        private String endpoint;
        private String previewUrl;
    }
}
