/*************************************************************************
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *
 *                COPYRIGHT (C) HONGLING CAPITAL CORPORATION 2012
 *    ALL RIGHTS RESERVED BY HONGLING CAPITAL CORPORATION. THIS PROGRAM
 * MUST BE USED  SOLELY FOR THE PURPOSE FOR WHICH IT WAS FURNISHED BY
 * HONGLING CAPITAL CORPORATION. NO PART OF THIS PROGRAM MAY BE REPRODUCED
 * OR DISCLOSED TO OTHERS,IN ANY FORM, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF HONGLING CAPITAL CORPORATION. USE OF COPYRIGHT NOTICE
 DOES NOT EVIDENCE PUBLICATION OF THE PROGRAM.
 *                  HONGLING CAPITAL CONFIDENTIAL AND PROPRIETARY
 *************************************************************************/

package com.distressed.asset.portal.service;

import com.distressed.asset.common.layui.DTree;
import com.distressed.asset.common.layui.TabMenu;
import com.distressed.asset.common.layui.XmSelect;
import com.distressed.asset.common.result.ResultBean;
import com.distressed.asset.portal.dto.AdminUserDTO;
import com.distressed.asset.portal.dto.BaseResourceDTO;
import com.distressed.asset.portal.dto.BaseRoleResourceDTO;
import com.distressed.asset.portal.dto.RoleDTO;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 权限菜单业务接口类。
 *
 * @author hongchao zhao at 2019-10-25 16:55
 */
@FeignClient(value = "portal-server", contextId = "menuService")
public interface MenuService {

    /**
     * 查询所有菜单。
     *
     * @return 所有菜单集合。
     */
    @PostMapping("/getMenuList")
    ResultBean<List<BaseResourceDTO>> getMenuList();

    /**
     * 根据查询条件分页查询菜单权限集合。
     *
     * <p>
     *     注意：分页使用了PageHelper插件，不需要写count方法，直接写一个条件查询即可，PageHelper会完成分页相关功能。
     * </p>
     *
     * @param params 查询条件。
     * @param pageNum 当前页码。
     * @param pageSize 每页显示条数。
     * @return 菜单权限集合。
     */
    @PostMapping("/pageMenuList")
    PageInfo<BaseResourceDTO> pageMenuList(@RequestBody(required = false) Map<String, Object> params,
                                           @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize);

    /**
     * 根据父节点递归遍历所有下级菜单，用于下拉列表树形结构。
     *
     * @param parentId 父节点。
     * @return 所有下级菜单集合。
     */
    @PostMapping("/getMenuListByParentId")
    ResultBean<List<DTree>> getMenuListByParentId(@RequestParam("parentId") Long parentId);

    /**
     * 根据父节点获取所有下级菜单列表。
     *
     * @param parentId 父节点。
     * @param adminUserId 登陆用户编号。
     * @return 下级菜单列表。
     */
    @PostMapping("/getMenuByParentId")
    ResultBean<List<BaseResourceDTO>> getMenuByParentId(@RequestParam("parentId") Long parentId, @RequestParam(value = "adminUserId") Long adminUserId);

    /**
     * 根据父节点获取所有下级菜单列表，返回结果为TabMenu模式数据。
     *
     * @param parentId 菜单父节点编号。
     * @param adminUserId 当前登陆用户编号。
     * @return TabMenu菜单数据。
     */
    @PostMapping("/getTabMenuByParentId")
    ResultBean<List<TabMenu>> getTabMenuByParentId(@RequestParam("parentId") Long parentId, @RequestParam(value = "adminUserId") Long adminUserId);

    /**
     * 获取xm-select的数据。
     *
     * @return 数据。
     */
    @PostMapping("/getXmSelectData")
    ResultBean<List<Map<String,Object>>> getXmSelectData();

    /**
     * 新增或修改菜单权限数据。
     *
     * @param baseResourceDTO 菜单权限数据。
     * @return 变更结果。
     */
    @PostMapping("/saveOrUpdateBaseResource")
    ResultBean saveOrUpdateBaseResource(@RequestBody BaseResourceDTO baseResourceDTO);

    /**
     * 根据编号获取权限资源详情。
     *
     * @param id 权限编号。
     * @return 权限资源详情。
     */
    @PostMapping("/getBaseResourceById")
    ResultBean<BaseResourceDTO> getBaseResourceById(@RequestParam("id") Long id);

    /**
     * 根据权限菜单编号删除当前菜单。
     *
     * @param id 权限编号。
     * @return 变更结果
     */
    @PostMapping("/deleteBaseResourceById")
    ResultBean deleteBaseResourceById(@RequestParam("id") Long id);

    /**
     * 根据条件分页查询角色列表记录。
     *
     * @param params 查询条件。
     * @param pageNum 当前页码。
     * @param pageSize 每页显示条数。
     * @return 角色列表记录。
     */
    @PostMapping("/getRoleList")
    PageInfo<RoleDTO> getRoleList(@RequestBody(required = false) Map<String, Object> params,
                                  @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize);

    /**
     * 新增或修改角色。
     *
     * @param roleDTO 角色信息。
     * @return 变更结果。
     */
    @PostMapping("/saveOrUpdateRole")
    ResultBean saveOrUpdateRole(@RequestBody RoleDTO roleDTO);

