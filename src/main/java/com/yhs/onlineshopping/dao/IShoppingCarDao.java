package com.yhs.onlineshopping.dao;


import com.yhs.onlineshopping.pojo.Order;
import com.yhs.onlineshopping.pojo.ShoppingCar;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IShoppingCarDao {
    /**
     * 添加购物车
     * @param shoppingCar
     */
    public void toAddShoppingCar(ShoppingCar shoppingCar);

    /**
     * 根据用户ID查看购物车
     * @param uid
     * @return
     */
    List<ShoppingCar> toFindShoppingCarByUid(int uid);

    /**
     * 根据购物车唯一标识cid查询该条信息
     * @param cid
     * @return
     */
    ShoppingCar toFindShoppingByCid(int cid);

    /**
     * 删除购物车
     * @param cid
     */
    void toDeleteShoppingCarByCid(int cid);

    /**
     * 购买商品
     * @param order
     */
    void toBuyProduct(Order order);

    /**
     * 根据商品的名称查询该商品是否在购物车内
     * @param name
     * @return
     */
    Integer toFindShoppingByName(String name);

    /**
     * 增加购物车已有商品的数量
     * @param shoppingCar
     */
    void toAlterCount(ShoppingCar shoppingCar);
}
