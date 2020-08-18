package com.ssreader.novel.utils;

import com.ssreader.novel.MyObjectBox;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.AudioChapter;
import com.ssreader.novel.model.AudioChapter_;
import com.ssreader.novel.model.Audio_;
import com.ssreader.novel.model.Book;

import com.ssreader.novel.model.BookChapter;


import com.ssreader.novel.model.BookChapter_;
import com.ssreader.novel.model.BookMarkBean_;
import com.ssreader.novel.model.Book_;
import com.ssreader.novel.model.Comic;
import com.ssreader.novel.model.ComicChapter;
import com.ssreader.novel.model.ComicChapter_;
import com.ssreader.novel.model.Comic_;
import com.ssreader.novel.model.Downoption;

import com.ssreader.novel.model.Downoption_;
import com.ssreader.novel.ui.localshell.bean.LocalNotesBean;
import com.ssreader.novel.ui.localshell.localapp.LocalBook;
import com.ssreader.novel.model.BookMarkBean;
import com.ssreader.novel.ui.utils.MyToash;


import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * 书籍数据库管理类
 */
public class ObjectBoxUtils {

    public static BoxStore init() {
        BoxStore boxStore = null;
        try {
            boxStore = BWNApplication.applicationContext.getBoxStore();
            if (boxStore == null) {
                boxStore = MyObjectBox.builder().androidContext(BWNApplication.applicationContext).build();
                BWNApplication.applicationContext.setBoxStore(boxStore);
            }
        } catch (Exception e) {
        }
        return boxStore;
    }

