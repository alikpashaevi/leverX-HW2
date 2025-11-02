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
            System.out.println("products " + products);
            for (Product product : products) {
                System.out.println("product " + product);
                // randomly choose quantity
                int quantity = 1 + (int)(Math.random() * 3);
                Order order = new Order(name, product,quantity);
                queue.put(order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
