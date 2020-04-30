package com.distressed.asset.portal.dao;

import com.distressed.asset.portal.mapping.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RoleDAO {
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> selectAll();

    /**
     * 根据条件查询角色列表。
     *
     * @param params 查询条件。
     * @return 角色列表。
     */
    List<Role> selectBy(@Param("params") Map<String, Object> params);

    /**
     * 根据角色编号集合查询所有角色信息。
     *
     * @param ids 角色编号【以,分隔，1,2,3】
     * @return 角色列表。
     */
    List<Role> selectByIds(@Param("ids") String[] ids);

}