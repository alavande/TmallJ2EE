package org.tmall.entity;

/**
 *  订单项类
 */
public class OrderItem {

    private int number;
    // 与 product 多对一
    private Product product;
    // 与 order 多对一
    private Order order;
    // 与 user 多对一
    private User user;
    private int id;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
