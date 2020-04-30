package com.distressed.asset.admin.controller;

import com.github.pagehelper.PageInfo;
import com.distressed.asset.admin.config.shiro.ShiroService;
import com.distressed.asset.portal.dto.AdminUserDTO;
import com.distressed.asset.portal.dto.BaseResourceDTO;
import com.distressed.asset.portal.dto.RoleDTO;
import com.distressed.asset.portal.service.MenuService;
import com.distressed.asset.common.layui.*;
import com.distressed.asset.common.result.LayUIResult;
import com.distressed.asset.common.result.ResultBean;
import com.distressed.asset.common.utils.CryptographUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制台-权限管理核心控制器。
 *
 * @author SuperZhao
 * @version 1.0
 * @date 2019-10-26 17:30
 */
@Controller
@RequestMapping("/platform")
public class PlatformController {

    private static Logger LOG = LoggerFactory.getLogger(PlatformController.class);

    @Resource
    private MenuService menuService;
    @Resource
    private ShiroService shiroService;

    //################################################【系统权限相关方法】########################################################//

    @RequestMapping("/list")
    public String list(HttpServletRequest request, Model model) {
        //LOG.debug("进入后台权限列表。。。");

        model.addAttribute("msg", "");
        //如果用普通表格形式即跳转：menuList
        //如果用树形表格形式即跳转：menuTreeList
        return "platform/menuTreeList";
    }

    @ResponseBody
    @RequestMapping(value = "/listData",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult<List<BaseResourceDTO>> listData(HttpServletRequest request, @RequestParam(value = "name", required = false) String name,
                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "limit", defaultValue = "10") int limit) {
        //LOG.debug("获取菜单数据：当前页码【{}】每页条数【{}】,搜索条件：【{}】。。。", page, limit, name);
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        PageInfo<BaseResourceDTO> menuList = menuService.pageMenuList(params, page, limit);
        return LayUIResult.successForData(menuList.getList(),menuList.getTotal());
    }

