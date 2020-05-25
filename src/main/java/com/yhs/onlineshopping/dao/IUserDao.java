package com.yhs.onlineshopping.dao;


import com.yhs.onlineshopping.pojo.Admin;
import com.yhs.onlineshopping.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserDao {
    /**
     * 验证用户登录
     * @param user
     * @return
     */
    Integer doLogin(User user);

    /**
     * 用户注册
     * @param user
     */
    void registerUser(User user);
    /**
     * 根据uid查询User信息
     * @param uid
     * @return
     */
    User toFindUserById(int uid);

    /**
     * 根据username查询用户id
     * @param name
     * @return
     */
    User toFindUidByName(String name);
    /**
     * 修改地址信息
     * @param user
     */
    void toAlterAddressById(User user);
    void toAlterPasswordById(User user);

    /**
     * 删除地址
     * @param user
     */
    void toDeleteAddressById(User user);
}
