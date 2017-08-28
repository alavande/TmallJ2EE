package org.tmall.entity;

/**
 * 商品属性类
 * 用于对应属性名称：如重量，颜色
 */
public class Property {

    private String name;
    // 与 Category 为多对一的关系
    private Category category;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
}
