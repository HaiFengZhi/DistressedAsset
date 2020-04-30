package com.distressed.asset.portal.dao;

import com.distressed.asset.portal.mapping.Attachment;

import java.util.List;

public interface AttachmentDAO {
    int deleteByPrimaryKey(Long id);

    int insert(Attachment record);

    int insertSelective(Attachment record);

    Attachment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Attachment record);

    int updateByPrimaryKey(Attachment record);

    List<Attachment> selectAll();

    /**
     * 根据唯一键uuid获取对应的附件信息。
     *
     * @param uuid 唯一索引uuid。
     * @return 附件信息。
     */
    Attachment selectByUuid(String uuid);

    List<Attachment> selectPass();

    int deletePass(Long id);
}