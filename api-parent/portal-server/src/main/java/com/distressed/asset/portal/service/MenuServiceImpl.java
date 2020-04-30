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

import com.distressed.asset.common.annotation.EncryptMethod;
import com.distressed.asset.common.annotation.LocalTransaction;
import com.distressed.asset.common.cache.RedisService;
import com.distressed.asset.common.exception.RowsUpdateNotMatchException;
import com.distressed.asset.common.layui.CheckArr;
import com.distressed.asset.common.layui.DTree;
import com.distressed.asset.common.layui.TabMenu;
import com.distressed.asset.common.layui.XmSelect;
import com.distressed.asset.common.result.Result;
import com.distressed.asset.common.result.ResultBean;
import com.distressed.asset.common.utils.*;
import com.distressed.asset.portal.dao.*;
import com.distressed.asset.portal.dto.AdminUserDTO;
import com.distressed.asset.portal.dto.BaseResourceDTO;
import com.distressed.asset.portal.dto.BaseRoleResourceDTO;
import com.distressed.asset.portal.dto.RoleDTO;
import com.distressed.asset.portal.mapping.*;
import com.distressed.asset.portal.transform.AdminUserDT;
import com.distressed.asset.portal.transform.BaseResourceDT;
import com.distressed.asset.portal.transform.BaseRoleResourceDT;
import com.distressed.asset.portal.transform.RoleDT;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限菜单业务实现类。
 *
 * @author hongchao zhao at 2019-10-25 17:06
 */
@RestController
@CacheConfig(cacheNames = "menuCache")
public class MenuServiceImpl implements MenuService {

    private static Logger LOG = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Resource
    private BaseResourceDAO baseResourceDAO;
    @Resource
    private BaseRoleResourceDAO baseRoleResourceDAO;
    @Resource
    private RoleDAO roleDAO;
    @Resource
    private AdminUserDAO adminUserDAO;
    @Resource
    private AdminUserRoleDAO adminUserRoleDAO;
    @Resource
    private RedisService redisService;

    @Value("${distressed.asset.file.manager.show}")
    private String filePath;


    @Override
    //key直接指定当前的键值
    @Cacheable(keyGenerator = "cacheKeyGenerator",unless = "#result.data == null")
    public ResultBean<List<BaseResourceDTO>> getMenuList() {
        return ResultBean.successForData(new BaseResourceDT().fromByList(baseResourceDAO.selectAll()));
    }

    @Override
    //keyGenerator指定缓存键生成规则
    @Cacheable(keyGenerator = "cacheKeyGenerator",unless = "#result.total == 0")
    public PageInfo<BaseResourceDTO> pageMenuList(Map<String, Object> params, int pageNum, int pageSize) {
        //设置分页开始
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<BaseResourceDTO> list = new BaseResourceDT().fromByList(baseResourceDAO.selectBy(params));
        //4. 根据返回的集合，创建PageInfo对象
        PageInfo<BaseResourceDTO> info = new PageInfo<>(list);
        info.setTotal(page.getTotal());
        return info;
    }

    @Override
    @Cacheable(keyGenerator = "cacheKeyGenerator",unless = "#result.data == null ")
    public ResultBean<List<DTree>> getMenuListByParentId(Long parentId) {
        return ResultBean.successForData(getChildrenList(parentId));
    }

    @Override
    @Cacheable(keyGenerator = "cacheKeyGenerator",unless = "#result.data == null ")
    public ResultBean<List<BaseResourceDTO>> getMenuByParentId(Long parentId, Long adminUserId) {
        if (CommonUtils.isBlank(parentId, adminUserId)) {
            return ResultBean.failed(Result.GLOBAL.MISSING_PARAMS);
        }
        ResultBean<String> idsBean = getResourcesByUserId(adminUserId);
        if (!idsBean.isSuccess()) {
            return ResultBean.failed(idsBean.getMessage());
        }
        return ResultBean.successForData(getChildrenMenu(parentId, idsBean.getData()));
    }

