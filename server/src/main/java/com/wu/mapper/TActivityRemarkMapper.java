package com.wu.mapper;

import com.wu.Query.ActivityRemarkQuery;
import com.wu.Query.BaseQuery;
import com.wu.commons.DataScope;
import com.wu.model.TActivityRemark;

import java.util.List;

public interface TActivityRemarkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TActivityRemark record);

    int insertSelective(TActivityRemark record);

    TActivityRemark selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TActivityRemark record);

    int updateByPrimaryKey(TActivityRemark record);

    @DataScope(tableAlias = "tar", tableField = "create_by")
    List<TActivityRemark> selectListByPage(ActivityRemarkQuery activityRemarkQuery);
}