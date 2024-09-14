package cn.odboy.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.odboy.config.FileProperties;
import cn.odboy.domain.LocalStorage;
import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.infra.response.PageResult;
import cn.odboy.infra.response.PageUtil;
import cn.odboy.mapper.LocalStorageMapper;
import cn.odboy.service.LocalStorageService;
import cn.odboy.util.MyFileUtil;
import cn.odboy.util.StringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class LocalStorageServiceImpl extends ServiceImpl<LocalStorageMapper, LocalStorage> implements LocalStorageService {

    private final LocalStorageMapper localStorageMapper;
    private final FileProperties properties;

    @Override
    public PageResult<LocalStorage> queryAll(LocalStorage.QueryArgs criteria, Page<Object> page) {
        return PageUtil.toPage(localStorageMapper.findAll(criteria, page));
    }

    @Override
    public List<LocalStorage> queryAll(LocalStorage.QueryArgs criteria) {
        return localStorageMapper.findAll(criteria);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LocalStorage create(String name, MultipartFile multipartFile) {
        MyFileUtil.checkSize(properties.getMaxSize(), multipartFile.getSize());
        String suffix = MyFileUtil.getExtensionName(multipartFile.getOriginalFilename());
        String type = MyFileUtil.getFileType(suffix);
        String size = MyFileUtil.getSize(multipartFile.getSize());
        File file = MyFileUtil.upload(multipartFile, properties.getPath().getPath() + type + File.separator);
        if (ObjectUtil.isNull(file)) {
            throw new BadRequestException("上传失败");
        }
        try {
            name = StringUtil.isBlank(name) ? MyFileUtil.getFileNameNoEx(multipartFile.getOriginalFilename()) : name;
            LocalStorage localStorage = new LocalStorage(
                    file.getName(),
                    name,
                    suffix,
                    file.getPath(),
                    type,
                    size
            );
            save(localStorage);
            return localStorage;
        } catch (Exception e) {
            MyFileUtil.del(file);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(LocalStorage resources) {
        LocalStorage localStorage = getById(resources.getId());
        localStorage.copy(resources);
        saveOrUpdate(localStorage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            LocalStorage storage = getById(id);
            MyFileUtil.del(storage.getPath());
            removeById(storage);
        }
    }

    @Override
    public void download(List<LocalStorage> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LocalStorage localStorage : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("文件名", localStorage.getRealName());
            map.put("备注名", localStorage.getName());
            map.put("文件类型", localStorage.getType());
            map.put("文件大小", localStorage.getSize());
            map.put("创建者", localStorage.getCreateBy());
            map.put("创建日期", localStorage.getCreateTime());
            list.add(map);
        }
        MyFileUtil.downloadExcel(list, response);
    }
}
