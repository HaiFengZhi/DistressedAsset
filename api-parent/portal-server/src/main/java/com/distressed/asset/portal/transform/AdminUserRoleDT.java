package com.distressed.asset.portal.transform;


import com.distressed.asset.common.transform.DataTransform;
import com.distressed.asset.portal.dto.AdminUserRoleDTO;
import com.distressed.asset.portal.mapping.AdminUserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户-角色数据结构关联表。
 *
 * {@link AdminUserRole}<->{@link AdminUserRoleDTO}互转辅助对象
 * @author yanjie wan at 2019-10-28 14:54
 */
public class AdminUserRoleDT implements DataTransform<AdminUserRole, AdminUserRoleDTO> {
    private Long userId;

    private Long roleId;


    @Override
    public AdminUserRoleDTO from(AdminUserRole adminUserRole) {
        if(adminUserRole==null)
        {
            return null;
        }
        AdminUserRoleDTO adminUserRoleDTO=new AdminUserRoleDTO();
        adminUserRoleDTO.setUserId(adminUserRole.getUserId());
        adminUserRoleDTO.setRoleId(adminUserRole.getRoleId());
        return adminUserRoleDTO;
    }

    @Override
    public AdminUserRoleDTO from(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<AdminUserRoleDTO> fromByList(List<AdminUserRole> list) {
        if(list==null)
        {
            return null;
        }
        List<AdminUserRoleDTO> dtoList=new ArrayList<AdminUserRoleDTO>();
        for(AdminUserRole adminUserRole:list)
        {
            dtoList.add(from(adminUserRole));
        }
        return null;
    }

    @Override
    public List<AdminUserRoleDTO> fromByListMap(List<Map<String, Object>> list) {
        return null;
    }

    @Override
    public AdminUserRole to(AdminUserRoleDTO adminUserRoleDTO) {
        if(adminUserRoleDTO==null)
        {
            return null;
        }
        AdminUserRole adminUserRole=new AdminUserRole();
        adminUserRole.setUserId(adminUserRoleDTO.getUserId());
        adminUserRole.setRoleId(adminUserRoleDTO.getRoleId());
        return adminUserRole;
    }
}