package com.distressed.asset.portal.transform;


import com.distressed.asset.common.transform.DataTransform;
import com.distressed.asset.portal.dto.AttachmentDTO;
import com.distressed.asset.portal.mapping.Attachment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 附件是系统中公共的数据结构。
 *
 * {@link Attachment} <->{@link AttachmentDTO}互转辅助对象
 * @author yanjie wan at 2019-10-28 15:00
 */
public class AttachmentDT implements DataTransform<Attachment, AttachmentDTO> {
    @Override
    public AttachmentDTO from(Attachment attachment) {
        if(attachment==null)
        {
            return null;
        }
        AttachmentDTO attachmentDTO=new AttachmentDTO();
        attachmentDTO.setId(attachment.getId());
        attachmentDTO.setModel(attachment.getModel());
        attachmentDTO.setUuid(attachment.getUuid());
        attachmentDTO.setUserId(attachment.getUserId());
        attachmentDTO.setUploadTime(attachment.getUploadTime());
        attachmentDTO.setOriginalFilename(attachment.getOriginalFilename());
        attachmentDTO.setFileSize(attachment.getFileSize());
        attachmentDTO.setAccessPath(attachment.getAccessPath());
        attachmentDTO.setActualPath(attachment.getActualPath());
        attachmentDTO.setDescription(attachment.getDescription());
        attachmentDTO.setType(attachment.getType());
        attachmentDTO.setStatus(attachment.getStatus());
        attachmentDTO.setUpdateTime(attachment.getUpdateTime());
        return attachmentDTO;
    }

    @Override
    public AttachmentDTO from(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<AttachmentDTO> fromByList(List<Attachment> list) {
        if(list==null)
        {
            return null;
        }
        List<AttachmentDTO> dtoList=new ArrayList<AttachmentDTO>();
        for(Attachment attachment:list)
        {
            dtoList.add(from(attachment));
        }
        return dtoList;
    }

    @Override
    public List<AttachmentDTO> fromByListMap(List<Map<String, Object>> list) {
        return null;
    }

    @Override
    public Attachment to(AttachmentDTO attachmentDTO) {
        if(attachmentDTO==null)
        {
            return null;
        }
        Attachment attachment=new Attachment();
        attachment.setId(attachmentDTO.getId());
        attachment.setUserId(attachmentDTO.getUserId());
        attachment.setModel(attachmentDTO.getModel());
        attachment.setUuid(attachmentDTO.getUuid());
        attachment.setUploadTime(attachmentDTO.getUploadTime());
        attachment.setOriginalFilename(attachmentDTO.getOriginalFilename());
        attachment.setFileSize(attachmentDTO.getFileSize());
        attachment.setAccessPath(attachmentDTO.getAccessPath());
        attachment.setActualPath(attachmentDTO.getActualPath());
        attachment.setDescription(attachmentDTO.getDescription());
        attachment.setType(attachmentDTO.getType());
        attachment.setStatus(attachmentDTO.getStatus());
        attachment.setUpdateTime(attachmentDTO.getUpdateTime());
        return attachment;
    }
}