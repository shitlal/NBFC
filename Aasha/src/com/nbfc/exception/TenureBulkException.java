package com.nbfc.exception;

public class TenureBulkException extends RuntimeException {

	private static final long serialVersionUID = -4794572499177930357L;

	private String ExceptionMessage;

	public TenureBulkException(String ExceptionMessage) {
		this.ExceptionMessage = ExceptionMessage;

	}

	public String getExceptionMessage() {
		return ExceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		ExceptionMessage = exceptionMessage;
	}

}
