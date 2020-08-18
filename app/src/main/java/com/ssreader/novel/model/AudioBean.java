package com.ssreader.novel.model;

import java.util.List;

/**
 * 用于接收有声数据
 *
 * @author admin
 * @version 2020/02/21
 */
public class AudioBean {

    private Audio audio;

    private User user;

    private BaseAd advert;

    public Audio getAudio() {
        return audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BaseAd getAdvert() {
        return advert;
    }

    public void setAdvert(BaseAd advert) {
        this.advert = advert;
    }

    public class Audio {

        public long audio_id, book_id;

        private String name, cover, description, content, duration_time;

        public String author;

        public String views, total_favors, display_label, finished, play_num, hot_num, horizontal_cover;

        public int is_collect, is_baoyue, is_finished, total_chapter;

        public String total_comment, last_chapter_time, last_chapter;

        public List<Tag> tag;

        public List<String> color;

        public String flag, words_price, chapter_price;

        public long getAudio_id() {
            return audio_id;
        }

        public void setAudio_id(long audio_id) {
            this.audio_id = audio_id;
        }

        public long getBook_id() {
            return book_id;
        }

        public void setBook_id(long book_id) {
            this.book_id = book_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDuration_time() {
            return duration_time;
        }

        public void setDuration_time(String duration_time) {
            this.duration_time = duration_time;
        }

        public String getViews() {
            return views;
        }

        public void setViews(String views) {
            this.views = views;
        }

        public String getTotal_favors() {
            return total_favors;
        }

        public void setTotal_favors(String total_favors) {
            this.total_favors = total_favors;
        }

        public String getDisplay_label() {
            return display_label;
        }

        public void setDisplay_label(String display_label) {
            this.display_label = display_label;
        }

        public String getFinished() {
            return finished;
        }

        public void setFinished(String finished) {
            this.finished = finished;
        }

        public String getPlay_num() {
            return play_num;
        }

        public void setPlay_num(String play_num) {
            this.play_num = play_num;
        }

        public String getHot_num() {
            return hot_num;
        }

        public void setHot_num(String hot_num) {
            this.hot_num = hot_num;
        }

        public String getHorizontal_cover() {
            return horizontal_cover;
        }

        public void setHorizontal_cover(String horizontal_cover) {
            this.horizontal_cover = horizontal_cover;
        }

        public int getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(int is_collect) {
            this.is_collect = is_collect;
        }

        public int getIs_baoyue() {
            return is_baoyue;
        }

        public void setIs_baoyue(int is_baoyue) {
            this.is_baoyue = is_baoyue;
        }

        public int getIs_finished() {
            return is_finished;
        }

        public void setIs_finished(int is_finished) {
            this.is_finished = is_finished;
        }

        public String getTotal_comment() {
            return total_comment;
        }

        public void setTotal_comment(String total_comment) {
            this.total_comment = total_comment;
        }

        public int getTotal_chapter() {
            return total_chapter;
        }

        public void setTotal_chapter(int total_chapter) {
            this.total_chapter = total_chapter;
        }

        public String getLast_chapter_time() {
            return last_chapter_time;
        }

        public void setLast_chapter_time(String last_chapter_time) {
            this.last_chapter_time = last_chapter_time;
        }

        public String getLast_chapter() {
            return last_chapter;
        }

        public void setLast_chapter(String last_chapter) {
            this.last_chapter = last_chapter;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getWords_price() {
            return words_price;
        }

        public void setWords_price(String words_price) {
            this.words_price = words_price;
        }

        public String getChapter_price() {
            return chapter_price;
        }

        public void setChapter_price(String chapter_price) {
            this.chapter_price = chapter_price;
        }

        public List<Tag> getTag() {
            return tag;
        }

        public void setTag(List<Tag> tag) {
            this.tag = tag;
        }

        public List<String> getColor() {
            return color;
        }

        public void setColor(List<String> color) {
            this.color = color;
        }

        public class Tag {

            private String tab, color;

            public String getTab() {
                return tab;
            }

            public void setTab(String tab) {
                this.tab = tab;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }
        }
    }

    public class User {

        private int is_vip;

        public int getIs_vip() {
            return is_vip;
        }

        public void setIs_vip(int is_vip) {
            this.is_vip = is_vip;
        }
    }
}
