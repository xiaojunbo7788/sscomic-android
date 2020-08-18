package com.ssreader.novel.ui.read.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.BookCatalogMarkRefresh;
import com.ssreader.novel.eventbus.BookEndRecommendRefresh;
import com.ssreader.novel.eventbus.RefreshPageFactoryChapter;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.model.BookChapterCatalog;
import com.ssreader.novel.model.ChapterContent;
import com.ssreader.novel.model.GetBookChapterList;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.LoginActivity;
import com.ssreader.novel.ui.activity.MainActivity;
import com.ssreader.novel.ui.dialog.WaitDialogUtils;
import com.ssreader.novel.ui.read.ReadActivity;
import com.ssreader.novel.model.BookMarkBean;
import com.ssreader.novel.ui.read.page.TxtPage;
import com.ssreader.novel.ui.utils.MainFragmentTabUtils;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.read.page.PageLoader;
import com.ssreader.novel.utils.FileManager;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ssreader.novel.constant.Constant.LOCAL_BOOKID;
import static com.ssreader.novel.constant.Constant.LordNext;

public class ChapterManager {

    public List<BookChapter> mChapterList;
    public Book baseBook;
    public BookChapter mCurrentChapter;

    private long mBookId, chapter_id;

    public ChapterManager() {
        mChapterList = new ArrayList<>();
    }

    public static ChapterManager mReadingManager;
    public Activity mContext;

    public static ChapterManager getInstance() {
        if (mReadingManager == null) {
            mReadingManager = new ChapterManager();
        }
        return mReadingManager;
    }

    public void openBook(Activity activity, Book baseBook) {
        mContext = activity;
        WaitDialogUtils.showDialog(mContext);
        mBookId = baseBook.book_id;
        chapter_id = baseBook.current_chapter_id;
        this.baseBook = baseBook;
        loadChapterList(null);
    }

    public void openBook(Activity activity, Book baseBook, List<BookChapter> updateList) {
        mContext = activity;
        WaitDialogUtils.showDialog(mContext);
        this.baseBook = baseBook;
        mBookId = baseBook.book_id;
        this.chapter_id = baseBook.current_chapter_id;
        if (mChapterList != null) {
            mChapterList.clear();
        }
        mChapterList.addAll(updateList);
        openCurrentChapter(null);
    }

    public void openBook(Activity activity, Book baseBook, BookMarkBean bookMarkBean) {
        mContext = activity;
        WaitDialogUtils.showDialog(mContext);
        this.baseBook = baseBook;
        mBookId = baseBook.book_id;
        this.chapter_id = baseBook.current_chapter_id;
        loadChapterList(bookMarkBean);
    }

    /**
     * @param context
     * @return 先关闭界面，再打开
     */
    public boolean openBook(Activity context, BookMarkBean bookMarkBean) {
        WaitDialogUtils.dismissDialog();
        EventBus.getDefault().post(new BookEndRecommendRefresh(true, false));
        EventBus.getDefault().post(new BookCatalogMarkRefresh(true));
        Intent intent = new Intent(context, ReadActivity.class);
        if (bookMarkBean != null) {
            intent.putExtra("mark_id", bookMarkBean.getMark_id());
        }
        context.startActivity(intent);
        return true;
    }

    /**
     * 打开当前章节
     *
     * @param bookMarkBean
     */
    public void openCurrentChapter(BookMarkBean bookMarkBean) {
        if (mBookId > LOCAL_BOOKID) {
            mCurrentChapter = mChapterList.get(0);
            openBook(mContext, bookMarkBean);
        } else {
            BookChapter mCurrentChapterTemp = getChapter(chapter_id);
            if (mCurrentChapterTemp != null) {
                notfindChapter(mCurrentChapterTemp, new ChapterDownload() {
                    @Override
                    public void finish(boolean HasData) {
                        if (HasData) {
                            mCurrentChapter = mCurrentChapterTemp;
                            openBook(mContext, bookMarkBean);
                        } else {
                            MyToash.ToashError(mContext, LanguageUtil.getString(mContext, InternetUtils.internet(mContext) ? R.string.chapterupdateing : R.string.splashactivity_nonet));
                            WaitDialogUtils.dismissDialog();
                        }
                    }
                });
            } else {
                MyToash.ToashError(mContext, LanguageUtil.getString(mContext, InternetUtils.internet(mContext) ? R.string.chapterupdateing : R.string.splashactivity_nonet));
                WaitDialogUtils.dismissDialog();
            }
        }
    }

