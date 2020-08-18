package com.ssreader.novel.model;

import java.util.List;

public class BookChapterCatalog {

    public int total_chapter;

    public Author author;

    public List<BookChapter> chapter_list;

    public class Author{

        private long author_id;

        private String author_avatar, author_note;

        private String author_name;

        public long getAuthor_id() {
            return author_id;
        }

        public void setAuthor_id(long author_id) {
            this.author_id = author_id;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getAuthor_avatar() {
            return author_avatar;
        }

        public void setAuthor_avatar(String author_avatar) {
            this.author_avatar = author_avatar;
        }

        public String getAuthor_note() {
            return author_note;
        }

        public void setAuthor_note(String author_note) {
            this.author_note = author_note;
        }
    }
}
