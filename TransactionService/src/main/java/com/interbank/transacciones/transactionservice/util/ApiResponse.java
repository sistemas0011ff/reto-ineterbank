package com.interbank.transacciones.transactionservice.util;

public class ApiResponse<T> {
    private int status;
    private boolean success;
    private String message;
    private T data;
    
    public ApiResponse() {
    }
    
    public ApiResponse(boolean success, String message, int status) {
        this.success = success;
        this.message = message;
        this.data = null;
        this.status = status; // Asegúrate de asignar el status aquí
    }

    public ApiResponse(boolean success, String message, T data, int status) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.status = status; // Asegúrate de asignar el status aquí
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}