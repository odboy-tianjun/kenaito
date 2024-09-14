package cn.odboy.repository;

import io.minio.ObjectWriteResponse;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface MinioRepository {
    void createBucketIfNotExist();

    /**
     * 桶不存在才创建桶
     */
    void exitsBucket();

    boolean exitsBucketReturnBoolean();

    /**
     * 获取文件流
     *
     * @param objectName 文件名
     * @return 二进制流
     */
    InputStream getObjectInputStream(String objectName) throws Exception;

    /**
     * 断点下载
     *
     * @param objectName 文件名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度
     * @return 二进制流
     */
    InputStream getObjectInputStream(String objectName, long offset, long length) throws Exception;

    /**
     * 根据文件路径获取分享链接
     *
     * @param objectName 对象名 (文件夹名+文件名)
     * @return /
     */
    String getPreviewFileUrl(String objectName);

    /**
     * 通过对象路径名称删除
     *
     * @param objName /
     */
    void removeByObjName(String objName);

    /**
     * 文件上传
     *
     * @param objName  /
     * @param file     /
     * @param mimeType /
     * @return /
     */
    ObjectWriteResponse uploadObject(String objName, MultipartFile file, String mimeType);

    /**
     * 获取所有的bucket
     *
     * @return /
     */
    List<Bucket> listBuckets();

    /**
     * 根据bucket遍历对象列表
     *
     * @param bucket /
     * @return /
     */
    Iterable<Result<Item>> listObjects(String bucket);
}
