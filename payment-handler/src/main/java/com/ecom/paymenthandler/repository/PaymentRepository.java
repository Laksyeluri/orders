package com.ecom.paymenthandler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.paymenthandler.model.PaymentRecord;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentRecord, Long> {
}