package com.ssreader.novel.ui.localshell.localapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.model.BookComicStoare;
import com.ssreader.novel.model.PublicIntent;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.WebViewActivity;
import com.ssreader.novel.ui.adapter.PublicMainAdapter;
import com.ssreader.novel.ui.localshell.adapter.BookListAdapter;
import com.ssreader.novel.ui.localshell.filesearcher.FileSearcher;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoaderInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.LOCAL_BOOKID;

public class LocalShelfFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView publicRecycleview;
    @BindView(R.id.fragment_novel_allchoose)
    TextView fragment_novel_allchoose;
    @BindView(R.id.shelf_Book_delete_del)
    TextView mDeleteBtn;
    @BindView(R.id.fragment_novel_cancle)
    TextView fragmentNovelCancle;
    @BindView(R.id.shelf_Book_delete_btn)
    LinearLayout shelfBookDeleteBtn;

    private ViewHolder viewHolder;
    private View activity_main_RadioGroup;
    private List<LocalBook> books;
    private BookListAdapter mAdapter;
    private List<LocalBook> checkedBookList = new ArrayList<LocalBook>();
    public int LocalShelfType;
    public boolean isAdd;
    public LocalShelfFragment(View activity_main_RadioGroup) {
        this.activity_main_RadioGroup = activity_main_RadioGroup;
    }

    public LocalShelfFragment() {
    }

    public LocalShelfFragment(int LocalShelfType, View activity_main_RadioGroup) {
        this.LocalShelfType = LocalShelfType;
        this.activity_main_RadioGroup = activity_main_RadioGroup;
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_shelf;
    }

    @OnClick({R.id.fragment_novel_allchoose, R.id.shelf_Book_delete_del, R.id.fragment_novel_cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_novel_allchoose:
                boolean selectAll = (checkedBookList.size() == books.size());
                checkedBookList.clear();
                if (!selectAll) {
                    checkedBookList.addAll(books);
                }
                mAdapter.notifyDataSetChanged();
                refreshBtn();
                break;
            case R.id.shelf_Book_delete_del:
                if (!checkedBookList.isEmpty()) {
                    ObjectBoxUtils.deleteData(checkedBookList, LocalBook.class);
                    books.removeAll(checkedBookList);
                    deleteRefarsh();
                }
                break;
            case R.id.fragment_novel_cancle:
                deleteRefarsh();
                break;
        }
    }

    private void deleteRefarsh() {
        mAdapter.notifyDataSetChanged();
        checkedBookList.clear();
        shelfBookDeleteBtn.setVisibility(View.GONE);
        mAdapter.isDeleteButtonVisible = false;
        activity_main_RadioGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public void initView() {
        publicRecycleview.setPullRefreshEnabled(false);
        publicRecycleview.setLoadingMoreEnabled(false);
        publicRecycleview.setPadding(ImageUtil.dp2px(activity, 8), ImageUtil.dp2px(activity, 15),
                ImageUtil.dp2px(activity, 8), 0);
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_local_head_shelf_banner, null);
        viewHolder = new ViewHolder(view);
        publicRecycleview.addHeaderView(view);
        books = ObjectBoxUtils.getLocalBookShelfData();
        if (books != null && books.size() > 0) {
            isAdd = false;
            viewHolder.fragment_head_addbook_layout.setVisibility(View.GONE);
            for (LocalBook book : books) {
                if (book.isLike == 1) {
                    checkedBookList.add(book);
                }
            }
        } else {
            isAdd = true;
            viewHolder.fragment_head_addbook.setBackground(MyShape.setMyshape(activity, ImageUtil.dp2px(activity, 4), ImageUtil.dp2px(activity, 4), ImageUtil.dp2px(activity, 4), ImageUtil.dp2px(activity, 4), 1, ContextCompat.getColor(activity, R.color.black_3), 0));
            viewHolder.fragment_head_addbook_layout.setVisibility(View.VISIBLE);
        }
        if (LocalShelfType == 0) {
            initSCRecyclerView(publicRecycleview, RecyclerView.VERTICAL, 3);
        } else {
            viewHolder.fragment_head_addbook_layout.setVisibility(View.GONE);
            initSCRecyclerView(publicRecycleview, RecyclerView.VERTICAL, 0);
        }
        MyToash.Log("BookListAdapter",isAdd+"/////////");
        mAdapter = new BookListAdapter(activity, books, isAdd, checkedBookList, new SCOnItemClickListener<LocalBook>() {
            @Override
            public void OnItemClickListener(int flag, int position, LocalBook localBook) {
                if (!mAdapter.isDeleteButtonVisible) {
                    Book book = ObjectBoxUtils.getBook(localBook.book_id);
                    if (book == null) {
                        book = new Book();
                        book.name = localBook.name.substring(0, Math.min(5, localBook.name.length()));
                        book.book_id = LOCAL_BOOKID + localBook.book_id;
                        book.setCurrent_chapter_id(0);
                        ObjectBoxUtils.addData(book, Book.class);
                    }
                    BookChapter querychapterItem = ObjectBoxUtils.getBookChapter(book.book_id);
                    if (querychapterItem == null) {
                        querychapterItem = new BookChapter();
                        querychapterItem.chapter_id = book.book_id;
                        querychapterItem.setChapter_id(0);
                        querychapterItem.setChapter_title("第一章");
                        querychapterItem.setIs_vip(0);
                        querychapterItem.setIs_preview(0);
                        querychapterItem.setDisplay_order(0);
                        querychapterItem.setChapteritem_begin(0);
                        querychapterItem.setBook_id(book.book_id);
                        querychapterItem.setChapter_path(localBook.Local_path);
                        querychapterItem.next_chapter = 0;
                        querychapterItem.last_chapter = 0;
                        ObjectBoxUtils.addData(querychapterItem, BookChapter.class);
                    }
                    List<BookChapter> updateList = new ArrayList<>();
                    updateList.add(querychapterItem);
                    ChapterManager.getInstance().openBook(activity, book, updateList);
                } else {
                    if (checkedBookList.contains(localBook)) {
                        checkedBookList.remove(localBook);
                    } else {
                        checkedBookList.add(localBook);
                    }
                    refreshBtn();
                }
            }

            @Override
            public void OnItemLongClickListener(int flag, int position, LocalBook localBook) {
                mAdapter.isDeleteButtonVisible = true;
                shelfBookDeleteBtn.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
                activity_main_RadioGroup.setVisibility(View.GONE);
            }
        });
        publicRecycleview.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        if (LocalShelfType == 0) {
            List<String> imagesUrl = new ArrayList<>();
            imagesUrl.add("https://goss.cfp.cn/creative/vcg/800/new/VCG211275127430.jpg?x-oss-process=image/format,jpg/interlace,1");
            imagesUrl.add("https://goss.cfp.cn/creative/vcg/800/new/VCG211205101912.jpg?x-oss-process=image/format,jpg/interlace,1");
            // 设置banner样式
            viewHolder.banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
            viewHolder.banner.setDelayTime(5000);
            // 设置图片加载器
            viewHolder.banner.setImageLoader(new ImageLoaderInterface() {
                @Override
                public void displayImage(Context context, Object path, View imageView) {
                    Glide.with(activity).load(path).into((ImageView) imageView);
                }

                @Override
                public View createImageView(Context context) {
                    return null;
                }
            });
            // 设置图片集合
            viewHolder.banner.setImages(imagesUrl);
            // 设置banner动画效果
            viewHolder.banner.setBannerAnimation(Transformer.Default);
            // 点击事件
            viewHolder.banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    Intent intent = new Intent();
                    intent.setClass(activity, WebViewActivity.class);
                    intent.putExtra("flag", "yinsi");
                    switch (position) {
                        case 0:
                            intent.putExtra("title", "实时疫情");
                            intent.putExtra("url", "https://voice.baidu.com/act/newpneumonia/newpneumonia/?from=osari_pc_1");
                            break;
                        case 1:
                            intent.putExtra("title", "新型冠状病毒肺炎");
                            intent.putExtra("url", "https://www.baidu.com/bh/dict/ydxx_8829639274010099959?contentid=ydxx_8829639274010099959&frsrcid=search_fy&from=dicta");
                            break;
                    }
                    startActivity(intent);
                }
            });
            //banner设置方法全部调用完毕时最后调用
            viewHolder.banner.start();
        } else {
            ReaderParams readerParams = new ReaderParams(activity);
            readerParams.putExtraParams("channel_id", 1);
            HttpUtils.getInstance().sendRequestRequestParams(activity,Api.mBookStoreUrl, readerParams.generateParamsJson(), responseListener);
        }
    }

    List<String> bannerList;

    @Override
    public void initInfo(String json) {
        if (LocalShelfType == 1) {
            bannerList = new ArrayList<>();
            BookComicStoare bookStoare = gson.fromJson(json, BookComicStoare.class);
            List<PublicIntent> banner = bookStoare.getBanner();
            for (PublicIntent publicIntent : banner) {
                bannerList.add(publicIntent.image);
            }
            viewHolder.banner
                    .setDelayTime(5000)
                    .setImages(bannerList)
                    .setBannerStyle(BannerConfig.NOT_INDICATOR)
                    .setBannerAnimation(Transformer.Default)
                    .setImageLoader(new ImageLoaderInterface() {
                        @Override
                        public void displayImage(Context context, Object path, View imageView) {
                            Glide.with(activity).load(path).into((ImageView) imageView);
                        }

                        @Override
                        public View createImageView(Context context) {
                            return null;
                        }
                    })
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            banner.get(position).intentBannerTo(activity);
                        }
                    }).start();
            PublicMainAdapter publicMainAdapter = new PublicMainAdapter(bookStoare.getLabel(), Constant.BOOK_CONSTANT, activity);
            publicRecycleview.setAdapter(publicMainAdapter);
            publicMainAdapter.notifyDataSetChanged();

        }
    }

    public void refreshBtn() {
        int size = checkedBookList.size();
        if (size == books.size()) {
            fragment_novel_allchoose.setText(LanguageUtil.getString(activity, R.string.app_cancel_select_all));
        } else {
            fragment_novel_allchoose.setText(LanguageUtil.getString(activity, R.string.app_allchoose));
        }
        if (size == 0) {
            mDeleteBtn.setClickable(false);
            mDeleteBtn.setBackgroundColor(ContextCompat.getColor(activity, R.color.graybg));
            mDeleteBtn.setTextColor(Color.GRAY);
            mDeleteBtn.setText(String.format(LanguageUtil.getString(activity, R.string.app_delete_plural), 0));

        } else {
            mDeleteBtn.setClickable(true);
            mDeleteBtn.setText(String.format(LanguageUtil.getString(activity, R.string.app_delete_plural), size));
            mDeleteBtn.setTextColor(Color.WHITE);
            mDeleteBtn.setBackgroundColor(Color.RED);
        }
    }

    public class ViewHolder {

        @BindView(R.id.local_shelf_banner)
        Banner banner;
        @BindView(R.id.fragment_head_addbook)
        TextView fragment_head_addbook;
        @BindView(R.id.fragment_head_addbook_layout)
        View fragment_head_addbook_layout;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.fragment_head_addbook})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fragment_head_addbook:
                    new FileSearcher(activity)
                            .withExtension("txt")
                            .withSizeLimit(10 * 1024, -1)
                            .search(new FileSearcher.FileSearcherCallback() {
                                @Override
                                public void onSelect(final List<File> files) {
                                    addBookFromFile(activity, files);
                                }
                            });
                    break;
            }
        }
    }


    public void addBookFromFile(final Activity context, final List<File> files) {
        final List<LocalBook> newDataList = new ArrayList<>();
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("处理中");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (File file : files) {
                    LocalBook book = new LocalBook(file.getName(), file.getPath());
                    if (!books.contains(book)) {
                        newDataList.add(book);
                    }
                }
                if (newDataList.isEmpty()) {
                    dialog.cancel();
                    return;
                }
                ObjectBoxUtils.addData(newDataList, LocalBook.class);
                books.clear();
                books.addAll(ObjectBoxUtils.getLocalBookShelfData());
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isAdd = false;
                        viewHolder.fragment_head_addbook_layout.setVisibility(View.GONE);
                        mAdapter = new BookListAdapter(activity, books, isAdd, checkedBookList, new SCOnItemClickListener<LocalBook>() {
                            @Override
                            public void OnItemClickListener(int flag, int position, LocalBook localBook) {
                                if (!mAdapter.isDeleteButtonVisible) {
                                    Book book = ObjectBoxUtils.getBook(localBook.book_id);
                                    if (book == null) {
                                        book = new Book();
                                        book.name = localBook.name.substring(0, Math.min(5, localBook.name.length()));
                                        book.book_id = LOCAL_BOOKID + localBook.book_id;
                                        book.setCurrent_chapter_id(0);
                                        ObjectBoxUtils.addData(book, Book.class);
                                    }
                                    BookChapter querychapterItem = ObjectBoxUtils.getBookChapter(book.book_id);
                                    if (querychapterItem == null) {
                                        querychapterItem = new BookChapter();
                                        querychapterItem.chapter_id = book.book_id;
                                        querychapterItem.setChapter_id(0);
                                        querychapterItem.setChapter_title("第一章");
                                        querychapterItem.setIs_vip(0);
                                        querychapterItem.setIs_preview(0);
                                        querychapterItem.setDisplay_order(0);
                                        querychapterItem.setChapteritem_begin(0);
                                        querychapterItem.setBook_id(book.book_id);
                                        querychapterItem.setChapter_path(localBook.Local_path);
                                        querychapterItem.next_chapter = 0;
                                        querychapterItem.last_chapter = 0;
                                        ObjectBoxUtils.addData(querychapterItem, BookChapter.class);
                                    }
                                    List<BookChapter> updateList = new ArrayList<>();
                                    updateList.add(querychapterItem);
                                    ChapterManager.getInstance().openBook(activity, book, updateList);
                                } else {
                                    if (checkedBookList.contains(localBook)) {
                                        checkedBookList.remove(localBook);
                                    } else {
                                        checkedBookList.add(localBook);
                                    }
                                    refreshBtn();
                                }
                            }

                            @Override
                            public void OnItemLongClickListener(int flag, int position, LocalBook localBook) {
                                mAdapter.isDeleteButtonVisible = true;
                                shelfBookDeleteBtn.setVisibility(View.VISIBLE);
                                mAdapter.notifyDataSetChanged();
                                activity_main_RadioGroup.setVisibility(View.GONE);
                            }
                        });
                        publicRecycleview.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                dialog.cancel();
            }
        }).start();
    }

}
