package com.wu.config.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.wu.ServerApplication;
import com.wu.model.TDicValue;
import com.wu.result.DicEnum;

import java.util.List;

/**
 * 线索状态转换器
 *
 */
public class StateConverter implements Converter<Integer> {

    /**
     * 把Excel中的数据转换为Java中的数据
     * 也就是Excel中的 “已联系”  ----> Java类中是 27
     *
     * @param cellData
     * @param contentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public Integer convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        //cellData是Excel中读取到的数据，是“已联系”、“未联系”
        String cellStateName = cellData.getStringValue();

        List<TDicValue> tDicValueList = (List<TDicValue>) ServerApplication.cacheMap.get(DicEnum.STATE.getCode());
        for (TDicValue tDicValue : tDicValueList) {
            Integer id  = tDicValue.getId();
            String name = tDicValue.getTypeValue();

            if (cellStateName.equals(name)) {
                return id;
            }
        }
        return -1;
    }
}
