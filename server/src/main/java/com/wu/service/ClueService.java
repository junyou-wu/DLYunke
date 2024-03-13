package com.wu.service;

import com.github.pagehelper.PageInfo;
import com.wu.model.TClue;

import java.io.InputStream;

public interface ClueService {

    PageInfo<TClue> getClueByPage(Integer current);

    void importExcel(InputStream inputStream,String token);
}