    @Override
    @Cacheable(keyGenerator = "cacheKeyGenerator",unless = "#result.data == null ")
    public ResultBean<List<TabMenu>> getTabMenuByParentId(Long parentId, Long adminUserId) {
        if (CommonUtils.isBlank(parentId, adminUserId)) {
            return ResultBean.failed(Result.GLOBAL.MISSING_PARAMS);
        }
        ResultBean<String> idsBean = getResourcesByUserId(adminUserId);
        if (!idsBean.isSuccess()) {
            return ResultBean.failed(idsBean.getMessage());
        }
        return ResultBean.successForData(getChildrenTabMenu(parentId, idsBean.getData()));
    }

    /**
     * 辅助方法：根据登陆用户编号，获取可用权限编号集合字符串。
     *
     * @param adminUserId 登陆用户编号。
     * @return 可用权限编号集合字符串。
     */
    private ResultBean<String> getResourcesByUserId(Long adminUserId){
        String roleIds = adminUserRoleDAO.selectConcatByUserId(adminUserId);
        if (MyStringUtils.isBlank(roleIds)) {
            return ResultBean.failed("当前用户未分配角色！");
        }
        String resourceIds = baseRoleResourceDAO.selectConcatByRoleIds(roleIds.split(","));
        if (MyStringUtils.isBlank(resourceIds)) {
            return ResultBean.failed("当前用户未分配权限！");
        }
        return ResultBean.successForData(resourceIds);
    }

    @Override
    public ResultBean<List<Map<String, Object>>> getXmSelectData() {
        List<BaseResource> baseResources = baseResourceDAO.selectAll();
        List<Map<String, Object>> dataList = new ArrayList<>();
        if (baseResources != null && baseResources.size() > 0) {
            Map<String,Object> data;
            for (BaseResource baseResource : baseResources){
                data = new HashMap<>();
                data.put("value",baseResource.getId());
                data.put("name",baseResource.getName());
                dataList.add(data);
            }
        }
        return ResultBean.successForData(dataList);
    }

    @Override
    //更新或是新增时，一样清掉缓存，用于查询时都为最新结果
    @CacheEvict(cacheNames = "menuCache", allEntries = true)
    public ResultBean saveOrUpdateBaseResource(BaseResourceDTO baseResourceDTO) {
        //DTO转化为数据库对象
        BaseResource baseResource = new BaseResourceDT().to(baseResourceDTO);
        baseResource.setStatus(0);
        int count = 0;
        if (baseResource.getId()==null){
            count = baseResourceDAO.insert(baseResource);
        }else {
            count = baseResourceDAO.updateByPrimaryKey(baseResource);
        }
        if (count !=1){
            throw new RowsUpdateNotMatchException("变更菜单权限数据异常，菜单数据：" + baseResource.toString());
        }
        return ResultBean.success();
    }

    @Override
    public ResultBean<BaseResourceDTO> getBaseResourceById(Long id) {
        BaseResource baseResource = baseResourceDAO.selectByPrimaryKey(id);
        if (baseResource == null){
            return ResultBean.failed("当前权限菜单编号不存在！");
        }
        return ResultBean.successForData(new BaseResourceDT().from(baseResource));
    }

    @Override
    @LocalTransaction
    //删除时，清空当前缓存目录下所有缓存
    @CacheEvict(cacheNames = "menuCache", allEntries = true)
    public ResultBean deleteBaseResourceById(Long id) {
        //删除权限菜单记录
        LOG.debug("删除菜单权限【{}】信息",id);
        baseResourceDAO.deleteByPrimaryKey(id);
        //删除菜单要同时删除相关联的角色菜单关联记录
        int count = baseRoleResourceDAO.deleteByResourceId(id);
        LOG.debug("删除菜单权限【{}】与角色关联信息【{}】条", id, count);
        return ResultBean.success();
    }

    @Override
    public PageInfo<RoleDTO> getRoleList(Map<String, Object> params, int pageNum, int pageSize) {
        //设置分页开始
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<RoleDTO> roleList = new RoleDT().fromByList(roleDAO.selectBy(params));
        //4. 根据返回的集合，创建PageInfo对象
        PageInfo<RoleDTO> info = new PageInfo<>(roleList);
        info.setTotal(page.getTotal());
        return info;
    }

