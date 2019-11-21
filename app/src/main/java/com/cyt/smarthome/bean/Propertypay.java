package com.cyt.smarthome.bean;

public class Propertypay {

    /**
     * payment_id : 3
     * payment_name : 社区小饭桌
     * payment_staff_name : household01
     * pending_fee : 100
     * payment_status : 0
     * record_time : 2019-2-16
     * payment_time : null
     */

    private String payment_id;
    private String payment_name;
    private String payment_staff_name;
    private String pending_fee;
    private String payment_status;
    private String record_time;
    private Object payment_time;

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPayment_name() {
        return payment_name;
    }

    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }

    public String getPayment_staff_name() {
        return payment_staff_name;
    }

    public void setPayment_staff_name(String payment_staff_name) {
        this.payment_staff_name = payment_staff_name;
    }

    public String getPending_fee() {
        return pending_fee;
    }

    public void setPending_fee(String pending_fee) {
        this.pending_fee = pending_fee;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getRecord_time() {
        return record_time;
    }

    public void setRecord_time(String record_time) {
        this.record_time = record_time;
    }

    public Object getPayment_time() {
        return payment_time;
    }

    public void setPayment_time(Object payment_time) {
        this.payment_time = payment_time;
    }
}
