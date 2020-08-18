package com.ssreader.novel.model;

/**
 * Created by  on 2018/8/16.
 */
public class PurchaseItem {

    private String label;
    private int total_price;
    private int original_price;
    private int buy_num;
    private String discount;
    public Actual_cost actual_cost;

    public static class Actual_cost {
        public int gold_cost;
        public int silver_cost;
    }

    public Original_cost original_cost;

    public static class Original_cost {
        public int gold_cost;
        public int silver_cost;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(int original_price) {
        this.original_price = original_price;
    }

    public int getBuy_num() {
        return buy_num;
    }

    public void setBuy_num(int buy_num) {
        this.buy_num = buy_num;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}

