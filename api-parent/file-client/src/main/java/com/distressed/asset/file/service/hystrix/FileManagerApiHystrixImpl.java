package com.distressed.asset.file.service.hystrix;

import com.distressed.asset.common.result.ResultBean;
import com.distressed.asset.file.service.FileManagerApi;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


/**
 * 上传下载文件熔断类。
 *
 * @author SuperZhao
 * @version 1.0
 * @date 2019-11-14 21:53
 */
@Component
public class FileManagerApiHystrixImpl implements FileManagerApi {

    @Override
    public ResultBean uploadFile(MultipartFile file, String directory, Long userId, String description) {
        return ResultBean.failed("上传文件请求超时！");
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(String uuid) {
        return null;
    }

    @Override
    public ResponseEntity<byte[]> show(String uuid) {
        return null;
    }

    @Override
    public ResultBean<String> showImage(String uuid) {
        return null;
    }
}