    @ResponseBody
    @RequestMapping(value = "/listTreeData",
            method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public LayUIResult<List<BaseResourceDTO>> listTreeData(HttpServletRequest request) {
        ResultBean<List<BaseResourceDTO>> menuListBean = menuService.getMenuList();
        if (!menuListBean.isSuccess()){
            return LayUIResult.failed("获取可用菜单列表失败！");
        }
        return LayUIResult.successForData(menuListBean.getData());
    }

    /**
     * 预添加和预修改皆经过这个方法。
     *
     * @param model 返回页面对象。
     * @param id 权限菜单编号。
     * @return 跳转页面。
     */
    @RequestMapping("/toAdd")
    public String toAdd(Model model, @RequestParam(value = "id", required = false) Long id) {
        //LOG.debug("进入权限资源变更页面，当前id为【{}】。。。", id);
        if (id != null){
            ResultBean<BaseResourceDTO> baseResource = menuService.getBaseResourceById(id);
            if (baseResource.isSuccess()){
                model.addAttribute("baseResource",baseResource.getData());
            }
        }
        return "platform/menu-add";
    }

    @ResponseBody
    @RequestMapping(value = "/getMenuList",
            method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public LayUIResult<List<BaseResourceDTO>> getMenuList(){
        //LOG.debug("获取所有菜单列表。。。。");
        ResultBean<List<BaseResourceDTO>> menuListBean = menuService.getMenuList();
        if (!menuListBean.isSuccess()){
            return LayUIResult.failed("获取可用菜单列表失败！");
        }
        return LayUIResult.successForData(menuListBean.getData());
    }

    @ResponseBody
    @RequestMapping(value = "/getMenuListByParentId",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public DTreeResponse getMenuListByParentId(@RequestParam(value="parentId",defaultValue="0")Long parentId){
        //LOG.debug("获取当前父节点【{}】下的所有节点。。。。", parentId);
        DTreeResponse response = new DTreeResponse();
        ResultBean<List<DTree>> menuListBean = menuService.getMenuListByParentId(parentId);
        if (!menuListBean.isSuccess()){
            response.setCode(menuListBean.getCode());
            response.setMsg(menuListBean.getMessage());
        }else {
            response.setStatus(new Status());
            response.setData(menuListBean.getData());
        }
        //LOG.debug("菜单树返回值：【{}】。",response.toString());
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/saveMenu",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult saveMenu(HttpServletRequest request, @ModelAttribute BaseResourceDTO baseResourceDTO){
        //LOG.debug("进入保存权限菜单方法，当前参数baseResourceDTO===>【{}】。。。。", baseResourceDTO);
        ResultBean resultBean = menuService.saveOrUpdateBaseResource(baseResourceDTO);
        if (!resultBean.isSuccess()){
            return LayUIResult.failed(resultBean.getMessage());
        }
        //更新系统鉴权列表
        shiroService.updatePermission();
        String type = "add";
        if (baseResourceDTO.getId() != null) {
            type = "update";
            //清除当前权限关联用户的缓存权限
            shiroService.cleanCachedAuthorByResourceId(baseResourceDTO.getId());
        }
        return LayUIResult.successForData("菜单权限保存成功啦！", type);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteMenu",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult deleteMenu(HttpServletRequest request, @RequestParam(value = "id") Long id){
        //LOG.debug("进入删除权限菜单方法，当前参数id===>【{}】。。。。", id);
        ResultBean resultBean = menuService.deleteBaseResourceById(id);
        if (!resultBean.isSuccess()){
            return LayUIResult.failed(resultBean.getMessage());
        }
        //更新系统鉴权列表
        shiroService.updatePermission();
        return LayUIResult.success("菜单权限删除成功！");
    }

    @ResponseBody
    @RequestMapping(value = "/openOrCloseMenu",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult openOrCloseMenu(HttpServletRequest request, @RequestParam(value = "id") Long id, @RequestParam(value = "status") Boolean status) {
        //LOG.debug("进入变更权限菜单方法，当前参数id===>【{}/{}】。。。。", id, status);
        ResultBean<BaseResourceDTO> resultBean = menuService.getBaseResourceById(id);
        if (!resultBean.isSuccess()) {
            return LayUIResult.failed(resultBean.getMessage());
        }
        //变更权限状态
        BaseResourceDTO baseResourceDTO = resultBean.getData();
        baseResourceDTO.setShowOr(status);
        ResultBean result = menuService.saveOrUpdateBaseResource(baseResourceDTO);
        if (!result.isSuccess()) {
            LayUIResult.failed(result.getMessage());
        }
        //更新系统鉴权列表
        shiroService.updatePermission();
        return LayUIResult.success("权限菜单显示状态变更成功！");
    }

    @RequestMapping("/toTest")
    public String menuTest(HttpServletRequest request, Model model) {
        //LOG.debug("进入测试导航和Tab结合页面。。。");

        model.addAttribute("msg", "后台数据");
        return "platform/menuTest";
    }

    @ResponseBody
    @RequestMapping(value = "/getChildrenMenu",
            method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public LayUIResult getChildrenMenu(HttpServletRequest request, @RequestParam(value="parentId",defaultValue="1")Long parentId){
        //LOG.debug("获取子菜单，当前参数parentId===>【{}】。。。。", parentId);
        AdminUserDTO adminUser = (AdminUserDTO) SecurityUtils.getSubject().getPrincipal();
        //LOG.debug("当前登陆用户【{}/{}/{}】", adminUser.getId(), adminUser.getNickname(), parentId);
        ResultBean<List<BaseResourceDTO>> childrenMenu = menuService.getMenuByParentId(parentId, adminUser.getId());
        if (!childrenMenu.isSuccess()){
            return LayUIResult.failed("获取权限菜单失败！");
        }
        return LayUIResult.successForData(childrenMenu.getData());
    }

    //获取多页面菜单框架
    @ResponseBody
    @RequestMapping(value = "/getChildrenTabMenu",
            method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public LayUIResult getChildrenTabMenu(HttpServletRequest request, @RequestParam(value = "parentId", defaultValue = "0") Long parentId) {
        //LOG.debug("获取子菜单，当前参数parentId===>【{}】。。。。", parentId);
        AdminUserDTO adminUser = (AdminUserDTO) SecurityUtils.getSubject().getPrincipal();
        //LOG.debug("当前登陆用户【{}/{}/{}】", adminUser.getId(), adminUser.getNickname(), parentId);
        ResultBean<List<TabMenu>> childrenMenu = menuService.getTabMenuByParentId(parentId, adminUser.getId());
        if (!childrenMenu.isSuccess()) {
            return LayUIResult.failed("获取权限菜单失败！");
        }
        return LayUIResult.successForData(childrenMenu.getData());
    }

    @ResponseBody
    @RequestMapping(value = "/getXmSelectData",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult getXmSelectData(HttpServletRequest request){
        //LOG.debug("获取xm-select的数据。。。。");
        ResultBean<List<Map<String, Object>>> childrenMenu = menuService.getXmSelectData();
        if (!childrenMenu.isSuccess()){
            return LayUIResult.failed("获取权限菜单失败！");
        }
        return LayUIResult.successForData(childrenMenu.getData());
    }

    //################################################【系统角色相关方法】########################################################//

    @RequestMapping("/roleList")
    public String roleList(HttpServletRequest request, Model model) {
        //LOG.debug("进入角色列表。。。");

        model.addAttribute("msg", "");
        return "platform/roleList";
    }

    @ResponseBody
    @RequestMapping(value = "/getRoleList",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult<List<RoleDTO>> getRoleList(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "limit", defaultValue = "10") int limit) {
        //LOG.debug("获取菜单数据：当前页码【{}】每页条数【{}】,搜索条件：【{}】。。。", page, limit, name);
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        PageInfo<RoleDTO> roleList = menuService.getRoleList(params, page, limit);
        return LayUIResult.successForData(roleList.getList(), roleList.getTotal());
    }


    @ResponseBody
    @RequestMapping(value = "/deleteRole",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult deleteRole(HttpServletRequest request, @RequestParam(value = "id") Long id){
        //LOG.debug("进入删除角色方法，当前参数id===>【{}】。。。。", id);
        ResultBean resultBean = menuService.deleteRole(id);
        if (!resultBean.isSuccess()){
            return LayUIResult.failed(resultBean.getMessage());
        }
        //清空当前角色下的所有登陆用户缓存权限
        shiroService.cleanCachedAuthorByRoleId(id);
        return LayUIResult.success("角色删除成功！");
    }

    @ResponseBody
    @RequestMapping(value = "/openOrCloseRole",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult openOrCloseRole(HttpServletRequest request, @RequestParam(value = "id") Long id, @RequestParam(value = "status") Boolean status) {
        //LOG.debug("进入变更角色状态方法，当前参数id===>【{}】。。。。", id);
        ResultBean<RoleDTO> resultBean = menuService.getRoleById(id);
        if (!resultBean.isSuccess()){
            return LayUIResult.failed(resultBean.getMessage());
        }
        RoleDTO role = resultBean.getData();
        role.setStatus(status);
        ResultBean result = menuService.saveOrUpdateRole(role);
        if (!result.isSuccess()) {
            LayUIResult.failed(result.getMessage());
        }
        //清空当前角色下的所有登陆用户缓存权限
        shiroService.cleanCachedAuthorByRoleId(role.getId());
        return LayUIResult.success("当前角色状态变更成功！");
    }

    @ResponseBody
    @RequestMapping(value = "/saveRole",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult saveRole(HttpServletRequest request, @ModelAttribute RoleDTO roleDTO){
        //LOG.debug("进入保存角色方法，当前参数roleDTO===>【{}】。。。。", roleDTO);
        ResultBean resultBean = menuService.saveOrUpdateRole(roleDTO);
        if (!resultBean.isSuccess()){
            return LayUIResult.failed(resultBean.getMessage());
        }
        String type = "add";
        if (roleDTO.getId() != null) {
            type = "update";
            //修改角色时，要清空当前所有登陆状态的用户缓存权限
            shiroService.cleanCachedAuthorByRoleId(roleDTO.getId());
        }
        return LayUIResult.successForData("角色保存成功啦！", type);
    }

    @ResponseBody
    @RequestMapping(value = "/getRoleMenu",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult getRoleMenu(HttpServletRequest request, @RequestParam(value = "roleId") Long roleId){
        //LOG.debug("进入获取角色关联权限方法，当前参数【{}】。。。。", roleId);
        ResultBean<String> resultBean = menuService.getRoleBaseResource(roleId);
        if (!resultBean.isSuccess()){
            return LayUIResult.failed(resultBean.getMessage());
        }
        return LayUIResult.successForData(resultBean.getData());
    }

    @ResponseBody
    @RequestMapping(value = "/saveRoleMenu",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult saveRoleMenu(HttpServletRequest request, @RequestParam(value = "roleId") Long roleId,@RequestParam(value = "menuIds") String menuIds){
        //LOG.debug("进入保存角色权限方法，当前参数【{}/{}】。。。。", roleId, menuIds);
        ResultBean resultBean = menuService.saveRoleMenuList(roleId, menuIds);
        if (!resultBean.isSuccess()) {
            return LayUIResult.failed(resultBean.getMessage());
        }
        //修改角色时，要清空当前所有登陆状态的用户缓存权限
        shiroService.cleanCachedAuthorByRoleId(roleId);
        return LayUIResult.successForData("角色权限保存成功啦！");
    }

    //################################################【系统用户相关方法】########################################################//

    @RequestMapping("/adminList")
    public String adminList(HttpServletRequest request, Model model) {
        //LOG.debug("进入后台用户列表。。。");

        model.addAttribute("msg", "");
        return "platform/adminList";
    }

    @ResponseBody
    @RequestMapping(value = "/getAdminList",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult<List<AdminUserDTO>> getAdminList(@RequestParam(value = "loginUsername", required = false) String loginUsername,
                                                        @RequestParam(value = "nickname", required = false) String nickname,
                                                        @RequestParam(value = "boundCellphone", required = false) String boundCellphone,
                                                        @RequestParam(value = "boundEmail", required = false) String boundEmail,
                                                        @RequestParam(value = "status", required = false) String status,
                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "limit", defaultValue = "10") int limit) {
        //LOG.debug("获取用户数据：当前页码【{}】每页条数【{}】,搜索条件：【{}/{}/{}/{}/{}】。。。", page, limit, loginUsername, nickname, boundCellphone, boundEmail, status);
        Map<String, Object> params = new HashMap<>();
        params.put("loginUsername", loginUsername);
        params.put("nickname", nickname);
        params.put("boundCellphone", boundCellphone);
        params.put("boundEmail", boundEmail);
        params.put("status",status);
        PageInfo<AdminUserDTO> adminList = menuService.getAdminList(params, page, limit);
        return LayUIResult.successForData(adminList.getList(), adminList.getTotal());
    }

    @RequestMapping("/toAdmin")
    public String toAdmin(HttpServletRequest request, Model model,@RequestParam(value = "id", required = false) Long id) {
        //LOG.debug("进入后台用户列表，当前用户ID【{}】。。。", id);
        if (id != null) {
            ResultBean<AdminUserDTO> adminUser = menuService.getAdminById(id);
            if (adminUser.isSuccess() && adminUser.getData() != null) {
                model.addAttribute("adminUser", adminUser.getData());
            }
        }
        model.addAttribute("msg", "");
        return "platform/adminAdd";
    }

    @ResponseBody
    @RequestMapping(value = "/saveAdmin",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult saveAdmin(HttpServletRequest request, @ModelAttribute AdminUserDTO adminUserDTO){
        //LOG.debug("进入保存管理员信息方法，当前参数adminUserDTO===>【{}】。。。。", adminUserDTO);
        ResultBean resultBean = menuService.saveOrUpdateAdmin(adminUserDTO);
        if (!resultBean.isSuccess()){
            return LayUIResult.failed(resultBean.getMessage());
        }
        String type = "add";
        if (adminUserDTO.getId() != null) {
            type = "update";
            //变更用户信息后，要更新登陆中的用户信息
            //shiroService.kickOutUser(adminUserDTO.getId(), true);
            shiroService.updateAdminSession(adminUserDTO);
        }
        return LayUIResult.successForData("后台用户保存成功啦！",type);
    }

    @ResponseBody
    @RequestMapping(value = "/generatePwd",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult generatePwd(HttpServletRequest request, @RequestParam(value = "adminId") Long adminId, @RequestParam(value = "password") String pwd) {
        //LOG.debug("进入保初始化密码方法，当前参数roleDTO===>【{}/{}】。。。。", adminId, pwd);
        ResultBean resultBean = menuService.generateAdminPwd(adminId, CryptographUtils.MD5(pwd), 0);
        if (!resultBean.isSuccess()) {
            return LayUIResult.failed(resultBean.getMessage());
        }
        //重置密码后，强制踢出当前用户的登陆状态
        shiroService.kickOutUser(adminId, true);
        return LayUIResult.success("后台管理员密码初始化成功！");
    }

    @ResponseBody
    @RequestMapping(value = "/saveAdminRoles",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult saveAdminRoles(HttpServletRequest request, @RequestParam(value = "adminId") Long adminId,@RequestParam(value = "roleIds") String roleIds){
        //LOG.debug("进入保存用户角色方法，当前参数【{}/{}】。。。。", adminId,roleIds);
        ResultBean resultBean = menuService.saveAdminRoles(adminId,roleIds);
        if (!resultBean.isSuccess()){
            return LayUIResult.failed(resultBean.getMessage());
        }
        //更新当前变更管理员的缓存权限，不用退出登陆状态
        shiroService.kickOutUser(adminId, false);
        return LayUIResult.successForData("用户角色保存成功啦！");
    }

    @ResponseBody
    @RequestMapping(value = "/getRoleSelectList",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public DTreeResponse getRoleSelectList(){
        //LOG.debug("获取当前所有可用的角色列表，用以分配用户角色。。。。");
        DTreeResponse response = new DTreeResponse();
        ResultBean<List<XmSelect>> roleSelectList = menuService.getRoleSelectList();
        if (!roleSelectList.isSuccess()){
            response.setCode(roleSelectList.getCode());
            response.setMsg(roleSelectList.getMessage());
        }else {
            response.setStatus(new Status());
            response.setData(roleSelectList.getData());
        }
        //LOG.debug("角色列表返回值：【{}】。",response.toString());
        return response;
    }


    @ResponseBody
    @RequestMapping(value = "/openOrCloseAdmin",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult openOrCloseAdmin(HttpServletRequest request, @RequestParam(value = "id") Long id, @RequestParam(value = "status") Boolean status) {
        //LOG.debug("进入变更用户状态方法，当前参数id===>【{}】。。。。", id);
        ResultBean<AdminUserDTO> resultBean = menuService.getAdminById(id);
        if (!resultBean.isSuccess()){
            return LayUIResult.failed(resultBean.getMessage());
        }
        AdminUserDTO adminUser = resultBean.getData();
        adminUser.setStatus(status);
        ResultBean result = menuService.saveOrUpdateAdmin(adminUser);
        if (!result.isSuccess()) {
            LayUIResult.failed(result.getMessage());
        }
        if (!status){
            //如果禁用，则更新当前变更管理员的缓存权限，并强制退出登陆状态
            shiroService.kickOutUser(id, true);
        }
        return LayUIResult.success("当前用户状态变更成功！");
    }

    @ResponseBody
    @RequestMapping(value = "/deleteAdmin",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult deleteAdmin(HttpServletRequest request, @RequestParam(value = "id") Long id){
        //LOG.debug("进入删除用户方法，当前参数id===>【{}】。。。。", id);
        ResultBean resultBean = menuService.deleteAdmin(id);
        if (!resultBean.isSuccess()){
            return LayUIResult.failed(resultBean.getMessage());
        }
        //更新当前变更管理员的缓存权限，并强制退出登陆状态
        shiroService.kickOutUser(id, true);
        return LayUIResult.success("当前用户删除成功！");
    }

    @ResponseBody
    @RequestMapping(value = "/getAdminRole",
            method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public LayUIResult getAdminRole(HttpServletRequest request, @RequestParam(value = "adminId") Long adminId){
        //LOG.debug("进入获取管理员角色关联方法，当前参数【{}】。。。。", adminId);
        ResultBean<String> resultBean = menuService.getAdminRoles(adminId);
        if (!resultBean.isSuccess()){
            return LayUIResult.failed(resultBean.getMessage());
        }
        return LayUIResult.successForData(resultBean.getData().split(","));
    }

}
