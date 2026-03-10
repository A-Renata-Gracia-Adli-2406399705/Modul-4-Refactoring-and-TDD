package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    private Order createOrder() {

        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Keyboard");
        product.setProductQuantity(1);

        return new Order(
                "1",
                List.of(product),
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
    void testHistoryForm() throws Exception {

        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-history"));
    }

    @Test
    void testOrderHistory() throws Exception {

        Mockito.when(orderService.findAllByAuthor("Bambang"))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(post("/order/history")
                        .param("author","Bambang"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-history-list"));
    }

    @Test
    void testPayOrderPage() throws Exception {

        Order order = createOrder();

        Mockito.when(orderService.findById("1"))
                .thenReturn(order);

        mockMvc.perform(get("/order/pay/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-pay"));
    }

    @Test
    void testPayOrder() throws Exception {

        Order order = createOrder();

        Payment payment = new Payment(
                "10",
                "voucher",
                order,
                new HashMap<>()
        );

        Mockito.when(orderService.findById("1"))
                .thenReturn(order);

        Mockito.when(paymentService.addPayment(
                        Mockito.eq(order),
                        Mockito.eq("voucher"),
                        Mockito.anyMap()))
                .thenReturn(payment);

        mockMvc.perform(post("/order/pay/1")
                        .param("method","voucher"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-pay-success"));
    }
}