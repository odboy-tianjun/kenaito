package cn.odboy.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.config.AppProperties;
import cn.odboy.domain.MinioStorage;
import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.infra.response.PageResult;
import cn.odboy.infra.response.PageUtil;
import cn.odboy.mapper.MinioStorageMapper;
import cn.odboy.repository.MinioRepository;
import cn.odboy.service.MinioStorageService;
import cn.odboy.util.FileMimeUtil;
import cn.odboy.util.MyFileUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.minio.ObjectWriteResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioStorageServiceImpl extends ServiceImpl<MinioStorageMapper, MinioStorage> implements MinioStorageService {
    private final MinioRepository minioRepository;
    private final AppProperties properties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadObject(String name, MultipartFile file) {
        if (file == null || file.getSize() == 0) {
            throw new BadRequestException("参数file错误");
        }
        minioRepository.createBucketIfNotExist();
        // 文件后缀
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        // 文件名
        String filename = StrUtil.isNotBlank(name) ? name + "." + suffix : file.getOriginalFilename();
        // 在上传时设置contentType, 可以进行文件预览
        String mimeType = FileMimeUtil.of(suffix);
        ObjectWriteResponse response = minioRepository.uploadObject(filename, file, mimeType);
        MinioStorage record = new MinioStorage();
        record.setFileName(filename);
        record.setSuffix(suffix);
        record.setFileType(MyFileUtil.getFileType(suffix));
        record.setFileSize(file.getSize());
        record.setObjName(response.object());
        record.setObjTag(response.etag());
        record.setObjVersionId(response.versionId());
        record.setObjBucket(response.bucket());
        record.setObjRegion(response.region());
        save(record);
    }

    @Override
    public PageResult<MinioStorage.QueryPage> queryAll(MinioStorage.QueryArgs criteria, Page<Object> page) {
        PageResult<MinioStorage.QueryPage> pageResult = PageUtil.toPage(baseMapper.findAll(criteria, page));
        List<MinioStorage.QueryPage> handleCollect = pageResult.getContent().parallelStream().peek(c -> {
            c.setFileSizeDesc(DataSizeUtil.format(c.getFileSize()));
            c.setEndpoint(properties.getMinio().getEndpoint());
        }).collect(Collectors.toList());
        pageResult.setContent(handleCollect);
        return pageResult;
    }

    @Override
    public List<MinioStorage.QueryPage> queryAll(MinioStorage.QueryArgs criteria) {
        return baseMapper.findAll(criteria);
    }

    @Override
    public void download(List<MinioStorage.QueryPage> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MinioStorage.QueryPage minioStorage : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("文件名", minioStorage.getFileName());
            map.put("备注名", minioStorage.getObjName());
            map.put("文件类型", minioStorage.getFileType());
            map.put("文件大小", minioStorage.getFileSize());
            map.put("创建者", minioStorage.getCreateBy());
            map.put("创建日期", minioStorage.getCreateTime());
            list.add(map);
        }
        MyFileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Long[] ids) {
        minioRepository.exitsBucket();
        for (Long id : ids) {
            MinioStorage storage = getById(id);
            try {
                minioRepository.removeByObjName(storage.getObjName());
            } catch (Exception e) {
                log.error("批量删除minio文件失败, objName=" + storage.getObjName(), e);
            }
            removeById(storage.getId());
        }
    }

    @Override
    public void updateFileName(MinioStorage resources) {
        minioRepository.exitsBucket();
        MinioStorage storage = new MinioStorage();
        storage.setId(resources.getId());

        String fileName = resources.getFileName();
        String suffix = "." + resources.getSuffix();
        if (StrUtil.endWithAnyIgnoreCase(fileName, suffix)) {
            storage.setFileName(fileName);
        } else {
            storage.setFileName(fileName + suffix);
        }
        updateById(storage);
    }

    @Override
    public String downloadFile(Long id, HttpServletResponse response) throws Exception {
        minioRepository.exitsBucket();
        MinioStorage storage = getById(id);
        return minioRepository.getPreviewFileUrl(storage.getObjName());
    }

    @Override
    public boolean existByObjName(String objName) {
        return exists(lambdaQuery().eq(MinioStorage::getObjName, objName));
    }
}
