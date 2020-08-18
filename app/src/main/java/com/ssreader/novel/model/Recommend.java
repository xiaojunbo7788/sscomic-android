package com.ssreader.novel.model;

import java.util.List;

public class Recommend {

    public List<RecommendProduc> book;
    public List<RecommendProduc> comic;
    public List<RecommendProduc> audio;

    public class RecommendProduc {
        public long comic_id;//": 536,
        public long book_id;//": 536,
        public long audio_id;
        public List<String> author;
        public String name;//": "带着萌宝向前冲",
        public String cover;//": "http://ssreader.oss-cn-beijing.aliyuncs.com/cover/536/2cb9e3fc90ccac0eabe74098730ac904.jpeg?x-oss-process=image%2Fresize%2Cw_300%2Ch_400%2Cm_lfit",
        public String description;//": "五年前，她稀里糊涂地走错了房间爬到了别人的床上，连男人的样貌都没有记住却留了种……五年后，处处和她作对的林安冲出来说，要抢了她孩子的父亲，笑话，她连孩子他爹都不知道是谁....",
        public int total_chapter;//": 93
        public boolean isChoose;
    }
}
