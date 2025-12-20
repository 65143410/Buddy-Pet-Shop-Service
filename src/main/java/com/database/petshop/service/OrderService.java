package com.database.petshop.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.database.petshop.entity.OrderDetailEntity;
import com.database.petshop.entity.OrderEntity;
import com.database.petshop.entity.ProductEntity;
import com.database.petshop.repository.CustomerRepository;
import com.database.petshop.repository.OrderDetailRepository;
import com.database.petshop.repository.OrderRepository;
import com.database.petshop.repository.ProductRepository;
import com.database.petshop.repository.StaffRepository;
import com.database.petshop.repository.StatusRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderDetailRepository orderDetailRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private StaffRepository staffRepo;

    @Autowired
    private StatusRepository statusRepo;

    public List<OrderEntity> findAllOrders() {
        return orderRepo.findAll();
    }

    public OrderEntity findOrderById(Long id) {
        return orderRepo.findById(id).orElse(null);
    }

    @Transactional
    public OrderEntity createOrder(OrderEntity order, List<OrderDetailEntity> details) {
        if (order.getCustomer() == null || order.getCustomer().getCustomerId() == null) {
            throw new RuntimeException("ไม่สามารถสร้างออเดอร์ได้: กรุณาระบุข้อมูลลูกค้า");
        }
        customerRepo.findById(order.getCustomer().getCustomerId())
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลลูกค้า ID: " + order.getCustomer().getCustomerId()));

        if (order.getStaff() != null && order.getStaff().getStaffId() != null) {
            staffRepo.findById(order.getStaff().getStaffId())
                    .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลพนักงาน ID: " + order.getStaff().getStaffId()));
        }

        if (order.getStatus() != null && order.getStatus().getStatusId() != null) {
            statusRepo.findById(order.getStatus().getStatusId())
                    .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลสถานะ ID: " + order.getStatus().getStatusId()));
        }

        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(BigDecimal.ZERO);

        if (order.getStatus() == null) {
            com.database.petshop.entity.StatusEntity defaultStatus = new com.database.petshop.entity.StatusEntity();
            defaultStatus.setStatusId(1L);
            order.setStatus(defaultStatus);
        }

        OrderEntity savedOrder = orderRepo.save(order);

        BigDecimal calculatedTotal = BigDecimal.ZERO;

        for (OrderDetailEntity detail : details) {
            ProductEntity product = productRepo.findById(detail.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("ไม่พบสินค้า ID: " + detail.getProduct().getProductId()));

            if (product.getStock() < detail.getQuantity()) {
                throw new RuntimeException("สินค้า " + product.getProductName() + " มีสต็อกไม่พอ");
            }

            product.setStock(product.getStock() - detail.getQuantity());
            productRepo.save(product);

            detail.setOrder(savedOrder);
            detail.setProduct(product);
            detail.setUnitPrice(product.getPrice());

            orderDetailRepo.save(detail);

            BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(detail.getQuantity()));
            calculatedTotal = calculatedTotal.add(itemTotal);
        }

        savedOrder.setTotalAmount(calculatedTotal);
        return orderRepo.save(savedOrder);
    }

    @Transactional
    public OrderEntity acceptOrder(Long orderId, Long staffId) {

        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์ ID: " + orderId));

        com.database.petshop.entity.StaffEntity staff = staffRepo.findById(staffId)
                .orElseThrow(() -> new RuntimeException("ไม่พบพนักงาน ID: " + staffId));

        if (order.getStaff() != null) {
            throw new RuntimeException("ออเดอร์นี้มีพนักงานคนอื่นรับไปแล้ว");
        }

        order.setStaff(staff);

        com.database.petshop.entity.StatusEntity inProgressStatus = new com.database.petshop.entity.StatusEntity();
        inProgressStatus.setStatusId(2L);
        order.setStatus(inProgressStatus);

        return orderRepo.save(order);
    }

    public List<OrderEntity> findUnassignedOrders() {
        Long pendingStatusId = 1L;
        return orderRepo.findByStaffIsNullAndStatusStatusIdOrderByOrderDateAsc(pendingStatusId);
    }

    @Transactional
    public OrderEntity completeOrder(Long orderId) {
        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์ ID: " + orderId));

        com.database.petshop.entity.StatusEntity completedStatus = new com.database.petshop.entity.StatusEntity();
        completedStatus.setStatusId(3L);
        order.setStatus(completedStatus);

        return orderRepo.save(order);
    }

    @Transactional
    public OrderEntity cancelOrder(Long orderId) {
        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์ ID: " + orderId));

        for (OrderDetailEntity detail : order.getOrderDetails()) {
            ProductEntity product = detail.getProduct();
            product.setStock(product.getStock() + detail.getQuantity()); // บวกคืนเข้าสต็อก
            productRepo.save(product);
        }

        com.database.petshop.entity.StatusEntity cancelledStatus = new com.database.petshop.entity.StatusEntity();
        cancelledStatus.setStatusId(4L);
        order.setStatus(cancelledStatus);

        return orderRepo.save(order);
    }

    public List<OrderEntity> findOrdersByStaff(Long staffId) {
        Long inProgressStatusId = 2L;
        return orderRepo.findByStaffStaffIdAndStatusStatusId(staffId, inProgressStatusId);
    }

    public Map<String, Object> getDailySalesReport(LocalDate date) {
    BigDecimal totalSales = orderRepo.sumTotalSalesByDate(date, date);
    Long orderCount = orderRepo.countCompletedOrdersByDate(date, date);

    Map<String, Object> report = new HashMap<>();
    report.put("reportDate", date);
    report.put("totalRevenue", totalSales != null ? totalSales : BigDecimal.ZERO);
    report.put("orderCount", orderCount);
    
    return report;
}
}
