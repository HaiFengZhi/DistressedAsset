package com.distressed.asset.file.service;

import com.distressed.asset.portal.dto.AttachmentDTO;
import com.distressed.asset.portal.service.AttachmentService;
import com.distressed.asset.common.enums.UploadModelEnum;
import com.distressed.asset.common.result.ResultBean;
import com.distressed.asset.common.utils.GUIDUtils;
import com.distressed.asset.common.utils.MyDateUtils;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件上传下载实现类。
 *
 * @author SuperZhao
 * @version 1.0
 * @date 2019-11-14 21:59
 */
@RestController
//解决跨域
@CrossOrigin(origins = "*",maxAge = 3600)
@CacheConfig(cacheNames = "fileManageCache")
public class FileManagerApiImpl implements FileManagerApi{

    private static Logger LOG = LoggerFactory.getLogger(FileManagerApiImpl.class);

    /**
     * 下载文件访问路径
     */
    @Value("${file.manager.access.path}")
    private String fileUrl;

    /**
     * 上传文件路径
     */
    @Value("${file.manager.upload.path}")
    private String uploadPath;

    @Value("${distressed.asset.file.manager.url}")
    private String fileShowUrl;

    @Resource
    private AttachmentService attachmentService;


    @Override
    public ResultBean uploadFile(MultipartFile file, String directory, Long userId, String description) {
        //判断附件文件是否为空
        if (Objects.isNull(file) || file.isEmpty()) {
            LOG.error("文件为空");
            return ResultBean.failed(500, "上传对象为空");
        }
        long size = file.getSize();
        String contentType = file.getContentType();
        String name = file.getName();
        String orgFilename = file.getOriginalFilename();

        //区分文件模块
        UploadModelEnum modelEnum = UploadModelEnum.getValue(directory);

        //获取当前系统的目录分割符
        String separator = File.separator;
        //设置附件文件保存目录
        Date date = new Date();
        String datePath= new SimpleDateFormat("yyyy/MM/dd/").format(date);
        String filePath = uploadPath + modelEnum.getDirectory() + "/" + datePath;
        String accessPath = fileUrl + modelEnum.getDirectory() + "/" + datePath;
        //防止windows和linux系统目录冲突作一次转换
        filePath = filePath.replace("/", separator);
        accessPath = accessPath.replace("/", separator);
        LOG.info("文件保存目录：" + filePath);
        LOG.info("文件访问目录：" + accessPath);

        try {
            assert orgFilename != null;
            //在服务器生成实际保存名称，防止服务器上重复文件异常或覆盖
            String suffix = orgFilename.substring(orgFilename.lastIndexOf("."));
            String actualName = GUIDUtils.makeUploadFileName() + suffix;
            byte[] bytes = file.getBytes();
            Path path = Paths.get(filePath + actualName);
            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(Paths.get(filePath));
            }
            //文件写入指定路径
            Files.write(path, bytes);
            LOG.debug("文件写入成功");
            //组装一下实际的文件目录
            filePath += actualName;
            accessPath += actualName;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.failed(500, "后端异常");
        }

