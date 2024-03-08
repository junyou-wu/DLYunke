package com.wu.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wu.Query.BaseQuery;
import com.wu.Query.UserQuery;
import com.wu.constant.Constants;
import com.wu.manager.RedisManager;
import com.wu.mapper.TRoleMapper;
import com.wu.mapper.TUserMapper;
import com.wu.model.TRole;
import com.wu.model.TUser;
import com.wu.service.UserService;
import com.wu.util.CacheUtils;
import com.wu.util.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private TUserMapper tUserMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    TRoleMapper tRoleMapper;

    @Resource
    RedisManager redisManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        TUser tUser = tUserMapper.selectByLoginAct(username);
        if (tUser == null) {
            throw new UsernameNotFoundException("登录账号不存在");
        }
        List<TRole> tRoleList = tRoleMapper.selectByUserId(tUser.getId());
        //字符串的角色列表
        List<String> stringRoleList = new ArrayList<>();
        tRoleList.forEach(tRole -> {
            stringRoleList.add(tRole.getRole());
        });
        tUser.setRoleList(stringRoleList);
        return tUser;
    }

    @Override
    public PageInfo<TUser> getUserByPage(Integer current) {
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        // 2.查询
        List<TUser> list = tUserMapper.getUserByPage(BaseQuery.builder().build());
        // 3.封装分页数据到PageInfo
        PageInfo<TUser> info = new PageInfo<>(list);
        return info;
    }


    @Override
    public TUser getUserDetailById(Integer id) {
        return tUserMapper.getUserById(id);
    }

    @Override
    public int insertUser(UserQuery userQuery) {

        String userToken = userQuery.getToken();
        TUser createUser = JWTUtils.parseUserFromJWT(userToken);
        TUser tUser = new TUser();
        BeanUtils.copyProperties(userQuery,tUser);
        tUser.setLoginPwd(passwordEncoder.encode(tUser.getLoginPwd()));
        tUser.setCreateTime(new Date());
        tUser.setCreateBy(createUser.getId());
        return tUserMapper.insertSelective(tUser);
    }

    @Override
    public int updateUser(UserQuery userQuery) {
        TUser tUser = new TUser();

        BeanUtils.copyProperties(userQuery, tUser);

        if (StringUtils.hasText(userQuery.getLoginPwd())) {
            tUser.setLoginPwd(passwordEncoder.encode(userQuery.getLoginPwd())); //密码加密
        }

        tUser.setEditTime(new Date()); //编辑时间

        //登录人的id
        Integer loginUserId = JWTUtils.parseUserFromJWT(userQuery.getToken()).getId();
        tUser.setEditBy(loginUserId); //创建人

        return tUserMapper.updateByPrimaryKeySelective(tUser);
    }

    @Override
    public int delUserById(Integer id) {
        return tUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int batchDelUserIds(List<String> idList) {
        return tUserMapper.deleteByIds(idList);
    }

    @Override
    public int updateLoginTime(Integer id) {
        Date date = new Date();
        return tUserMapper.updateLastLoginTime(id,date);
    }

    @Override
    public List<TUser> getOwnerList() {
        //1、从redis查询
        //2、redis查不到，就从数据库查询，并且把数据放入redis（5分钟过期）
        return CacheUtils.getCacheData(() -> {
                    //生产，从缓存redis查询数据
                    return (List<TUser>)redisManager.getValue(Constants.REDIS_OWNER_KEY);
                },
                () -> {
                    //生产，从mysql查询数据
                    return (List<TUser>)tUserMapper.selectByOwner();
                },
                (t) -> {
                    //消费，把数据放入缓存redis
                    redisManager.setValue(Constants.REDIS_OWNER_KEY, t);
                }
        );
    }
}
