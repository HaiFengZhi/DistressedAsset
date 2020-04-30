package com.distressed.asset.portal.dao;

import com.distressed.asset.portal.mapping.Area;

import java.util.List;

public interface AreaDAO {
    int deleteByPrimaryKey(String code);

    int insert(Area record);

    int insertSelective(Area record);

    Area selectByPrimaryKey(String code);

    int updateByPrimaryKeySelective(Area record);

    int updateByPrimaryKey(Area record);

    List<Area> selectAll();

    /**
     * 根据地区编号获取下级地区列表。
     *
     * @param code 地区编号。
     * @return 地区列表。
     */
    List<Area> selectByParentCode(String code);
}