    @Override
    //删除时，清空当前缓存目录下所有缓存
    @CacheEvict(cacheNames = "menuCache", allEntries = true)
    public ResultBean saveOrUpdateRole(RoleDTO roleDTO) {
        Role role = new RoleDT().to(roleDTO);
        //变更时间皆为系统当前时间，忽略前端时间
        role.setCreateTime(MyDateUtils.now());
        int count = 0;
        if (role.getId()==null){
            count = roleDAO.insert(role);
        }else {
            count = roleDAO.updateByPrimaryKey(role);
        }
        if (count !=1){
            throw new RowsUpdateNotMatchException("变更角色数据异常，角色数据：" + role.toString());
        }
        return ResultBean.success();
    }

    @Override
    //删除时，清空当前缓存目录下所有缓存
    @CacheEvict(cacheNames = "menuCache", allEntries = true)
    public ResultBean deleteRole(Long id) {
        //删除角色
        LOG.debug("删除系统角色【{}】", id);
        roleDAO.deleteByPrimaryKey(id);
        //删除与该角色关联的权限记录
        int count = baseRoleResourceDAO.deleteByRoleId(id);
        LOG.debug("删除与系统角色【{}】关联的权限记录【{}】条。", id , count);
        //删除与该角色关联的用户记录
        count = adminUserRoleDAO.deleteByRoleId(id);
        LOG.debug("删除与系统角色【{}】关联的用户记录【{}】条。", id ,count);
        return ResultBean.success();
    }

    @Override
    public ResultBean<RoleDTO> getRoleById(Long id) {
        Role role = roleDAO.selectByPrimaryKey(id);
        if (role == null){
            return ResultBean.failed("当前角色编号不存在！");
        }
        return ResultBean.successForData(new RoleDT().from(role));
    }

    @Override
    public ResultBean<List<XmSelect>> getRoleSelectList() {
        List<Role> dataList = roleDAO.selectBy(new HashMap<String, Object>() {{
            put("status", "1");
        }});
        if (dataList == null || dataList.size() < 1) {
            return ResultBean.failed("当前角色列表为空！");
        }
        return ResultBean.successForData(new RoleDT().fromByRoleList(dataList));
    }

    @Override
    @LocalTransaction
    @CacheEvict(cacheNames = "menuCache", allEntries = true)
    public ResultBean saveRoleMenuList(Long roleId, String menuIds) {
        LOG.debug("开始变更当前角色【{}】的权限列表【{}】。", roleId, menuIds);
        if (CommonUtils.isBlank(roleId, menuIds)) {
            return ResultBean.failed("变更角色权限失败，参数不能为空！");
        }
        //直接清除当前角色下的所有权限，
        int count = baseRoleResourceDAO.deleteByRoleId(roleId);
        LOG.debug("删除角色【{}】下的【{}】条权限。", roleId, count);
        //重新指添加新的权限
        String[] menuIdArr = menuIds.split(",");
        List<BaseRoleResource> dataList = new ArrayList<>();
        BaseRoleResource baseRoleResource;
        for (String menuId : menuIdArr) {
            baseRoleResource = new BaseRoleResource();
            baseRoleResource.setRoleId(roleId);
            baseRoleResource.setResourceId(Long.parseLong(menuId));
            dataList.add(baseRoleResource);
        }
        count = baseRoleResourceDAO.insertBatch(dataList);
        LOG.debug("更新角色【{}】下的【{}】条权限。", roleId, count);
        return ResultBean.success();
    }

    @Override
    public ResultBean<List<BaseRoleResourceDTO>> getBaseRoleResource(Long roleId) {
        List<BaseRoleResource> list = baseRoleResourceDAO.selectByRoleId(roleId);
        if (list == null){
            return ResultBean.failed("当前角色未关联权限信息！");
        }
        return ResultBean.successForData(new BaseRoleResourceDT().fromByList(list));
    }

    @Override
    public ResultBean<String> getRoleBaseResource(Long roleId) {
        String resourceIds = baseRoleResourceDAO.selectConcatByRoleId(roleId);
        if (resourceIds == null){
            return ResultBean.failed("当前角色未关联权限信息！");
        }
        return ResultBean.successForData(resourceIds);
    }

