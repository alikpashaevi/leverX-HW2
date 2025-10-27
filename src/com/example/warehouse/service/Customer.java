package com.example.warehouse.service;

import com.example.warehouse.model.Order;
import com.example.warehouse.model.Product;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.BlockingQueue;

@RequiredArgsConstructor
public class Customer implements Runnable{
    private final String name;
    private final List<Product> products;
    private final BlockingQueue<Order> queue;

    @Override
    public void run() {
        try {
            Product product = products.getFirst();
            int quantity = 1;
            Order order = new Order(name, product,quantity);
            queue.put(order);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
