package com.cyt.smarthome.bean;

public class Maintain {
    private String maintain_id;
    private String repair_id;
    private String repair_name;
    private String maintain_staff_name;
    private String maintain_fees;
    private String maintain_time;
    private String payment_status;
    private String maintain_status;
    private String repair_content;

    public String getMaintain_id() {
        return maintain_id;
    }

    public void setMaintain_id(String maintain_id) {
        this.maintain_id = maintain_id;
    }

    public String getRepair_content() {
        return repair_content;
    }

    public void setRepair_content(String repair_content) {
        this.repair_content = repair_content;
    }

    public String getRepair_id() {
        return repair_id;
    }

    public void setRepair_id(String repair_id) {
        this.repair_id = repair_id;
    }

    public String getRepair_name() {
        return repair_name;
    }

    public void setRepair_name(String repair_name) {
        this.repair_name = repair_name;
    }

    public String getMaintain_staff_name() {
        return maintain_staff_name;
    }

    public void setMaintain_staff_name(String maintain_staff_name) {
        this.maintain_staff_name = maintain_staff_name;
    }

    public String getMaintain_fees() {
        return maintain_fees;
    }

    public void setMaintain_fees(String maintain_fees) {
        this.maintain_fees = maintain_fees;
    }

    public String getMaintain_time() {
        return maintain_time;
    }

    public void setMaintain_time(String maintain_time) {
        this.maintain_time = maintain_time;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getMaintain_status() {
        return maintain_status;
    }

    public void setMaintain_status(String maintain_status) {
        this.maintain_status = maintain_status;
    }
}
