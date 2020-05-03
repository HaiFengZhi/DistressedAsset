package com.distressed.asset.portal.transform;

import com.distressed.asset.portal.dto.UserDTO;
import com.distressed.asset.portal.mapping.User;
import com.distressed.asset.common.transform.DataTransform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户信息表。
 *
 * {@link User}<-> {@link UserDTO}互转辅助对象
 * @author yanjie wan at 2019-10-29 15:01
 */
public class UserDT implements DataTransform<User, UserDTO> {
    @Override
    public UserDTO from(User user) {
        if(user==null)
        {
            return null;
        }
        UserDTO userDTO=new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setLoginUsername(user.getLoginUsername());
        userDTO.setNickname(user.getNickname());
        userDTO.setPassword(user.getPassword());
        userDTO.setPortrait(user.getPortrait());
        userDTO.setBoundCellphone(user.getBoundCellphone());
        userDTO.setBoundEmail(user.getBoundEmail());
        userDTO.setWechatKey(user.getWechatKey());
        userDTO.setShareCode(user.getShareCode());
        userDTO.setRegisteredTime(user.getRegisteredTime());
        userDTO.setLastLoginTime(user.getLastLoginTime());
        userDTO.setType(user.getType());
        userDTO.setVipStartDate(user.getVipStartDate());
        userDTO.setVipEndDate(user.getVipEndDate());
        userDTO.setVipStatus(user.getVipStatus());
        userDTO.setSignature(user.getSignature());
        userDTO.setUserType(user.getUserType());
        userDTO.setRecommendCode(user.getRecommendCode());
        userDTO.setStatus(user.getStatus());
        return userDTO;
    }

    @Override
    public UserDTO from(Map<String, Object> map) {
        return null;
    }

    @Override
    public List<UserDTO> fromByList(List<User> list) {
        if(list==null)
        {
            return null;
        }
        List<UserDTO> dtoList=new ArrayList<>();
        for(User user:list)
        {
            dtoList.add(from(user));
        }
        return dtoList;
    }

    @Override
    public List<UserDTO> fromByListMap(List<Map<String, Object>> list) {
        return null;
    }

    @Override
    public User to(UserDTO userDTO) {
        if(userDTO==null)
        {
            return null;
        }
        User user=new User();
        user.setId(userDTO.getId());
        user.setLoginUsername(userDTO.getLoginUsername());
        user.setNickname(userDTO.getNickname());
        user.setPassword(userDTO.getPassword());
        user.setPortrait(userDTO.getPortrait());
        user.setBoundCellphone(userDTO.getBoundCellphone());
        user.setBoundEmail(userDTO.getBoundEmail());
        user.setWechatKey(userDTO.getWechatKey());
        user.setShareCode(userDTO.getShareCode());
        user.setRegisteredTime(userDTO.getRegisteredTime());
        user.setLastLoginTime(userDTO.getLastLoginTime());
        user.setType(userDTO.getType());
        user.setVipStartDate(userDTO.getVipStartDate());
        user.setVipEndDate(userDTO.getVipEndDate());
        user.setVipStatus(userDTO.getVipStatus());
        user.setSignature(userDTO.getSignature());
        user.setUserType(userDTO.getUserType());
        user.setRecommendCode(userDTO.getRecommendCode());
        user.setStatus(userDTO.getStatus());
        return user;
    }
}