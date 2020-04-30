package com.distressed.asset.portal.dao;

import com.distressed.asset.portal.mapping.BaseResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BaseResourceDAO {
    int deleteByPrimaryKey(Long id);

    int insert(BaseResource record);

    int insertSelective(BaseResource record);

    BaseResource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BaseResource record);

    int updateByPrimaryKey(BaseResource record);

    List<BaseResource> selectAll();

    /**
     * 根据条件查询当前菜单权限列表。
     *
     * @param params 查询条件。
     * @return 菜单权限列表。
     */
    List<BaseResource> selectBy(@Param("params") Map<String, Object> params);

    /**
     * 获取当前可选择的菜单和模块列表，排除不显示和按钮列表。
     *
     * @return 菜单权限列表。
     */
    List<BaseResource> selectMenuList();

    /**
     * 根据父节点获取所有下级节点。
     *
     * @param parentId 父节点。
     * @return 所有下级节点。
     */
    List<BaseResource> selectByParentId(@Param(value = "parentId") Long parentId);

    /**
     * 根据编号集合字符串获取权限列表。
     *
     * @param ids 编号集合字符串。
     * @param parentId 父节点编号，可为空。
     * @return 权限列表。
     */
    List<BaseResource> selectByIds(@Param("ids") String[] ids, @Param(value = "parentId") Long parentId);
}