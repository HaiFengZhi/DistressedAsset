package com.distressed.asset.file.service;

import com.distressed.asset.common.result.ResultBean;
import com.distressed.asset.file.config.FeignMultipartSupportConfig;
import com.distressed.asset.file.service.hystrix.FileManagerApiHystrixImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理类接口。
 *
 * @author SuperZhao
 * @version 1.0
 * @date 2019-11-14 21:52
 */
@FeignClient(value = "file-manager-service", contextId = "fileManagerApi", fallback = FileManagerApiHystrixImpl.class,
        configuration = FeignMultipartSupportConfig.class)
public interface FileManagerApi {

    /**
     * 上传文件。
     *
     * @param file 文件流。
     * @param directory 文件在服务器上保存的特殊目录。
     * @param userId 操作人。
     * @param description 上传备注。
     * @return 上传结果。
     */
    @RequestMapping(method = RequestMethod.POST, value = "/uploadFile",
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    ResultBean uploadFile(@RequestPart(value = "file") MultipartFile file,
                          @RequestParam(value = "directory", required = false) String directory,
                          @RequestParam(value = "userId", required = false) Long userId,
                          @RequestParam(value = "description", required = false) String description);

    /**
     * 下载文件。
     *
     * @param uuid 文件uuid。
     * @return 文件流。
     */
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<byte[]> downloadFile(@RequestParam(value = "uuid") String uuid);

    /**
     * 根据uuid显示文件信息。
     *
     * @param uuid 文件uuid。
     * @return 显示文件流。
     */
    @RequestMapping(value = "/show", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<byte[]> show(@RequestParam(value = "uuid") String uuid);

    /**
     * 根据UUID获取图片访问路径【只用于图片，其它文件类型可以使用方法【show】】。
     *
     * @param uuid 文件uuid。
     * @return 图片路径。
     */
    @RequestMapping(value = "/showImage", method = RequestMethod.GET)
    ResultBean<String> showImage(@RequestParam(value = "uuid") String uuid);

}
