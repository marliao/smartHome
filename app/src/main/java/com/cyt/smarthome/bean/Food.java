package com.cyt.smarthome.bean;

public class Food {
    private String week_foood_name;
    private String week_food_type;
    private String food_provide_time;
    private int food_money;
    private int food_account;

    public int getFood_account() {
        return food_account;
    }

    public void setFood_account(int food_account) {
        this.food_account = food_account;
    }

    public int getFood_money() {
        return food_money;
    }

    public void setFood_money(int food_money) {
        this.food_money = food_money;
    }

    public String getWeek_foood_name() {
        return week_foood_name;
    }

    public void setWeek_foood_name(String week_foood_name) {
        this.week_foood_name = week_foood_name;
    }

    public String getWeek_food_type() {
        return week_food_type;
    }

    public void setWeek_food_type(String week_food_type) {
        this.week_food_type = week_food_type;
    }

    public String getFood_provide_time() {
        return food_provide_time;
    }

    public void setFood_provide_time(String food_provide_time) {
        this.food_provide_time = food_provide_time;
    }
}
