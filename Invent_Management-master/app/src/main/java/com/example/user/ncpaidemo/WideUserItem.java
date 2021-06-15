package com.example.user.ncpaidemo;

public class WideUserItem {
    private String lCategory;
    private String sCategory;
    private String unit;
    private int nowAmount;
    private int total_price;
    private int maxCount;
    private int maxDay;

    public String getlCategory() {
        return lCategory;
    }

    public void setlCategory(String lCategory) {
        this.lCategory = lCategory;
    }

    public String getsCategory() {
        return sCategory;
    }

    public void setsCategory(String sCategory) {
        this.sCategory = sCategory;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getNowAmount() {
        return nowAmount;
    }

    public void setNowAmount(int nowAmount) {
        this.nowAmount = nowAmount;
    }


    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getMaxDay() {
        return maxDay;
    }

    public void setMaxDay(int maxDay) {
        this.maxDay = maxDay;
    }


    public WideUserItem(String sCategory, String unit, int nowAmount, int maxCount, int total_price) {
        this.sCategory = sCategory;
        this.unit = unit;
        this.nowAmount = nowAmount;
        this.total_price = total_price;
        this.maxCount = maxCount;
    }

    public WideUserItem() {

    }

    public void print() {
        System.out.print("\n lCategory : " + lCategory + " | ");
        System.out.print("sCategory : " + sCategory + " | ");
        System.out.print("Maxcount : " + maxCount + " | ");
        System.out.print("MAxDay : " + maxDay + " | ");
        System.out.print("NowAmount : " + nowAmount + " ");
        System.out.print(" | total_price : " + total_price + " ");
        System.out.println(unit + " | ");

    }
}
