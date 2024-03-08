package com.wu.mapper;

import com.wu.Query.BaseQuery;
import com.wu.commons.DataScope;
import com.wu.model.TUser;

import java.util.Date;
import java.util.List;

public interface TUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);

    TUser selectByLoginAct(String username);

    @DataScope(tableAlias = "tu",tableField = "id")
    List<TUser> getUserByPage(BaseQuery query);

    TUser getUserById(Integer id);

    int deleteByIds(List<String> idList);

    int updateLastLoginTime(Integer id, Date date);

    List<TUser> selectByOwner();
}