    @Override
    @EncryptMethod
    public PageInfo<AdminUserDTO> getAdminList(Map<String, Object> params, int pageNum, int pageSize) {
        //设置分页开始
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<AdminUserDTO> roleList = new AdminUserDT().fromByList(adminUserDAO.selectBy(params));
        //4. 根据返回的集合，创建PageInfo对象
        PageInfo<AdminUserDTO> info = new PageInfo<>(roleList);
        info.setTotal(page.getTotal());
        return info;
    }


    /**
     * 根据父节点递归查询当前模块下的菜单列表。
     *
     * @param parentId 父节点。
     * @return 菜单列表。
     */
    private List<BaseResourceDTO> getChildrenMenu(Long parentId, String ids) {
        List<BaseResourceDTO> currentList = new ArrayList<>();
        //获取当前父节点下的所有子菜单
        List<BaseResourceDTO> childrenList = new BaseResourceDT().fromByList(baseResourceDAO.selectByIds(ids.split(","), parentId));
        if (childrenList != null && childrenList.size() > 0) {
            BaseResourceDTO menuChild;
            for (BaseResourceDTO baseResourceDTO : childrenList) {
                // 只获取菜单以上目录，不要按钮
                if (baseResourceDTO.getLevel() != 2 && baseResourceDTO.getShowOr()) {
                    menuChild = new BaseResourceDTO();
                    //只取几个要用的属性
                    menuChild.setId(baseResourceDTO.getId());
                    menuChild.setParentId(parentId);
                    menuChild.setName(baseResourceDTO.getName());
                    menuChild.setAccessUrl(baseResourceDTO.getAccessUrl());
                    //判断如果父节点为0时，不往下取，因为是根模块
                    if (parentId != 0) {
                        menuChild.setChildren(getChildrenMenu(baseResourceDTO.getId(), ids));
                    }
                    currentList.add(menuChild);
                }
            }
        }
        return currentList;
    }

    /**
     * 根据父节点递归查询当前模块下的菜单列表，以TabMenu数据模式返回。
     *
     * @param parentId 父节点。
     * @return 菜单列表。
     */
    private List<TabMenu> getChildrenTabMenu(Long parentId, String ids) {
        List<TabMenu> currentList = new ArrayList<>();
        //获取当前父节点下的所有子菜单
        List<BaseResourceDTO> childrenList = new BaseResourceDT().fromByList(baseResourceDAO.selectByIds(ids.split(","), parentId));
        if (childrenList != null && childrenList.size() > 0) {
            TabMenu menuChild;
            for (BaseResourceDTO baseResourceDTO : childrenList) {
                // 只获取菜单以上目录，不要按钮
                if (baseResourceDTO.getLevel() != 2 && baseResourceDTO.getShowOr()) {
                    menuChild = new TabMenu();
                    //只取几个要用的属性
                    menuChild.setTitle(baseResourceDTO.getName());
                    //暂不设置图标
                    menuChild.setIcon("");
                    menuChild.setHref(baseResourceDTO.getAccessUrl());
                    menuChild.setSpread(false);
                    menuChild.setMenu1(baseResourceDTO.getId() + "");
                    menuChild.setLevel(baseResourceDTO.getLevel());
                    //判断如果父节点为0时，不往下取，因为是根模块
                    if (parentId != 0) {
                        menuChild.setChildren(getChildrenTabMenu(baseResourceDTO.getId(), ids));
                    }
                    currentList.add(menuChild);
                }
            }
        }
        return currentList;
    }

