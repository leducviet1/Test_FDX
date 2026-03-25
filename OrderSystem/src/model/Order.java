package model;

import java.math.BigDecimal;

public class Order {
    private String orderId;
    private String orderName;
    private String customerName;
    private BigDecimal price;
    private String address;
    public Order(String orderId,String orderName, String customerName, BigDecimal price, String address) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.customerName = customerName;
        this.price = price;
        this.address = address;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String toLine() {
        return orderId + "|" + orderName + "|" + customerName + "|" + address + "|" + price;
    }
}
