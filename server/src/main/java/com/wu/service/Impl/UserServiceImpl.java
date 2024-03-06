package com.wu.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wu.constant.Constants;
import com.wu.mapper.TUserMapper;
import com.wu.model.TUser;
import com.wu.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private TUserMapper tUserMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        TUser tUser = tUserMapper.selectByLoginAct(username);
        if (tUser == null) {
            throw new UsernameNotFoundException("登录账号不存在");
        }
        return tUser;
    }

    @Override
    public PageInfo<TUser> getUserByPage(Integer current) {
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        // 2.查询
        List<TUser> list = tUserMapper.getUserByPage();
        // 3.封装分页数据到PageInfo
        PageInfo<TUser> info = new PageInfo<>(list);
        return info;
    }

    @Override
    public TUser getUserById(Integer id) {
        return tUserMapper.getUserById(id);
    }
}
