package com.ssreader.novel.model;

import java.util.List;

public class SearchBox {

    public String label;//": "分类",
    public String field;//": "cat1",
    public List<SearchBoxLabe> list;//

    public SearchBox() {
    }

    public SearchBox(String label, String field, List<SearchBoxLabe> list) {
        this.label = label;
        this.field = field;
        this.list = list;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<SearchBoxLabe> getList() {
        return list;
    }

    public void setList(List<SearchBoxLabe> list) {
        this.list = list;
    }

    public class SearchBoxLabe {
        public String display;//": "全部",
        public String value;//": 0
        public int checked;

        public int getChecked() {
            return checked;
        }

        public void setChecked(int checked) {
            this.checked = checked;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
