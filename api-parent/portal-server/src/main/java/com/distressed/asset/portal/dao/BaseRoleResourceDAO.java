package com.distressed.asset.portal.dao;

import com.distressed.asset.portal.mapping.BaseRoleResource;

public interface BaseRoleResourceDAO {
    int deleteByPrimaryKey(BaseRoleResource key);

    int insert(BaseRoleResource record);

    int insertSelective(BaseRoleResource record);
}