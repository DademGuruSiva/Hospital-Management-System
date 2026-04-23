package com.hospital.hospitalmanagement.model;

import jakarta.persistence.*;

@Entity
public class BillItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String itemName;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    public BillItem() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
    public Bill getBill() { return bill; }
    public void setBill(Bill bill) { this.bill = bill; }
}