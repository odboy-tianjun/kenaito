package cn.odboy.service;

import cn.odboy.domain.LocalStorage;
import cn.odboy.infra.response.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface LocalStorageService extends IService<LocalStorage> {

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    PageResult<LocalStorage> queryAll(LocalStorage.QueryArgs criteria, Page<Object> page);

    /**
     * 查询全部数据
     *
     * @param criteria 条件
     * @return /
     */
    List<LocalStorage> queryAll(LocalStorage.QueryArgs criteria);

    /**
     * 上传
     *
     * @param name 文件名称
     * @param file 文件
     * @return /
     */
    LocalStorage create(String name, MultipartFile file);

    /**
     * 编辑
     *
     * @param resources 文件信息
     */
    void update(LocalStorage resources);

    /**
     * 多选删除
     *
     * @param ids /
     */
    void deleteAll(Long[] ids);

    /**
     * 导出数据
     *
     * @param localStorages 待导出的数据
     * @param response      /
     * @throws IOException /
     */
    void download(List<LocalStorage> localStorages, HttpServletResponse response) throws IOException;
}
