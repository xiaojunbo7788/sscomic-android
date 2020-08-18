package com.ssreader.novel.eventbus;

public class RefreshReadHistory {

    private int productType;

    public RefreshReadHistory(int productType) {
        this.productType = productType;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }
}
