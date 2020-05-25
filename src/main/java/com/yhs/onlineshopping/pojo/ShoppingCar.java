package com.yhs.onlineshopping.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("购物车实体类")
public class ShoppingCar implements Serializable {
    private int cid;
    private int uid;
    private String name;
    private String image;
    private float price;
    private int count;
    private String address;
    private String phone;
}
