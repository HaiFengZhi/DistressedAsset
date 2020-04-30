package com.distressed.asset.portal.dao;

import com.distressed.asset.portal.mapping.Attachment;

public interface AttachmentDAO {
    int deleteByPrimaryKey(Long id);

    int insert(Attachment record);

    int insertSelective(Attachment record);

    Attachment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Attachment record);

    int updateByPrimaryKey(Attachment record);
}