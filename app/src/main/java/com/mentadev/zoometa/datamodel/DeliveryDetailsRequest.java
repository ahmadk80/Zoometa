package com.mentadev.zoometa.datamodel;

import java.io.Serializable;

public class DeliveryDetailsRequest implements Serializable {
    private String deliveryNoteId;

    public String getDeliveryNoteId() {
        return deliveryNoteId;
    }

    public void setDeliveryNoteId(String deliveryNoteId) {
        this.deliveryNoteId = deliveryNoteId;
    }
}
