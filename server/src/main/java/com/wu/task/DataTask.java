package com.wu.task;

import com.wu.ServerApplication;
import com.wu.model.TActivity;
import com.wu.model.TDicType;
import com.wu.model.TDicValue;
import com.wu.model.TProduct;
import com.wu.result.DicEnum;
import com.wu.service.ActivityService;
import com.wu.service.DicTypeService;
import com.wu.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableScheduling //开启定时任务
@Component
public class DataTask {

    @Resource
    private DicTypeService dicTypeService;

    @Resource
    private ProductService productService;

    @Resource
    private ActivityService activityService;

    //调度的意思
    //@Scheduled(cron = "${project.task.cron}", zone = "Asia/Shanghai", timeUnit = TimeUnit.MILLISECONDS, initialDelay  = 1000)
    @Scheduled(fixedDelayString = "${project.task.delay}", zone = "Asia/Shanghai", timeUnit = TimeUnit.MILLISECONDS, initialDelay  = 1000)
    public void task() {
        //定时任务要执行的业务逻辑代码
        System.out.println("定时任务业务逻辑执行......" + new Date());

        List<TDicType> tDicTypeList = dicTypeService.loadAllDicData();

        tDicTypeList.forEach(tDicType -> {
            String typeCode = tDicType.getTypeCode();
            List<TDicValue> tDicValueList = tDicType.getDicValueList();
            ServerApplication.cacheMap.put(typeCode, tDicValueList);
        });

        //查询所有在售产品
        List<TProduct> tProductList = productService.getAllOnSaleProduct();
        ServerApplication.cacheMap.put(DicEnum.PRODUCT.getCode(), tProductList);

        //查询所有正在进行的市场活动
        List<TActivity> tActivityList = activityService.getOngoingActivity();
        ServerApplication.cacheMap.put(DicEnum.ACTIVITY.getCode(), tActivityList);
    }
}
