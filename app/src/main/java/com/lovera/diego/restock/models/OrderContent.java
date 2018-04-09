package com.lovera.diego.restock.models;

import com.google.firebase.database.PropertyName;

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
    @PropertyName("Order")
    public String getOrder() {
        return Order;
    }
    @PropertyName("Order")
    public void setOrder(String Order) {
        this.Order = Order;
    }
    @PropertyName("Product")
    public String getProduct() {
        return Product;
    }
    @PropertyName("Product")
    public void setProduct(String Product) {
        this.Product = Product;
    }
    @PropertyName("Quantity")
    public String getQuantity() {
        return Quantity;
    }
    @PropertyName("Quantity")
    public void setQuantity(String Quantity) {
        this.Quantity = Quantity;
    }
    @PropertyName("Total")
    public String getTotal() {
        return Total;
    }
    @PropertyName("Total")
    public void setTotal(String Total) {
        this.Total = Total;
    }
}
