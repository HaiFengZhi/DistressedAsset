package com.distressed.asset.portal.transform;


import com.distressed.asset.common.transform.DataTransform;
import com.distressed.asset.portal.dto.AdminUserDTO;
import com.distressed.asset.portal.mapping.AdminUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 后台用户信息表。
 * {@link AdminUser}<->{@link AdminUserDTO}互转辅助对象
 *
 * @author yanjie wan at 2019-10-28 14:45
 */
public class AdminUserDT implements DataTransform<AdminUser, AdminUserDTO> {

    @Override
    public AdminUserDTO from(AdminUser adminUser) {
        if(adminUser==null) {
            return null;
        }
        AdminUserDTO adminUserDTO=new AdminUserDTO();
        adminUserDTO.setId(adminUser.getId());
        adminUserDTO.setLoginUsername(adminUser.getLoginUsername());
        adminUserDTO.setNickname(adminUser.getNickname());
        adminUserDTO.setPassword(adminUser.getPassword());
        adminUserDTO.setSecurityPassword(adminUser.getSecurityPassword());
        adminUserDTO.setPortrait(adminUser.getPortrait());
        adminUserDTO.setCreateTime(adminUser.getCreateTime());
        adminUserDTO.setLastLoginTime(adminUser.getLastLoginTime());
        adminUserDTO.setType(adminUser.getType());
        adminUserDTO.setStatus(adminUser.getStatus());
        adminUserDTO.setBoundCellphone(adminUser.getBoundCellphone());
        adminUserDTO.setBoundEmail(adminUser.getBoundEmail());
        adminUserDTO.setModifyPwdOr(adminUser.getModifyPwdOr());
        return adminUserDTO;
    }

    @Override
    public AdminUserDTO from(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<AdminUserDTO> fromByList(List<AdminUser> list) {
        if(list==null) {
            return null;
        }
        List<AdminUserDTO> dtoList=new ArrayList<AdminUserDTO>();
        for(AdminUser adminUser:list)
        {
            dtoList.add(from(adminUser));
        }
        return dtoList;
    }

    @Override
    public List<AdminUserDTO> fromByListMap(List<Map<String, Object>> list) {
        return null;
    }

    @Override
    public AdminUser to(AdminUserDTO adminUserDTO) {
        if(adminUserDTO==null) {
            return null;
        }
        AdminUser adminUser=new AdminUser();
        adminUser.setId(adminUserDTO.getId());
        adminUser.setLoginUsername(adminUserDTO.getLoginUsername());
        adminUser.setNickname(adminUserDTO.getNickname());
        adminUser.setPassword(adminUserDTO.getPassword());
        adminUser.setSecurityPassword(adminUserDTO.getSecurityPassword());
        adminUser.setPortrait(adminUserDTO.getPortrait());
        adminUser.setCreateTime(adminUserDTO.getCreateTime());
        adminUser.setLastLoginTime(adminUserDTO.getLastLoginTime());
        adminUser.setType(adminUserDTO.getType());
        adminUser.setStatus(adminUserDTO.getStatus());
        adminUser.setBoundCellphone(adminUserDTO.getBoundCellphone());
        adminUser.setBoundEmail(adminUserDTO.getBoundEmail());
        adminUser.setModifyPwdOr(adminUserDTO.getModifyPwdOr());
        return adminUser;
    }
}