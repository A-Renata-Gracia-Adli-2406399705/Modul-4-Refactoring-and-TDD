package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;
    private Order order;

    public Payment(String id, String method, Order order, Map<String,String> paymentData) {
        this.id = id;
        this.method = method;
        this.order = order;
        this.paymentData = paymentData;
    }

    public void setStatus(String status) {
        if (!status.equals("SUCCESS") &&
                !status.equals("REJECTED") &&
                !status.equals("PENDING")) {

            throw new IllegalArgumentException("Invalid payment status");
        }

        this.status = status;
    }
}