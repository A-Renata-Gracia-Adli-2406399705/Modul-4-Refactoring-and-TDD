package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {

    PaymentRepository paymentRepository;
    Payment payment;

    @BeforeEach
    void setUp() {

        paymentRepository = new PaymentRepository();

        List<Product> products = new ArrayList<>();

        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Test Product");
        product.setProductQuantity(1);

        products.add(product);

        Order order = new Order("1", products, 123L, "Bambang");

        payment = new Payment("1", "VOUCHER", order, new HashMap<>());
    }

    @Test
    void testSavePayment() {

        Payment result = paymentRepository.save(payment);

        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testFindByIdMultiplePayments() {

        paymentRepository.save(payment);

        Payment payment2 = new Payment("2", "COD", payment.getOrder(), new HashMap<>());
        paymentRepository.save(payment2);

        Payment result = paymentRepository.findById("2");

        assertNotNull(result);
        assertEquals("2", result.getId());
    }

    @Test
    void testFindByIdFound() {

        paymentRepository.save(payment);

        Payment result = paymentRepository.findById("1");

        assertEquals("1", result.getId());
    }

    @Test
    void testFindByIdNotFound() {

        Payment result = paymentRepository.findById("999");

        assertNull(result);
    }

    @Test
    void testFindAllPayments() {

        paymentRepository.save(payment);

        Payment payment2 = new Payment("2", "COD", payment.getOrder(), new HashMap<>());
        paymentRepository.save(payment2);

        List<Payment> payments = paymentRepository.findAll();

        assertEquals(2, payments.size());
        assertEquals("1", payments.get(0).getId());
        assertEquals("2", payments.get(1).getId());
    }
}