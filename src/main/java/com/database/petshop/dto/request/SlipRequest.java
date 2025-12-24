package com.database.petshop.dto.request;

import java.math.BigDecimal;

public class SlipRequest {
    private Long orderId;
    private String file; 
    private BigDecimal amount;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public SlipRequest(Long orderId, String file, BigDecimal amount) {
        this.orderId = orderId;
        this.file = file;
        this.amount = amount;
    }


}
