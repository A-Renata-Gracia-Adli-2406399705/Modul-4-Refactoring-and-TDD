package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
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

class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    private Payment createPayment() {

        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Keyboard");
        product.setProductQuantity(1);

        Order order = new Order(
                "1",
                List.of(product),
                System.currentTimeMillis(),
                "Bambang"
        );

        return new Payment(
                "10",
                "voucher",
                order,
                new HashMap<>()
        );
    }

    @Test
    void testPaymentDetailForm() throws Exception {

        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment-detail-form"));
    }

    @Test
    void testPaymentDetail() throws Exception {

        Payment payment = createPayment();

        Mockito.when(paymentService.getPayment("10"))
                .thenReturn(payment);

        mockMvc.perform(get("/payment/detail/10"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment-detail"));
    }

    @Test
    void testAdminList() throws Exception {

        Mockito.when(paymentService.getAllPayments())
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment-admin-list"));
    }

    @Test
    void testAdminDetail() throws Exception {

        Payment payment = createPayment();

        Mockito.when(paymentService.getPayment("10"))
                .thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/10"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment-admin-detail"));
    }

    @Test
    void testSetStatus() throws Exception {

        Payment payment = createPayment();

        Mockito.when(paymentService.getPayment("10"))
                .thenReturn(payment);

        Mockito.when(paymentService.setStatus(payment,"SUCCESS"))
                .thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/10")
                        .param("status","SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment-admin-detail"));
    }
}