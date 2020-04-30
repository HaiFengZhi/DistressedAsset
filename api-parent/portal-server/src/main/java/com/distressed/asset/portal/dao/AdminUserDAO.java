package com.distressed.asset.portal.dao;

import com.distressed.asset.portal.mapping.AdminUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AdminUserDAO {
    int deleteByPrimaryKey(Long id);

    int insert(AdminUser record);

    int insertSelective(AdminUser record);

    AdminUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AdminUser record);

    int updateByPrimaryKey(AdminUser record);

    List<AdminUser> selectAll();

    /**
     * 根据用户编号更新用户密码。
     *
     * @param id 用户编号。
     * @param password 用户密码。
     * @param modifyPwdOr 是否修改标识。
     * @return 结果。
     */
    int generatePwd(Long id,String password,Integer modifyPwdOr);

    /**
     * 根据条件查询后台用户列表。
     *
     * @param params 查询条件。
     * @return 用户列表。
     */
    List<AdminUser> selectBy(@Param("params") Map<String, Object> params);

    /**
     * 根据用户名获取管理员列表。
     *
     * @param loginUsername 登陆名。
     * @return 管理员列表。
     */
    List<AdminUser> selectByLoginName(@Param("loginUsername") String loginUsername);

    /**
     * 根据用户名和密码校验登陆管理员。
     *
     * @param loginUsername 登陆名。
     * @param password      密码。
     * @return 管理员信息。
     */
    AdminUser selectLogin(@Param("loginUsername") String loginUsername, @Param("password") String password);

    /**
     * 根据用户编号集合获取管理员信息列表。
     *
     * @param ids 用户编号集合。
     * @return 管理员集合。
     */
    List<AdminUser> selectByIds(@Param("ids") String[] ids);
}