package com.ssreader.novel.model;

import java.util.List;

public class InfoBookItem {

    public InfoBook book;
    public List<Comment> comment;
    public List<BaseLabelBean> label;
    public BaseAd advert;

    public BaseAd getAdvert() {
        return advert;
    }

    public void setAdvert(BaseAd advert) {
        this.advert = advert;
    }

    public InfoBook getBook() {
        return book;
    }

    public void setBook(InfoBook Book) {
        this.book = Book;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public List<BaseLabelBean> getLabel() {
        return label;
    }

    public void setLabel(List<BaseLabelBean> label) {
        this.label = label;
    }
}