    /**
     * 添加数据
     */
    public static <T> long addData(T o, Class c) {
        MyToash.Log("down_chapters-",c.getName());
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                return boxStore.boxFor(c).put(o);
            }
        } catch (Throwable e) {
        }
        return  0;
    }

    public static <T> void addData(List<T> oo, Class c) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                boxStore.boxFor(c).put(oo);
            }
        } catch (Exception e) {
        }
    }

    public static <T> List<T> getAllData(Class clazz) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<T> box = boxStore.boxFor(clazz);

                return box.getAll();
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public static List<Book> getBookShelfData() {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<Book> box = boxStore.boxFor(Book.class);
                List<Book> joes = box.query().equal(Book_.is_collect, 1).build().find();
                return joes;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public static List<Downoption> getDownoptionsfData(long book_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<Downoption> box = boxStore.boxFor(Downoption.class);
                List<Downoption> joes = box.query().equal(Downoption_.book_id, book_id).build().find();
                return joes;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public static List<BookChapter> getBookChapterItemfData(long book_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<BookChapter> box = boxStore.boxFor(BookChapter.class);
                List<BookChapter> joes = box.query().equal(BookChapter_.book_id, book_id).build().find();
                return joes;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public static List<ComicChapter> getcomicDownOptionList(long comic_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<ComicChapter> box = boxStore.boxFor(ComicChapter.class);
                List<ComicChapter> joes = box.query()
                        .equal(ComicChapter_.comic_id, comic_id)
                        .equal(ComicChapter_.downStatus, 1)
                        .build().find();
                return joes;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public static List<Comic> getyetDownComicList() {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<Comic> box = boxStore.boxFor(Comic.class);
                List<Comic> joes = box.query()
                        .notEqual(Comic_.down_chapters, 0)
                        .build().find();
                return joes;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public static List<ComicChapter> getComicChapterItemfData(long comic_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<ComicChapter> box = boxStore.boxFor(ComicChapter.class);
                List<ComicChapter> joes = box.query().equal(ComicChapter_.comic_id, comic_id).build().find();
                return joes;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public static List<AudioChapter> getAudioChapterItemfData(long audio_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<AudioChapter> box = boxStore.boxFor(AudioChapter.class);
                List<AudioChapter> joes = box.query().equal(AudioChapter_.audio_id, audio_id).build().find();
                return joes;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public static ComicChapter getComicChapter(long id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<ComicChapter> box = boxStore.boxFor(ComicChapter.class);
                return box.get(id);
            }
        } catch (Exception e) {
        }
        return null;

    }

    public static Book getBook(long id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<Book> box = boxStore.boxFor(Book.class);
                return box.get(id);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static BookChapter getBookChapter(long chapter_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<BookChapter> box = boxStore.boxFor(BookChapter.class);
                return box.get(chapter_id);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static Comic getComic(long id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<Comic> box = boxStore.boxFor(Comic.class);
                return box.get(id);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static List<Comic> getComicShelfData() {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<Comic> box = boxStore.boxFor(Comic.class);
                List<Comic> joes = box.query().equal(Comic_.is_collect, 1).build().find();
                return joes;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public static List<Audio> getAudioShelfData() {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<Audio> box = boxStore.boxFor(Audio.class);
                List<Audio> joes = box.query().equal(Audio_.is_collect, 1).build().find();
                return joes;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    //删除数据
    public static <T> void deleteData(Object o, Class clazz) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<T> box = boxStore.boxFor(clazz);
                box.remove((T) o);
            }
        } catch (Exception e) {
        }
    }

    //删除数据
    public static <T> void deletALLeData(List<T> o, Class clazz) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<T> box = boxStore.boxFor(clazz);
                box.remove(o);
            }
        } catch (Exception e) {
        }
    }

    public static Downoption getDownoption(long id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<Downoption> box = boxStore.boxFor(Downoption.class);
                Downoption joe = box.query().equal(Downoption_.id, id).build().findFirst();
                return joe;
            }
        } catch (Exception e) {
        }
        return null;
    }

    //删除数据
    public static void deleteDownoption(long id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<Downoption> box = boxStore.boxFor(Downoption.class);
                box.remove(id);
            }
        } catch (Exception e) {
        }
       /* try {
            Downoption downoption = getDownoption(id);
            deleteData(downoption, Downoption.class);
        } catch (Exception e) {
        }*/
    }

    public static List<BookChapter> getBookChapterData(long book_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<BookChapter> box = boxStore.boxFor(BookChapter.class);
                List<BookChapter> joes = box.query().equal(BookChapter_.book_id, book_id).build().find();
                return joes;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public static List<BookChapter> getBookChapterMoreChapter_id(long book_id, long Chapter_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<BookChapter> box = boxStore.boxFor(BookChapter.class);
                List<BookChapter> joes = box.query().equal(BookChapter_.book_id, book_id).build().find();
                return joes;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public static BookChapter getBookChapterFirstData(long book_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<BookChapter> box = boxStore.boxFor(BookChapter.class);
                BookChapter joes = box.query().equal(BookChapter_.book_id, book_id).build().findFirst();
                if (joes != null) {
                    return joes;
                }
                return joes;
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 删除数据
     */
    public static <T> void removeAllData(Class clazz) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<T> box = boxStore.boxFor(clazz);
                box.removeAll();
            }
        } catch (Exception e) {
        }
    }

    public static void removeAllBookChapterData(long book_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<BookChapter> box = boxStore.boxFor(BookChapter.class);
                box.remove(getBookChapterItemfData(book_id));
            }
        } catch (Exception w) {
        }
    }

    public static void removeAllComicChapterData(long comic_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<ComicChapter> box = boxStore.boxFor(ComicChapter.class);
                box.remove(getComicChapterItemfData(comic_id));
            }
        } catch (Exception e) {
        }
    }

    public static void removeAllAudioChapterData(long audio_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<AudioChapter> box = boxStore.boxFor(AudioChapter.class);
                box.remove(getAudioChapterItemfData(audio_id));
            }
        } catch (Exception e) {
        }
    }

    public static List<LocalBook> getLocalBookShelfData() {
        BoxStore boxStore = init();
        if (boxStore != null && !boxStore.isClosed()) {
            Box<LocalBook> box = boxStore.boxFor(LocalBook.class);
            List<LocalBook> joes = box.query().build().find();
            return joes;
        }
        return new ArrayList<>();
    }

    public static List<LocalNotesBean> getLocalBookNoteData() {
        BoxStore boxStore = init();
        if (boxStore != null && !boxStore.isClosed()) {
            Box<LocalNotesBean> box = boxStore.boxFor(LocalNotesBean.class);
            List<LocalNotesBean> joes = box.query().build().find();
            return joes;
        }
        return new ArrayList<>();
    }

    public static Audio getAudio(long id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<Audio> box = boxStore.boxFor(Audio.class);
                return box.get(id);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static List<AudioChapter> getAudioChapterData(long audio_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<AudioChapter> box = boxStore.boxFor(AudioChapter.class);
                List<AudioChapter> joes = box.query().equal(AudioChapter_.audio_id, audio_id).build().find();
                return joes;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public static AudioChapter getAudioChapter(long chapter_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<AudioChapter> box = boxStore.boxFor(AudioChapter.class);
                return box.get(chapter_id);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static List<Audio> getyetDownAudioList() {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<Audio> box = boxStore.boxFor(Audio.class);
                List<Audio> joes = box.query()
                        .notEqual(Audio_.down_chapters, 0)
                        .build().find();
                return joes;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    /**
     * 添加书签
     */
    public static long addBookMarkBean(BookMarkBean bookMarkBean) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                return  boxStore.boxFor(BookMarkBean.class).put(bookMarkBean);
            }
        } catch (Exception e) {
        } catch (Error error) {
        }
        return  0;
    }

    /**
     * @return 获取全部书签列表
     */
    public static List<BookMarkBean> getBookMarkBeanList(long book_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<BookMarkBean> box = boxStore.boxFor(BookMarkBean.class);
                List<BookMarkBean> joes = box.query()
                        .equal(BookMarkBean_.book_id, book_id)
                        .build().find();
                return joes;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    /**
     * @return 获取指定书签
     */
    public static BookMarkBean getBookMarkBean(long BookMarkBean_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<BookMarkBean> box = boxStore.boxFor(BookMarkBean.class);
                return box.get(BookMarkBean_id);
            }
        } catch (Exception e) {
        }
        return  null;
    }

    /**
     * 获取指定章节书签全部列表
     * @param chapter_id
     * @return
     */
    public static List<BookMarkBean> getChapterBookMarkBeanList(long chapter_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<BookMarkBean> box = boxStore.boxFor(BookMarkBean.class);
                List<BookMarkBean> joes = box.query()
                        .equal(BookMarkBean_.chapter_id, chapter_id)
                        .build().find();
                return joes;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    /**
     * 删除书签
     * @param mark_id
     * @return
     */
    public static boolean removeMarkBean(long mark_id) {
        try {
            BoxStore boxStore = init();
            if (boxStore != null && !boxStore.isClosed()) {
                Box<BookMarkBean> box = boxStore.boxFor(BookMarkBean.class);
                return  box.remove(mark_id);
            }
        } catch (Exception e) {
        }
        return false;
    }
}
