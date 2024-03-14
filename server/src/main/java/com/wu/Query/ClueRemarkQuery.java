package com.wu.Query;

import lombok.Data;

@Data
public class ClueRemarkQuery extends BaseQuery {
    private Integer id;

    private Integer clueId;

    private Integer noteWay;

    private String noteContent;

    private Integer deleted;


}
