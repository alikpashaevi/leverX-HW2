package com.example.warehouse.util;

import com.example.warehouse.model.Order;
import com.example.warehouse.model.Product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Analytics {
    public static void calculateAnalytics(List<Order> orders) {
        long totalOrders = orders.parallelStream()
                .count();
        System.out.println("Total Orders Processed: " + totalOrders);

        double totalProfit = orders.parallelStream().
                mapToDouble(order -> order.getProduct().getPrice() * order.getQuantity())
                .sum();
        System.out.println("Total Profit: $" + totalProfit);

        Map<Product, Integer> productSales = orders.parallelStream()
                .collect(Collectors.groupingByConcurrent(
                        Order::getProduct,
                        Collectors.summingInt(Order::getQuantity)
                ));

        System.out.println("Top 3 best selling products:");
        productSales.entrySet().parallelStream()
                .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed())
                .limit(3)
                .forEach(entry ->
                        System.out.println("  " + entry.getKey().getName() +
                                ": " + entry.getValue() + " units sold")
                );
    }
}
