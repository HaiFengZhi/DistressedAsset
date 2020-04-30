package com.distressed.asset.portal.dao;

import com.distressed.asset.portal.mapping.AdminUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminUserRoleDAO {
    int deleteByPrimaryKey(AdminUserRole key);

    int insert(AdminUserRole record);

    int insertSelective(AdminUserRole record);

    List<AdminUserRole> selectAll();

    /**
     * 根据用户编号删除相关联的角色列表。
     *
     * @param userId 用户编号。
     * @return 返回行数。
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据角色编号删除相关联的用户列表。
     *
     * @param roleId 角色编号。
     * @return 返回行数。
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量添加用户与角色关联信息。
     *
     * @param dataList 批量信息。
     * @return 返回行数。
     */
    int insertBatch(@Param("dataList")List<AdminUserRole> dataList);

    /**
     * 根据管理员编号获取匹配的角色字段。
     *
     * @param userId 管理员编号。
     * @return 角色列表。
     */
    String selectConcatByUserId(@Param("userId") Long userId);

    /**
     * 根据角色编号获取用户列表字段。
     *
     * @param roleId 角色编号。
     * @return 用户列表字段。
     */
    String selectConcatByRoleId(@Param("roleId") Long roleId);
}