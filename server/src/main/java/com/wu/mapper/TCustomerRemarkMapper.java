package com.wu.mapper;

import com.wu.Query.ClueRemarkQuery;
import com.wu.Query.CustomerRemarkQuery;
import com.wu.commons.DataScope;
import com.wu.model.TCustomer;
import com.wu.model.TCustomerRemark;

import java.util.List;

public interface TCustomerRemarkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TCustomerRemark record);

    int insertSelective(TCustomerRemark record);

    TCustomerRemark selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TCustomerRemark record);

    int updateByPrimaryKey(TCustomerRemark record);

    @DataScope(tableAlias = "tcr", tableField = "create_by")
    List<TCustomerRemark> selectCustomerRemarkByPage(CustomerRemarkQuery customerRemarkQuery);
}