    /**
     * 根据编号删除角色信息。
     *
     * @param id 角色编号。
     * @return 结果。
     */
    @PostMapping("/deleteRole")
    ResultBean deleteRole(@RequestParam("id") Long id);

    /**
     * 根据角色编号获取角色信息。
     *
     * @param id 角色编号。
     * @return 角色信息。
     */
    @PostMapping("/getRoleById")
    ResultBean<RoleDTO> getRoleById(@RequestParam("id") Long id);

    /**
     * 获取当前所有可用的角色列表【用于下拉分配用户角色】。
     *
     * @return 角色列表。
     */
    @PostMapping("/getRoleSelectList")
    ResultBean<List<XmSelect>> getRoleSelectList();

    /**
     * 分配角色相关权限。
     *
     * @param roleId 角色编号。
     * @param menuIds 权限列表。
     * @return 结果。
     */
    @PostMapping("/saveRoleMenuList")
    ResultBean saveRoleMenuList(@RequestParam("roleId") Long roleId, @RequestParam("menuIds") String menuIds);

    /**
     * 根据角色编号获取关联的权限列表。
     *
     * @param roleId 角色编号。
     * @return 权限列表。
     */
    @PostMapping("/getBaseRoleResource")
    ResultBean<List<BaseRoleResourceDTO>> getBaseRoleResource(@RequestParam("roleId") Long roleId);

    /**
     * 根据角色获取一个权限编号集，用于赋值。
     *
     * @param roleId 角色编号。
     * @return 结果集。
     */
    @PostMapping("/getRoleBaseResource")
    ResultBean<String> getRoleBaseResource(@RequestParam("roleId") Long roleId);

    /**
     * 根据查询条件分页查询后台用户信息列表。
     *
     * @param params 查询条件。
     * @param pageNum 当前页。
     * @param pageSize 每页显示条数。
     * @return 用户列表信息。
     */
    @PostMapping("/getAdminList")
    PageInfo<AdminUserDTO> getAdminList(@RequestBody(required = false) Map<String, Object> params,
                                        @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize);

    /**
     * 新增或修改后台用户信息。
     *
     * @param adminUserDTO 后台用户信息。
     * @return 结果。
     */
    @PostMapping("/saveOrUpdateAdmin")
    ResultBean saveOrUpdateAdmin(@RequestBody AdminUserDTO adminUserDTO);

    /**
     * 变更管理员信息。
     *
     * @param adminUserDTO 管理员信息。
     * @return 结果。
     */
    @PostMapping("/updateAdmin")
    ResultBean updateAdmin(@RequestBody AdminUserDTO adminUserDTO);

    /**
     * 初始化管理员用户密码。
     *
     * @param id       管理员编号。
     * @param password 密码。
     * @return 结果。
     */
    @PostMapping("/generateAdminPwd")
    ResultBean generateAdminPwd(@RequestParam("adminId") Long id, @RequestParam("password") String password, @RequestParam("modifyPwdOr") Integer modifyPwdOr);

    /**
     * 根据用户编号删除用户信息。
     *
     * @param id 用户编号。
     * @return 结果。
     */
    @PostMapping("/deleteAdmin")
    ResultBean deleteAdmin(@RequestParam("id") Long id);

    /**
     * 根据用户编号获取用户信息。
     *
     * @param id 用户编号。
     * @return 结果。
     */
    @PostMapping("/getAdminById")
    ResultBean<AdminUserDTO> getAdminById(@RequestParam("id") Long id);

    /**
     * 为后台用户分配角色。
     *
     * @param adminId 用户编号。
     * @param roleIds 角色列表。
     * @return 结果。
     */
    @PostMapping("/saveAdminRoles")
    ResultBean saveAdminRoles(@RequestParam("adminId") Long adminId, @RequestParam("roleIds") String roleIds);

    /**
     * 根据管理员编号获取相应的管理角色列表组合。
     *
     * @param adminId 管理员编号。
     * @return 角色字段列表。
     */
    @PostMapping("/getAdminRoles")
    ResultBean<String> getAdminRoles(@RequestParam("adminId") Long adminId);

    /**
     * 根据用户编号获取用户角色。
     *
     * @param adminUserId 管理员编号。
     * @return 管理员角色。
     */
    @PostMapping("/selectRolesByUserId")
    ResultBean<List<RoleDTO>> selectRolesByUserId(@RequestParam("userId") Long adminUserId);

    /**
     * 根据用户编号获取当前用户所有权限列表。
     *
     * @param adminUserId 管理员用户编号。
     * @return 权限列表。
     */
    @PostMapping("/selectResourcesByUserId")
    ResultBean<List<BaseResourceDTO>> selectResourcesByUserId(@RequestParam("userId") Long adminUserId);

    /**
     * 根据角色编号获取管理员编号列表。
     *
     * @param roleId 角色编号。
     * @return 管理员编号集合字符串。
     */
    @PostMapping("/selectAdminIdsByRoleId")
    ResultBean<String> selectAdminIdsByRoleId(@RequestParam("roleId") Long roleId);

    /**
     * 根据权限编号获取对应的角色编号列表。
     *
     * @param resourceId 权限编号。
     * @return 角色编号列表。
     */
    @PostMapping("/selectRoleIdsByResourceId")
    ResultBean<String> selectRoleIdsByResourceId(@RequestParam("resourceId") Long resourceId);


}
