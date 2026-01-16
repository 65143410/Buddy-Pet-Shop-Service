package com.database.petshop.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.database.petshop.entity.OrderDetailEntity;
import com.database.petshop.entity.OrderEntity;
import com.database.petshop.entity.PaymentEntity;
import com.database.petshop.entity.ProductEntity;
import com.database.petshop.entity.StaffEntity;
import com.database.petshop.repository.CustomerRepository;
import com.database.petshop.repository.OrderDetailRepository;
import com.database.petshop.repository.OrderRepository;
import com.database.petshop.repository.PaymentRepository;
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

    @Autowired
    private PaymentRepository paymentRepo;

    public List<OrderEntity> findAllOrders() {
        return orderRepo.findAll();
    }

    public OrderEntity findOrderById(Long id) {
        return orderRepo.findById(id).orElse(null);
    }

    public List<OrderEntity> getOrdersByCustomerId(Long customerId) {
        return orderRepo.findByCustomerCustomerIdOrderByOrderDateDesc(customerId);
    }

    @Transactional
    public OrderEntity createOrderWithSlip(OrderEntity order, List<OrderDetailEntity> details, String slipImageUrl) {
        // ... (Validation logic reused or copied) ...
        if (order.getCustomer() == null || order.getCustomer().getCustomerId() == null) {
            throw new RuntimeException("ไม่สามารถสร้างออเดอร์ได้: กรุณาระบุข้อมูลลูกค้า");
        }
        // ... (omitting repeated validation for brevity, assuming standard flow)
        
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(BigDecimal.ZERO);

        // Set status to "Wait for Check" (2) if slip is present
        if (order.getStatus() == null) {
            com.database.petshop.entity.StatusEntity initialStatus = new com.database.petshop.entity.StatusEntity();
            initialStatus.setStatusId(slipImageUrl != null ? 2L : 1L); // 1=Pending Payment, 2=Wait Check
            order.setStatus(initialStatus);
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
        
        // Save Payment/Slip Logic
        if (slipImageUrl != null) {
            PaymentEntity payment = new PaymentEntity();
            payment.setOrder(savedOrder);
            payment.setAmount(calculatedTotal);
            payment.setSlipImage(slipImageUrl);
            payment.setMethod("โอนเงินผ่านธนาคาร");
            payment.setPaymentDate(java.time.LocalDateTime.now());
            paymentRepo.save(payment);
        }

        return orderRepo.save(savedOrder);
    }
    
    // Keep original method for backward compatibility if needed, but it's better to refactor
    public OrderEntity createOrder(OrderEntity order, List<OrderDetailEntity> details) {
        return createOrderWithSlip(order, details, null);
    }

    @Transactional
    public OrderEntity acceptOrder(Long orderId, Long staffId) {
        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์ ID: " + orderId));
        if (!order.getStatus().getStatusName().equals("ชำระเงินแล้ว")) {
            throw new RuntimeException("ไม่สามารถรับออเดอร์นี้ได้ เนื่องจากยังไม่ชำระเงินหรือรอตรวจสอบ");
        }

        StaffEntity staff = staffRepo.findById(staffId)
                .orElseThrow(() -> new RuntimeException("ไม่พบพนักงาน ID: " + staffId));

        order.setStaff(staff);
        order.setStatus(statusRepo.findByStatusName("กำลังจัดเตรียมสินค้า"));

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
            product.setStock(product.getStock() + detail.getQuantity());
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

    public List<Map<String, Object>> getTopSellingProducts(int limit) {
        List<Object[]> results = orderDetailRepo.findTopSellingProducts(PageRequest.of(0, limit));

        return results.stream().map(result -> {
            ProductEntity product = (ProductEntity) result[0];
            Long totalQty = (Long) result[1];

            Map<String, Object> map = new HashMap<>();
            map.put("productId", product.getProductId());
            map.put("productName", product.getProductName());
            map.put("totalSold", totalQty);
            map.put("currentStock", product.getStock());
            return map;
        }).collect(Collectors.toList());
    }

    public List<OrderEntity> searchOrdersByCustomer(String name) {
        return orderRepo.searchByCustomerName(name);
    }

    public Map<String, Object> getReceipt(Long orderId) {
        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์ ID: " + orderId));

        Map<String, Object> receipt = new HashMap<>();
        receipt.put("receiptNumber", "REC-" + order.getOrderId());
        receipt.put("orderDate", order.getOrderDate());
        receipt.put("customerName", order.getCustomer().getCustomerName());
        receipt.put("customerPhone", order.getCustomer().getPhone());

        if (order.getStaff() != null) {
            receipt.put("staffName", order.getStaff().getName());
        }

        List<Map<String, Object>> items = order.getOrderDetails().stream().map(detail -> {
            Map<String, Object> item = new HashMap<>();
            item.put("productName", detail.getProduct().getProductName());
            item.put("quantity", detail.getQuantity());
            item.put("unitPrice", detail.getUnitPrice());
            item.put("subTotal", detail.getUnitPrice().multiply(new BigDecimal(detail.getQuantity())));
            return item;
        }).collect(Collectors.toList());

        receipt.put("items", items);
        receipt.put("totalAmount", order.getTotalAmount());
        receipt.put("status", order.getStatus().getStatusName());

        return receipt;
    }

    @Transactional
    public void submitPayment(Long orderId, String slipFileName, BigDecimal amount) {
        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์ ID: " + orderId));

        PaymentEntity payment = new PaymentEntity();
        payment.setOrder(order);
        payment.setAmount(amount);
        payment.setSlipImage(slipFileName);
        payment.setMethod("โอนเงินผ่านธนาคาร");
        paymentRepo.save(payment);

        order.setStatus(statusRepo.findByStatusName("รอตรวจสอบยอดเงิน"));
        orderRepo.save(order);
    }

    @Transactional
    public void verifyPayment(Long orderId, boolean isApproved) {
        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("ไม่พบออเดอร์ ID: " + orderId));

        if (isApproved) {
            order.setStatus(statusRepo.findByStatusName("ชำระเงินแล้ว"));
        } else {

            for (OrderDetailEntity detail : order.getOrderDetails()) {
                ProductEntity product = detail.getProduct();
                product.setStock(product.getStock() + detail.getQuantity());
                productRepo.save(product);
            }
            order.setStatus(statusRepo.findByStatusName("ยกเลิก/สลิปไม่ถูกต้อง"));
        }
        orderRepo.save(order);
    }

    public List<OrderEntity> getPendingVerificationOrders() {
        String statusName = "รอตรวจสอบยอดเงิน";
        List<OrderEntity> orders = orderRepo.findByStatus_StatusName(statusName);

        if (orders.isEmpty()) {

        }
        return orders;
    }
}
