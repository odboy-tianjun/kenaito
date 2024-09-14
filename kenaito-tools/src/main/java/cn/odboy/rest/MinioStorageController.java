package cn.odboy.rest;

import cn.odboy.annotation.Log;
import cn.odboy.domain.MinioStorage;
import cn.odboy.service.MinioStorageService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Api(tags = "工具：对象存储管理")
@RequestMapping("/api/minioStorage")
public class MinioStorageController {
    private final MinioStorageService minioStorageService;

    @PostMapping
    @ApiOperation("上传文件")
    @PreAuthorize("@el.check('storage:add')")
    public ResponseEntity<Object> createFile(@RequestParam(required = false) String name, @RequestParam("file") MultipartFile file) {
        minioStorageService.uploadObject(name, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation("查询文件")
    @PreAuthorize("@el.check('storage:list')")
    public ResponseEntity<Object> queryFile(MinioStorage.QueryArgs criteria, Page<Object> page) {
        return new ResponseEntity<>(minioStorageService.queryAll(criteria, page), HttpStatus.OK);
    }

    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('storage:list')")
    public void exportFile(HttpServletResponse response, MinioStorage.QueryArgs criteria) throws IOException {
        minioStorageService.download(minioStorageService.queryAll(criteria), response);
    }

    @PutMapping
    @Log("修改文件")
    @ApiOperation("修改文件")
    @PreAuthorize("@el.check('storage:edit')")
    public ResponseEntity<Object> updateFileName(@Validated @RequestBody MinioStorage resources) {
        minioStorageService.updateFileName(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除文件")
    @DeleteMapping
    @ApiOperation("多选删除")
    public ResponseEntity<Object> deleteFile(@RequestBody Long[] ids) {
        minioStorageService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("下载文件")
    @PostMapping(value = "/downloadFile")
    @PreAuthorize("@el.check('storage:list')")
    @ApiOperation("下载文件")
    public ResponseEntity<Object> downloadFile(@RequestBody Long id, HttpServletResponse response) throws Exception {
        return new ResponseEntity<>(minioStorageService.downloadFile(id, response), HttpStatus.OK);
    }
}
