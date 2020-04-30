package com.distressed.asset.portal.mapping;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 附件是系统中公共的数据结构，可以和其他需要附件的数据结构进行泛化关联(any)。
 *
 * @author zhaohc
 * @date 2020-04-30
 */
public class Attachment implements Serializable {
    /**
     * 主键标识，自增。
     */
    private Long id;

    /**
     * 
     */
    private Long userId;

    /**
     * 附件UUID，用于下载暴露字段
     */
    private String uuid;

    /**
     * 附件所属模块（用于区分文件目录，参见枚举：UploadModelEnum）
     */
    private String model;

    /**
     * 上传时间，格式：yyyy-MM-dd hh:mm:ss。
     */
    private Date uploadTime;

    /**
     * 附件的原始文件名称。
     */
    private String originalFilename;

    /**
     * 文件大小，以K为单位。
     */
    private Integer fileSize;

    /**
     * 访问路径的后缀(可能是服务器存储路径的一部分后缀)，不包含WEB访问的根上下文。举例：/upload/create.jpg。
     */
    private String accessPath;

    /**
     * 附件在服务器保存的绝对路径。
     */
    private String actualPath;

    /**
     * 备注描述。
     */
    private String description;

    /**
     * 附件类型。'IMG'：图片；'PDF'：PDF；'DOC'：DOC/DOCX；'EXL'：EXCEL；'TXT'：文本文件；'OTE'：其他。
     */
    private String type;

    /**
     * 状态：0正常，1删除
     */
    private Integer status;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * t_attachment
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键标识，自增。
     * @return id 主键标识，自增。
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键标识，自增。
     * @param id 主键标识，自增。
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return user_id 
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId 
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 附件UUID，用于下载暴露字段
     * @return uuid 附件UUID，用于下载暴露字段
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * 附件UUID，用于下载暴露字段
     * @param uuid 附件UUID，用于下载暴露字段
     */
    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    /**
     * 附件所属模块（用于区分文件目录，参见枚举：UploadModelEnum）
     * @return model 附件所属模块（用于区分文件目录，参见枚举：UploadModelEnum）
     */
    public String getModel() {
        return model;
    }

    /**
     * 附件所属模块（用于区分文件目录，参见枚举：UploadModelEnum）
     * @param model 附件所属模块（用于区分文件目录，参见枚举：UploadModelEnum）
     */
    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    /**
     * 上传时间，格式：yyyy-MM-dd hh:mm:ss。
     * @return upload_time 上传时间，格式：yyyy-MM-dd hh:mm:ss。
     */
    public Date getUploadTime() {
        return uploadTime;
    }

    /**
     * 上传时间，格式：yyyy-MM-dd hh:mm:ss。
     * @param uploadTime 上传时间，格式：yyyy-MM-dd hh:mm:ss。
     */
    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    /**
     * 附件的原始文件名称。
     * @return original_filename 附件的原始文件名称。
     */
    public String getOriginalFilename() {
        return originalFilename;
    }

    /**
     * 附件的原始文件名称。
     * @param originalFilename 附件的原始文件名称。
     */
    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename == null ? null : originalFilename.trim();
    }

    /**
     * 文件大小，以K为单位。
     * @return file_size 文件大小，以K为单位。
     */
    public Integer getFileSize() {
        return fileSize;
    }

    /**
     * 文件大小，以K为单位。
     * @param fileSize 文件大小，以K为单位。
     */
    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * 访问路径的后缀(可能是服务器存储路径的一部分后缀)，不包含WEB访问的根上下文。举例：/upload/create.jpg。
     * @return access_path 访问路径的后缀(可能是服务器存储路径的一部分后缀)，不包含WEB访问的根上下文。举例：/upload/create.jpg。
     */
    public String getAccessPath() {
        return accessPath;
    }

    /**
     * 访问路径的后缀(可能是服务器存储路径的一部分后缀)，不包含WEB访问的根上下文。举例：/upload/create.jpg。
     * @param accessPath 访问路径的后缀(可能是服务器存储路径的一部分后缀)，不包含WEB访问的根上下文。举例：/upload/create.jpg。
     */
    public void setAccessPath(String accessPath) {
        this.accessPath = accessPath == null ? null : accessPath.trim();
    }

    /**
     * 附件在服务器保存的绝对路径。
     * @return actual_path 附件在服务器保存的绝对路径。
     */
    public String getActualPath() {
        return actualPath;
    }

    /**
     * 附件在服务器保存的绝对路径。
     * @param actualPath 附件在服务器保存的绝对路径。
     */
    public void setActualPath(String actualPath) {
        this.actualPath = actualPath == null ? null : actualPath.trim();
    }

    /**
     * 备注描述。
     * @return description 备注描述。
     */
    public String getDescription() {
        return description;
    }

    /**
     * 备注描述。
     * @param description 备注描述。
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 附件类型。'IMG'：图片；'PDF'：PDF；'DOC'：DOC/DOCX；'EXL'：EXCEL；'TXT'：文本文件；'OTE'：其他。
     * @return type 附件类型。'IMG'：图片；'PDF'：PDF；'DOC'：DOC/DOCX；'EXL'：EXCEL；'TXT'：文本文件；'OTE'：其他。
     */
    public String getType() {
        return type;
    }

    /**
     * 附件类型。'IMG'：图片；'PDF'：PDF；'DOC'：DOC/DOCX；'EXL'：EXCEL；'TXT'：文本文件；'OTE'：其他。
     * @param type 附件类型。'IMG'：图片；'PDF'：PDF；'DOC'：DOC/DOCX；'EXL'：EXCEL；'TXT'：文本文件；'OTE'：其他。
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 状态：0正常，1删除
     * @return status 状态：0正常，1删除
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 状态：0正常，1删除
     * @param status 状态：0正常，1删除
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 修改时间
     * @return update_time 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 修改时间
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object target) {
        return EqualsBuilder.reflectionEquals(this, target, false);
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(17, 37, this);
    }

}