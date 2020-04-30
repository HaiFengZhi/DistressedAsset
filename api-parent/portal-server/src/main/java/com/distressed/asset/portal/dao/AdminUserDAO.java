package com.distressed.asset.portal.dao;

import com.distressed.asset.portal.mapping.AdminUser;

public interface AdminUserDAO {
    int deleteByPrimaryKey(Long id);

    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    AdminUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdminUser record);

    int updateByPrimaryKey(AdminUser record);
}