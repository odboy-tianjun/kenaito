package cn.odboy.service;

import cn.odboy.domain.MinioStorage;
import cn.odboy.infra.response.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface MinioStorageService extends IService<MinioStorage> {
    /**
     * 上传对象
     *
     * @param fileName 自定义文件名
     * @param file     文件内容
     */
    void uploadObject(String fileName, MultipartFile file);

    /**
     * 分页查询所有
     *
     * @param criteria /
     * @param page     /
     * @return /
     */
    PageResult<MinioStorage.QueryPage> queryAll(MinioStorage.QueryArgs criteria, Page<Object> page);

    /**
     * 查询所有
     *
     * @param criteria /
     * @return /
     */
    List<MinioStorage.QueryPage> queryAll(MinioStorage.QueryArgs criteria);

    /**
     * 下载文件记录
     *
     * @param queryAll /
     * @param response /
     */
    void download(List<MinioStorage.QueryPage> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 根据id批量删除
     *
     * @param ids /
     */
    void deleteAll(Long[] ids);

    /**
     * 修改文件名称
     *
     * @param resources /
     */
    void updateFileName(MinioStorage resources);

    /**
     * 下载文件
     *
     * @param id       /
     * @param response /
     */
    String downloadFile(Long id, HttpServletResponse response) throws Exception;

    /**
     * 根据对象名称判断是否存在
     *
     * @param objName /
     * @return /
     */
    boolean existByObjName(String objName);
}
