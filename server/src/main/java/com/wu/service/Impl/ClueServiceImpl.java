package com.wu.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wu.Query.BaseQuery;
import com.wu.constant.Constants;
import com.wu.mapper.TClueMapper;
import com.wu.model.TClue;
import com.wu.service.ClueService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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


}
