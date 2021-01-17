package com.mentadev.zoometa.datamodel;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeliveryNoteScanning implements Serializable {
    private String deliveryNoteId;
    private String name;
    private Date scanningDate;

    private String deliveryNotePortName;
    private String deliveryNoteStatus;
    private Date deliveryNoteDate;

     public String getDeliveryNoteId() {
        return deliveryNoteId;
    }

    public void setDeliveryNoteId(String deliveryNoteId) {
        this.deliveryNoteId = deliveryNoteId;
    }

    public Date getScanningDate() {
        return scanningDate;
    }
    public String getScanningDateDisplay() {
         if(getScanningDate() == null){
             return "";
         }
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(getScanningDate());
    }

    public void setScanningDate(Date scanningDate) {
        this.scanningDate = scanningDate;
    }

    public Date getDeliveryNoteDate() {
        return deliveryNoteDate;
    }

    public void setDeliveryNoteDate(Date deliveryNoteDate) {
        this.deliveryNoteDate = deliveryNoteDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeliveryNotePortName() {
        return deliveryNotePortName;
    }

    public void setDeliveryNotePortName(String deliveryNotePortName) {
        this.deliveryNotePortName = deliveryNotePortName;
    }

    public String getDeliveryNoteStatus() {
        return deliveryNoteStatus;
    }

    public void setDeliveryNoteStatus(String deliveryNoteStatus) {
        this.deliveryNoteStatus = deliveryNoteStatus;
    }
}
