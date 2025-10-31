package com.example.warehouse.service;

import com.example.warehouse.model.Order;
import com.example.warehouse.model.Product;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class Warehouse {
    private final ConcurrentHashMap<Product, Integer> stock = new ConcurrentHashMap<>();

    public void addProduct(Product product, int quantity) {
        stock.put(product, quantity);
    }

    public boolean processOrder(Order order) {
        Product product = order.getProduct();
        int quantity = order.getQuantity();

        if (stock.get(product) >= quantity) {
            Integer result = stock.computeIfPresent(product, (p, q) -> {
                if (q >= quantity) {
                    return q - quantity;
                }
                return q;
            });

            return result != null && stock.get(product) + quantity >= quantity;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("Current Inventory:");
        stock.forEach((product, quantity) ->
            System.out.println(product.getName() + ": " + quantity)
        );
    }

}
