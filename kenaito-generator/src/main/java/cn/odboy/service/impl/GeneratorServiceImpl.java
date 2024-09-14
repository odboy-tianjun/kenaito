package cn.odboy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ZipUtil;
import cn.odboy.domain.ColumnConfig;
import cn.odboy.domain.GenConfig;
import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.infra.response.PageResult;
import cn.odboy.infra.response.PageUtil;
import cn.odboy.mapper.ColumnInfoMapper;
import cn.odboy.service.GeneratorService;
import cn.odboy.util.GenUtil;
import cn.odboy.util.MyFileUtil;
import cn.odboy.util.StringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl extends ServiceImpl<ColumnInfoMapper, ColumnConfig> implements GeneratorService {

    private final ColumnInfoMapper columnInfoMapper;
    private final String CONFIG_MESSAGE = "请先配置生成器";

    @Override
    public PageResult<ColumnConfig.QueryPage> getTables(String name, Page<Object> page) {
        return PageUtil.toPage(columnInfoMapper.getTables(name, page));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ColumnConfig> getColumns(String tableName) {
        List<ColumnConfig> columnInfos = columnInfoMapper.findByTableNameOrderByIdAsc(tableName);
        if (CollectionUtil.isNotEmpty(columnInfos)) {
            return columnInfos;
        } else {
            columnInfos = query(tableName);
            saveBatch(columnInfos);
            return columnInfos;
        }
    }

    @Override
    public List<ColumnConfig> query(String tableName) {
        List<ColumnConfig> columnInfos = columnInfoMapper.getColumns(tableName);
        for (ColumnConfig columnInfo : columnInfos) {
            columnInfo.setTableName(tableName);
            if (GenUtil.PK.equalsIgnoreCase(columnInfo.getKeyType())
                    && GenUtil.EXTRA.equalsIgnoreCase(columnInfo.getExtra())) {
                columnInfo.setNotNull(false);
            }
        }
        return columnInfos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sync(List<ColumnConfig> columnInfos, List<ColumnConfig> columnInfoList) {
        // 第一种情况，数据库类字段改变或者新增字段
        for (ColumnConfig columnInfo : columnInfoList) {
            // 根据字段名称查找
            List<ColumnConfig> columns = columnInfos.stream().filter(c -> c.getColumnName().equals(columnInfo.getColumnName())).collect(Collectors.toList());
            // 如果能找到，就修改部分可能被字段
            if (CollectionUtil.isNotEmpty(columns)) {
                ColumnConfig column = columns.get(0);
                column.setColumnType(columnInfo.getColumnType());
                column.setExtra(columnInfo.getExtra());
                column.setKeyType(columnInfo.getKeyType());
                if (StringUtil.isBlank(column.getRemark())) {
                    column.setRemark(columnInfo.getRemark());
                }
                saveOrUpdate(column);
            } else {
                // 如果找不到，则保存新字段信息
                save(columnInfo);
            }
        }
        // 第二种情况，数据库字段删除了
        for (ColumnConfig columnInfo : columnInfos) {
            // 根据字段名称查找
            List<ColumnConfig> columns = columnInfoList.stream().filter(c -> c.getColumnName().equals(columnInfo.getColumnName())).collect(Collectors.toList());
            // 如果找不到，就代表字段被删除了，则需要删除该字段
            if (CollectionUtil.isEmpty(columns)) {
                removeById(columnInfo);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(List<ColumnConfig> columnInfos) {
        saveOrUpdateBatch(columnInfos);
    }

    @Override
    public void generator(GenConfig genConfig, List<ColumnConfig> columns) {
        if (genConfig.getId() == null) {
            throw new BadRequestException(CONFIG_MESSAGE);
        }
        try {
            GenUtil.generatorCode(columns, genConfig);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException("生成失败，请手动处理已生成的文件");
        }
    }

    @Override
    public ResponseEntity<Object> preview(GenConfig genConfig, List<ColumnConfig> columns) {
        if (genConfig.getId() == null) {
            throw new BadRequestException(CONFIG_MESSAGE);
        }
        List<Map<String, Object>> genList = GenUtil.preview(columns, genConfig);
        return new ResponseEntity<>(genList, HttpStatus.OK);
    }

    @Override
    public void download(GenConfig genConfig, List<ColumnConfig> columns, HttpServletRequest request, HttpServletResponse response) {
        if (genConfig.getId() == null) {
            throw new BadRequestException(CONFIG_MESSAGE);
        }
        try {
            File file = new File(GenUtil.download(columns, genConfig));
            String zipPath = file.getPath() + ".zip";
            ZipUtil.zip(file.getPath(), zipPath);
            MyFileUtil.downloadFile(request, response, new File(zipPath), true);
        } catch (IOException e) {
            throw new BadRequestException("打包失败");
        }
    }
}
