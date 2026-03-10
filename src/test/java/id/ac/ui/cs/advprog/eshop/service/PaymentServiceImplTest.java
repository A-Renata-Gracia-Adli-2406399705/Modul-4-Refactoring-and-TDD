package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    Order order;

    @BeforeEach
    void setUp() {

        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Test Product");
        product.setProductQuantity(1);

        List<Product> products = new ArrayList<>();
        products.add(product);

        order = new Order("1", products, 123L, "Bambang");
    }

    @Test
    void testSetStatusSuccess() {

        Payment payment = new Payment("1","COD",order,new HashMap<>());

        paymentService.setStatus(payment,"SUCCESS");

        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
    }

    @Test
    void testSetStatusRejected() {

        Payment payment = new Payment("1","COD",order,new HashMap<>());

        paymentService.setStatus(payment,"REJECTED");

        assertEquals("REJECTED", payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
    }

    @Test
    void testGetPayment() {

        Payment payment = new Payment("1","COD",order,new HashMap<>());

        when(paymentRepository.findById("1")).thenReturn(payment);

        Payment result = paymentService.getPayment("1");

        assertEquals(payment, result);
    }

    @Test
    void testGetAllPayments() {

        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment("1","COD",order,new HashMap<>()));

        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(1, result.size());
    }

    @Test
    void testAddPaymentVoucherSuccess() {

        Map<String,String> data = new HashMap<>();
        data.put("voucherCode","ESHOP1234ABC5678");

        Payment payment = paymentService.addPayment(order,"VOUCHER",data);

        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
    }

    @Test
    void testAddPaymentVoucherRejected() {

        Map<String,String> data = new HashMap<>();
        data.put("voucherCode","INVALID");

        Payment payment = paymentService.addPayment(order,"VOUCHER",data);

        assertEquals("REJECTED", payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
    }

    @Test
    void testAddPaymentCODSuccess() {

        Map<String,String> data = new HashMap<>();
        data.put("address","Depok");
        data.put("deliveryFee","10000");

        Payment payment = paymentService.addPayment(order,"COD",data);

        assertNotEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testAddPaymentCODRejectedIfAddressEmpty() {

        Map<String,String> data = new HashMap<>();
        data.put("address","");
        data.put("deliveryFee","10000");

        Payment payment = paymentService.addPayment(order,"COD",data);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testVoucherNull() {

        Map<String,String> data = new HashMap<>();
        data.put("voucherCode", null);

        Payment payment = paymentService.addPayment(order,"VOUCHER",data);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testVoucherLengthInvalid() {

        Map<String,String> data = new HashMap<>();
        data.put("voucherCode","ESHOP123");

        Payment payment = paymentService.addPayment(order,"VOUCHER",data);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testVoucherWrongPrefix() {

        Map<String,String> data = new HashMap<>();
        data.put("voucherCode","ABCDEF1234567890");

        Payment payment = paymentService.addPayment(order,"VOUCHER",data);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testVoucherDigitLessThanEight() {

        Map<String,String> data = new HashMap<>();
        data.put("voucherCode","ESHOPABCDEFGH123");

        Payment payment = paymentService.addPayment(order,"VOUCHER",data);

        assertEquals("REJECTED", payment.getStatus());
    }

}