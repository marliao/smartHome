package com.cyt.smarthome.bean;

public class HouseKeepHistory {

    /**
     * housekeeping_id : 2
     * ahousekeeping_id : 2
     * ahousekeeping_name : household01
     * housekeeping_staff_name : staff04
     * housekeeping_addr : 15号楼3单元502室
     * housekeeping_content : 小时工
     * housekeeping_fees : 200
     * housekeeping_time : 2019-3-17-15:00
     * housekeeping_duration : 2
     * housekeeping_assess : null
     * payment_status : 0
     * housekeeping_status : 1
     */

    private String housekeeping_id;
    private String ahousekeeping_id;
    private String ahousekeeping_name;
    private String housekeeping_staff_name;
    private String housekeeping_addr;
    private String housekeeping_content;
    private String housekeeping_fees;
    private String housekeeping_time;
    private String housekeeping_duration;
    private Object housekeeping_assess;
    private String payment_status;
    private String housekeeping_status;

    public String getHousekeeping_id() {
        return housekeeping_id;
    }

    public void setHousekeeping_id(String housekeeping_id) {
        this.housekeeping_id = housekeeping_id;
    }

    public String getAhousekeeping_id() {
        return ahousekeeping_id;
    }

    public void setAhousekeeping_id(String ahousekeeping_id) {
        this.ahousekeeping_id = ahousekeeping_id;
    }

    public String getAhousekeeping_name() {
        return ahousekeeping_name;
    }

    public void setAhousekeeping_name(String ahousekeeping_name) {
        this.ahousekeeping_name = ahousekeeping_name;
    }

    public String getHousekeeping_staff_name() {
        return housekeeping_staff_name;
    }

    public void setHousekeeping_staff_name(String housekeeping_staff_name) {
        this.housekeeping_staff_name = housekeeping_staff_name;
    }

    public String getHousekeeping_addr() {
        return housekeeping_addr;
    }

    public void setHousekeeping_addr(String housekeeping_addr) {
        this.housekeeping_addr = housekeeping_addr;
    }

    public String getHousekeeping_content() {
        return housekeeping_content;
    }

    public void setHousekeeping_content(String housekeeping_content) {
        this.housekeeping_content = housekeeping_content;
    }

    public String getHousekeeping_fees() {
        return housekeeping_fees;
    }

    public void setHousekeeping_fees(String housekeeping_fees) {
        this.housekeeping_fees = housekeeping_fees;
    }

    public String getHousekeeping_time() {
        return housekeeping_time;
    }

    public void setHousekeeping_time(String housekeeping_time) {
        this.housekeeping_time = housekeeping_time;
    }

    public String getHousekeeping_duration() {
        return housekeeping_duration;
    }

    public void setHousekeeping_duration(String housekeeping_duration) {
        this.housekeeping_duration = housekeeping_duration;
    }

    public Object getHousekeeping_assess() {
        return housekeeping_assess;
    }

    public void setHousekeeping_assess(Object housekeeping_assess) {
        this.housekeeping_assess = housekeeping_assess;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getHousekeeping_status() {
        return housekeeping_status;
    }

    public void setHousekeeping_status(String housekeeping_status) {
        this.housekeeping_status = housekeeping_status;
    }
}
