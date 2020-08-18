package com.ssreader.novel.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.ssreader.novel.constant.Api;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.UserUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

@Entity
public class Book implements Comparable<Book>, Serializable {

    public Book() {

    }

    //id字段是必要的字段，不可忽略，不可用修饰符修饰
    @Id(assignable = true)
    public long book_id;
    public String name;
    public int is_collect;
    public String cover;
    public String author;
    public int new_chapter;
    public String allPercent;
    public int total_chapter;
    public long current_chapter_id;
    public long current_chapter_id_hasData;
    // 用于存储当前章节名
    public String current_chapter_text;
    public long current_listen_chapter_id;
    public int is_read;
    public int current_chapter_displayOrder;
    public int current_chapter_listen_displayOrder;
    // 该值不能用于储存章节名，是用于判断章节目录是否有更新来使用的
    public String Chapter_text;
    public String description;
    public int BookselfPosition;
    public String language;
    // 语速
    public String speed = "60";
    // 是不是推荐书籍
    public boolean isRecommend;
    // 作家
    public long author_id;
    public String author_name, author_avatar, author_note;
    public boolean isLocal;
    @Transient
    public long mark_id;

    public long getCurrent_listen_chapter_id() {
        return current_listen_chapter_id;
    }

    public void setCurrent_listen_chapter_id(long current_listen_chapter_id) {
        this.current_listen_chapter_id = current_listen_chapter_id;
    }

    public boolean isRecommend() {
        return isRecommend;
    }

