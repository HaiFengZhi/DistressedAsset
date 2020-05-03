package com.distressed.asset.portal.dao;

import com.distressed.asset.portal.mapping.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserDAO {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    User selectByPrimaryKey(Long id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    List<User> selectBy(@Param(value = "params") Map<String, Object> params);
    Map<String,Object> selectById(Long id);
    Integer selectCountByUserName(@Param(value = "loginUsername") String loginUsername, @Param(value = "id") Long id);
}