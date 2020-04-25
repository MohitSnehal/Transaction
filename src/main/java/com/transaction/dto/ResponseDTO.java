package com.transaction.dto;

public class ResponseDTO<T> {

    private T payload ;

    private Boolean success = true ;

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}