    /**
     * 打开指定章节
     *
     * @param refreshPageFactoryChapter
     * @param mPageLoader
     */
    public void openCurrentChapter(RefreshPageFactoryChapter refreshPageFactoryChapter, PageLoader mPageLoader) {
        if (refreshPageFactoryChapter.bookChapterList != null && !refreshPageFactoryChapter.bookChapterList.isEmpty()) {
            //mChapterList.clear();
            //mChapterList.addAll(refreshPageFactoryChapter.bookChapterList);
            openCurrentChapter(true, refreshPageFactoryChapter.bookChapter, mPageLoader);
        } else {
            MyToash.Log("HTTP1-","openCurrentChapter");
            openCurrentChapter(false, refreshPageFactoryChapter.bookChapter, mPageLoader);
        }

    }

    /**
     * 打开指定章节
     *
     * @param isFromCatalog
     * @param querychapterItem
     * @param mPageLoader
     */
    public void openCurrentChapter(boolean isFromCatalog, BookChapter querychapterItem, PageLoader mPageLoader) {
        if (querychapterItem != null) {
            notfindChapter(querychapterItem, new ChapterDownload() {
                @Override
                public void finish(boolean HasData) {
                    if (HasData) {
                        mCurrentChapter = querychapterItem;
                        chapter_id = mCurrentChapter.chapter_id;
                        if (mPageLoader != null) {
                            mPageLoader.openChapter(querychapterItem);
                        }
                        if (isFromCatalog) {
                            // 关闭目录、书签界面
                            EventBus.getDefault().post(new BookCatalogMarkRefresh(true));
                        }
                    } else {
                        MyToash.ToashError(mContext, LanguageUtil.getString(mContext, InternetUtils.internet(mContext) ? R.string.chapterupdateing : R.string.splashactivity_nonet));
                        WaitDialogUtils.dismissDialog();
                    }
                }
            });
        } else {
            MyToash.ToashError(mContext, LanguageUtil.getString(mContext, InternetUtils.internet(mContext) ? R.string.chapterupdateing : R.string.splashactivity_nonet));
            WaitDialogUtils.dismissDialog();
        }
    }

    public BookChapter getCurrentChapter() {
        return mCurrentChapter;
    }

    public void setCurrentChapter(BookChapter chapter) {
        mCurrentChapter = chapter;
    }

    public boolean hasNextChapter() {
        if (mCurrentChapter == null) {
            return false;
        }
        return mCurrentChapter.getNext_chapter() != 0;
    }

    public boolean IsGetChapter;

    public interface GetChapterInterface {
        void getChapterOver(BookChapter bookChapter);
    }

    private GetChapterInterface getChapterInterface;

    public void setGetChapterInterface(GetChapterInterface getChapterInterface) {
        this.getChapterInterface = getChapterInterface;
    }

    public BookChapter getChapter(final long chapterId) {
        IsGetChapter = true;
        if (chapterId != 0) {
            if (mChapterList != null) {
                for (BookChapter chapterItem : mChapterList) {
                    if (chapterItem.getChapter_id() == chapterId) {
                        IsGetChapter = false;
                        if (getChapterInterface != null) {
                            getChapterInterface.getChapterOver(chapterItem);
                        }
                        return chapterItem;
                    }
                }
                IsGetChapter = false;
                if (getChapterInterface != null) {
                    getChapterInterface.getChapterOver(null);
                }
                return mChapterList.get(mChapterList.size() - 1);
            }
        }

        BookChapter chapterItem = mChapterList.get(0);
        IsGetChapter = false;
        if (getChapterInterface != null) {
            getChapterInterface.getChapterOver(chapterItem);
        }
        return chapterItem;
    }

