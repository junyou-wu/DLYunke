package com.wu.service;

import com.wu.Query.ActivityQuery;
import com.wu.model.TActivity;
import com.github.pagehelper.PageInfo;

import java.util.List;

// Easy Code 插件（Idea插件生成  controller 、service、dao）

public interface ActivityService {

    PageInfo<TActivity> getActivityByPage(Integer current, ActivityQuery activityQuery);

    int saveActivity(ActivityQuery activityQuery);

    TActivity getActivityById(Integer id);

    int updateActivity(ActivityQuery activityQuery);

    List<TActivity> getOngoingActivity();
}
