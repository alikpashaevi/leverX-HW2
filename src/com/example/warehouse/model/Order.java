package com.example.warehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Order {
    private String customerName;
    private Product product;
    private int quantity;
}
