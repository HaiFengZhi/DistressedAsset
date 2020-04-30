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

package com.distressed.asset.portal.transform;


import com.distressed.asset.common.layui.XmSelect;
import com.distressed.asset.common.transform.DataTransform;
import com.distressed.asset.portal.dto.RoleDTO;
import com.distressed.asset.portal.mapping.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link Role}<->{@link RoleDTO}互转辅助对象。
 *
 * @author hongchao zhao at 2019-10-25 16:49
 */
public class RoleDT implements DataTransform<Role, RoleDTO> {
    @Override
    public RoleDTO from(Role role) {
        if (role == null) {
            return null;
        }
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setDescription(role.getDescription());
        roleDTO.setCreateTime(role.getCreateTime());
        roleDTO.setStatus(role.getStatus());
        return roleDTO;
    }

    @Override
    public RoleDTO from(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<RoleDTO> fromByList(List<Role> list) {
        if (list == null) {
            return null;
        }
        List<RoleDTO> dtolist = new ArrayList<RoleDTO>();
        for (Role role : list) {
            dtolist.add(from(role));
        }
        return dtolist;
    }

    @Override
    public List<RoleDTO> fromByListMap(List<Map<String, Object>> list) {
        return null;
    }

    @Override
    public Role to(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        role.setCreateTime(roleDTO.getCreateTime());
        role.setStatus(roleDTO.getStatus());
        return role;
    }

    /**
     * 将角色对象转为XmSelect对象。
     *
     * @param list 角色对象集合。
     * @return 下拉对象集合。
     */
    public List<XmSelect> fromByRoleList(List<Role> list) {
        if (list == null) {
            return null;
        }
        List<XmSelect> dataList = new ArrayList<>();
        for (Role role : list) {
            dataList.add(XmSelect.from(role.getName(), role.getId() + ""));
        }
        return dataList;
    }
}
