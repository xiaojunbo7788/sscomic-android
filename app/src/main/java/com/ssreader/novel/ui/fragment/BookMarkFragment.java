package com.ssreader.novel.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.eventbus.RefreshBookInfo;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.ui.adapter.BookMarkAdapter;
import com.ssreader.novel.model.BookMarkBean;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

import static com.ssreader.novel.constant.Constant.LOCAL_BOOKID;

public class BookMarkFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView scRecyclerView;
    @BindView(R.id.fragment_option_noresult_text)
    TextView noResultText;
    @BindView(R.id.fragment_option_noresult)
    LinearLayout noResultPop;

    private List<BookMarkBean> bookMarkBeanList = new ArrayList<>();;
    private BookMarkAdapter bookMarkAdapter;
    private Book book;
    private boolean isFromBookRead;

    public BookMarkFragment() {

    }

    public BookMarkFragment(Book book, boolean isFromBookRead) {
        this.book = book;
        this.isFromBookRead = isFromBookRead;
    }

    @Override
    public int initContentView() {
        USE_EventBus=true;
        return R.layout.fragment_readhistory;
    }

    @Override
    public void initView() {
        noResultText.setText(LanguageUtil.getString(activity, R.string.app_mark_no_result));
        initSCRecyclerView(scRecyclerView, RecyclerView.VERTICAL, 0);
        scRecyclerView.setLoadingMoreEnabled(false);
        scRecyclerView.setPullRefreshEnabled(false);
        bookMarkAdapter = new BookMarkAdapter(bookMarkBeanList, activity);
        scRecyclerView.setAdapter(bookMarkAdapter);
        if (book != null) {
            bookMarkBeanList.addAll(ObjectBoxUtils.getBookMarkBeanList(book.book_id));
        }
        if (bookMarkBeanList != null && !bookMarkBeanList.isEmpty()) {
            if(bookMarkBeanList.size() > 1) {
                Collections.sort(bookMarkBeanList);
            }
            setNoResult(false);
            bookMarkAdapter.notifyDataSetChanged();
        } else {
            setNoResult(true);
        }
        initListener();
    }

    private void initListener() {
        bookMarkAdapter.setOnBeanClickListener(new BookMarkAdapter.OnBeanClickListener() {
            @Override
            public void onClick(BookMarkBean bookMarkBean) {
                book.setCurrent_chapter_id(bookMarkBean.getChapter_id());
                ObjectBoxUtils.addData(book, Book.class);
                ChapterManager.getInstance().openBook(activity,book, bookMarkBean);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshBookInfo refresh) {
        if (book.book_id < LOCAL_BOOKID) {
            // 书籍是否相等
            if (refresh.isSave && refresh.book != null && book.book_id == refresh.book.book_id) {
                bookMarkBeanList.clear();
                bookMarkBeanList.addAll(ObjectBoxUtils.getBookMarkBeanList(book.book_id));
                if (bookMarkBeanList != null && !bookMarkBeanList.isEmpty()) {
                    if(bookMarkBeanList.size() > 1) {
                        Collections.sort(bookMarkBeanList);
                    }
                    setNoResult(false);
                    bookMarkAdapter.notifyDataSetChanged();
                } else {
                    setNoResult(true);
                }
            }
        }
    }

    /**
     * 设置没有内容时的UI
     *
     * @param isShow
     */
    private void setNoResult(boolean isShow) {
        if (isShow) {
            scRecyclerView.setVisibility(View.GONE);
            noResultPop.setVisibility(View.VISIBLE);
        } else {
            noResultPop.setVisibility(View.GONE);
            scRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
