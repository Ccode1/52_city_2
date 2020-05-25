package com.yhs.onlineshopping.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("用户实体类")
public class User implements Serializable {
    private int uid;
    private String username;
    private String password;
    private String address;
    private String phone;
    private String email;
}
