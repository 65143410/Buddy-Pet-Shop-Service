package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "Order_Detail")
@IdClass(OrderDetailId.class)
public class OrderDetailEntity implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Order_ID")
    private OrderEntity order;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Product_ID")
    private ProductEntity product;

    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @Column(name = "Unit_Price", nullable = false) 
    private BigDecimal unitPrice;

    public OrderEntity getOrder() {
        return order;
    }
    
    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public ProductEntity getProduct() {
        return product;
    }
    
    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}

