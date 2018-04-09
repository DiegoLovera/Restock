package com.lovera.diego.restock.models;

public class OrderContent {
    private String Order;
    private String Product;
    private String Quantity;
    private String Total;

    public OrderContent(){

    }

    public OrderContent(String order, String product, String quantity, String total){
        this.Order = order;
        this.Product = product;
        this.Quantity = quantity;
        this.Total = total;
    }

    public String getOrder() {
        return Order;
    }

    public void setOrder(String order) {
        Order = order;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantitiy) {
        Quantity = quantitiy;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }
}
