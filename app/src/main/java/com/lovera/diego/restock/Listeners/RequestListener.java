package com.lovera.diego.restock.Listeners;

import com.lovera.diego.restock.models.Order;

import java.util.EventListener;

public interface RequestListener extends EventListener{
    void OnRequestStatusChanged(Order order);
}