    /**
     * 根据父节点获取树形菜单树。
     *
     * @param parentId 父节点。
     * @return 菜单树。
     */
    private List<DTree> getChildrenList(Long parentId) {
        LOG.debug("当前菜单树父节点为【{}】。", parentId);
        //初始化子菜单
        List<DTree> currentList = new ArrayList<>();
        //获取当前父节点下的所有子菜单
        List<BaseResourceDTO> childrenList = new BaseResourceDT().fromByList(baseResourceDAO.selectByParentId(parentId));
        if (childrenList != null && childrenList.size() > 0) {
            DTree dTree;
            //初始化复选框参数
            List<CheckArr> checkArr = new ArrayList<CheckArr>();
            CheckArr check = new CheckArr();
            check.setType("0");
            check.setIsChecked("0");
            checkArr.add(check);
            for (BaseResourceDTO baseResourceDTO : childrenList) {
                dTree = new DTree();
                //说明是根目录
                dTree.setId(baseResourceDTO.getId() + "");
                dTree.setParentId(parentId + "");
                dTree.setTitle(baseResourceDTO.getName());
                //设置按钮为最后一层不可选
                //if (baseResourceDTO.getLevel() == 2) {
                //    dTree.setDisabled(Boolean.TRUE);
                //}
                //添加复选框属性
                dTree.setCheckArr(checkArr);
                //按钮不用再往下查
                if (baseResourceDTO.getLevel() != 2) {
                    dTree.setChildren(getChildrenList(baseResourceDTO.getId()));
                }
                currentList.add(dTree);
            }
        }
        return currentList;
    }

    @Override
    @EncryptMethod
    public ResultBean saveOrUpdateAdmin(AdminUserDTO adminUserDTO) {
        //将前端数据转为数据库对象
        AdminUser adminUser = new AdminUserDT().to(adminUserDTO);
        //为空时默认为禁用状态
        if (adminUser.getStatus() == null) {
            adminUser.setStatus(Boolean.FALSE);
        }
        int count = 0;
        if (adminUser.getId() == null){
            adminUser.setCreateTime(MyDateUtils.now());
            //密码加密
            adminUser.setPassword(CryptographUtils.MD5(adminUser.getPassword()));
            count = adminUserDAO.insert(adminUser);
        }else {
            //前端只允许修改这几个字段，避免覆盖其它字段
            AdminUser current = adminUserDAO.selectByPrimaryKey(adminUser.getId());
            current.setLoginUsername(adminUser.getLoginUsername());
            current.setNickname(adminUser.getNickname());
            current.setPortrait(adminUser.getPortrait());
            //不能直接修改用户密码
            //current.setPassword(adminUser.getPassword());
            current.setBoundCellphone(adminUser.getBoundCellphone());
            current.setBoundEmail(adminUser.getBoundEmail());
            current.setStatus(adminUser.getStatus());
            count = adminUserDAO.updateByPrimaryKey(current);
            //更新用户缓存的头像信息，无所谓有没有存过
            if (MyStringUtils.isNotBlank(current.getPortrait())){
                redisService.set(Constants.Global.REDIS_ADMIN_USER_PORTRAIT + current.getId(), filePath + current.getPortrait());
            }
        }
        if (count !=1){
            throw new RowsUpdateNotMatchException("变更后台用户数据异常，角色数据：" + adminUser.toString());
        }
        return ResultBean.success();
    }

    @Override
    public ResultBean updateAdmin(AdminUserDTO adminUserDTO) {
        //将前端数据转为数据库对象
        AdminUser adminUser = new AdminUserDT().to(adminUserDTO);
        return ResultBean.successForData(adminUserDAO.updateByPrimaryKeySelective(adminUser));
    }

    @Override
    public ResultBean generateAdminPwd(Long id, String password, Integer modifyPwdOr) {
        return ResultBean.successForData(adminUserDAO.generatePwd(id, password, modifyPwdOr));
    }

    @Override
    @CacheEvict(cacheNames = "menuCache", allEntries = true)
    public ResultBean deleteAdmin(Long id) {
        adminUserDAO.deleteByPrimaryKey(id);
        LOG.debug("删除后台用户信息【{}】", id);
        //删除与当前用户关联的角色信息
        int count = adminUserRoleDAO.deleteByUserId(id);
        LOG.debug("删除后台用户信息【{}】关联的【{}】条关联角色", id, count);
        return ResultBean.success();
    }

