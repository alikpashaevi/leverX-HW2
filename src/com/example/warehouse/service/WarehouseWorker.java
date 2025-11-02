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
                boolean orderProcessing = warehouse.processOrder(order);
                if (orderProcessing) {
                    System.out.println("Order processed for " + order.getCustomerName() +
                            ": " + order.getQuantity() + " × " + order.getProduct().getName());
                } else {
                    System.out.println("Order could not be processed for " + order.getCustomerName() +
                            ": " + order.getQuantity() + " × " + order.getProduct().getName());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
