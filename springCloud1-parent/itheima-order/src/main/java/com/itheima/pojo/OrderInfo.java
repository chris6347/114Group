package com.itheima.pojo;

public class OrderInfo {

    private String username;
    private String orderId;
    private String itemId;
    private String itemName;
    private Long price;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public OrderInfo() {
    }

    public OrderInfo(String username, String orderId, String itemId, String itemName, Long price) {
        this.username = username;
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "username='" + username + '\'' +
                ", orderId='" + orderId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", price=" + price +
                '}';
    }
}
