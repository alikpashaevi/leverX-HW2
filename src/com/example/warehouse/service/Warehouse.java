package com.example.warehouse.service;

import com.example.warehouse.model.Order;
import com.example.warehouse.model.Product;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class Warehouse {
    private final ConcurrentHashMap<Product, Integer> stock = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<Order> processedOrders = new CopyOnWriteArrayList<>();

    public void addProduct(Product product, int quantity) {
        stock.put(product, quantity);
    }

    public boolean processOrder(Order order) {
        Product product = order.getProduct();
        int quantity = order.getQuantity();

        if (stock.get(product) >= quantity) {
            stock.computeIfPresent(product, (p, q) -> {
                if (q >= quantity) {
                    return q - quantity;
                }
                return q;
            });

            // let's not null-check for simplicity
            processedOrders.add(order);
            return true;

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
