package com.yhs.onlineshopping.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("商品实体类")
public class Products implements Serializable {
 private int pid;
 private int cateid;
 private String name;
 private String image;
 private float price;
 private int salesCount;
}
