package com.wu.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wu.Query.BaseQuery;
import com.wu.Query.ClueQuery;
import com.wu.Query.ClueRemarkQuery;
import com.wu.config.listener.UploadDataListener;
import com.wu.constant.Constants;
import com.wu.mapper.TClueMapper;
import com.wu.mapper.TClueRemarkMapper;
import com.wu.model.TActivityRemark;
import com.wu.model.TClue;
import com.wu.model.TClueRemark;
import com.wu.service.ClueService;
import com.wu.util.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class ClueServiceImpl implements ClueService {
    @Resource
    private TClueMapper tClueMapper;

    @Resource
    private TClueRemarkMapper tClueRemarkMapper;

    @Override
    public PageInfo<TClue> getClueByPage(Integer current) {
        // 1.设置PageHelper
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        // 2.查询
        List<TClue> list = tClueMapper.selectClueByPage(BaseQuery.builder().build());
        // 3.封装分页数据到PageInfo
        PageInfo<TClue> info = new PageInfo<>(list);
        return info;
    }


    @Override
    public void importExcel(InputStream inputStream,String token) {
        EasyExcel.read(inputStream, TClue.class, new UploadDataListener(tClueMapper, token))
                .sheet()
                .doRead();
    }

    @Override
    public Boolean checkPhone(String phone) {
        int count = tClueMapper.selectByCount(phone);
        return count <= 0;
    }

    @Override
    public int addClue(ClueQuery clueQuery) {
        int count = tClueMapper.selectByCount(clueQuery.getPhone());
        if (count <= 0) {
            TClue tClue = new TClue();

            //把前端提交过来的参数数据对象ClueQuery复制到TClue对象中
            //Spring框架有个工具类BeanUtils可以进行对象的复制,复制的条件要求是：两个对象的字段名要相同，字段的类型也相同，这样才可以复制
            BeanUtils.copyProperties(clueQuery, tClue);

            //解析jwt得到userId
            Integer loginUserId = JWTUtils.parseUserFromJWT(clueQuery.getToken()).getId();

            tClue.setCreateTime(new Date()); //创建时间
            tClue.setCreateBy(loginUserId); //创建人id

            return tClueMapper.insertSelective(tClue);
        } else {
            throw new RuntimeException("该手机号已经录入过了，不能再录入");
        }
    }

    @Override
    public int updateClue(ClueQuery clueQuery) {

        TClue tClue = new TClue();
        BeanUtils.copyProperties(clueQuery, tClue);

        Integer loginUserId = JWTUtils.parseUserFromJWT(clueQuery.getToken()).getId();

        tClue.setEditTime(new Date());
        tClue.setEditBy(loginUserId);

        return tClueMapper.updateByPrimaryKeySelective(tClue);
    }

    @Override
    public TClue getClueById(Integer id) {
        return tClueMapper.selectDetailById(id);
    }

    @Override
    public int delClueById(Integer id) {
        return tClueMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int batchDelActivitiesById(List<String> idList) {
        return tClueMapper.deleteByIds(idList);
    }

    @Override
    public PageInfo<TClueRemark> getClueRemarkByPage(Integer current, ClueRemarkQuery clueRemarkQuery) {
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        // 2.查询
        List<TClueRemark> list = tClueRemarkMapper.selectClueRemarkByPage(clueRemarkQuery);
        // 3.封装分页数据到PageInfo
        PageInfo<TClueRemark> info = new PageInfo<>(list);
        return info;
    }

    @Override
    public int saveClueRemark(ClueRemarkQuery clueRemarkQuery) {
        TClueRemark tClueRemark = new TClueRemark();

        //把ClueRemarkQuery对象里面的属性数据复制到TClueRemark对象里面去(复制要求：两个对象的属性名相同，属性类型要相同，这样才能复制)
        BeanUtils.copyProperties(clueRemarkQuery, tClueRemark);

        tClueRemark.setCreateTime(new Date()); //创建时间

        //登录人的id
        Integer loginUserId = JWTUtils.parseUserFromJWT(clueRemarkQuery.getToken()).getId();
        tClueRemark.setCreateBy(loginUserId); //创建人

        return tClueRemarkMapper.insertRemarkSelective(tClueRemark);
    }

    @Override
    public TClueRemark getRemark(Integer id) {
        return tClueRemarkMapper.selectRemarkByPrimaryKey(id);
    }

    @Override
    public int updateRemark(ClueRemarkQuery clueRemarkQuery) {
        TClueRemark tClueRemark = new TClueRemark();
        BeanUtils.copyProperties(clueRemarkQuery,tClueRemark);
        tClueRemark.setEditTime(new Date());
        tClueRemark.setEditBy(JWTUtils.parseUserFromJWT(clueRemarkQuery.getToken()).getId());
        return tClueRemarkMapper.updateByPrimaryKeySelective(tClueRemark);
    }

    @Override
    public int deleteRemark(Integer id) {
        TClueRemark tClueRemark = new TClueRemark();
        tClueRemark.setId(id);
        tClueRemark.setDeleted(1);
        return tClueRemarkMapper.updateByPrimaryKeySelective(tClueRemark);
    }
}
