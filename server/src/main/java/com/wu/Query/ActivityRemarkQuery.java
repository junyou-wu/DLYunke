package com.wu.Query;

import lombok.Data;

@Data
public class ActivityRemarkQuery extends BaseQuery{
    int activityId;
    String noteContent;
}
