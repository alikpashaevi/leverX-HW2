package com.example.warehouse;

import com.example.warehouse.model.Order;
import com.example.warehouse.model.Product;
import com.example.warehouse.service.Customer;
import com.example.warehouse.service.Warehouse;
import com.example.warehouse.service.WarehouseWorker;
import com.example.warehouse.util.Analytics;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Warehouse warehouse = new Warehouse();
        BlockingQueue<Order> queue = new LinkedBlockingQueue<>();

        Product product = new Product("Apple", 1.99);
        Product product2 = new Product("Banana", 2.99);
        Product product3 = new Product("Orange", 3.99);
        Product product4 = new Product("Pear", 4.99);

        warehouse.addProduct(product, 10);
        warehouse.addProduct(product2, 6);
        warehouse.addProduct(product3, 6);
        warehouse.addProduct(product4, 5);

        List<Product> products = Arrays.asList(product, product2, product3, product4);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(new WarehouseWorker(warehouse, queue));
        executorService.submit(new WarehouseWorker(warehouse, queue));

        ExecutorService executorService2 = Executors.newFixedThreadPool(3);
        executorService2.submit(new Customer("Alik", products, queue));
        executorService2.submit(new Customer("Bob", products, queue));
        executorService2.submit(new Customer("Cody", products, queue));

        Thread.sleep(2000);

        executorService.shutdownNow();
        executorService2.shutdownNow();

        warehouse.displayInventory();

        Analytics.calculateAnalytics(warehouse.getProcessedOrders());

    }
}