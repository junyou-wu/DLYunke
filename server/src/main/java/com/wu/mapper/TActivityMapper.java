package com.wu.mapper;

import com.wu.Query.ActivityQuery;
import com.wu.commons.DataScope;
import com.wu.model.TActivity;

import java.util.List;

public interface TActivityMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByIds(List<String> idList);

    int insert(TActivity record);

    int insertSelective(TActivity record);

    TActivity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TActivity record);

    int updateByPrimaryKey(TActivity record);

    @DataScope(tableAlias = "ta", tableField = "owner_id")
    List<TActivity> selectActivityByPage(ActivityQuery query);

    TActivity selectDetailByPrimaryKey(Integer id);

    List<TActivity> selectOngoingActivity();

    Integer selectByCount();

}