    /**
     * 加载列表
     *
     * @param bookMarkBean
     */
    public void loadChapterList(BookMarkBean bookMarkBean) {
        baseBook.getOpenBookChapterList(mContext, chapter_id, new GetBookChapterList() {
            @Override
            public void getBookChapterList(List<BookChapter> bookChapterList) {
                if (bookChapterList != null && !bookChapterList.isEmpty()) {
                    mChapterList.clear();
                    mChapterList.addAll(bookChapterList);
                    openCurrentChapter(bookMarkBean);
                } else {
                    MyToash.ToashError(mContext, LanguageUtil.getString(mContext, InternetUtils.internet(mContext) ? R.string.chapterupdateing : R.string.splashactivity_nonet));
                    WaitDialogUtils.dismissDialog();
                }
            }
        });
    }

    /*************************************    获取章节内容   ************************************/

    /**
     * 获取章节内容
     *
     * @param querychapterItem
     * @param download
     */
    public static void notfindChapter(final BookChapter querychapterItem, final ChapterDownload download) {
        if (querychapterItem == null) {
            WaitDialogUtils.dismissDialog();
            if (download != null) {
                download.finish(false);
            }
            return;
        }
        if (querychapterItem.book_id < LOCAL_BOOKID) {
            MyToash.Log("http1", querychapterItem.is_preview + "");
            boolean PathIsExist = FileManager.isExistLocalBookTxtPath(querychapterItem);
            MyToash.Log("http1", PathIsExist + "");
            if (!PathIsExist ||
                    (querychapterItem.is_preview == 1//如果是登录状态并且开启了自动购买，并且当前是预览章节的话 强行每次都走接口使用服务端数据
                            && InternetUtils.internet(BWNApplication.applicationContext)
                            && UserUtils.isLogin(BWNApplication.applicationContext)
                            && ShareUitls.getSetBoolean(BWNApplication.applicationContext, Constant.AUTOBUY, false))) {
                getChapter_text(querychapterItem.book_id, querychapterItem.chapter_id, new GetChapter_text() {
                            @Override
                            public void getChapter_text(ChapterContent chapterContent) {// &&
                                MainFragmentTabUtils.UserCenterRefarsh=true;
                               // MyToash.Log("http1text", PathIsExist + "  " + chapterContent.getChapter_id() + "   " + querychapterItem.chapter_id);
                                if (chapterContent != null && (!PathIsExist || chapterContent.getIs_preview() != querychapterItem.is_preview)) {
                                    querychapterItem.setUpdate_time(chapterContent.getUpdate_time());
                                    querychapterItem.setIs_preview(chapterContent.getIs_preview());
                                    querychapterItem.chapter_text = chapterContent.getContent();
                                    String filepath = FileManager.getLocalBookTxtPath(querychapterItem);

                                    FileManager.createFile(filepath, chapterContent.getContent().getBytes());
                                    querychapterItem.setChapter_path(filepath);

                                    MyToash.Log("http1", querychapterItem.chapter_path);

                                    ObjectBoxUtils.addData(querychapterItem, BookChapter.class);
                                }
                                if (download != null) {
                                    download.finish(true);
                                }
                            }
                        }
                );
            } else {
                MyToash.Log("http1_true", PathIsExist + "");
                querychapterItem.chapter_path = FileManager.getLocalBookTxtPath(querychapterItem);
                if (download != null) {
                    download.finish(true);
                }
            }
            if (download != null && LordNext && InternetUtils.internet(BWNApplication.applicationContext)) {
                notfindChapter(ChapterManager.getInstance().getChapter(querychapterItem.getLast_chapter()), null);
                notfindChapter(ChapterManager.getInstance().getChapter(querychapterItem.getNext_chapter()), null);
            }
            LordNext = true;
        } else {
            if (querychapterItem.getChapter_path() == null) {
                if (download != null) {
                    download.finish(false);
                }
            }
        }
    }

    public interface ChapterDownload {

        void finish(boolean HasData);
    }

