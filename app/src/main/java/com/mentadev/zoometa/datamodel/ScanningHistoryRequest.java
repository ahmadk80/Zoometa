package com.mentadev.zoometa.datamodel;

import java.io.Serializable;
import java.util.Date;

public class ScanningHistoryRequest implements Serializable {
    private String From;
    private String To;

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        this.From = from;
    }

    public String isTo() {
        return To;
    }

    public void setTo(String to) {
        this.To = to;
    }
}
