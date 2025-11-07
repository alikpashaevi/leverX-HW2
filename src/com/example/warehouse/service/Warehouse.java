package com.example.warehouse.service;

import com.example.warehouse.model.Order;
import com.example.warehouse.model.Product;
import com.example.warehouse.model.Reservation;
import lombok.Getter;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class Warehouse {
    private final ConcurrentHashMap<Product, Integer> stock = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<Order> processedOrders = new CopyOnWriteArrayList<>();

    private final ConcurrentHashMap<String, Reservation> reservations = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Product, Integer> reservedQuantities = new ConcurrentHashMap<>();

    public void addProduct(Product product, int quantity) {
        stock.put(product, quantity);
    }

    public String reserveProduct(String customerName, Product product, int quantity) {
        Integer result = stock.computeIfPresent(product, (p, q) -> {
            int reserved = reservedQuantities.getOrDefault(product, 0);
            int available = q - reserved;

            if (available >= quantity) {
                reservedQuantities.merge(product, quantity, Integer::sum);
                return q;
            }
            return null;
        });

        if (result != null) {
            int reserved = reservedQuantities.get(product);
            int q = stock.get(product);

            if (reserved >= quantity && (q - reserved + quantity) >= quantity) {
                String reservationId = UUID.randomUUID().toString().substring(0, 6);
                Reservation reservation = new Reservation(
                        reservationId,
                        customerName,
                        product,
                        quantity
                );
                reservations.put(reservationId, reservation);

                System.out.println("RESERVED [" + reservationId + "] " +
                        customerName + ": " + quantity + " × " + product.getName());
                return reservationId;
            }
        }
        System.out.println("RESERVATION FAILED for " + customerName +
                ": " + quantity + " × " + product.getName() + " (not enough available)");
        return null;
    }

    public void cancelReservation(String reservationId) {
        Reservation reservation = reservations.remove(reservationId);

        if (reservation != null) {
            Product product = reservation.getProduct();
            int quantity = reservation.getQuantity();

            reservedQuantities.computeIfPresent(product, (p, reserved) ->
                    Math.max(0, reserved - quantity)
            );

            System.out.println("CANCELLED reservation [" + reservationId + "] " +
                    reservation.getCustomerName() + ": " +
                    quantity + " × " + product.getName());
            return;
        }

        System.out.println("CANCELLATION FAILED: Reservation [" + reservationId + "] not found");
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

    public void displayReservations() {
        System.out.println("Active Reservations:");
        if (reservations.isEmpty()) {
            System.out.println("No active reservations");
        } else {
            reservations.values().forEach(r ->
                    System.out.println("[" + r.getReservationId() + "] " +
                            r.getCustomerName() + ": " +
                            r.getQuantity() + " × " + r.getProduct().getName())
            );
        }
    }

}
