package com.ssreader.novel.model;


import java.util.List;

public class InfoAudioItem {

    public List<BaseBookComic> list;
    public Relation relation;
    public BaseAd advert;

    public BaseAd getAdvert() {
        return advert;
    }

    public void setAdvert(BaseAd advert) {
        this.advert = advert;
    }

    public List<BaseBookComic> getList() {
        return list;
    }

    public void setList(List<BaseBookComic> list) {
        this.list = list;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public class Relation{

        private long book_id, audio_id;

        private long chapter_id;

        private String chapter_title;

        public long getBook_id() {
            return book_id;
        }

        public void setBook_id(long book_id) {
            this.book_id = book_id;
        }

        public long getAudio_id() {
            return audio_id;
        }

        public void setAudio_id(long audio_id) {
            this.audio_id = audio_id;
        }

        public long getChapter_id() {
            return chapter_id;
        }

        public void setChapter_id(long chapter_id) {
            this.chapter_id = chapter_id;
        }

        public String getChapter_title() {
            return chapter_title;
        }

        public void setChapter_title(String chapter_title) {
            this.chapter_title = chapter_title;
        }
    }
}
