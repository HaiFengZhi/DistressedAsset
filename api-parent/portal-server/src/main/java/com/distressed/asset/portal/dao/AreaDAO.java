package com.distressed.asset.portal.dao;

import com.distressed.asset.portal.mapping.Area;

public interface AreaDAO {
    int deleteByPrimaryKey(String code);

    int insert(Area record);

    int insertSelective(Area record);

    Area selectByPrimaryKey(String code);

    int updateByPrimaryKeySelective(Area record);

    int updateByPrimaryKey(Area record);
}