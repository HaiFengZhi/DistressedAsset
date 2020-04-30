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
import com.distressed.asset.portal.dto.BaseResourceDTO;
import com.distressed.asset.portal.mapping.BaseResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link BaseResource}<->{@link BaseResourceDTO}互转辅助对象。
 *
 * @author hongchao zhao at 2019-10-25 16:39
 */
public class BaseResourceDT implements DataTransform<BaseResource, BaseResourceDTO> {
    @Override
    public BaseResourceDTO from(BaseResource baseResource) {
        if (baseResource == null) {
            return null;
        }
        BaseResourceDTO baseResourceDTO = new BaseResourceDTO();
        baseResourceDTO.setId(baseResource.getId());
        baseResourceDTO.setName(baseResource.getName());
        baseResourceDTO.setAccessUrl(baseResource.getAccessUrl());
        baseResourceDTO.setParentId(baseResource.getParentId());
        baseResourceDTO.setDescription(baseResource.getDescription());
        baseResourceDTO.setLevel(baseResource.getLevel());
        baseResourceDTO.setShowOr(baseResource.getShowOr());
        baseResourceDTO.setSequence(baseResource.getSequence());
        baseResourceDTO.setMainPermissionsOr(baseResource.getMainPermissionsOr());
        baseResourceDTO.setStatus(baseResource.getStatus());
        return baseResourceDTO;
    }

    @Override
    public BaseResourceDTO from(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<BaseResourceDTO> fromByList(List<BaseResource> list) {
        if (list == null) {
            return null;
        }
        List<BaseResourceDTO> dtolist = new ArrayList<BaseResourceDTO>();
        for (BaseResource baseResource : list) {
            dtolist.add(from(baseResource));
        }
        return dtolist;
    }

    @Override
    public List<BaseResourceDTO> fromByListMap(List<Map<String, Object>> list) {
        return null;
    }

    @Override
    public BaseResource to(BaseResourceDTO baseResourceDTO) {
        if (baseResourceDTO == null) {
            return null;
        }
        BaseResource baseResource = new BaseResource();
        baseResource.setId(baseResourceDTO.getId());
        baseResource.setName(baseResourceDTO.getName());
        baseResource.setAccessUrl(baseResourceDTO.getAccessUrl());
        baseResource.setParentId(baseResourceDTO.getParentId());
        baseResource.setDescription(baseResourceDTO.getDescription());
        baseResource.setLevel(baseResourceDTO.getLevel());
        baseResource.setShowOr(baseResourceDTO.getShowOr());
        baseResource.setSequence(baseResourceDTO.getSequence());
        baseResource.setMainPermissionsOr(baseResourceDTO.getMainPermissionsOr());
        baseResource.setStatus(baseResourceDTO.getStatus());
        return baseResource;
    }
}
