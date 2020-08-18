package com.ssreader.novel.model;

import java.util.List;

public class InfoBook {

    public String views;//": 0,
    public String display_label;//": "连载中 | 网游竞技 | 141万字",
    public String total_words;//": "141万字",
    public String words_price;//": 10,
    public String chapter_pric;//e": 0,
    public int total_comment;//": "6",
    public String hot_num;
    public String total_favors;
    public int total_chapters;//": 542,
    public String last_chapter_time;//": "更新于4个月前",
    public String last_chapter;//": "第542 鬼门",
    public long book_id, audio_id;//": 10086, //漫画id
    public String name;//"": "乔乔的奇妙冒险", //漫画名称
    public String cover;//"": "http://i0.hdslb.com/bfs/manga-static/4761ca39b699bb2738c326ce812c6c56ee3b1458.jpg", //竖封面
    public String author;//"": "黎明C", //作者
    public String description;//"": "美貌千金与帅气王爷", //描述
    public String is_finished;//"": 1, //是否完结 1已完结 0连载中
    public String flag;//"": "更新至32话", //角标
    public List<BaseTag> tag;
    public String sinici;
    public String original;
    public String created_at;
    public List<String> tags;
}
