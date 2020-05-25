package com.yhs.onlineshopping.dao;

import com.yhs.onlineshopping.pojo.Products;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IProductsDao {
    /**
     * 查询所有商品信息
     *
     * @return
     */
    List<Products> toDisplayProducts();

    /**
     * 根据cateid查询跳蚤市场信息
     * @param cateid
     * @return
     */
    List<Products> toDisplayProductsByCateid(int cateid);
    /**
     * 根据商品ID删除商品
     * @param pid
     */
    public void deleteProductById(int pid);

    /**
     * 根据Pid查询商品
     * @param pid
     * @return
     */
    Products toFindProductByPid(int pid);

    /**
     * 根据Pid修改商品信息
     * @param products
     */
    public void toAlterPrdouctSuccess(Products products);

    /**
     * 返回表中4个数据
     * @return
     */
    List<Products> toFindFourProduct();
    /**
     * 返回表中4个数据
     * @return
     */
    List<Products> toFindSevenProduct();
    List<Products> toFindManyProductsByKeyWords(String name,int cateid);
}
