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
import com.distressed.asset.portal.dto.AreaDTO;
import com.distressed.asset.portal.mapping.Area;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link Area}<->{@link AreaDTO}互转辅助对象。
 *
 * @author hongchao zhao at 2019-10-17 16:07
 */
public class AreaDT implements DataTransform<Area, AreaDTO> {

    @Override
    public AreaDTO from(Area area) {
        if (area == null){
            return null;
        }
        AreaDTO areaDTO = new AreaDTO();
        areaDTO.setCode(area.getCode());
        areaDTO.setName(area.getName());
        areaDTO.setParentCode(area.getParentCode());
        areaDTO.setRoot(area.getRoot());
        areaDTO.setZone(area.getZone());
        areaDTO.setStatus(area.getStatus());
        return areaDTO;
    }

    @Override
    public AreaDTO from(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<AreaDTO> fromByList(List<Area> list) {
        if (list == null) {
            return null;
        }
        List<AreaDTO> dtolist = new ArrayList<AreaDTO>();
        for (Area area : list) {
            dtolist.add(from(area));
        }
        return dtolist;
    }

    @Override
    public List<AreaDTO> fromByListMap(List<Map<String, Object>> list) {
        return null;
    }

    @Override
    public Area to(AreaDTO areaDTO) {
        if (areaDTO == null){
            return null;
        }
        Area area = new Area();
        area.setCode(areaDTO.getCode());
        area.setName(areaDTO.getName());
        area.setParentCode(areaDTO.getParentCode());
        area.setRoot(areaDTO.getRoot());
        area.setZone(areaDTO.getZone());
        area.setStatus(areaDTO.getStatus());
        return area;
    }
}
