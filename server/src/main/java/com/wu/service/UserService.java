package com.wu.service;

import com.github.pagehelper.PageInfo;
import com.wu.Query.UserQuery;
import com.wu.model.TUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    PageInfo<TUser> getUserByPage(Integer current);

    TUser getUserDetailById(Integer id);

    int insertUser(UserQuery userQuery);

    int updateUser(UserQuery userQuery);

    int delUserById(Integer id);

    int batchDelUserIds(List<String> idList);

    int updateLoginTime(Integer id);

    List<TUser> getOwnerList();
}
