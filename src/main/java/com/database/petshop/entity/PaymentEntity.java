package com.database.petshop.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Payment")
public class PaymentEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Payment_ID")
    private Long paymentId;

    @Column(name = "Payment_Date", nullable = false)
    private LocalDateTime paymentDate = LocalDateTime.now();

    @Column(name = "Amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "Method")
    private String method; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Order_ID", nullable = false)
    private OrderEntity order;

    

}
