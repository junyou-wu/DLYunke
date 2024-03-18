package com.wu.service;

import com.wu.result.NameValue;
import com.wu.result.SummaryData;

import java.util.List;

public interface StatisticService {

    SummaryData loadSummaryData();

    List<NameValue> loadSaleFunnelData();

    List<NameValue> loadSourcePieData();
}
