package com.wu.Query;

import lombok.Data;

@Data
public class ActivityRemarkQuery extends BaseQuery{

    Integer id;

    Integer activityId;

    String noteContent;
}
