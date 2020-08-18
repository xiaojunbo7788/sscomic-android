package com.ssreader.novel.ui.read.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseDialogFragment;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.RefreshBookSelf;
import com.ssreader.novel.eventbus.RefreshShelf;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.ui.activity.BookInfoActivity;
import com.ssreader.novel.model.BookMarkBean;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.read.page.PageLoader;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.MyShare;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.xiaomi.mipush.sdk.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 更多弹窗
 */
public class ReadHeadMoreDialog extends BaseDialogFragment {

    @BindView(R.id.dialog_read_head_bookinfo)
    LinearLayout dialogReadHeadBookinfo;
    @BindView(R.id.dialog_read_head_bookmark)
    LinearLayout dialogReadHeadBookmark;
    @BindView(R.id.dialog_read_head_bookshelf)
    LinearLayout dialogReadHeadBookshelf;
    @BindView(R.id.dialog_read_head_bookshare)
    LinearLayout dialogReadHeadBookshare;
    @BindView(R.id.dialog_read_head_layout)
    LinearLayout dialog_read_head_layout;
    @BindView(R.id.dialog_read_head_bookmark_img)
    ImageView dialog_read_head_bookmark_img;
    @BindView(R.id.dialog_read_head_bookshelf_img)
    ImageView dialog_read_head_bookshelf_img;
    @BindView(R.id.dialog_read_head_bookshelf_title)
    TextView dialog_read_head_bookshelf_title;
    @BindView(R.id.dialog_read_head_bookmark_title)
    TextView dialog_read_head_bookmark_title;

    private BookMarkBean isAddBookMarkBean;
    // 是否添加了书签
    private boolean yetAddBookMarkBean;
    // 本页的书签
    private List<BookMarkBean> localBookMarkBeans;

    private Book baseBook;
    private PageLoader pageLoader;
    private OnDialogDismissListener dialogDismissListener;

    public ReadHeadMoreDialog() {

    }

    public ReadHeadMoreDialog(Activity activity, Book book, PageLoader pageLoader, OnDialogDismissListener dialogDismissListener) {
        super(true);
        this.activity = activity;
        baseBook = book;
        this.pageLoader = pageLoader;
        this.dialogDismissListener = dialogDismissListener;
    }

    @Override
    public int initContentView() {
        return R.layout.dialog_read_head_move;
    }