        ///附件大小计算转换
        String fSize = null;
        if (size > 1048576) {
            fSize = (size / 1048576) + "M";
        } else if (size < 1024) {
            fSize = (size) + "B";
        } else {
            fSize = (size / 1024) + "KB";
        }
        LOG.info(orgFilename + "-->" + fSize);
        //返回一个attachment对象
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setUserId(userId);
        attachmentDTO.setUuid(GUIDUtils.makeUUID());
        attachmentDTO.setModel(modelEnum.getName());
        attachmentDTO.setUploadTime(MyDateUtils.now());
        attachmentDTO.setOriginalFilename(orgFilename);
        attachmentDTO.setFileSize((int) size);
        attachmentDTO.setAccessPath(accessPath);
        attachmentDTO.setActualPath(filePath);
        attachmentDTO.setDescription(description);
        attachmentDTO.setType(contentType);
        attachmentDTO.setStatus(0);
        ResultBean<Long> resultBean = attachmentService.saveOrUpdateAttachment(attachmentDTO);
        if (resultBean.isSuccess()){
            Long attachmentId = resultBean.getData();
            attachmentDTO.setId(attachmentId);
        }
        LOG.debug("上传对象：" + attachmentDTO.toString());
        //返回上传结果
        Map<String,Object> result = new HashMap<>();
        //上传文件的UUID标识
        result.put("uuid",attachmentDTO.getUuid());
        //上传文件的访问地址
        result.put("url",attachmentDTO.getAccessPath());
        return ResultBean.success("上传成功！", result);
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(String uuid) {
        LOG.debug("下载文件标识【{}】", uuid);
        ResponseEntity<byte[]> result = null;
        InputStream inputStream = null;
        try {
            // 获取文件信息
            ResultBean<AttachmentDTO> resultBean = attachmentService.queryAttachmentByUuid(uuid);
            if (!resultBean.isSuccess() || resultBean.getData() == null) {
                LOG.debug("当前下载文件编号不存在！");
                return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
            }
            AttachmentDTO attachmentDTO = resultBean.getData();
            String filePath = attachmentDTO.getActualPath();
            LOG.debug("当前下载文件目录：【{}】", filePath);
            inputStream = new FileInputStream(new File(filePath));
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            HttpHeaders heads = new HttpHeaders();
            // 处理IE下载文件的中文名称乱码的问题，采用原上传文件名
            String fileName = new String(attachmentDTO.getOriginalFilename().getBytes(), Charset.forName("iso-8859-1"));
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

    @Override
    public ResponseEntity<byte[]> show(String uuid) {
        LOG.debug("显示文件标识【{}】", uuid);
        ResponseEntity<byte[]> result = null;
        InputStream inputStream = null;
        try {
            // 获取文件信息
            ResultBean<AttachmentDTO> resultBean = attachmentService.queryAttachmentByUuid(uuid);
            if (!resultBean.isSuccess() || resultBean.getData() == null) {
                LOG.debug("当前显示的文件编号不存在！");
                return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
            }
            AttachmentDTO attachmentDTO = resultBean.getData();
            String filePath = attachmentDTO.getActualPath();
            LOG.debug("当前显示的文件目录：【{}】，文件类型：【{}】。", filePath, attachmentDTO.getType());
            File file = new File(filePath);
            inputStream = new FileInputStream(file);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            HttpHeaders heads = new HttpHeaders();
            heads.add(HttpHeaders.CONTENT_TYPE, attachmentDTO.getType());
            heads.add(HttpHeaders.CONTENT_LENGTH, file.length() + "");
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

    @Override
    public ResultBean<String> showImage(String uuid) {
        LOG.debug("显示文件标识【{}】", uuid);
        // 获取文件信息
        ResultBean<AttachmentDTO> resultBean = attachmentService.queryAttachmentByUuid(uuid);
        if (!resultBean.isSuccess() || resultBean.getData() == null) {
            LOG.debug("当前显示的文件编号不存在！");
            return ResultBean.failed("当前文件不存在！");
        }
        AttachmentDTO attachmentDTO = resultBean.getData();
        String filePath = fileShowUrl + attachmentDTO.getAccessPath();
        LOG.debug("当前显示的文件访问路径：【{}】，文件类型：【{}】。", filePath, attachmentDTO.getType());
        return ResultBean.successForData(filePath);
    }

    public static void main(String[] args) {
        String filePath = "E:\\upload\\portrait\\2019\\11\\19\\201911191546551724579.gif";
        String fileSeparator = File.separator;
        String fileName = "header.gif";
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(GUIDUtils.makeUploadFileName() + suffix);
        System.out.println(fileSeparator);
        System.out.println(filePath);
        System.out.println(filePath.replace("/", fileSeparator));
        MagicMatch magicMatch;
        try {
            magicMatch = Magic.getMagicMatch(new File(filePath), false);
        } catch (Exception exp) {
            exp.printStackTrace();
            return;
        }
        String mimeType = magicMatch.getMimeType();
        System.out.println("file mime type is : " + mimeType);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        System.out.println(uuid);
        System.out.println(GUIDUtils.makeUUID());

    }
}
