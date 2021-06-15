package com.example.user.ncpaidemo;

import java.io.Serializable;

public class RecipeItem implements Serializable {
    private String sCategory;
    private int count;
    private int amount;
    private int price;
    private String unit;


    public RecipeItem(String sCategory, int count, int amount, int price) {
        this.sCategory = sCategory;
        this.count = count;
        this.amount = amount;
        this.price = price;
    }

    public RecipeItem(String sCategory) {
        this.sCategory = sCategory;
    }

    public RecipeItem() {
    }

    public String getsCategory() {
        return sCategory;
    }

    public void setsCategory(String sCategory) {
        this.sCategory = sCategory;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public RecipeItem(String sCategory, String unit) {
        this.sCategory = sCategory;
        this.unit = unit;
    }

    public void print() {
        System.out.print("\n Recipe Item 클래스 리스트 \n sCategory : " + sCategory + " | ");
        System.out.print("count : " + count + " | ");
        System.out.print("amount : " + amount + " | ");
        System.out.println("price] : " + price + " | ");


    }
}
