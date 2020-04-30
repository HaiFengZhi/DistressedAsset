package com.distressed.asset.portal.dao;

import com.distressed.asset.portal.mapping.Role;

public interface RoleDAO {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}