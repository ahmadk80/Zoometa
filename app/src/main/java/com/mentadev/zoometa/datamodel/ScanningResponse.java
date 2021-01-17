package com.mentadev.zoometa.datamodel;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class ScanningResponse implements Serializable {
    private String message;
    private boolean result;
    private Object data;

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
}