    @Override
    public void initView() {
        localBookMarkBeans = new ArrayList<>();
        dialog_read_head_layout.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 8),
                ImageUtil.dp2px(activity, 8), 0, 0,
                ContextCompat.getColor(activity, R.color.white)));
        if (baseBook.is_collect == 0) {
            dialog_read_head_bookshelf_img.setImageResource(R.mipmap.img_add_bookshel);
            dialog_read_head_bookshelf_title.setText(LanguageUtil.getString(activity, R.string.BookInfoActivity_jiarushujia));
            dialog_read_head_bookshelf_title.setTextColor(ContextCompat.getColor(activity, R.color.black_3));
            dialog_read_head_bookshelf_title.setEnabled(true);
        } else {
            dialog_read_head_bookshelf_img.setImageResource(R.mipmap.img_del_bookshel);
            dialog_read_head_bookshelf_title.setText(LanguageUtil.getString(activity, R.string.BookInfoActivity_jiarushujias));
            dialog_read_head_bookshelf_title.setTextColor(ContextCompat.getColor(activity, R.color.gray_9));
            dialog_read_head_bookshelf_title.setEnabled(false);
        }
    }

    @OnClick({R.id.dialog_read_head_bookinfo, R.id.dialog_read_head_bookmark,
            R.id.dialog_read_head_bookshelf, R.id.dialog_read_head_bookshare,
            R.id.dialog_read_head_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_read_head_close:
                dismissAllowingStateLoss();
                break;
            case R.id.dialog_read_head_bookinfo:
                Intent intent = new Intent(activity, BookInfoActivity.class)
                        .putExtra("book_id", baseBook.book_id)
                        .putExtra("From", "ReadActivity");
                activity.startActivity(intent);
                dismissAllowingStateLoss();
                break;
            case R.id.dialog_read_head_bookmark:
                try {
                    if (isAddBookMarkBean == null || pageLoader == null ||
                            pageLoader.mCurPage == null || pageLoader.mCurPage.pageStyle != 0) {
                        if (pageLoader != null && pageLoader.mCurPage != null) {
                            MyToash.ToashError(activity, pageLoader.mCurPage.pageStyle == 1 ? R.string.BookInfoActivity_mark_no1 :
                                    (pageLoader.mCurPage.pageStyle == 3 ? R.string.BookInfoActivity_mark_no2 : R.string.BookInfoActivity_mark_no3));
                        }
                        return;
                    }
                } catch (Exception e) {
                    return;
                }
                if (!yetAddBookMarkBean) {
                    isAddBookMarkBean.setAddTime(System.currentTimeMillis());
                    isAddBookMarkBean.setMark_id(ObjectBoxUtils.addBookMarkBean(isAddBookMarkBean));
                    localBookMarkBeans.add(isAddBookMarkBean);
                    setMarkUi(true, null);
                } else {
                    removeMarks();
                    setMarkUi(false, null);
                    isAddBookMarkBean = null;
                    // 先过滤
                    initData();
                }
                break;
            case R.id.dialog_read_head_bookshelf:
                addBookToLocalShelf();
                break;
            case R.id.dialog_read_head_bookshare:
                dismissAllowingStateLoss();
                MyShare.chapterShare(activity, baseBook.book_id, baseBook.current_chapter_id, 1);
                break;
        }
    }

    @Override
    public void initData() {
        if (pageLoader == null || pageLoader.bookChapter == null ||
                pageLoader.mCurPage == null || pageLoader.mCurPage.pageStyle != 0) {
            return;
        }
        if (pageLoader.mCurPageList != null && !pageLoader.mCurPageList.isEmpty() &&
                        pageLoader.mCurPage.lines != null && !pageLoader.mCurPage.lines.isEmpty()) {
            // 获取章节内容
            String chapterContent = ChapterManager.getInstance().getChapterContent(pageLoader.mCurPageList);
            // 获取该章节的所有书签
            List<BookMarkBean> bookMarkBeanList = ObjectBoxUtils.getChapterBookMarkBeanList(pageLoader.bookChapter.getChapter_id());
            // 获取当前页的内容
            String pageContent = pageLoader.mCurPage.getLineTexts();
            int pageNum = pageLoader.mCurPage.position;
            // 获取区间
            int startCoordinate = chapterContent.indexOf(pageContent);
            int endCoordinate = startCoordinate + pageContent.length() - 1;
            if (bookMarkBeanList != null && !bookMarkBeanList.isEmpty()) {
                localBookMarkBeans.clear();
                // 重新刷新书签
                for (BookMarkBean bookMarkBean : bookMarkBeanList) {
                    if ((pageNum == 0 && bookMarkBean.getPosition() == 0) ||
                            (startCoordinate <= bookMarkBean.getCoordinate() && bookMarkBean.getCoordinate() <= endCoordinate)) {
                        MyToash.Log("read_mark_dialog", startCoordinate + "--"
                                + bookMarkBean.getCoordinate() +  "--" + endCoordinate);
                        localBookMarkBeans.add(bookMarkBean);
                        // 章节首尾
                        setMarkUi(true, bookMarkBean);
                    }
                }
            }
            // 先准备好数据
            if (isAddBookMarkBean == null) {
                isAddBookMarkBean = new BookMarkBean();
                isAddBookMarkBean.setTitle(pageLoader.bookChapter.getChapter_title());
                isAddBookMarkBean.setChapter_id(pageLoader.bookChapter.chapter_id);
                isAddBookMarkBean.setBook_id(baseBook.book_id);
                isAddBookMarkBean.setPosition(pageNum);
                isAddBookMarkBean.setCoordinate(startCoordinate);
                isAddBookMarkBean.setContent(pageLoader.mCurPage.getLineTexts());
            }
        }
    }

    @Override
    public void initInfo(String json) {

    }

    public void addBookToLocalShelf() {
        if (baseBook.is_collect == 0) {
            baseBook.is_collect = 1;
            ObjectBoxUtils.addData(baseBook, Book.class);
            EventBus.getDefault().post(new RefreshShelf(Constant.BOOK_CONSTANT, new RefreshBookSelf(baseBook, 1)));
            dialog_read_head_bookshelf_img.setImageResource(R.mipmap.img_del_bookshel);
            dialog_read_head_bookshelf_title.setText(LanguageUtil.getString(activity, R.string.BookInfoActivity_jiarushujias));
            dialog_read_head_bookshelf_title.setTextColor(ContextCompat.getColor(activity, R.color.gray_9));
            dialog_read_head_bookshelf_title.setEnabled(false);
            MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.BookInfoActivity_jiarushujias));
        }
    }

    /**
     * 设置书签的UI
     *
     * @param isHasMark
     */
    private void setMarkUi(boolean isHasMark, BookMarkBean bookMarkBean) {
        yetAddBookMarkBean = isHasMark;
        if (isHasMark) {
            if (bookMarkBean != null) {
                isAddBookMarkBean = bookMarkBean;
            }
            dialog_read_head_bookmark_img.setImageResource(R.mipmap.img_bookmark_del);
            dialog_read_head_bookmark_title.setText(LanguageUtil.getString(activity, R.string.ReadActivity_delBookMark));
        } else {
            dialog_read_head_bookmark_img.setImageResource(R.mipmap.img_bookmark_add);
            dialog_read_head_bookmark_title.setText(LanguageUtil.getString(activity, R.string.ReadActivity_addBookMark));
        }
    }

    /**
     * 删除书签
     */
    private void removeMarks() {
        if (localBookMarkBeans != null && !localBookMarkBeans.isEmpty()) {
            for (BookMarkBean bookMarkBean : localBookMarkBeans) {
                ObjectBoxUtils.removeMarkBean(bookMarkBean.getMark_id());
            }
            localBookMarkBeans.clear();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dialogDismissListener != null) {
            dialogDismissListener.onDismiss();
        }
    }

    public interface OnDialogDismissListener {

        void onDismiss();
    }
}
