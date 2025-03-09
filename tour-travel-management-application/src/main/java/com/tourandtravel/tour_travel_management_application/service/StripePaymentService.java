package com.tourandtravel.tour_travel_management_application.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.tourandtravel.tour_travel_management_application.model.Payment;
import com.tourandtravel.tour_travel_management_application.repositories.PaymentRepository;

@Service
public class StripePaymentService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment processPayment(String tokenId, BigDecimal amount, String currency) {
        // Set Stripe API Key
        Stripe.apiKey = stripeApiKey;

        try {
            // Create a charge
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", convertToCents(amount));
            chargeParams.put("currency", currency.toLowerCase());
            chargeParams.put("source", tokenId);

            Charge charge = Charge.create(chargeParams);

            // Create Payment entity
            Payment payment = new Payment();
            payment.setAmount(amount);
            payment.setTransactionId(charge.getId());
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
            payment.setPaymentMethod(Payment.PaymentMethod.STRIPE);

            return paymentRepository.save(payment);

        } catch (Exception e) {
            // Handle payment failure
            Payment failedPayment = new Payment();
            failedPayment.setAmount(amount);
            failedPayment.setStatus(Payment.PaymentStatus.FAILED);
            failedPayment.setPaymentMethod(Payment.PaymentMethod.STRIPE);

            return paymentRepository.save(failedPayment);
        }
    }

    private Long convertToCents(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).longValue();
    }

    public boolean refundPayment(String transactionId) {
        Stripe.apiKey = stripeApiKey;

        try {
            Charge charge = Charge.retrieve(transactionId);
            charge.getAmountRefunded(); //check with refund() -> getAmountRefunded() method
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
