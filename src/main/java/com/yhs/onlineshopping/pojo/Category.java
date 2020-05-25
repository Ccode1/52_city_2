package com.yhs.onlineshopping.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("商品种类实体类")
public class Category implements Serializable {
    private int cateid;
    private String catename;
    private String image;
}
