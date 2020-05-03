package com.distressed.asset.portal.service;

import com.distressed.asset.portal.dao.UserDAO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.distressed.asset.portal.dto.UserDTO;
import com.distressed.asset.portal.mapping.User;
import com.distressed.asset.portal.transform.UserDT;
import com.distressed.asset.common.exception.RowsUpdateNotMatchException;
import com.distressed.asset.common.result.ResultBean;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CacheConfig(cacheNames = "userCache")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDAO userDAO;

    @Override
    public PageInfo<UserDTO> pageUserList(Map<String, Object> params, int pageNum, int pageSize) {
        //设置分页开始
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<UserDTO> list = new UserDT().fromByList(userDAO.selectBy(params));
        //4. 根据返回的集合，创建PageInfo对象
        PageInfo<UserDTO> info = new PageInfo<>(list);
        info.setTotal(page.getTotal());
        return info;
    }

    @Override
    public Map<String, Object> userDetail(Long id) {
        return userDAO.selectById(id);
    }

    @Override
    public ResultBean saveOrUpdateUser(UserDTO userDTO) {
        //DTO转化为数据库对象
        User user = new UserDT().to(userDTO);
        user.setStatus(0);
        int count = 0;
        if (user.getId()==null){
            count = userDAO.insert(user);
            //todo:新增用户账户信息
//            Account account =new Account();
//            account.setUserId(user.getId());
//            account.setAmount(new BigDecimal(0));
//            account.setFrozenAmount(new BigDecimal(0));
//            account.setTotalAmount(new BigDecimal(0));
//            account.setStatus(0);
//            account.setVersion(0);
//            accountMapper.insert(account);
        }else {
            count = userDAO.updateByPrimaryKey(user);
        }
        if (count !=1){
            throw new RowsUpdateNotMatchException("变更用户数据异常，用户数据：" + user.toString());
        }
        return ResultBean.success();
    }

    @Override
    public ResultBean updateUser(UserDTO userDTO) {
        User user = new UserDT().to(userDTO);
        int count = userDAO.updateByPrimaryKey(user);
        if (count !=1){
            throw new RowsUpdateNotMatchException("变更用户数据异常，用户数据：" + user.toString());
        }
        return ResultBean.success();
    }

    @Override
    public ResultBean<Integer> getCountByUserName(String loginUsername,Long id) {
        Integer count= userDAO.selectCountByUserName(loginUsername,id);
        return ResultBean.successForData(count);
    }

    @Override
    public ResultBean<UserDTO> checkUserLogin(String loginUsername, String password) {
        Map<String,Object> params = new HashMap<>(1);
        params.put("loginUsername",loginUsername);
        List<User> list = userDAO.selectBy(params);
        if (list!=null && list.size()>0){
            return ResultBean.successForData(new UserDT().from(list.get(0)));
        }
        return ResultBean.failed("当前用户不存在！");
    }
}
