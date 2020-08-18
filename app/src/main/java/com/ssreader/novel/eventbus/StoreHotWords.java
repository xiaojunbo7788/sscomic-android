package com.ssreader.novel.eventbus;

import java.util.List;

public class StoreHotWords {

    public int productType;
    public List<String> HotWord;
    public int sex;

    public StoreHotWords(int productType, List<String> hotWord, int sex) {
        this.productType = productType;
        this.HotWord = hotWord;
        this.sex = sex;
    }
}
