package org.tmall.entity;

import java.util.List;

/**
 * 商品类别类
 */
public class Category {

    private String name;
    private int id;
    // 与 product 一对多
    List<Product> products;
    // 用于首页竖状导航分类，一个类别对应多行商品行，每行包含多个商品名
    List<List<Product>> productsByRow;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<List<Product>> getProductsByRow() {
        return productsByRow;
    }

    public void setProductsByRow(List<List<Product>> productsByRow) {
        this.productsByRow = productsByRow;
    }

    // 测试时打印名称用
    @Override
    public String toString() {
        return "Category [name=" + name + "]";
    }
}
