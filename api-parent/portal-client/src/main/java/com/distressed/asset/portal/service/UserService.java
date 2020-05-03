package com.distressed.asset.portal.service;

import com.github.pagehelper.PageInfo;
import com.distressed.asset.portal.dto.UserDTO;
import com.distressed.asset.common.result.ResultBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "portal-server",contextId = "userService")
public interface UserService {
    /**
     * 根据查询条件分页查询菜单权限集合。
     *
     * @param params 查询条件。
     * @param pageNum 当前页码。
     * @param pageSize 每页显示条数。
     * @return 菜单权限集合。
     */
    @PostMapping("/pageUserList")
    PageInfo<UserDTO> pageUserList(@RequestBody(required = false) Map<String, Object> params, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize);

    /**
     * 根据编号查询用户详情信息
     * @param id
     * @return
     */
    @PostMapping("/userDetail")
    Map<String,Object> userDetail(@RequestParam("id") Long id);
    /**
     * 新增或修改用户数据。
     *
     * @param userDTO 用户数据。
     * @return 变更结果。
     */
    @PostMapping("/saveOrUpdateUser")
    ResultBean saveOrUpdateUser(@RequestBody UserDTO userDTO);

    /**
     * 变更用户信息。
     *
     * @param userDTO 用户数据。
     * @return 变更结果。
     */
    @PostMapping("/updateUser")
    ResultBean updateUser(@RequestBody UserDTO userDTO);

    @PostMapping("/getCountByUserName")
    ResultBean<Integer> getCountByUserName(@RequestParam("loginUsername") String loginUsername, @RequestParam(value = "id") Long id);

    @PostMapping("/checkUserLogin")
    ResultBean<UserDTO> checkUserLogin(@RequestParam("loginUsername") String loginUsername, @RequestParam("password") String password);

}
