package com.yhs.onlineshopping.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("管理员实体类")
public class Admin implements Serializable {
    private int aid;
    private String username;
    private String password;
}
