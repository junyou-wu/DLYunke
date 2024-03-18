package com.wu.mapper;

import com.wu.Query.BaseQuery;
import com.wu.Query.ClueRemarkQuery;
import com.wu.commons.DataScope;
import com.wu.model.TClue;
import com.wu.model.TClueRemark;
import com.wu.result.NameValue;

import java.util.List;

public interface TClueMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TClue record);

    int insertSelective(TClue record);

    TClue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TClue record);

    int updateByPrimaryKey(TClue record);

    List<TClue> selectClueByPage(BaseQuery build);

    void saveClue(List<TClue> tClueList);

    int selectByCount(String phone);

    TClue selectDetailById(Integer id);

    int deleteByIds(List<String> idList);

    Integer selectClueByCount();

    List<NameValue> selectBySource();
}