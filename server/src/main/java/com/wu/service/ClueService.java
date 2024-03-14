package com.wu.service;

import com.github.pagehelper.PageInfo;
import com.wu.Query.ClueQuery;
import com.wu.Query.ClueRemarkQuery;
import com.wu.model.TClue;
import com.wu.model.TClueRemark;

import java.io.InputStream;
import java.util.List;

public interface ClueService {

    PageInfo<TClue> getClueByPage(Integer current);

    void importExcel(InputStream inputStream,String token);

    Boolean checkPhone(String phone);

    int addClue(ClueQuery clueQuery);

    int updateClue(ClueQuery clueQuery);

    TClue getClueById(Integer id);

    int delClueById(Integer id);

    int batchDelActivitiesById(List<String> idList);

    PageInfo<TClueRemark> getClueRemarkByPage(Integer current, ClueRemarkQuery clueRemarkQuery);

    int saveClueRemark(ClueRemarkQuery clueRemarkQuery);

    TClueRemark getRemark(Integer id);

    int updateRemark(ClueRemarkQuery clueRemarkQuery);

    int deleteRemark(Integer id);
}
