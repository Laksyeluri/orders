package com.ecom.paymenthandler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.paymenthandler.model.PaymentRecord;
import com.ecom.paymenthandler.repository.PaymentRepository;

import jakarta.transaction.Transactional;

@Service
public class PaymentService {

	@Autowired
    private  PaymentRepository paymentRepository;

    @Transactional
    public void saveToDatabase(PaymentRecord record) {
        paymentRepository.save(record);
    }
}