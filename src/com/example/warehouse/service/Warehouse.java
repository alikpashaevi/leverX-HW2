package com.example.warehouse.service;

import com.example.warehouse.model.Product;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class Warehouse {
    private final ConcurrentHashMap<Product, Integer> stock = new ConcurrentHashMap<>();

    public void addProduct(Product product, int quantity) {
        stock.put(product, quantity);
    }

    public boolean processOrder(Product product, int quantity) {
        return stock.computeIfPresent(product, (p, q) -> {
            if (q >= quantity) {
                System.out.println(Thread.currentThread().getName() +
                        " processed " + quantity + " × " + product.getName());
                return q - quantity;
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " not enough stock for " + product.getName());
                return q;
            }
        }) != null;
    }

    public void displayInventory() {
        System.out.println("Current Inventory:");
        stock.forEach((product, quantity) ->
            System.out.println(product.getName() + ": " + quantity)
        );
    }

}
