package org.tmall.entity;

/**
 * 商品图片类
 */
public class ProductImage {

    private String type;
    // 与 product 多对一
    private Product product;
    private int id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
