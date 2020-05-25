package com.yhs.onlineshopping.dao;
import com.yhs.onlineshopping.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ILoginDao {
    //管理员登录
    Integer doLogin(Admin admin);
}
