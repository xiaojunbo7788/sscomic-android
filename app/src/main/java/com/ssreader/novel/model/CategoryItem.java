package com.ssreader.novel.model;

import java.util.List;

/**
 * 分类首页每一条目
 */
public class CategoryItem {

    public List<SearchBox> search_box;
    public OptionItem list;

    public List<SearchBox> getSearch_box() {
        return search_box;
    }

    public void setSearch_box(List<SearchBox> search_box) {
        this.search_box = search_box;
    }

    public OptionItem getList() {
        return list;
    }

    public void setList(OptionItem list) {
        this.list = list;
    }


}
