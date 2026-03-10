package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment(UUID.randomUUID().toString(), method, order, paymentData);

        if(method.equalsIgnoreCase("voucher")) {
            String code = paymentData.get("voucherCode");

            if(isValidVoucher(code)) {
                payment.setStatus("SUCCESS");
                order.setStatus("SUCCESS");
            } else {
                payment.setStatus("REJECTED");
                order.setStatus("FAILED");
            }
        }

        if(method.equalsIgnoreCase("cod")) {

            String address = paymentData.get("address");
            String deliveryFee = paymentData.get("deliveryFee");

            if(address == null || address.isEmpty()
                    || deliveryFee == null || deliveryFee.isEmpty()) {

                payment.setStatus("REJECTED");
                order.setStatus("FAILED");
            }
        }

        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);

        if(status.equals("SUCCESS")) {
            payment.getOrder().setStatus("SUCCESS");
        }

        if(status.equals("REJECTED")) {
            payment.getOrder().setStatus("FAILED");
        }

        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    private boolean isValidVoucher(String code) {

        if(code == null) return false;

        if(code.length() != 16) return false;

        if(!code.startsWith("ESHOP")) return false;

        int digitCount = 0;

        for(char c : code.toCharArray()) {
            if(Character.isDigit(c)) digitCount++;
        }

        return digitCount >= 8;
    }
}