    public static void downChapter(Activity mContext, final long mBookId, DownChapter downChapter) {
        ReaderParams params = new ReaderParams(mContext);
        params.putExtraParams("book_id", mBookId);
        HttpUtils.getInstance().sendRequestRequestParamsSubThread(mContext, Api.mChapterCatalogUrl_old, params.generateParamsJson(), new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String result) {
                        HandleDownChpter(result, mBookId, new GetBookChapterList() {
                            @Override
                            public void getBookChapterList(List<BookChapter> bookChapterList) {
                                mContext.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!bookChapterList.isEmpty()) {
                                            downChapter.success(bookChapterList);
                                        } else {
                                            MyToash.ToashError(mContext, LanguageUtil.getString(mContext, InternetUtils.internet(mContext) ? R.string.chapterupdateing : R.string.splashactivity_nonet));
                                            downChapter.fail();
                                        }
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        downChapter.fail();
                    }
                }
        );
    }

    public interface DownChapter {

        void success(List<BookChapter> bookChapterList);

        void fail();
    }

    private static void HandleDownChpter(String result, long mBookId, GetBookChapterList getBookChapterList) {
        BookChapterCatalog bookChapterCatalog = HttpUtils.getGson().fromJson(result, BookChapterCatalog.class);
        Book book = ObjectBoxUtils.getBook(mBookId);
        if (book == null) {
            getBookChapterList.getBookChapterList(new ArrayList<>());
            return;
        }
        if (book.total_chapter != bookChapterCatalog.total_chapter) {
            book.total_chapter = bookChapterCatalog.total_chapter;
            if (bookChapterCatalog.author != null) {
                book.author_id = bookChapterCatalog.author.getAuthor_id();
                book.author_avatar = bookChapterCatalog.author.getAuthor_avatar();
//                if (bookChapterCatalog.author.getAuthor_name() != null
//                        && !bookChapterCatalog.author.getAuthor_name().isEmpty()
//                        && !TextUtils.isEmpty(bookChapterCatalog.author.getAuthor_name().get(0))) {
//                    StringBuilder name = new StringBuilder();
//                    for (int i = 0; i < bookChapterCatalog.author.getAuthor_name().size(); i++) {
//                        if (i == 0) {
//                            name.append(bookChapterCatalog.author.getAuthor_name().get(i));
//                        } else {
//                            name.append(" ").append(bookChapterCatalog.author.getAuthor_name().get(i));
//                        }
//                    }
//                }
                book.author_name = bookChapterCatalog.author.getAuthor_name().replaceAll(",", " ");
                book.author_note = bookChapterCatalog.author.getAuthor_note();
            }
            ObjectBoxUtils.addData(book, Book.class);
        }
        List<BookChapter> bookChaptersLocal = ObjectBoxUtils.getBookChapterData(mBookId);
        String Chapter_text_Sign = UserUtils.MD5(result);
        List<BookChapter> bookChapters = bookChapterCatalog.chapter_list;
        if (bookChapters == null || bookChapters.isEmpty()) {
            getBookChapterList.getBookChapterList(bookChaptersLocal);
            return;
        }
        if (bookChaptersLocal.isEmpty() || book.Chapter_text == null) {
            book.total_chapter = bookChapters.size();
            book.Chapter_text = Chapter_text_Sign;
            ObjectBoxUtils.addData(book, Book.class);
            ObjectBoxUtils.addData(bookChapters, BookChapter.class);
            getBookChapterList.getBookChapterList(bookChapters);
        } else {
            if (Chapter_text_Sign.equals(book.getChapter_text())) {
                getBookChapterList.getBookChapterList(bookChaptersLocal);
            } else {
                book.total_chapter = bookChapters.size();
                book.Chapter_text = Chapter_text_Sign;
                ObjectBoxUtils.addData(book, Book.class);
                if (!bookChapters.isEmpty()) {
                    for (BookChapter chapterItem : bookChapters) {
                        int position = bookChaptersLocal.indexOf(chapterItem);
                        if (position != -1) {
                            BookChapter bookChapterLocal = bookChaptersLocal.get(position);
                            chapterItem.is_read = bookChapterLocal.is_read;
                            chapterItem.PagePos = bookChapterLocal.PagePos;
                            chapterItem.chapteritem_begin = bookChapterLocal.chapteritem_begin;
                            chapterItem.chapter_path = bookChapterLocal.chapter_path;
                        }
                    }
                    ObjectBoxUtils.deletALLeData(bookChaptersLocal, BookChapter.class);
                    ObjectBoxUtils.addData(bookChapters, BookChapter.class);
                    getBookChapterList.getBookChapterList(bookChapters);
                } else {
                    getBookChapterList.getBookChapterList(new ArrayList<>());
                }
            }
        }
    }

