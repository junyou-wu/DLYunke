package com.wu.service;

import com.github.pagehelper.PageInfo;
import com.wu.Query.ClueRemarkQuery;
import com.wu.Query.CustomerQuery;
import com.wu.Query.CustomerRemarkQuery;
import com.wu.model.TClue;
import com.wu.model.TCustomer;
import com.wu.model.TCustomerRemark;
import com.wu.result.CustomerExcel;

import java.util.List;

public interface CustomerService {
    Boolean convertCustomer(CustomerQuery customerQuery);
    PageInfo<TCustomer> getCustomerByPage(Integer current);

    public List<CustomerExcel> getCustomerByExcel(List<String> idList);

    TCustomer getCustomerById(Integer id);

    int saveCustomerRemark(CustomerRemarkQuery customerRemarkQuery);

    PageInfo<TCustomerRemark> getCustomerRemarkByPage(Integer current, CustomerRemarkQuery customerRemarkQuery);

    TCustomerRemark getRemark(Integer id);

    int updateCustomerRemark(CustomerRemarkQuery customerRemarkQuery);

    int deleteRemark(Integer id);
}
