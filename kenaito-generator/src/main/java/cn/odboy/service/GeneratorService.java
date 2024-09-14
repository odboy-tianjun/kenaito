package cn.odboy.service;

import cn.odboy.domain.ColumnConfig;
import cn.odboy.domain.GenConfig;
import cn.odboy.infra.response.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface GeneratorService extends IService<ColumnConfig> {

    /**
     * 查询数据库元数据
     *
     * @param name 表名
     * @param page 分页参数
     * @return /
     */
    PageResult<ColumnConfig.QueryPage> getTables(String name, Page<Object> page);

    /**
     * 得到数据表的元数据
     *
     * @param name 表名
     * @return /
     */
    List<ColumnConfig> getColumns(String name);

    /**
     * 同步表数据
     *
     * @param columnInfos    /
     * @param columnInfoList /
     */
    void sync(List<ColumnConfig> columnInfos, List<ColumnConfig> columnInfoList);

    /**
     * 保持数据
     *
     * @param columnInfos /
     */
    void save(List<ColumnConfig> columnInfos);

    /**
     * 代码生成
     *
     * @param genConfig 配置信息
     * @param columns   字段信息
     */
    void generator(GenConfig genConfig, List<ColumnConfig> columns);

    /**
     * 预览
     *
     * @param genConfig 配置信息
     * @param columns   字段信息
     * @return /
     */
    ResponseEntity<Object> preview(GenConfig genConfig, List<ColumnConfig> columns);

    /**
     * 打包下载
     *
     * @param genConfig 配置信息
     * @param columns   字段信息
     * @param request   /
     * @param response  /
     */
    void download(GenConfig genConfig, List<ColumnConfig> columns, HttpServletRequest request, HttpServletResponse response);

    /**
     * 查询数据库的表字段数据数据
     *
     * @param table /
     * @return /
     */
    List<ColumnConfig> query(String table);
}
