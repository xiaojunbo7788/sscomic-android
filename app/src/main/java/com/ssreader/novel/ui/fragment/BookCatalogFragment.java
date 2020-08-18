package com.ssreader.novel.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.eventbus.RefreshPageFactoryChapter;
import com.ssreader.novel.eventbus.RefreshBookInfo;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.model.GetBookChapterList;
import com.ssreader.novel.ui.activity.BookCatalogMarkActivity;
import com.ssreader.novel.ui.adapter.BookChapterAdapter;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.screcyclerview.MyContentLinearLayoutManager;
import com.ssreader.novel.ui.view.screcyclerview.RecycleViewMessageUtil;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.FileManager;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

import static com.ssreader.novel.constant.Constant.AgainTime;
import static com.ssreader.novel.constant.Constant.LOCAL_BOOKID;

public class BookCatalogFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView scRecyclerView;
    @BindView(R.id.fragment_option_noresult)
    LinearLayout noResultLayout;
    @BindView(R.id.fragment_option_noresult_text)
    TextView noResultText;

    private boolean LoadingMoreEnabled;
    private List<BookChapter> localBookChapterList;
    private long mBookId;
    private List<BookChapter> mItemList;
    private BookChapterAdapter mAdapter;
    private Book book;
    private boolean isFromBookRead;

    // 2 = 正序  1 = 倒序
    private int order_by;
    // 是否切换正序倒序
    private boolean Order_by;
    // 当前阅读的章节
    private long chapter_id;
    // 2 = 下拉   1 = 加载更多
    private int scroll_type;
    private MyContentLinearLayoutManager layoutManager;
    private boolean initFirst = true;
    private long ClickTime;

    public BookCatalogFragment(Book book, Boolean isFromBookRead) {
        this.book = book;
        this.isFromBookRead = isFromBookRead;
    }

    public BookCatalogFragment() {
    }

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.public_recycleview;
    }

    @Override
    public void initView() {
        noResultText.setText(LanguageUtil.getString(activity, R.string.app_no_catalog_bean));
        mItemList = new ArrayList<>();
        layoutManager = new MyContentLinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        scRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new BookChapterAdapter(activity, mItemList, scOnItemClickListener);
        scRecyclerView.setAdapter(mAdapter, true);
        initListener();
        if (!InternetUtils.internet(activity)) {
            setNoResultLayout(true);
        }
        // 单独设置图片到的大小
        if (book == null) {
            book = ChapterManager.getInstance().baseBook;
        }
        if (book != null) {
            mBookId = book.book_id;
            mAdapter.current_chapter_id = book.getCurrent_chapter_id();
            chapter_id = book.getCurrent_chapter_id();
            if (!InternetUtils.internet(activity)) {
                // 没有网
                if (!isFromBookRead) {
                    localBookChapterList = ObjectBoxUtils.getBookChapterItemfData(mBookId);
                } else {
                    localBookChapterList = ChapterManager.getInstance().mChapterList;
                }
                if (localBookChapterList != null && !localBookChapterList.isEmpty()) {
                    mItemList.clear();
                    mItemList.addAll(localBookChapterList);
                    mAdapter.NoLinePosition = mItemList.size() - 1;
                    mNotifyDataSetChanged(true);
                }
                http_flag = 1;
            } else {
                if (isFromBookRead) {
                    localBookChapterList = ChapterManager.getInstance().mChapterList;
                    if (localBookChapterList != null && !localBookChapterList.isEmpty()) {
                        http_flag = 1;
                        mItemList.clear();
                        mItemList.addAll(localBookChapterList);
                        mAdapter.NoLinePosition = mItemList.size() - 1;
                        mNotifyDataSetChanged(true);
                        if (mItemList.size() == book.total_chapter) {
                            scRecyclerView.setLoadingMoreEnabled(false);
                        }
                    }
                }
            }
        }
        if (mBookId >= LOCAL_BOOKID) {
            scRecyclerView.setPullRefreshEnabled(false);
            scRecyclerView.setLoadingMoreEnabled(false);
        }
    }

    private void initListener() {
        scRecyclerView.setLoadingListener(new SCRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                scroll_type = 2;
                initData();
            }

            @Override
            public void onLoadMore() {
                scroll_type = 1;
                initData();
            }
        });
    }

    @Override
    public void initData() {
        if (http_flag == 0 && mBookId < LOCAL_BOOKID) {
            if (!mItemList.isEmpty()) {
                if (scroll_type == 2) {
                    if (order_by != 2) {
                        chapter_id = mItemList.get(0).getLast_chapter();
                    } else {
                        chapter_id = mItemList.get(0).getNext_chapter();
                    }
                } else {
                    if (order_by != 2) {
                        chapter_id = mItemList.get(mItemList.size() - 1).getNext_chapter();
                    } else {
                        chapter_id = mItemList.get(mItemList.size() - 1).getLast_chapter();
                    }
                }
                if (chapter_id == 0) {
                    if (scroll_type == 2) {
                        scRecyclerView.refreshComplete();
                    } else if (scroll_type == 1) {
                        scRecyclerView.loadMoreComplete();
                    }
                    return;
                }
            } else {
                scroll_type = 0;
                if (order_by != 0) {
                    chapter_id = 0;
                }
            }
            if (book != null) {
                book.getBookChapterList(activity, chapter_id, scroll_type, order_by, new GetBookChapterList() {
                    @Override
                    public void getBookChapterList(List<BookChapter> bookChapterlist) {

                        if (!bookChapterlist.isEmpty()) {
                            if (initFirst && mItemList.isEmpty() && bookChapterlist.size() < 10) {
                                long lastid = bookChapterlist.get(0).getLast_chapter();
                                if (lastid != 0) {
                                    book.getBookChapterList(activity, lastid, 2, 0, new GetBookChapterList() {
                                        @Override
                                        public void getBookChapterList(List<BookChapter> bookChapterlist1) {
                                            if (!bookChapterlist1.isEmpty()) {
                                                bookChapterlist.addAll(0, bookChapterlist1);
                                                lordMoreHandData(bookChapterlist);
                                            } else {
                                                lordMoreHandData(bookChapterlist);
                                            }
                                        }
                                    });
                                } else {
                                    lordMoreHandData(bookChapterlist);
                                }
                            } else {
                                lordMoreHandData(bookChapterlist);
                            }
                            if (Order_by) {
                                RecycleViewMessageUtil.MoveToPosition(layoutManager, scRecyclerView, 0);
                                Order_by = false;
                                if(isOrderChange!=null) {
                                    isOrderChange.isOrderChange(true);
                                }
                            }
                        }

                        if (scroll_type == 2) {
                            scRecyclerView.refreshComplete();
                        } else if (scroll_type == 1) {
                            scRecyclerView.loadMoreComplete();
                        }
                        initFirst = false;
                    }
                });
            }
        }
        http_flag = 0;
    }

    private void lordMoreHandData(List<BookChapter> bookChapterList) {
        if (bookChapterList == null || bookChapterList.isEmpty()) {
            return;
        }
        if (scroll_type == 2) {
            mItemList.addAll(0, bookChapterList);
        } else if (scroll_type == 1) {
            mItemList.addAll(bookChapterList);
        } else {
            mItemList.addAll(bookChapterList);
        }

        if (!mItemList.isEmpty()) {
            if (order_by != 2) {
                scRecyclerView.setPullRefreshEnabled(mItemList.get(0).getLast_chapter() != 0);
                LoadingMoreEnabled = mItemList.get(mItemList.size() - 1).getNext_chapter() != 0;
                scRecyclerView.setLoadingMoreEnabled(LoadingMoreEnabled);
                if (!LoadingMoreEnabled) {
                    mAdapter.NoLinePosition = mItemList.size() - 1;
                    scRecyclerView.setLoadingMoreEnabled(false);
                } else {
                    mAdapter.NoLinePosition = -1;
                    scRecyclerView.setLoadingMoreEnabled(true);
                }
            } else {
                LoadingMoreEnabled = mItemList.get(mItemList.size() - 1).getLast_chapter() != 0;
                scRecyclerView.setLoadingMoreEnabled(LoadingMoreEnabled);
                scRecyclerView.setPullRefreshEnabled(mItemList.get(0).getNext_chapter() != 0);
                if (!LoadingMoreEnabled) {
                    scRecyclerView.setLoadingMoreEnabled(false);
                    mAdapter.NoLinePosition = mItemList.size() - 1;
                } else {
                    mAdapter.NoLinePosition = -1;
                    scRecyclerView.setLoadingMoreEnabled(true);
                }
            }
        }

        if (scroll_type == 2) {
            mNotifyDataSetChanged(true);
        } else if (scroll_type == 1) {
            setNoResultLayout(false);
            mAdapter.notifyDataSetChanged();
        } else {
            mNotifyDataSetChanged(true);
        }
    }

    @Override
    public void initInfo(String json) {

    }
    private  BookCatalogMarkActivity.IsOrderChange isOrderChange;
    public void setOrder(BookCatalogMarkActivity.IsOrderChange isOrderChange) {
        this.isOrderChange=isOrderChange;
        if (!mItemList.isEmpty()) {
            if(!isFromBookRead) {
                Order_by = true;
                order_by = order_by == 2 ? 1 : 2;
                mItemList.clear();
                initData();
               // OrderChange = order_by == 2;
            }else {
                Collections.reverse(mItemList);
                mAdapter.notifyDataSetChanged();
                RecycleViewMessageUtil.MoveToPosition(layoutManager, scRecyclerView, 0);
                isOrderChange.isOrderChange(true);
            }
        } else {
           // OrderChange = false;
            isOrderChange.isOrderChange(false);
        }
    }

    SCOnItemClickListener scOnItemClickListener = new SCOnItemClickListener<BookChapter>() {
        @Override
        public void OnItemClickListener(int flag, int position, BookChapter bookChapter) {
            long ClickTimeNew = System.currentTimeMillis();
            if (ClickTimeNew - ClickTime > AgainTime) {
                ClickTime = ClickTimeNew;
                boolean PathIsExist = FileManager.isExistLocalBookTxtPath(bookChapter);
                if (PathIsExist || InternetUtils.internet(activity)) {
                    book.current_chapter_displayOrder = bookChapter.display_order;
                    book.setCurrent_chapter_id(bookChapter.getChapter_id());
                    ObjectBoxUtils.addData(book, Book.class);
                    if (order_by == 2) {
                        Collections.reverse(mItemList);
                    }
                    bookChapter.setPagePos(0);
                    ObjectBoxUtils.addData(bookChapter, BookChapter.class);
                    if (!isFromBookRead) {
                        ChapterManager.getInstance().openBook(activity, book, mItemList);
                    } else {
                        EventBus.getDefault().post(new RefreshPageFactoryChapter(mItemList, bookChapter));
                    }
                } else {
                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.splashactivity_nonet));
                }
            }

        }

        @Override
        public void OnItemLongClickListener(int flag, int position, BookChapter o) {

        }
    };


    /**
     * 刷新目录数据
     *
     * @param refresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshBookInfo refresh) {
        if (mBookId < LOCAL_BOOKID) {
            // 书籍是否相等
            if (refresh.isSave && refresh.book != null && book.book_id == refresh.book.book_id) {
                useManagerData();
            }
        }
    }


    /**
     * 此时可以确定阅读界面已经打开
     * 用于刷新目录条目
     */
    private void useManagerData() {
        mAdapter.current_chapter_id = ChapterManager.getInstance().mCurrentChapter.chapter_id;
        chapter_id = book.current_chapter_id = mAdapter.current_chapter_id;
        mItemList.clear();
        mItemList.addAll(ChapterManager.getInstance().mChapterList);
        if (order_by == 2) {
            Collections.reverse(mItemList);
        }
        if (!mItemList.isEmpty()) {
            if (order_by != 2) {
                scRecyclerView.setPullRefreshEnabled(mItemList.get(0).getLast_chapter() != 0);
                LoadingMoreEnabled = mItemList.get(mItemList.size() - 1).getNext_chapter() != 0;
                if (!LoadingMoreEnabled) {
                    mAdapter.NoLinePosition = mItemList.size() - 1;

                    scRecyclerView.setLoadingMoreEnabled(false);
                } else {
                    mAdapter.NoLinePosition = -1;
                    scRecyclerView.setLoadingMoreEnabled(true);
                }
                scRecyclerView.setLoadingMoreEnabled(LoadingMoreEnabled);
            } else {
                LoadingMoreEnabled = mItemList.get(mItemList.size() - 1).getLast_chapter() != 0;
                scRecyclerView.setLoadingMoreEnabled(LoadingMoreEnabled);
                scRecyclerView.setPullRefreshEnabled(mItemList.get(0).getNext_chapter() != 0);
                if (!LoadingMoreEnabled) {
                    scRecyclerView.setLoadingMoreEnabled(false);
                    mAdapter.NoLinePosition = mItemList.size() - 1;
                } else {
                    mAdapter.NoLinePosition = -1;
                    scRecyclerView.setLoadingMoreEnabled(true);
                }
            }
            mNotifyDataSetChanged(true);
        }
    }

    private void mNotifyDataSetChanged(boolean isChange) {
        setNoResultLayout(false);
        mAdapter.notifyDataSetChanged();
        if (!mItemList.isEmpty() && isChange) {
            int mDisplayOrder = 0;
            for (BookChapter bookChapter : mItemList) {
                mDisplayOrder++;
                if (bookChapter.chapter_id == chapter_id) {
                    if (mDisplayOrder > 10) {
                        RecycleViewMessageUtil.MoveToPosition(layoutManager, scRecyclerView, mDisplayOrder);
                    }
                    return;
                }
            }
        }
    }

    private void setNoResultLayout(boolean isEmpty) {
        if (isEmpty) {
            noResultLayout.setVisibility(View.VISIBLE);
            scRecyclerView.setVisibility(View.GONE);
        } else {
            scRecyclerView.setVisibility(View.VISIBLE);
            noResultLayout.setVisibility(View.GONE);
        }
    }
}
