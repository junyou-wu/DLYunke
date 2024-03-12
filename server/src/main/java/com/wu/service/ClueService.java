package com.wu.service;

import com.github.pagehelper.PageInfo;
import com.wu.Query.BaseQuery;
import com.wu.model.TClue;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ClueService {

    PageInfo<TClue> getClueByPage(Integer current);
}