    public void setRecommend(boolean recommend) {
        isRecommend = recommend;
    }

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

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public String getLanguage() {
        return language;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getbook_id() {
        return book_id;
    }

    public void setbook_id(long book_id) {
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

    public int getNew_chapter() {
        return new_chapter;
    }

    public void setNew_chapter(int new_chapter) {
        this.new_chapter = new_chapter;
    }

    public String getAllPercent() {
        return allPercent;
    }

    public void setAllPercent(String allPercent) {
        this.allPercent = allPercent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotal_chapter() {
        return total_chapter;
    }

    public void setTotal_chapter(int total_chapter) {
        this.total_chapter = total_chapter;
    }

    public long getCurrent_chapter_id() {
        return current_chapter_id;
    }

    public void setCurrent_chapter_id(long current_chapter_id) {
        this.current_chapter_id = current_chapter_id;
    }

    public String getCurrent_chapter_text() {
        return current_chapter_text;
    }

    public void setCurrent_chapter_text(String current_chapter_text) {
        this.current_chapter_text = current_chapter_text;
    }

    public int getCurrent_chapter_displayOrder() {
        return current_chapter_displayOrder;
    }

    public void setCurrent_chapter_displayOrder(int current_chapter_displayOrder) {
        this.current_chapter_displayOrder = current_chapter_displayOrder;
    }

    public int getCurrent_chapter_listen_displayOrder() {
        return current_chapter_listen_displayOrder;
    }

    public void setCurrent_chapter_listen_displayOrder(int current_chapter_listen_displayOrder) {
        this.current_chapter_listen_displayOrder = current_chapter_listen_displayOrder;
    }

    public String getChapter_text() {
        return Chapter_text;
    }

    public void setChapter_text(String chapter_text) {
        Chapter_text = chapter_text;
    }

    public int getBookselfPosition() {
        return BookselfPosition;
    }

    public void setBookselfPosition(int BookselfPosition) {
        this.BookselfPosition = BookselfPosition;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public long getMark_id() {
        return mark_id;
    }

    public void setMark_id(long mark_id) {
        this.mark_id = mark_id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Book) {
            return (book_id == (((Book) obj).book_id));
        }
        return super.equals(obj);
    }

    public Book(long book_id, String name, int is_collect, String cover, String author,
                int new_chapter, String allPercent, int total_chapter, long current_chapter_id,
                int current_chapter_displayOrder, String chapter_text, String description,
                int bookselfPosition, String speed, long mark_id) {
        this.book_id = book_id;
        this.name = name;
        this.is_collect = is_collect;
        this.cover = cover;
        this.author = author;
        this.new_chapter = new_chapter;
        this.allPercent = allPercent;
        this.total_chapter = total_chapter;
        this.current_chapter_id = current_chapter_id;
        this.current_chapter_displayOrder = current_chapter_displayOrder;
        Chapter_text = chapter_text;
        this.description = description;
        BookselfPosition = bookselfPosition;
        this.speed = speed;
        this.mark_id = mark_id;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
    }

    @Override
    public int hashCode() {
        return (int) book_id;
    }

    public long getBook_id() {
        return book_id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "book_id=" + book_id +
                ", name='" + name + '\'' +
                ", is_collect=" + is_collect +
                ", cover='" + cover + '\'' +
                ", author='" + author + '\'' +
                ", new_chapter=" + new_chapter +
                ", allPercent='" + allPercent + '\'' +
                ", total_chapter=" + total_chapter +
                ", current_chapter_id=" + current_chapter_id +
                ", current_chapter_displayOrder=" + current_chapter_displayOrder +
                ", Chapter_text='" + Chapter_text + '\'' +
                ", description='" + description + '\'' +
                ", BookselfPosition=" + BookselfPosition +
                '}';
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    @Override
    public int compareTo(Book Book) {
        return Book.BookselfPosition - this.BookselfPosition;
    }

    /**
     * 获取目录
     *
     * @param activity
     * @param chapter_id
     * @param scroll_type
     * @param order_by
     * @param getBookChapterList
     */
    public void getBookChapterList(Activity activity, long chapter_id, int scroll_type, int order_by, GetBookChapterList getBookChapterList) {
        Book book = this;
        ReaderParams readerParams = new ReaderParams(activity);
        if (chapter_id != 0) {
            readerParams.putExtraParams("chapter_id", chapter_id);
        }
        if (scroll_type != 0) {
            readerParams.putExtraParams("scroll_type", scroll_type);
        }
        if (order_by != 0) {
            readerParams.putExtraParams("order_by", order_by);
        }
        readerParams.putExtraParams("book_id", book.book_id);
        httpCatgory(activity, chapter_id, scroll_type,getBookChapterList, book, readerParams);
    }

    public void getOpenBookChapterList(Activity activity, long chapter_id,GetBookChapterList getBookChapterList) {
        Book book = this;
        ReaderParams readerParams = new ReaderParams(activity);
        if (chapter_id != 0) {
            readerParams.putExtraParams("chapter_id", chapter_id);
        }
        readerParams.putExtraParams("book_id", book.book_id);
        readerParams.putExtraParams("num", 10);
        httpCatgory(activity, chapter_id, 0,getBookChapterList, book, readerParams);
    }

    private void httpCatgory(Activity activity, long chapter_id, int scroll_type,GetBookChapterList getBookChapterList, Book book, ReaderParams readerParams) {
        HttpUtils.getInstance().sendRequestRequestParamsSubThread(activity,chapter_id == -1 ? Api.mChapterCatalogUrl_old : Api.mChapterCatalogUrl,
                readerParams.generateParamsJson(),  new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            BookChapterCatalog bookChapterCatalog = HttpUtils.getGson().fromJson(response, BookChapterCatalog.class);
                            if (book.total_chapter != bookChapterCatalog.total_chapter) {
                                book.total_chapter = bookChapterCatalog.total_chapter;
                                if (bookChapterCatalog.author != null) {
                                    book.author_id = bookChapterCatalog.author.getAuthor_id();
                                    book.author_avatar = bookChapterCatalog.author.getAuthor_avatar();
//                                    if (bookChapterCatalog.author.getAuthor_name() != null
//                                            && !bookChapterCatalog.author.getAuthor_name().isEmpty()
//                                            && !TextUtils.isEmpty(bookChapterCatalog.author.getAuthor_name().get(0))) {
//                                        StringBuilder name = new StringBuilder();
//                                        for (int i = 0; i < bookChapterCatalog.author.getAuthor_name().size(); i++) {
//                                            if (i == 0) {
//                                                name.append(bookChapterCatalog.author.getAuthor_name().get(i));
//                                            } else {
//                                                name.append(" ").append(bookChapterCatalog.author.getAuthor_name().get(i));
//                                            }
//                                        }
//                                    }
                                    book.author_name = bookChapterCatalog.author.getAuthor_name().replaceAll(",", " ");
                                    book.author_note = bookChapterCatalog.author.getAuthor_note();
                                }
                                ObjectBoxUtils.addData(book, Book.class);
                            }
                            if (bookChapterCatalog.chapter_list != null && !bookChapterCatalog.chapter_list.isEmpty()) {
                                for (BookChapter bookChapter : bookChapterCatalog.chapter_list) {
                                    BookChapter bookChapterLocal = getLocalData(bookChapter.chapter_id);
                                    if (bookChapterLocal != null) {
                                        bookChapter.is_read = bookChapterLocal.is_read;
                                        bookChapter.PagePos = bookChapterLocal.PagePos;
                                        bookChapter.chapter_path = bookChapterLocal.chapter_path;
                                        bookChapter.chapter_text = bookChapterLocal.chapter_text;
                                    }
                                }
                                ObjectBoxUtils.addData(bookChapterCatalog.chapter_list, BookChapter.class);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getBookChapterList.getBookChapterList(bookChapterCatalog.chapter_list);
                                    }
                                });
                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getBookChapterList.getBookChapterList(new ArrayList<>());
                                    }
                                });
                            }
                        } else {
                            if (scroll_type == 0) {
                                getBookChapterList.getBookChapterList(ObjectBoxUtils.getBookChapterData(book.book_id));
                            }
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        if (scroll_type == 0) {
                            getBookChapterList.getBookChapterList(ObjectBoxUtils.getBookChapterData(book.book_id));
                        }
                    }
                });
    }

    public BookChapter getLocalData(long chapter_id) {

        return ObjectBoxUtils.getBookChapter(chapter_id);
    }
}
