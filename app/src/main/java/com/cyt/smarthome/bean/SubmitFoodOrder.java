package com.cyt.smarthome.bean;

import cn.bmob.v3.BmobObject;

public class SubmitFoodOrder extends BmobObject {
    private String food_name;
    private int food_money;
    private int food_account;
    private String currentUser;

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public int getFood_money() {
        return food_money;
    }

    public void setFood_money(int food_money) {
        this.food_money = food_money;
    }

    public int getFood_account() {
        return food_account;
    }

    public void setFood_account(int food_account) {
        this.food_account = food_account;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
}