    /**
     * 获取内容
     *
     * @param book_id
     * @param chapter_id
     * @param getChapter_text
     */
    public static void getChapter_text(final long book_id, final long chapter_id, GetChapter_text getChapter_text) {
        ReaderParams params = new ReaderParams(BWNApplication.applicationContext.getActivity());
        params.putExtraParams("book_id", book_id);
        params.putExtraParams("chapter_id", chapter_id);
        String json = params.generateParamsJson();
        MyToash.Log("http1_text",chapter_id+"  "+(BWNApplication.applicationContext.getActivity()).getClass().getName());
        HttpUtils.getInstance().sendRequestRequestParams(BWNApplication.applicationContext.getActivity(), Api.chapter_text, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        MyToash.Log("http1_text",result+"");
                        if (!TextUtils.isEmpty(result)) {
                            ChapterContent chapterContent = HttpUtils.getGson().fromJson(result, ChapterContent.class);
                            if (chapterContent.getContent() != null) {
                                getChapter_text.getChapter_text(chapterContent);
                            } else {
                                WaitDialogUtils.dismissDialog();
                            }
                        } else {
                            WaitDialogUtils.dismissDialog();
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        MyToash.Log("http1_text","onErrorResponse");
                        WaitDialogUtils.dismissDialog();
                        if (ex != null && ex.equals("no_net")) {
                            MyToash.ToashError(BWNApplication.applicationContext.getActivity(), LanguageUtil.getString(BWNApplication.applicationContext.getActivity(), R.string.splashactivity_nonet));
                        }
                    }
                }
        );
    }

    public interface GetChapter_text {

        void getChapter_text(ChapterContent chapterContent);
    }

    /********************************  获取显示章节的内容  *******************************/

    public String getChapterContent(List<TxtPage> txtPageList) {
        if (txtPageList != null && !txtPageList.isEmpty()) {
            String content = "";
            for (TxtPage txtPage : txtPageList) {
                content = content + txtPage.getLineTexts();
            }
            return content;
        } else {
            return "";
        }
    }

    /********************************  购买  *****************************/

    /**
     * 单章购买
     *
     * @param activity
     * @param bookId
     * @param chapterId
     * @param num
     */
    public void purchaseSingleChapter(Activity activity, String url, long bookId, final String chapterId,
                                      int num, OnPurchaseListener onPurchaseListener) {
        if (!UserUtils.isLogin(activity)) {
            if (onPurchaseListener != null) {
                onPurchaseListener.purchaseSuc(null);
            }
            Intent intent = new Intent();
            intent.setClass(activity, LoginActivity.class);
            activity.startActivity(intent);
            return;
        }
        ReaderParams params = new ReaderParams(activity);
        switch (url) {
            case Api.mChapterBuy:
                params.putExtraParams("book_id", bookId);
                break;
            case Api.audio_chapter_buy:
                params.putExtraParams("audio_id", bookId);
                break;
            case Api.COMIC_buy_buy:
                params.putExtraParams("comic_id", bookId);
                break;
        }
        String oneBuy = null;
        params.putExtraParams("chapter_id", chapterId);
        if (num > 0) {
            params.putExtraParams("num", num);
        } else if (num == -1) {
            oneBuy = "oneBuy";
        }
        HttpUtils.getInstance().sendRequestRequestParams(activity, url, params.generateParamsJson(), oneBuy, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        MainFragmentTabUtils.UserCenterRefarsh=true;
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            long[] chapter_ids = HttpUtils.getGson().fromJson(jsonObject.getString("chapter_ids"), long[].class);
                            if (onPurchaseListener != null) {
                                onPurchaseListener.purchaseSuc(chapter_ids);
                            }
                        } catch (JSONException e) {
                            if (onPurchaseListener != null) {
                                onPurchaseListener.purchaseSuc(null);
                            }
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        if (onPurchaseListener != null) {
                            onPurchaseListener.purchaseSuc(null);
                        }
                    }
                }
        );
    }

    public interface OnPurchaseListener {

        void purchaseSuc(long[] chapter_ids);
    }
}
