package id.ac.ui.cs.advprog.eshop.functional;

import id.ac.ui.cs.advprog.eshop.controller.OrderController;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import(OrderControllerFunctionalTest.MockConfig.class)
class OrderControllerFunctionalTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    OrderService orderService;

    @Autowired
    PaymentService paymentService;

    @TestConfiguration
    static class MockConfig {

        @Bean
        OrderService orderService() {
            return Mockito.mock(OrderService.class);
        }

        @Bean
        PaymentService paymentService() {
            return Mockito.mock(PaymentService.class);
        }
    }

    private Product createProductSample() {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Keyboard");
        product.setProductQuantity(1);
        return product;
    }

    private Order createOrderSample() {

        return new Order(
                "1",
                List.of(createProductSample()),
                System.currentTimeMillis(),
                "Bambang"
        );
    }

    @Test
    void testCreateOrderPage() throws Exception {

        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-create"));
    }

    @Test
    void testHistoryPage() throws Exception {

        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-history"));
    }

    @Test
    void testOrderHistory() throws Exception {

        when(orderService.findAllByAuthor("Bambang"))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(post("/order/history")
                        .param("author","Bambang"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-history-list"));
    }

    @Test
    void testPayOrderPage() throws Exception {

        Order order = createOrderSample();

        when(orderService.findById("1")).thenReturn(order);

        mockMvc.perform(get("/order/pay/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-pay"));
    }

    @Test
    void testPayOrderSubmit() throws Exception {

        Order order = createOrderSample();

        Payment payment = new Payment(
                "10",
                "voucher",
                order,
                new HashMap<>()
        );

        when(orderService.findById("1")).thenReturn(order);

        when(paymentService.addPayment(
                Mockito.eq(order),
                Mockito.anyString(),
                Mockito.anyMap()))
                .thenReturn(payment);

        mockMvc.perform(post("/order/pay/1")
                        .param("method","voucher")
                        .param("voucherCode","ESHOP12345678"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-pay-success"));
    }
}