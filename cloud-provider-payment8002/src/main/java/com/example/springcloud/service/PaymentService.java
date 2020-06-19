package com.example.springcloud.service;

import com.example.springcloud.entity.Payment;

public interface PaymentService {

    int create(Payment payment);

    Payment getPaymentById(int id);

}
