package com.wu.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wu.Query.BaseQuery;
import com.wu.config.listener.UploadDataListener;
import com.wu.constant.Constants;
import com.wu.mapper.TClueMapper;
import com.wu.model.TClue;
import com.wu.service.ClueService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class ClueServiceImpl implements ClueService {
    @Resource
    private TClueMapper tClueMapper;

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
}
