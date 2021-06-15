package com.example.user.ncpaidemo;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {
    private String name;
    private int price;
    private int total_unit_price;
    private int count;
    private String img;
    private ArrayList<RecipeItem> userItems = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<RecipeItem> getUserItems() {
        return userItems;
    }

    public void setUserItems(ArrayList<RecipeItem> userItems) {
        this.userItems = userItems;
    }

    public int getTotal_unit_price() {
        return total_unit_price;
    }

    public void setTotal_unit_price(int total_unit_price) {
        this.total_unit_price = total_unit_price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Recipe() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Recipe(String name, int price, int total_unit_price, int count, ArrayList<RecipeItem> wUserItems, String img) {
        this.name = name;
        this.price = price;
        this.userItems = wUserItems;
        this.total_unit_price =total_unit_price;
        this.count=count;
        this.img = img;
    }

    public Recipe(String name, int price, int total_unit_price, int count, ArrayList<RecipeItem> wUserItems) {
        this.name = name;
        this.price = price;
        this.userItems = wUserItems;
        this.total_unit_price =total_unit_price;
        this.count=count;
    }

    public void print() {
        System.out.print("name : " + name + " | ");
        System.out.print("price : " + price + " | ");
        System.out.print("total_unit_price : " + total_unit_price + " | ");
        System.out.println("count : " + count + " | ");
        System.out.print("::: 아이템리스트 ::: ");
        if(userItems!=null) {
            for (int i = 0; i < userItems.size(); i++) {
                userItems.get(i).print();
            }
        }
    }
}
