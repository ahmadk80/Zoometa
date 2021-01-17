package com.mentadev.zoometa.restconnector;

import java.io.Serializable;

public class ResponseEnvelop<T> implements Serializable {
    private String message;
    private boolean result;
    private T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
