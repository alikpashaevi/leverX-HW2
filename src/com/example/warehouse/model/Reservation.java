package com.example.warehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public class Reservation {
    private String reservationId;
    private String customerName;
    private Product product;
    private int quantity;
}
