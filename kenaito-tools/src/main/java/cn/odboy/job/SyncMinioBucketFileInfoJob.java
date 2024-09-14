package cn.odboy.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.odboy.config.AppProperties;
import cn.odboy.domain.MinioStorage;
import cn.odboy.repository.MinioRepository;
import cn.odboy.service.MinioStorageService;
import cn.odboy.util.MyFileUtil;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 同步Minio存储中的文件信息
 *
 * @date 2024-06-06
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SyncMinioBucketFileInfoJob {
    private final MinioRepository minioRepository;
    private final MinioStorageService minioStorageService;
    private final AppProperties properties;

    public void run() {
        AppProperties.MinioProp minioProp = properties.getMinio();
        List<Bucket> buckets = minioRepository.listBuckets();
        for (Bucket bucket : buckets) {
            Iterable<Result<Item>> results = minioRepository.listObjects(bucket.name());
            List<MinioStorage> batchInsertList = new ArrayList<>();
            for (Result<Item> result : results) {
                try {
                    Item item = result.get();
                    boolean exist = minioStorageService.existByObjName(item.objectName());
                    if (!exist) {
                        MinioStorage record = new MinioStorage();
                        record.setFileName(item.objectName());
                        String suffix = FileUtil.getSuffix(item.objectName());
                        record.setSuffix(suffix);
                        record.setFileType(MyFileUtil.getFileType(suffix));
                        record.setFileSize(item.size());
                        record.setObjName(item.objectName());
                        record.setObjTag(item.etag());
                        record.setObjVersionId(item.versionId());
                        record.setObjBucket(minioProp.getBucketName());
                        record.setObjRegion(minioProp.getRegion());
                        batchInsertList.add(record);
                    }
                } catch (Exception e) {
                    // 忽略异常
                }
            }
            if (CollUtil.isNotEmpty(batchInsertList)) {
                List<List<MinioStorage>> lists = CollUtil.split(batchInsertList, 300);
                for (List<MinioStorage> minioStorages : lists) {
                    try {
                        minioStorageService.saveBatch(minioStorages);
                    } catch (Exception e) {
                        log.error("批量保存Minio文件信息失败", e);
                    }
                }
            }
        }
    }
}
