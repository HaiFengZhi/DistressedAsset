package com.distressed.asset.portal.service;

import com.distressed.asset.common.result.ResultBean;
import com.distressed.asset.portal.dto.AdminUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 后台管理员接口。
 *
 * @version 1.0
 * @Author Administrator
 * @Date 2019-11-26 21:33
 **/
@FeignClient(value = "portal-server", contextId = "adminUserService")
public interface AdminUserService {

    /**
     * 测试当请求参数为对象时，加密其中注解参数。
     *
     * @param adminUserDTO 后台管理员信息。
     * @return
     */
    @PostMapping("/testSave")
    ResultBean testSave(@RequestBody AdminUserDTO adminUserDTO);

    /**
     * 测试当请求参数为Map时，加密其中特殊字段参数。
     *
     * @param params Map请求参数。
     * @return
     */
    @PostMapping("/testMap")
    ResultBean testMap(@RequestBody Map<String, Object> params);

    /**
     * 测试当返回结果为ResultBean，并且其中结果为对象时，解密其中注解参数。
     *
     * @param id 参数ID。
     * @return
     */
    @PostMapping("/testQuery")
    ResultBean<AdminUserDTO> testQuery(@RequestParam("id") Long id);

    /**
     * 测试当返回结果为List集合时，解密其中注解参数。
     *
     * @return
     */
    @PostMapping("/testQueryAll")
    ResultBean<List<AdminUserDTO>> testQueryAll();

    /**
     * 校验后台管理员用户登陆。
     *
     * @param loginUsername 用户名。
     * @param password      密码。
     * @return 校验结果。
     */
    @PostMapping("/checkAdminLogin")
    ResultBean<AdminUserDTO> checkAdminLogin(@RequestParam("loginUsername") String loginUsername, @RequestParam("password") String password);

    /**
     * 后台管理员登陆后变更操作。
     *
     * @param adminUserDTO 管理员信息。
     * @return 更新结果。
     */
    @PostMapping("/updateAdminLogin")
    ResultBean updateAdminLogin(@RequestBody AdminUserDTO adminUserDTO);



}
