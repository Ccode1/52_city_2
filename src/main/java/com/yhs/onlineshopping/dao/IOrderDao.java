package com.yhs.onlineshopping.dao;

import com.yhs.onlineshopping.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IOrderDao {
    /**
     * 查询所有订单
     * @return
     */
    List<Order> toFindAllOrders();

    /**
     * 根据Oid删除商品订单
     * @param oid
     */
    void toDeleteOrderById(int oid);

    /**
     * 根据Oid查询某个订单
     * @param oid
     * @return
     */
    Order toFindOrderById(int oid);

    /**
     * 修改订单信息
     * @param order
     */
    void toAlterOrderById(Order order);

    /**
     * 后台添加订单
     * @param order
     */
    void toAddOrder(Order order);

    /**
     * 查询用户订单
     * @param uid
     * @return
     */
    List<Order> toFindOrdersByUid(int uid);
}
