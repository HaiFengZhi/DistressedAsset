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


import com.distressed.asset.common.transform.DataTransform;
import com.distressed.asset.portal.dto.BaseRoleResourceDTO;
import com.distressed.asset.portal.mapping.BaseRoleResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link BaseRoleResource}<->{@link BaseRoleResourceDTO}互转辅助对象。
 *
 * @author hongchao zhao at 2019-10-25 16:46
 */
public class BaseRoleResourceDT implements DataTransform<BaseRoleResource, BaseRoleResourceDTO> {
    @Override
    public BaseRoleResourceDTO from(BaseRoleResource baseRoleResource) {
        if (baseRoleResource == null) {
            return null;
        }
        BaseRoleResourceDTO baseRoleResourceDTO = new BaseRoleResourceDTO();
        baseRoleResourceDTO.setRoleId(baseRoleResource.getRoleId());
        baseRoleResourceDTO.setResourceId(baseRoleResource.getResourceId());
        return baseRoleResourceDTO;
    }

    @Override
    public BaseRoleResourceDTO from(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<BaseRoleResourceDTO> fromByList(List<BaseRoleResource> list) {
        if (list == null) {
            return null;
        }
        List<BaseRoleResourceDTO> dtolist = new ArrayList<BaseRoleResourceDTO>();
        for (BaseRoleResource baseRoleResource : list) {
            dtolist.add(from(baseRoleResource));
        }
        return dtolist;
    }

    @Override
    public List<BaseRoleResourceDTO> fromByListMap(List<Map<String, Object>> list) {
        return null;
    }

    @Override
    public BaseRoleResource to(BaseRoleResourceDTO baseRoleResourceDTO) {
        if (baseRoleResourceDTO == null) {
            return null;
        }
        BaseRoleResource baseRoleResource = new BaseRoleResource();
        baseRoleResource.setRoleId(baseRoleResourceDTO.getRoleId());
        baseRoleResource.setResourceId(baseRoleResourceDTO.getResourceId());
        return baseRoleResource;
    }
}
