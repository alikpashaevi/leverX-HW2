package com.example.warehouse.service;

import com.example.warehouse.model.Order;
import com.example.warehouse.model.Product;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.BlockingQueue;

@RequiredArgsConstructor
public class WarehouseWorker implements Runnable {
    private final Warehouse warehouse;
    private final BlockingQueue<Order> queue;

    @Override
    public void run() {
        try {
            while (true) {
                Order order = queue.take();
                Product product = order.getProduct();
                warehouse.processOrder(product, order.getQuantity());
                if (warehouse.processOrder(product, order.getQuantity())) {
                    System.out.println("Order processed for " + order.getCustomerName() +
                            ": " + order.getQuantity() + " × " + product.getName());
                } else {
                    System.out.println("Order could not be processed for " + order.getCustomerName() +
                            ": " + order.getQuantity() + " × " + product.getName());
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
