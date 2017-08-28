package org.tmall.entity;

/**
 * 属性值类
 * 用于对应具体属性值：如红色，100cm
 * 需关联相对应商品与属性
 */
public class PropertyValue {

    private String value;
    // 与 product 多对一
    private Product product;
    // 与 property 多对一
    private Property property;
    private int id;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
