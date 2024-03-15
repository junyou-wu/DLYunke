package com.wu.Query;

import lombok.Data;

@Data
public class CustomerRemarkQuery extends BaseQuery{

    private Integer id;

    private Integer customerId;

    private Integer noteWay;

    private String noteContent;

}
