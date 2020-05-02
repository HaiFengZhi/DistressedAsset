/*************************************************************************
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *
 *                COPYRIGHT (C) HONGLING CAPITAL CORPORATION 2012
 *    ALL RIGHTS RESERVED BY HONGLING CAPITAL CORPORATION. THIS PROGRAM
 * MUST BE USED  SOLELY FOR THE PURPOSE FOR WHICH IT WAS FURNISHED BY
 * HONGLING CAPITAL CORPORATION. NO PART OF THIS PROGRAM MAY BE REPRODUCED
 * OR DISCLOSED TO OTHERS,IN ANY FORM, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF HONGLING CAPITAL CORPORATION. USE OF COPYRIGHT NOTICE
 DOES NOT EVIDENCE PUBLICATION OF THE PROGRAM.
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *************************************************************************/

package com.distressed.asset.file.controller;

import com.distressed.asset.common.enums.UploadModelEnum;
import com.distressed.asset.common.result.ResultBean;
import com.distressed.asset.common.utils.CommonUtils;
import com.distressed.asset.common.utils.FileUtils;
import com.distressed.asset.common.utils.GUIDUtils;
import com.distressed.asset.file.service.FileManagerApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * 文件管理服务器对外方法。
 *
 * @author hongchao zhao at 2019-11-18 10:06
 */
@RestController
//解决跨域
@CrossOrigin(origins = "*",maxAge = 3600)
@RequestMapping("/file")
public class FileManagerController {

    private static Logger LOG = LoggerFactory.getLogger(FileManagerController.class);

    @Resource
    private FileManagerApi fileManagerApi;

    /**
     * 附件文件上传。
     *
     * @param file 文件信息。
     * @return 上传结果。
     */
    @PostMapping(value = "/uploadEditor")
    public ResultBean uploadEditor(HttpServletRequest request, @RequestPart("file") MultipartFile file) {
        LOG.debug("content-type==>" + request.getHeader("Content-Type"));
        //通过远程访问，访问bidding系统
        ResultBean resultObj = fileManagerApi.uploadFile(file, UploadModelEnum.EDITOR.getName(), null, "wangEditor富文本编辑上传文件");
        //输入日志
        LOG.info("上传附件成功【{}】", resultObj);
        //返回结果
        return resultObj;
    }

    /**
     * 附件文件上传。
     *
     * @param file 文件信息。
     * @param directory 保存目录。
     * @param userId 用户编号。
     * @return 上传结果。
     */
    @PostMapping(value = "/upload")
    public ResultBean upload(HttpServletRequest request, @RequestPart("file") MultipartFile file,
                             @RequestParam("directory") String directory, @RequestParam("userId") Long userId,
                             @RequestParam(value = "description", required = false) String description) {

        LOG.debug("content-type==>" + request.getHeader("Content-Type"));
        //通过远程访问，访问bidding系统
        ResultBean resultObj = fileManagerApi.uploadFile(file, directory, userId, description);
        //输入日志
        LOG.info("上传附件成功");
        //返回结果
        return resultObj;
    }

    /**
     * 根据附件编号下载文件。
     *
     * @param uuid 文件编号。
     * @param request 请求。
     * @return 下载结果。
     */
    @RequestMapping(value = "/download",method = RequestMethod.GET)
    @ResponseBody
    public Object downFile(String uuid, HttpServletRequest request) {
        if (CommonUtils.isBlank(uuid)) {
            return ResultBean.failed("下载参数不能为空！");
        }
        // feign文件下载
        ResponseEntity<byte[]>  entity = fileManagerApi.downloadFile(uuid);
        System.out.println( entity.getStatusCode());
        LOG.debug("下载完成：【{}】",entity.getStatusCode());
        return entity;
    }

    @RequestMapping(value = "/show/{uuid}",method = RequestMethod.GET)
    @ResponseBody
    public Object show(@PathVariable(value = "uuid") String uuid, HttpServletRequest request) {
        if (CommonUtils.isBlank(uuid) || uuid.length() < 32) {
            return ResultBean.failed("显示参数不能为空！");
        }
        // feign文件下载
        ResponseEntity<byte[]> entity = null;
        try {
            entity = fileManagerApi.show(uuid);
            LOG.debug("显示文件结果：【{}】", entity.getStatusCode());
        } catch (Exception e) {
            LOG.debug("显示文件异常：【{}】", e.getMessage());
        }
        return entity;
    }

    @RequestMapping(value = "/show/image/{uuid}",method = RequestMethod.GET)
    @ResponseBody
    @Cacheable(value="fileImage", key="#p0",unless = "#result == null || #result == 'error'")
    public String showImage(@PathVariable(value = "uuid") String uuid) {
        if (CommonUtils.isBlank(uuid) || uuid.length() < 32) {
            return "error";
        }
        ResultBean<String> resultBean = fileManagerApi.showImage(uuid);
        LOG.debug("显示文件结果：【{}】", resultBean);
        return resultBean.getData();
    }

    /**
     * 根据文件地址下载。
     *
     * @param filePath 文件地址。
     * @return 下载文件。
     */
    @RequestMapping(value = "/downloadFile",method = RequestMethod.GET)
    @ResponseBody
    public Object downloadFile(String filePath,HttpServletRequest request) {
        if (CommonUtils.isBlank(filePath)) {
            return ResultBean.failed("下载参数不能为空！");
        }
        ResponseEntity<byte[]> result = null;
        InputStream inputStream = null;
        try {
            // 获取文件信息
            LOG.debug("当前下载文件目录：【{}】", filePath);
            inputStream = new FileInputStream(new File(filePath));
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            HttpHeaders heads = new HttpHeaders();
            // 处理IE下载文件的中文名称乱码的问题，生成随机文件名
            String fileName=GUIDUtils.makeUploadFileName().concat(FileUtils.getSuffix(filePath));
            LOG.debug("当前下载文件名：【{}】", fileName);
            //处理IE下载文件的中文名称乱码的问题
            String header = request.getHeader("User-Agent").toUpperCase();
            if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
                fileName = URLEncoder.encode(fileName, "utf-8");
                //IE下载文件名空格变+号问题
                fileName = fileName.replace("+", "%20");
            } else {
                fileName = new String(fileName.getBytes(), Charset.forName("iso-8859-1"));
            }
            heads.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
            heads.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            //返回文件下载流
            result = new ResponseEntity<byte[]>(bytes, heads, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
