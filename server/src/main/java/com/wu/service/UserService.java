package com.wu.service;

import com.github.pagehelper.PageInfo;
import com.wu.model.TUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    PageInfo<TUser> getUserByPage(Integer current);

    TUser getUserById(Integer id);
}
