package com.wu.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wu.Query.ClueRemarkQuery;
import com.wu.Query.CustomerQuery;
import com.wu.Query.CustomerRemarkQuery;
import com.wu.constant.Constants;
import com.wu.manager.CustomerManager;
import com.wu.mapper.TCustomerMapper;
import com.wu.mapper.TCustomerRemarkMapper;
import com.wu.model.TClue;
import com.wu.model.TClueRemark;
import com.wu.model.TCustomer;
import com.wu.model.TCustomerRemark;
import com.wu.result.CustomerExcel;
import com.wu.service.CustomerService;
import com.wu.util.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Resource
    TCustomerMapper tCustomerMapper;

    @Resource
    TCustomerRemarkMapper tCustomerRemarkMapper;

    @Resource
    CustomerManager customerManager;
    @Override
    public Boolean convertCustomer(CustomerQuery customerQuery) {
        return customerManager.convertCustomer(customerQuery);
    }

    @Override
    public PageInfo<TCustomer> getCustomerByPage(Integer current) {
        //1.设置PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        //2.查询
        List<TCustomer> list = tCustomerMapper.selectCustomerPage();
        //3.封装分页数据到PageInfo
        PageInfo<TCustomer> info = new PageInfo<>(list);

        return info;
    }

    @Override
    public List<CustomerExcel> getCustomerByExcel(List<String> idList) {
        List<CustomerExcel> customerExcelList = new ArrayList<>();

        List<TCustomer> tCustomerList = tCustomerMapper.selectCustomerByExcel(idList);

        //把从数据库查询出来的List<TCustomer>数据，转换为 List<CustomerExcel>数据
        tCustomerList.forEach(tCustomer -> {
            CustomerExcel customerExcel = new CustomerExcel();

            //需要一个一个设置，没有办法，因为没法使用BeanUtils复制
            customerExcel.setOwnerName(ObjectUtils.isEmpty(tCustomer.getOwnerDO()) ? Constants.EMPTY : tCustomer.getOwnerDO().getName());
            customerExcel.setActivityName(ObjectUtils.isEmpty(tCustomer.getActivityDO()) ? Constants.EMPTY : tCustomer.getActivityDO().getName());
            customerExcel.setFullName(tCustomer.getClueDO().getFullName());
            customerExcel.setAppellationName(ObjectUtils.isEmpty(tCustomer.getAppellationDO()) ? Constants.EMPTY : tCustomer.getAppellationDO().getTypeValue());
            customerExcel.setPhone(tCustomer.getClueDO().getPhone());
            customerExcel.setWeixin(tCustomer.getClueDO().getWeixin());
            customerExcel.setQq(tCustomer.getClueDO().getQq());
            customerExcel.setEmail(tCustomer.getClueDO().getEmail());
            customerExcel.setAge(tCustomer.getClueDO().getAge());
            customerExcel.setJob(tCustomer.getClueDO().getJob());
            customerExcel.setYearIncome(tCustomer.getClueDO().getYearIncome());
            customerExcel.setAddress(tCustomer.getClueDO().getAddress());
            customerExcel.setNeedLoadName(ObjectUtils.isEmpty(tCustomer.getNeedLoanDO()) ? Constants.EMPTY : tCustomer.getNeedLoanDO().getTypeValue());
            customerExcel.setProductName(ObjectUtils.isEmpty(tCustomer.getIntentionProductDO()) ? Constants.EMPTY : tCustomer.getIntentionProductDO().getName());
            customerExcel.setSourceName(ObjectUtils.isEmpty(tCustomer.getSourceDO()) ? Constants.EMPTY : tCustomer.getSourceDO().getTypeValue());
            customerExcel.setDescription(tCustomer.getDescription());
            customerExcel.setNextContactTime(tCustomer.getNextContactTime());

            customerExcelList.add(customerExcel);
        });
        return customerExcelList;
    }

    @Override
    public TCustomer getCustomerById(Integer id) {
        return tCustomerMapper.selectCustomerDetailById(id);
    }

    @Override
    public int saveCustomerRemark(CustomerRemarkQuery customerRemarkQuery) {
        TCustomerRemark tCustomerRemark = new TCustomerRemark();
        BeanUtils.copyProperties(customerRemarkQuery,tCustomerRemark);
        tCustomerRemark.setCreateTime(new Date());
        tCustomerRemark.setCreateBy(JWTUtils.parseUserFromJWT(customerRemarkQuery.getToken()).getId());
        return tCustomerRemarkMapper.insertSelective(tCustomerRemark);
    }
    @Override
    public PageInfo<TCustomerRemark> getCustomerRemarkByPage(Integer current, CustomerRemarkQuery customerRemarkQuery) {
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        // 2.查询
        List<TCustomerRemark> list = tCustomerRemarkMapper.selectCustomerRemarkByPage(customerRemarkQuery);
        // 3.封装分页数据到PageInfo
        PageInfo<TCustomerRemark> info = new PageInfo<>(list);
        return info;
    }

    @Override
    public TCustomerRemark getRemark(Integer id) {
        return tCustomerRemarkMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateCustomerRemark(CustomerRemarkQuery customerRemarkQuery) {
        TCustomerRemark tCustomerRemark = new TCustomerRemark();
        BeanUtils.copyProperties(customerRemarkQuery,tCustomerRemark);
        tCustomerRemark.setCreateTime(new Date());
        tCustomerRemark.setCreateBy(JWTUtils.parseUserFromJWT(customerRemarkQuery.getToken()).getId());
        return tCustomerRemarkMapper.updateByPrimaryKeySelective(tCustomerRemark);
    }

    @Override
    public int deleteRemark(Integer id) {

        TCustomerRemark tCustomerRemark = new TCustomerRemark();
        tCustomerRemark.setId(id);
        tCustomerRemark.setDeleted(1);
        return tCustomerRemarkMapper.updateByPrimaryKeySelective(tCustomerRemark);
    }
}
