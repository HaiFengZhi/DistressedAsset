package com.distressed.asset.portal.dao;

import com.distressed.asset.portal.mapping.BaseRoleResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseRoleResourceDAO {
    int deleteByPrimaryKey(BaseRoleResource key);

    int insert(BaseRoleResource record);

    int insertSelective(BaseRoleResource record);

    List<BaseRoleResource> selectAll();

    /**
     * 批量添加数据。
     *
     * @param dataList 批量列表。
     * @return 返回条数。
     */
    int insertBatch(@Param("dataList")List<BaseRoleResource> dataList);

    /**
     * 根据角色编号，删除当前角色下的所有角色权限关联数据。
     *
     * @param roleId 角色编号。
     * @return 返回条数。
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限编号，删除当前角色关联的所有角色权限关联数据。
     *
     * @param resourceId 权限编号。
     * @return 返回条数。
     */
    int deleteByResourceId(@Param("resourceId") Long resourceId);

    /**
     * 根据角色编号获取该角色所有权限列表。
     *
     * @param roleId 角色编号。
     * @return 返回结果。
     */
    List<BaseRoleResource> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色编号，将对应的权限结果拼装成一个字符串。
     *
     * @param roleId 角色编号。
     * @return 结果。
     */
    String selectConcatByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限编号获取对应的角色集合字符串。
     *
     * @param resourceId 权限编号。
     * @return 角色集合字符串。
     */
    String selectRoleIdsByResourceId(@Param("resourceId") Long resourceId);

    /**
     * 根据角色编号集获取对应的权限列表集合字符串。
     *
     * @param roleIds 角色集合字符串。
     * @return 权限集合字符串。
     */
    String selectConcatByRoleIds(@Param("roleIds") String[] roleIds);

}