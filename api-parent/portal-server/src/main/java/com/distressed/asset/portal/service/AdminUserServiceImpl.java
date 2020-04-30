package com.distressed.asset.portal.service;

import com.distressed.asset.portal.dao.*;
import com.distressed.asset.portal.dto.AdminUserDTO;
import com.distressed.asset.portal.mapping.AdminUser;
import com.distressed.asset.portal.transform.AdminUserDT;
import com.distressed.asset.common.annotation.EncryptMethod;
import com.distressed.asset.common.result.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 后台管理员业务实现。
 *
 * @version 1.0
 * @Author Administrator
 * @Date 2019-11-26 21:38
 **/
@RestController
@CacheConfig(cacheNames = "adminUserCache")
public class AdminUserServiceImpl implements AdminUserService {

    private static Logger LOG = LoggerFactory.getLogger(AdminUserServiceImpl.class);

    @Resource
    private AdminUserDAO adminUserDAO;
    @Resource
    private AdminUserRoleDAO adminUserRoleDAO;
    @Resource
    private RoleDAO roleDAO;
    @Resource
    private BaseRoleResourceDAO baseRoleResourceDAO;
    @Resource
    private BaseResourceDAO baseResourceDAO;

    @Override
    @EncryptMethod
    public ResultBean testSave(AdminUserDTO adminUserDTO) {
        LOG.info("testSave 业务逻辑入参 request:{}", adminUserDTO.toString());
        return null;
    }

    @Override
    @EncryptMethod
    public ResultBean testMap(Map<String, Object> params) {
        LOG.info("testMap 业务逻辑入参 request:{}", params.toString());
        return null;
    }

    @Override
    @EncryptMethod
    public ResultBean<AdminUserDTO> testQuery(Long id) {
        AdminUser adminUser = adminUserDAO.selectByPrimaryKey(id);
        AdminUserDTO adminUserDTO = new AdminUserDT().from(adminUser);
        return ResultBean.successForData(adminUserDTO);
    }

    @Override
    @EncryptMethod
    public ResultBean<List<AdminUserDTO>> testQueryAll() {
        return ResultBean.successForData(new AdminUserDT().fromByList(adminUserDAO.selectAll()));
    }

    @Override
    @EncryptMethod
    public ResultBean<AdminUserDTO> checkAdminLogin(String loginUsername, String password) {
        AdminUser adminUser = adminUserDAO.selectLogin(loginUsername, password);
        if (adminUser == null) {
            return ResultBean.failed("登陆名或密码错误！");
        }
        return ResultBean.successForData(new AdminUserDT().from(adminUser));
    }

    @Override
    @EncryptMethod
    public ResultBean updateAdminLogin(AdminUserDTO adminUserDTO) {
        //将前端数据转为数据库对象
        AdminUser adminUser = new AdminUserDT().to(adminUserDTO);
        int count = adminUserDAO.updateByPrimaryKey(adminUser);
        if (count != 1) {
            return ResultBean.failed("管理员信息变更失败！");
        }
        return ResultBean.success();
    }


}
