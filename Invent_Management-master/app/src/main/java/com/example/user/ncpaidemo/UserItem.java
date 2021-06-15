package com.example.user.ncpaidemo;

import android.text.Editable;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.user.ncpaidemo.UserItemAdapter.isStringDouble;

public class UserItem implements Serializable {

    private String name;
    private String store;
    private String lCategory;
    private String sCategory;
    private String unit;
    private int unit_price;
    private int price;
    private int count;
    private int amount;
    private int unit_amount;
    private int nDay;
    private int img;


    public String getName() {
        return name;
    }

    public String getStore() {
        return store;
    }

    public String getlCategory() {
        return lCategory;
    }

    public String getsCategory() {
        return sCategory;
    }

    public int getUnit_price() {
        return unit_price;
    }

    public int getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public String getUnit() {
        return unit;
    }

    public int getAmount() {
        return amount;
    }

    public int getUnit_amount() {
        return unit_amount;
    }

    public int getnDay() {
        return nDay;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setlCategory(String lCategory) {
        this.lCategory = lCategory;
    }

    public void setsCategory(String sCategory) {
        this.sCategory = sCategory;
    }

    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
        //this.price=unit_price*count;
    }

    public void setCount(int count){
        this.count=count;
    }

    public void setnDay(int nDay) {
        this.nDay = nDay;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setUnit_amount(int unit_amount){
        this.unit_amount = unit_amount;
    }

    public void setAmount(int amount) {this.amount = amount; }


    public UserItem() {
        this.name = "";
        this.store = "";
        this.lCategory = "";
        this.sCategory = "";
        this.unit_price = 0;
        this.price = 0;
        this.nDay = 0;
        this.count = 0;
    }

    public UserItem(UserItem userItem) {
        this.sCategory = userItem.getsCategory();
        this.lCategory = userItem.getlCategory();
    }


    public UserItem(String name, String lCategory, String sCategory, int nDay) {
        this.name = name;
        this.lCategory = lCategory;
        this.sCategory = sCategory;
        this.nDay = nDay;

    }

    public UserItem(String name, String store, String count, int unit_price, int price) {
        this.name = name;
        this.store = store;
        this.unit_price = unit_price;
        this.price = price;

        if (isStringDouble(count) == false) {
            this.count = 0;
        } else {
            this.count = Integer.parseInt(count);
        }
        ;

    }

    public UserItem(String lCategory, String sCategory, int nDay) {
        this.lCategory = lCategory;
        this.sCategory = sCategory;
        this.nDay = nDay;
    }

    public void setBase(String lCategory, String sCategory, int nDay) {
        this.lCategory = lCategory;
        this.sCategory = sCategory;
        this.nDay = nDay;
    }

    public void print() {
        System.out.print("name : " + name + " | ");
        System.out.print("Store : " + store + " | ");
        System.out.print("lCategory : " + lCategory + " | ");
        System.out.print("sCategory : " + sCategory + " | ");
        System.out.print("count : " + count + " | ");
        System.out.print("nDay : " + nDay + " | ");
        System.out.print("amount : " + amount + " ");
        System.out.print(" | unitAmount : " + unit_amount + " ");
        System.out.print(unit + " | ");
        System.out.print("price : " + price + " | ");
        System.out.println("unit_Price : " + unit_price);

    }

    public void print(String str) {
        System.out.println(str);
        print();
    }

    public void printName(ArrayList<UserItem> list) {
        System.out.print("######### Name 리스트 : ");
        for(int i=0; i<list.size(); i++){
            System.out.print(list.get(i).getName()+" | ");
        }
        System.out.println("\n");
    }

}


