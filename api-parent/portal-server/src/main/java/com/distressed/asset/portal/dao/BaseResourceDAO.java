package com.distressed.asset.portal.dao;

import com.distressed.asset.portal.mapping.BaseResource;

public interface BaseResourceDAO {
    int deleteByPrimaryKey(Long id);

    int insert(BaseResource record);

    int insertSelective(BaseResource record);

    BaseResource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BaseResource record);

    int updateByPrimaryKey(BaseResource record);
}