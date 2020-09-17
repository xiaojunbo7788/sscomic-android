package com.ssreader.novel.model;

import java.io.Serializable;
import java.util.List;

import io.objectbox.annotation.Transient;

public class BaseBookComic extends BaseAd implements Serializable {

    /**
     * book_id : 422
     * name : 耐瑟瑞尔的辉煌
     * cover : http://ssreader.oss-cn-beijing.aliyuncs.com/422/cover/ad852591c4189ab2fc74f948bd9645cb.jpeg?x-oss-process=image%2Fresize%2Cw_286%2Ch_400%2Cm_lfit
     * author : 黎明C
     * description : 假dnd爽文，借用些许设定，主角纯法师，冷血谨慎黑暗无情，全程无女主，没有扮猪吃虎，没有生死关头爆发，只有稳步前行，没有遍地强敌，主角总是别人的强敌，全心向耐瑟瑞尔的辉煌进发，欢迎各位加群548859...
     * tag : [{"tab":"现代爱情","color":"#71c5fb"},{"tab":"已完结","color":"#f98445"}]
     * is_finished : 1
     * property : []
     */
    public String name;
    public String cover;
    public String author;
    public List<ComicTag> author2;
    public String description;
    public String is_finished;
    public List<BaseTag> tag;
    public long book_id;
    public long comic_id;
    public long audio_id;
    public String horizontal_cover;
    public String vertical_cover;
    public int is_finish;
    public String finished;
    public String flag;
    public String title;
    public String hot_num;
    public String total_favors;
    public String total_views;
    public int total_chapters;
    public int total_comment;
    public boolean ISMaxCount;
    public long id;
    public int display_no;

    public String last_chapter_time;
    public List<ComicTag> sinici2;
    public String sinici;
    public String original;
    public List<ComicTag> original2;
    public String created_at;
    public List<String> tags;

    public static class ComicTag {
        private int type;
        private String author;
        private String original;
        private String sinici;
        private int is_collect;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }

        public String getSinici() {
            return sinici;
        }

        public void setSinici(String sinici) {
            this.sinici = sinici;
        }

        public int getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(int is_collect) {
            this.is_collect = is_collect;
        }
    }

    public BaseBookComic(long id, boolean ISMaxCount) {
        this.id = id;
        this.ISMaxCount = true;
    }

    public List<ComicTag> getAuthor2() {
        return author2;
    }

    public void setAuthor2(List<ComicTag> author2) {
        this.author2 = author2;
    }

    public List<ComicTag> getSinici2() {
        return sinici2;
    }

    public void setSinici2(List<ComicTag> sinici2) {
        this.sinici2 = sinici2;
    }

    public List<ComicTag> getOriginal2() {
        return original2;
    }

    public void setOriginal2(List<ComicTag> original2) {
        this.original2 = original2;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIs_finished() {
        return is_finished;
    }

    public void setIs_finished(String is_finished) {
        this.is_finished = is_finished;
    }

    public List<BaseTag> getTag() {
        return tag;
    }

    public void setTag(List<BaseTag> tag) {
        this.tag = tag;
    }

    public long getBook_id() {
        return book_id;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    public long getComic_id() {
        return comic_id;
    }

    public void setComic_id(long comic_id) {
        this.comic_id = comic_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHorizontal_cover() {
        return horizontal_cover;
    }

    public void setHorizontal_cover(String horizontal_cover) {
        this.horizontal_cover = horizontal_cover;
    }

    public String getVertical_cover() {
        return vertical_cover;
    }

    public void setVertical_cover(String vertical_cover) {
        this.vertical_cover = vertical_cover;
    }

    public int getIs_finish() {
        return is_finish;
    }

    public void setIs_finish(int is_finish) {
        this.is_finish = is_finish;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "BookComic{" +
                "name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", description='" + description + '\'' +
                ", is_finished=" + is_finished +
                ", book_id='" + book_id + '\'' +
                ", comic_id='" + comic_id + '\'' +
                ", horizontal_cover='" + horizontal_cover + '\'' +
                ", vertical_cover='" + vertical_cover + '\'' +
                ", is_finish=" + is_finish +
                ", flag='" + flag + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