    @Override
    @EncryptMethod
    public ResultBean<AdminUserDTO> getAdminById(Long id) {
        AdminUser adminUser = adminUserDAO.selectByPrimaryKey(id);
        if (adminUser == null){
            return ResultBean.failed("当前后台用户编号不存在！");
        }
        return ResultBean.successForData(new AdminUserDT().from(adminUser));
    }

    @Override
    @CacheEvict(cacheNames = "menuCache", allEntries = true)
    public ResultBean saveAdminRoles(Long adminId, String roleIds) {
        LOG.debug("开始变更当前用户【{}】的角色列表【{}】。", adminId, roleIds);
        if (CommonUtils.isBlank(adminId, roleIds)) {
            return ResultBean.failed("变更角色权限失败，参数不能为空！");
        }
        int count = adminUserRoleDAO.deleteByUserId(adminId);
        LOG.debug("删除用户【{}】的【{}】条关联角色信息。", adminId, count);
        //重新指添加新的权限
        String[] roleIdArr = roleIds.split(",");
        List<AdminUserRole> dataList = new ArrayList<>();
        AdminUserRole adminUserRole;
        for (String roleId : roleIdArr){
            adminUserRole = new AdminUserRole();
            adminUserRole.setUserId(adminId);
            adminUserRole.setRoleId(Long.parseLong(roleId));
            dataList.add(adminUserRole);
        }
        count = adminUserRoleDAO.insertBatch(dataList);
        LOG.debug("给用户【{}】添加了【{}】条关联角色信息。",adminId,count);
        return ResultBean.success();
    }

    @Override
    public ResultBean<String> getAdminRoles(Long adminId) {
        String roles = adminUserRoleDAO.selectConcatByUserId(adminId);
        if (roles == null){
            return ResultBean.failed("当前管理员未关联角色信息！");
        }
        return ResultBean.successForData(roles);
    }

    @Override
    @Cacheable(keyGenerator = "cacheKeyGenerator",unless = "#result.data == null ")
    public ResultBean<List<RoleDTO>> selectRolesByUserId(Long adminUserId) {
        String roleIds = adminUserRoleDAO.selectConcatByUserId(adminUserId);
        if (MyStringUtils.isBlank(roleIds)) {
            return ResultBean.failed("当前用户未分配角色！");
        }
        List<Role> roles = roleDAO.selectByIds(roleIds.split(","));
        if (roles == null || roles.size() < 1) {
            return ResultBean.failed("当前用户未分配角色！");
        }
        return ResultBean.successForData(new RoleDT().fromByList(roles));
    }

    @Override
    @Cacheable(keyGenerator = "cacheKeyGenerator",unless = "#result.data == null ")
    public ResultBean<List<BaseResourceDTO>> selectResourcesByUserId(Long adminUserId) {
        ResultBean<String> idsBean = getResourcesByUserId(adminUserId);
        if (!idsBean.isSuccess()){
            return ResultBean.failed(idsBean.getMessage());
        }
        List<BaseResource> resourceList = baseResourceDAO.selectByIds(idsBean.getData().split(","), null);
        if (resourceList == null || resourceList.size() < 1) {
            return ResultBean.failed("当前用户未分配权限！");
        }
        return ResultBean.successForData(new BaseResourceDT().fromByList(resourceList));
    }

    @Override
    @Cacheable(keyGenerator = "cacheKeyGenerator", unless = "#result.data == null ")
    public ResultBean<String> selectAdminIdsByRoleId(Long roleId) {
        String adminIds = adminUserRoleDAO.selectConcatByRoleId(roleId);
        if (MyStringUtils.isBlank(adminIds)) {
            return ResultBean.failed("当前角色未分配用户！");
        }
        return ResultBean.successForData(adminIds);
    }

    @Override
    @Cacheable(keyGenerator = "cacheKeyGenerator", unless = "#result.data == null ")
    public ResultBean<String> selectRoleIdsByResourceId(Long resourceId) {
        String roleIds = baseRoleResourceDAO.selectRoleIdsByResourceId(resourceId);
        if (MyStringUtils.isBlank(roleIds)) {
            return ResultBean.failed("当前权限未分配角色！");
        }
        return ResultBean.successForData(roleIds);
    }



}
