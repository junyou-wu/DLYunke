package com.wu.web;

import com.wu.ServerApplication;
import com.wu.model.TActivity;
import com.wu.model.TDicValue;
import com.wu.model.TProduct;
import com.wu.result.DicEnum;
import com.wu.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DicController {

    @GetMapping(value = "/api/dicValue/{typeCode}")
    public R loadDicValue(@PathVariable(value = "typeCode") String typeCode){
        if(typeCode.equals(DicEnum.ACTIVITY.getCode())){
            List<TActivity> activityList = (List<TActivity>) ServerApplication.cacheMap.get(typeCode);
            return R.OK(activityList);
        }else if(typeCode.equals(DicEnum.PRODUCT.getCode())){
            List<TProduct> tProductList = (List<TProduct>)ServerApplication.cacheMap.get(typeCode);
            return R.OK(tProductList);
        }else{
            List<TDicValue> tDicValueList = (List<TDicValue>)ServerApplication.cacheMap.get(typeCode);
            return R.OK(tDicValueList);
        }
    }
}
