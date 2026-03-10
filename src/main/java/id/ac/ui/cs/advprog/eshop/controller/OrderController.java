package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create")
    public String createOrderPage(Model model) {
        return "order-create";
    }

    @GetMapping("/history")
    public String historyForm() {
        return "order-history";
    }

    @PostMapping("/history")
    public String orderHistory(
            @RequestParam String author,
            Model model) {

        List<Order> orders = orderService.findAllByAuthor(author);

        model.addAttribute("orders", orders);
        model.addAttribute("author", author);

        return "order-history-list";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(
            @PathVariable String orderId,
            Model model) {

        Order order = orderService.findById(orderId);

        model.addAttribute("order", order);

        return "order-pay";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrder(
            @PathVariable String orderId,
            @RequestParam String method,
            @RequestParam Map<String,String> paymentData,
            Model model) {

        Order order = orderService.findById(orderId);

        Payment payment = paymentService.addPayment(order, method, paymentData);

        model.addAttribute("paymentId", payment.getId());

        return "order-pay-success";
    }
}