package com.distressed.asset.portal.dao;

import com.distressed.asset.portal.mapping.AdminUserRole;

public interface AdminUserRoleDAO {
    int deleteByPrimaryKey(AdminUserRole key);

    int insert(AdminUserRole record);

    int insertSelective(AdminUserRole record);
}