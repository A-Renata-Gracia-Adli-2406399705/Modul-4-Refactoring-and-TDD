package id.ac.ui.cs.advprog.eshop.functional;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
@Import(PaymentControllerFunctionalTest.MockConfig.class)
class PaymentControllerFunctionalTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PaymentService paymentService;

    @TestConfiguration
    static class MockConfig {

        @Bean
        PaymentService paymentService() {
            return Mockito.mock(PaymentService.class);
        }
    }

    private Payment createPaymentSample() {

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

        Payment payment = createPaymentSample();

        when(paymentService.getPayment("10")).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/10"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment-detail"));
    }

    @Test
    void testAdminPaymentList() throws Exception {

        when(paymentService.getAllPayments())
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment-admin-list"));
    }

    @Test
    void testAdminPaymentDetail() throws Exception {

        Payment payment = createPaymentSample();

        when(paymentService.getPayment("10")).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/10"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment-admin-detail"));
    }

    @Test
    void testSetStatus() throws Exception {

        Payment payment = createPaymentSample();

        when(paymentService.getPayment("10")).thenReturn(payment);
        when(paymentService.setStatus(payment,"SUCCESS")).thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/10")
                        .param("status","SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment-admin-detail"));
    }
}