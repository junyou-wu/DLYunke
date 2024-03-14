package com.wu.mapper;

import com.wu.Query.ClueRemarkQuery;
import com.wu.commons.DataScope;
import com.wu.model.TClueRemark;

import java.util.List;

public interface TClueRemarkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TClueRemark record);

    int insertSelective(TClueRemark record);

    TClueRemark selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TClueRemark record);

    int updateByPrimaryKey(TClueRemark record);

    @DataScope(tableAlias = "tcr", tableField = "create_by")
    List<TClueRemark> selectClueRemarkByPage(ClueRemarkQuery clueRemarkQuery);

    int insertRemarkSelective(TClueRemark tClueRemark);

    TClueRemark selectRemarkByPrimaryKey(Integer id);
}