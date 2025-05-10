package com.ecom.paymentexecutor.exception;

public class PaymentExecutorServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PaymentExecutorServiceException() {
		super();
	}

	public PaymentExecutorServiceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PaymentExecutorServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public PaymentExecutorServiceException(String message) {
		super(message);
	}

	public PaymentExecutorServiceException(Throwable cause) {
		super(cause);
	}
	
	

}
