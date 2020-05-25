package com.yhs.onlineshopping.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("订单实体类")
public class Order implements Serializable {
    private int oid;
    private int uid;
    private String name;
    private String image;
    private String address;
    private String phone;
    private int count;
    private float price;
    private int state;
}
