package cn.odboy.repository.impl;

import cn.odboy.config.AppProperties;
import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.infra.redis.RedisUtil;
import cn.odboy.repository.MinioRepository;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MinioRepositoryImpl implements MinioRepository {
    private static final String CACHE_KEY = "kenaito:minioStorage:previewUrl:";
    private MinioClient minioClient = null;

    @Autowired
    private AppProperties properties;
    @Autowired
    private RedisUtil redisUtil;

    @PostConstruct
    private void initClient() {
        try {
            AppProperties.MinioProp minioProp = properties.getMinio();
            this.minioClient = MinioClient.builder()
                    .endpoint(minioProp.getEndpoint())
                    .credentials(minioProp.getAccessKey(), minioProp.getSecretKey())
                    .build();
            log.info("构建Minio客户端成功");
        } catch (Exception e) {
            log.error("构建Minio客户端失败", e);
        }
    }

    @PreDestroy
    private void destoryClient() {
        try {
            minioClient.close();
            log.info("关闭Minio客户端成功");
        } catch (Exception e) {
            log.error("关闭Minio客户端失败", e);
        }
    }

    @Override
    public void createBucketIfNotExist() {
        if (!exitsBucketReturnBoolean()) {
            AppProperties.MinioProp minioProp = properties.getMinio();
            try {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .region(minioProp.getRegion())
                        .bucket(minioProp.getBucketName())
                        .objectLock(false)
                        .build());
            } catch (Exception e) {
                throw new BadRequestException(String.format("创建bucket=%s失败", minioProp.getBucketName()));
            }
        }
    }

    @Override
    public void exitsBucket() {
        try {
            AppProperties.MinioProp minioProp = properties.getMinio();
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProp.getBucketName()).build());
            if (!found) {
                throw new BadRequestException(String.format("bucket=%s 不存在, 请检查系统配置后再试", minioProp.getBucketName()));
            }
        } catch (Exception e) {
            log.error("查询bucket是否存在失败", e);
        }
    }

    @Override
    public boolean exitsBucketReturnBoolean() {
        try {
            AppProperties.MinioProp minioProp = properties.getMinio();
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProp.getBucketName()).build());
        } catch (Exception e) {
            log.error("查询bucket是否存在失败", e);
            return false;
        }
    }


    @Override
    public InputStream getObjectInputStream(String objectName) throws Exception {
        AppProperties.MinioProp minioProp = properties.getMinio();
        return minioClient.getObject(GetObjectArgs.builder()
                .region(minioProp.getRegion())
                .bucket(minioProp.getBucketName())
                .object(objectName)
                .build());
    }


    @Override
    public InputStream getObjectInputStream(String objectName, long offset, long length) throws Exception {
        AppProperties.MinioProp minioProp = properties.getMinio();
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .region(minioProp.getRegion())
                        .bucket(minioProp.getBucketName())
                        .object(objectName)
                        .offset(offset)
                        .length(length)
                        .build());
    }


    @Override
    public String getPreviewFileUrl(String objectName) {
        AppProperties.MinioProp minioProp = properties.getMinio();
        try {
            String key = String.format("%s:%s:%s", CACHE_KEY, minioProp.getBucketName(), objectName);
            Object o = redisUtil.get(key);
            if (o != null) {
                return (String) o;
            }
            GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .expiry(properties.getMinio().getShareExpireTime(), TimeUnit.DAYS)
                    .bucket(minioProp.getBucketName())
                    .object(objectName)
                    .build();
            String presignedObjectUrl = minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
            redisUtil.set(key, presignedObjectUrl, properties.getMinio().getShareExpireTime(), TimeUnit.DAYS);
            return presignedObjectUrl;
        } catch (Exception e) {
            log.error("获取分享链接失败", e);
            return "";
        }
    }

    @Override
    public void removeByObjName(String objName) {
        AppProperties.MinioProp minioProp = properties.getMinio();
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .region(minioProp.getRegion())
                    .bucket(minioProp.getBucketName())
                    .object(objName)
                    .build());
        } catch (Exception e) {
            log.error("文件删除失败", e);
            throw new BadRequestException("文件删除失败");
        }
    }

    @Override
    public ObjectWriteResponse uploadObject(String objName, MultipartFile file, String mimeType) {
        AppProperties.MinioProp minioProp = properties.getMinio();
        try {
            PutObjectArgs uploadObjectArgs = PutObjectArgs.builder()
                    .bucket(minioProp.getBucketName())
                    .object(objName)
                    .stream(file.getInputStream(), file.getInputStream().available(), -1)
                    .region(minioProp.getRegion())
                    .contentType(mimeType)
                    .build();
            ObjectWriteResponse response = minioClient.putObject(uploadObjectArgs);
            log.info("上传文件到Minio -> BucketName={} 成功", minioProp.getBucketName());
            return response;
        } catch (Exception e) {
            log.error(String.format("上传文件到Minio -> BucketName=%s 失败", minioProp.getBucketName()), e);
            try {
                removeByObjName(objName);
            } catch (Exception e2) {
                log.error(String.format("从Minio删除文件 -> BucketName=%s 失败", minioProp.getBucketName()), e2);
            }
            throw new BadRequestException("上传文件失败");
        }
    }

    @Override
    public List<Bucket> listBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            log.error("获取所有的bucket失败", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Iterable<Result<Item>> listObjects(String bucket) {
        return minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucket)
                .build());
    }
}
