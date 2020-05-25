package com.yhs.onlineshopping.dao;


import com.yhs.onlineshopping.pojo.Category;
import com.yhs.onlineshopping.pojo.Products;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICategoryDao {
    /**
     * 查看所有商品库信息
     * @return
     */
    List<Category> findAllCategories();
    /**
     * 添加商品种类
     */
    public void toAddCate(Category category);

    /**
     * 删除商品种类
     */
    public void toDeleteCate(Category category);

    /**
     * 通过商品种类ID添加商品
     * @param products
     */
    public void toAddProductByCateId(Products products);
    /**
     * 最多返回六个商品种类
     */
    List<Category> toFindCategoryLimitSix();

    /**
     * 根据商品种类id查询商品
     * @param cateid
     * @return
     */
    List<Products> toFindProductsBycateId(int cateid);

}
