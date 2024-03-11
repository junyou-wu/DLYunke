package com.wu.web;

import com.wu.Query.ActivityQuery;
import com.wu.Query.ActivityRemarkQuery;
import com.wu.model.TActivity;
import com.wu.model.TActivityRemark;
import com.wu.model.TUser;
import com.wu.result.R;
import com.wu.service.ActivityService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ActivityController {

    @Resource
    private ActivityService activityService;

    /**
     * 用户列表分页查询
     *
     * @param current
     * @return
     */
    @GetMapping(value = "/api/activitys")
    public R activityPage(@RequestParam(value = "current", required = false) Integer current, ActivityQuery activityQuery) {

        //required = false 表示参数可以传，也可以不传；
        //required = true 表示参数必须要传，不传会报错；
        if (current == null) {
            current = 1;
        }
        PageInfo<TActivity> pageInfo = activityService.getActivityByPage(current, activityQuery);
        return R.OK(pageInfo);
    }

    @PostMapping(value = "/api/activity/add")
    public R addActivity(ActivityQuery activityQuery, @RequestHeader(value = "Authorization") String token) {
        activityQuery.setToken(token);
        int save = activityService.saveActivity(activityQuery);
        return save >= 1 ? R.OK() : R.FAIL();
    }

    @GetMapping(value = "/api/activity/{id}")
    public R loadActivity(@PathVariable(value = "id") Integer id) {
        TActivity tActivity = activityService.getActivityById(id);
        return R.OK(tActivity);
    }

    @PostMapping(value = "/api/activity/edit")
    public R editActivity(ActivityQuery activityQuery, @RequestHeader(value = "Authorization") String token) {
        activityQuery.setToken(token);
        int update = activityService.updateActivity(activityQuery);
        return update >= 1 ? R.OK() : R.FAIL();
    }

    @PostMapping("/api/activity/remark/save")
    public R addActivityRemark(@RequestBody ActivityRemarkQuery activityRemarkQuery,@RequestHeader(value = "Authorization") String token){

        activityRemarkQuery.setToken(token);
        int addRemark = activityService.saveActivityRemark(activityRemarkQuery);

        return addRemark >= 1 ? R.OK() : R.FAIL();
    }

    @GetMapping("/api/activity/remark/remarkList")
    public R getRemarkList(@RequestParam(value = "current") Integer current, @RequestParam(value = "activityId") Integer activityId){

        ActivityRemarkQuery activityRemarkQuery = new ActivityRemarkQuery();
        activityRemarkQuery.setActivityId(activityId);

        if (current == null) {
            current = 1;
        }

        PageInfo<TActivityRemark> remarkList = activityService.getActivityRemarkList(current,activityRemarkQuery);

        return R.OK(remarkList);
    }
}
