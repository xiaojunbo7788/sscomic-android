package com.ssreader.novel.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.eventbus.RefreshAudioShelf;
import com.ssreader.novel.eventbus.RefreshBookSelf;
import com.ssreader.novel.eventbus.RefreshComicShelf;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.eventbus.RefreshShelf;
import com.ssreader.novel.eventbus.RefreshShelfCurrent;
import com.ssreader.novel.eventbus.ShelfDeleteRefresh;
import com.ssreader.novel.eventbus.ToStore;
import com.ssreader.novel.model.Announce;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.AudioChapter;
import com.ssreader.novel.model.BaseBookComic;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.Comic;
import com.ssreader.novel.model.PublicIntent;
import com.ssreader.novel.model.ShelfAudioBeen;
import com.ssreader.novel.model.ShelfBookBeen;
import com.ssreader.novel.model.ShelfComicBeen;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.MainHttpTask;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.AnnounceActivity;
import com.ssreader.novel.ui.activity.AudioInfoActivity;
import com.ssreader.novel.ui.activity.BookInfoActivity;
import com.ssreader.novel.ui.activity.ComicInfoActivity;
import com.ssreader.novel.ui.activity.ComicLookActivity;
import com.ssreader.novel.ui.activity.LoginActivity;
import com.ssreader.novel.ui.activity.TaskCenterActivity;
import com.ssreader.novel.ui.adapter.ShelfAdapter;
import com.ssreader.novel.ui.adapter.ShelfBannerHolderView;
import com.ssreader.novel.ui.audio.fragment.AudioInfoCatalogFragment;
import com.ssreader.novel.ui.audio.manager.AudioManager;
import com.ssreader.novel.ui.dialog.TaskCenterSignPopupWindow;
import com.ssreader.novel.ui.dialog.WaitDialog;
import com.ssreader.novel.ui.audio.AudioSoundActivity;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.MarqueeTextView;
import com.ssreader.novel.ui.view.MarqueeTextViewClickListener;
import com.ssreader.novel.ui.view.banner.ConvenientBanner;
import com.ssreader.novel.ui.view.banner.holder.CBViewHolderCreator;
import com.ssreader.novel.ui.view.banner.listener.OnItemClickListener;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.AgainTime;
import static com.ssreader.novel.constant.Constant.BOOK_CONSTANT;
import static com.ssreader.novel.constant.Constant.COMIC_CONSTANT;
import static com.ssreader.novel.constant.Constant.AUDIO_CONSTANT;

public class ShelfFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView publicRecycleview;
    @BindView(R.id.fragment_novel_allchoose)
    TextView fragmentNovelAllchoose;
    @BindView(R.id.shelf_Book_delete_del)
    TextView shelfBookDeleteDel;
    @BindView(R.id.shelf_Book_delete_btn)
    LinearLayout shelfBookDeleteBtn;

    private ShelfAdapter shelfAdapter;
    private List<Object> objectList;
    private List<Book> BookList;
    private List<Comic> comicList;
    private List<Audio> audioList;
    private String product;
    private ViewHolder viewHolder;
    private View viewHead;
    private long lod_time, ClickTime;

    private int productType;
    private int top_height;
    private int openPosition;
    private boolean auto_add;

    public ShelfFragment() {

    }

    public ShelfFragment(int productType, int top_height) {
        this.productType = productType;
        if (top_height != 0) {
            this.top_height = top_height;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            productType = savedInstanceState.getInt("productType");
            if (savedInstanceState.getInt("top_height") != 0) {
                top_height = savedInstanceState.getInt("top_height");
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("productType", productType);
        outState.putInt("top_height", top_height);
        super.onSaveInstanceState(outState);
    }

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.fragment_shelf;
    }

    @Override
    public void initView() {
        initSCRecyclerView(publicRecycleview, RecyclerView.VERTICAL, 3);
        publicRecycleview.setLoadingMoreEnabled(false);
        publicRecycleview.setPullRefreshEnabled(true);
        viewHead = LayoutInflater.from(activity).inflate(R.layout.item_shelf_head, null);
        publicRecycleview.setPadding(ImageUtil.dp2px(activity, 8), top_height, ImageUtil.dp2px(activity, 8), 0);
        viewHolder = new ViewHolder(viewHead);
        publicRecycleview.addHeaderView(viewHead);
        objectList = new ArrayList<>();
        if (productType == BOOK_CONSTANT) {
            BookList = new ArrayList<>();
            product = "ShelfBook";
            http_URL = Api.mBookCollectUrl;
            BookList.addAll(ObjectBoxUtils.getBookShelfData());
            Collections.sort(BookList);
            objectList.addAll(BookList);
        } else if (productType == COMIC_CONSTANT) {
            http_URL = Api.COMIC_SHELF;
            comicList = new ArrayList<>();
            product = "ShelfComic";
            comicList.addAll(ObjectBoxUtils.getComicShelfData());
            Collections.sort(comicList);
            objectList.addAll(comicList);
        } else if (productType == AUDIO_CONSTANT) {
            http_URL = Api.AUDIO_FAV_USER_FAV;
            audioList = new ArrayList<>();
            product = "ShelfAudio";
            audioList.addAll(ObjectBoxUtils.getAudioShelfData());
            Collections.sort(audioList);// 排序
            objectList.addAll(audioList);
        }
        shelfAdapter = new ShelfAdapter(activity, objectList, productType,
                shelfBookDeleteDel, scOnItemClickListener, fragmentNovelAllchoose);
        publicRecycleview.setAdapter(shelfAdapter);
    }

    @Override
    public void initData() {
        MainHttpTask.getInstance().getResultString(activity, http_flag != 0, product, new MainHttpTask.GetHttpData() {
            @Override
            public void getHttpData(String result) {
                http_flag = 1;
                responseListener.onResponse(result);
            }
        });
    }

    @Override
    public void initInfo(String json) {
        if (http_flag == -2) {
            EventBus.getDefault().post(new RefreshMine(1));
        } else {
            if (http_flag != -1) {
                updateData(json);
            }
        }
    }

    @OnClick({R.id.fragment_novel_allchoose, R.id.shelf_Book_delete_del, R.id.fragment_novel_cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_novel_allchoose:
                shelfAdapter.selectAll();
                break;
            case R.id.shelf_Book_delete_del:
                WaitDialog waitDialog = new WaitDialog(activity, 0).ShowDialog(true);
                StringBuilder builder = new StringBuilder();
                for (Object o : shelfAdapter.checkedBookList) {
                    if (productType == BOOK_CONSTANT) {
                        Book book = (Book) o;
                        book.is_collect = 0;
                        book.setRecommend(false);
                        ObjectBoxUtils.addData(book, Book.class);
                        builder.append("," + book.getbook_id());
                    } else if (productType == COMIC_CONSTANT) {
                        Comic comic = (Comic) o;
                        comic.is_collect = 0;
                        comic.isRecommend = false;
                        ObjectBoxUtils.addData(comic, Comic.class);
                        builder.append("," + comic.getComic_id());
                    } else if (productType == AUDIO_CONSTANT) {
                        Audio audio = (Audio) o;
                        audio.is_collect = 0;
                        audio.isRecommend = false;
                        ObjectBoxUtils.addData(audio, Audio.class);
                        builder.append("," + audio.getAudio_id());
                    }
                }
                objectList.removeAll(shelfAdapter.checkedBookList);
                if (productType == BOOK_CONSTANT) {
                    BookList.removeAll(shelfAdapter.checkedBookList);
                } else if (productType == COMIC_CONSTANT) {
                    comicList.removeAll(shelfAdapter.checkedBookList);
                } else if (productType == AUDIO_CONSTANT) {
                    audioList.removeAll(shelfAdapter.checkedBookList);
                }
                shelfAdapter.checkedBookList.clear();
                if (UserUtils.isLogin(activity)) {
                    http_flag = -1;
                    readerParams = new ReaderParams(activity);
                    String str = builder.toString();
                    String BookIdArr = str.substring(1);
                    if (productType == BOOK_CONSTANT) {
                        readerParams.putExtraParams("book_id", BookIdArr);
                        HttpUtils.getInstance().sendRequestRequestParams(activity, Api.mBookDelCollectUrl, readerParams.generateParamsJson(), responseListener);
                    } else if (productType == COMIC_CONSTANT) {
                        readerParams.putExtraParams("comic_id", BookIdArr);
                        HttpUtils.getInstance().sendRequestRequestParams(activity, Api.COMIC_SHELF_DEL, readerParams.generateParamsJson(), responseListener);
                    } else if (productType == AUDIO_CONSTANT) {
                        readerParams.putExtraParams("audio_id", BookIdArr);
                        HttpUtils.getInstance().sendRequestRequestParams(activity, Api.AUDIO_FAV_DEL, readerParams.generateParamsJson(), responseListener);
                    }

                }
                waitDialog.ShowDialog(false);
                setCancelDelete();
                break;
            case R.id.fragment_novel_cancle:
                setCancelDelete();
                break;
        }
    }

    /**
     * 取消删除
     */
    private void setCancelDelete() {
        if (shelfAdapter != null && shelfAdapter.mIsDeletable) {
            shelfAdapter.setDelStatus(false);
            if (shelfBookDeleteBtn.getVisibility() == View.VISIBLE) {
                shelfBookDeleteBtn.setVisibility(View.GONE);
            }
            if (viewHead == null) {
                viewHead = LayoutInflater.from(activity).inflate(R.layout.item_shelf_head, null);
            }
            publicRecycleview.addHeaderView(viewHead);
        }
    }

    public void setToTop() {
        if (productType == BOOK_CONSTANT) {
            Book mTopBook = BookList.get(openPosition);
            mTopBook.setBookselfPosition(10000);
            ObjectBoxUtils.addData(mTopBook, Book.class);
            Book old_book = BookList.get(0);
            old_book.setBookselfPosition(0);
            ObjectBoxUtils.addData(old_book, Book.class);
            BookList.add(0, mTopBook);
            BookList.remove(openPosition + 1);
            objectList.add(0, mTopBook);
            objectList.remove(openPosition + 1);
        } else if (productType == COMIC_CONSTANT) {
            Comic mTopBook = comicList.get(openPosition);
            mTopBook.setBookselfPosition(10000);
            ObjectBoxUtils.addData(mTopBook, Comic.class);
            Comic old_book = comicList.get(0);
            old_book.setBookselfPosition(0);
            ObjectBoxUtils.addData(old_book, Comic.class);
            comicList.add(0, mTopBook);
            comicList.remove(openPosition + 1);
            objectList.add(0, mTopBook);
            objectList.remove(openPosition + 1);
        } else if (productType == AUDIO_CONSTANT) {
            Audio mTopBook = audioList.get(openPosition);
            mTopBook.setBookselfPosition(10000);
            ObjectBoxUtils.addData(mTopBook, Audio.class);
            Audio old_book = audioList.get(0);
            old_book.setBookselfPosition(0);
            ObjectBoxUtils.addData(old_book, Audio.class);
            audioList.add(0, mTopBook);
            audioList.remove(openPosition + 1);
            objectList.add(0, mTopBook);
            objectList.remove(openPosition + 1);
        }
        shelfAdapter.notifyDataSetChanged();
    }

    private SCOnItemClickListener scOnItemClickListener = new SCOnItemClickListener() {
        @Override
        public void OnItemClickListener(int flag, int position, Object o) {
            long ClickTimeNew = System.currentTimeMillis();
            if (ClickTimeNew - ClickTime > AgainTime) {
                ClickTime = ClickTimeNew;
                openPosition = position;
                switch (flag) {
                    case -1://删除
                        shelfBookDeleteBtn.setVisibility(View.VISIBLE);
                        publicRecycleview.removeHeaderView(viewHead);
                        break;
                    case 1:
                        // 打开书籍
                        if (productType == BOOK_CONSTANT) {
                            Book book = (Book) o;
                            if (book.isRecommend) {
                                book.isRecommend = false;
                                shelfAdapter.notifyDataSetChanged();
                            }
                            ChapterManager.getInstance().openBook(activity, book);
                        } else if (productType == COMIC_CONSTANT) {
                            Comic comic = (Comic) o;
                            if (comic.isRecommend) {
                                comic.isRecommend = false;
                                shelfAdapter.notifyDataSetChanged();
                            }
                            Intent intent = new Intent(activity, ComicLookActivity.class);
                            intent.putExtra("baseComic", (Comic) o);
                            startActivity(intent);
                        } else if (productType == AUDIO_CONSTANT) {
                            // 先判断是否有目录
                            Audio audio = (Audio) o;
                            if (audio.isRecommend) {
                                audio.isRecommend = false;
                                shelfAdapter.notifyDataSetChanged();
                            }
                            audio.getAudioChapterList(activity, new AudioInfoCatalogFragment.GetAudioChapterList() {
                                @Override
                                public void getAudioChapterList(List<AudioChapter> audioChapters) {
                                    if (audioChapters == null || audioChapters.isEmpty()) {
                                        MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.chapterupdateing));
                                    } else {
                                        // 调起服务
                                        AudioManager.openService(activity);
                                        // 进入有声界面
                                        Intent audioIntent = new Intent();
                                        audioIntent.putExtra("audio", audio);
                                        audioIntent.putExtra("special_click", true);
                                        audioIntent.setClass(activity, AudioSoundActivity.class);
                                        startActivity(audioIntent);
                                        getActivity().overridePendingTransition(R.anim.activity_bottom_top_open, R.anim.activity_stay);
                                    }
                                }
                            });
                        }
                        break;
                    case 2:
                        // 跳转书城
                        EventBus.getDefault().post(new ToStore(productType));
                        break;
                }
            }
        }

        @Override
        public void OnItemLongClickListener(int flag, int position, Object o) {

        }
    };

    /**
     * 设置数据
     *
     * @param result
     */
    private void updateData(String result) {
        if (productType == BOOK_CONSTANT) {
            ShelfBookBeen shelfBookBeen = gson.fromJson(result, ShelfBookBeen.class);
            // 书架顶部轮播
            initBannerAnnounce(shelfBookBeen.announcement, shelfBookBeen.recommend_list);
            if (shelfBookBeen.list != null && !shelfBookBeen.list.isEmpty()) {
                for (Book book : shelfBookBeen.list) {
                    book.is_collect = 1;
                    int position = BookList.indexOf(book);
                    if (position != -1) {
                        Book LocalBook = BookList.get(position);
                        LocalBook.new_chapter = book.total_chapter - LocalBook.total_chapter;
                        LocalBook.total_chapter = book.total_chapter;
                        LocalBook.cover = book.cover;
                        LocalBook.author = book.author.replaceAll(",", " ");
                        ObjectBoxUtils.addData(LocalBook, Book.class);
                    } else {
                        objectList.add(book);
                        BookList.add(book);
                        ObjectBoxUtils.addData(book, Book.class);
                    }
                }
                shelfAdapter.notifyDataSetChanged();
            }
        } else if (productType == COMIC_CONSTANT) {
            ShelfComicBeen shelfComicBeen = gson.fromJson(result, ShelfComicBeen.class);
            initBannerAnnounce(shelfComicBeen.announcement, shelfComicBeen.recommend_list);
            if (shelfComicBeen.list != null && !shelfComicBeen.list.isEmpty()) {
                for (Comic comic : shelfComicBeen.list) {
                    comic.is_collect = 1;
                    int position = comicList.indexOf(comic);
                    if (position != -1) {
                        Comic LocalBook = comicList.get(position);
                        LocalBook.new_chapter = comic.total_chapters - LocalBook.total_chapters;
                        LocalBook.total_chapters = comic.total_chapters;
                        LocalBook.vertical_cover = comic.vertical_cover;
                        LocalBook.author = comic.author.replaceAll(",", " ");
                        ObjectBoxUtils.addData(LocalBook, Comic.class);
                    } else {
                        objectList.add(comic);
                        comicList.add(comic);
                        ObjectBoxUtils.addData(comic, Comic.class);
                    }
                }
                shelfAdapter.notifyDataSetChanged();
            }
        } else if (productType == AUDIO_CONSTANT) {
            ShelfAudioBeen shelfComicBeen = gson.fromJson(result, ShelfAudioBeen.class);
            initBannerAnnounce(shelfComicBeen.announce, shelfComicBeen.recommend);
            if (shelfComicBeen.list != null && !shelfComicBeen.list.isEmpty()) {
                for (Audio audio : shelfComicBeen.list) {
                    audio.is_collect = 1;
                    int position = audioList.indexOf(audio);
                    if (position != -1) {
                        Audio LocalBook = audioList.get(position);
                        LocalBook.new_chapter = audio.total_chapter - LocalBook.total_chapter;
                        LocalBook.total_chapter = audio.total_chapter;
                        LocalBook.cover = audio.cover;
                        LocalBook.author = audio.author.replaceAll(",", " ");
                        ObjectBoxUtils.addData(LocalBook, Audio.class);
                    } else {
                        objectList.add(audio);
                        audioList.add(audio);
                        ObjectBoxUtils.addData(audio, Audio.class);
                    }
                }
                shelfAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 设置轮播图
     *
     * @param announce
     * @param recommend_list
     */
    private void initBannerAnnounce(List<Announce> announce, List<BaseBookComic> recommend_list) {
        if (recommend_list != null && !recommend_list.isEmpty()) {
            viewHolder.fragmentShelfBannerLayout.setVisibility(View.VISIBLE);
            // 转换为publicIntent
            List<PublicIntent> publicIntents = new ArrayList<>();
            for (BaseBookComic baseBookComic : recommend_list) {
                PublicIntent publicIntent = new PublicIntent();
                if (productType == BOOK_CONSTANT) {
                    publicIntent.content = String.valueOf(baseBookComic.book_id);
                } else if (productType == COMIC_CONSTANT) {
                    publicIntent.content = String.valueOf(baseBookComic.comic_id);
                } else if (productType == AUDIO_CONSTANT) {
                    publicIntent.content = String.valueOf(baseBookComic.audio_id);
                }
                publicIntent.action = productType;
                publicIntent.title = baseBookComic.name;
                publicIntent.desc = baseBookComic.description;
                publicIntent.image = baseBookComic.cover;
                publicIntents.add(publicIntent);
            }
            if (publicIntents.size() > 1) {
                CBViewHolderCreator creator = new CBViewHolderCreator() {
                    @Override
                    public Object createHolder() {
                        return new ShelfBannerHolderView(productType);
                    }
                };
                viewHolder.fragmentShelfBannerMale
                        .setPages(1, creator, publicIntents)
                        .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                        .setPageIndicator(0, new int[]{R.mipmap.ic_shelf_no, R.mipmap.ic_shelf_yes})
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent();
                                if (productType == BOOK_CONSTANT) {
                                    intent.setClass(activity, BookInfoActivity.class);
                                    intent.putExtra("book_id", recommend_list.get(position).book_id);
                                } else if (productType == COMIC_CONSTANT) {
                                    intent.setClass(activity, ComicInfoActivity.class);
                                    intent.putExtra("comic_id", recommend_list.get(position).comic_id);
                                } else if (productType == AUDIO_CONSTANT) {
                                    intent.setClass(activity, AudioInfoActivity.class);
                                    intent.putExtra("audio_id", recommend_list.get(position).audio_id);
                                }
                                activity.startActivity(intent);
                            }
                        });
                viewHolder.fragmentShelfBannerMale.startTurning(5000);
            } else {
                viewHolder.fragmentShelfBannerMale.setPages(1, null, publicIntents);
            }
        } else {
            viewHolder.fragmentShelfBannerLayout.setVisibility(View.GONE);
        }
        if (announce != null && !announce.isEmpty()) {
            viewHolder.fragmentBookshelfMarqueeLayout.setVisibility(View.VISIBLE);
            viewHolder.fragmentBookshelfMarqueeLayout.setBackground(MyShape.setMyshape(60, ContextCompat.getColor(activity, R.color.graybg)));
            viewHolder.fragmentBookshelfMarquee.setTextArraysAndClickListener(announce, new MarqueeTextViewClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(activity, AnnounceActivity.class);
                    intent.putExtra("announce_content", announce.get(position).getTitle() + "/-/" + announce.get(position).getContent());
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * 刷新当前数据
     *
     * @param s
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshBookSelf(RefreshShelfCurrent s) {

        int p;
        if (s.productType == productType) {
            switch (productType) {
                case BOOK_CONSTANT:
                    p = BookList.indexOf(s.book);
                    if (p != -1) {
                        s.book.isRecommend = false;
                        boolean isRecommend = BookList.get(0).isRecommend;
                        BookList.set(p, s.book);
                        objectList.set(p, s.book);
                        if (p != 0) {
                            openPosition = p;
                            setToTop();
                        } else {
                            if (isRecommend) {
                                shelfAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    break;
                case COMIC_CONSTANT:

                    p = comicList.indexOf(s.comic);
                    if (p != -1) {

                        s.comic.isRecommend = false;
                        boolean isRecommend = comicList.get(0).isRecommend;
                       // MyToash.Log("RefreshShelfCurrent2",s.comic.current_chapter_id+"");
                        comicList.set(p, s.comic);
                        objectList.set(p, s.comic);
                        if (p != 0) {
                            openPosition = p;
                            setToTop();
                        } else {
                            //   shelfAdapter.notifyDataSetChanged();
                         publicRecycleview.notifyItemChanged(0);
                           /* if (isRecommend) {
                                shelfAdapter.notifyDataSetChanged();
                            }*/
                            //shelfAdapter.notifyItemChanged(1);
                        }

                    }
                    break;
                case AUDIO_CONSTANT:
                    p = audioList.indexOf(s.audio);
                    if (p != -1) {
                        s.audio.isRecommend = false;
                        boolean isRecommend = audioList.get(0).isRecommend;
                        audioList.set(p, s.audio);
                        objectList.set(p, s.audio);
                        if (p != 0) {
                            openPosition = p;
                            setToTop();
                        } else {
                            if (isRecommend) {
                                shelfAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 刷新书架
     *
     * @param s
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshBookSelf(RefreshShelf s) {
        if (s.getProductType() == productType) {
            long new_time;
            if ((new_time = System.currentTimeMillis()) - lod_time < 200) {
                return;
            }
            lod_time = new_time;
            if (s.refreshBookSelf == null && s.refreshComicShelf == null && s.refreshAudioShelf == null) {//登录
                initData();
            } else {
                if (s.refreshComicShelf != null) {
                    // 操作漫画数据
                    if (s.refreshComicShelf.baseComics != null) {
                        setComicBean(true, s.refreshComicShelf);
                    } else if (s.refreshComicShelf.ADD == -1) {
                        int position = 0;
                        Flag:
                        for (Comic c : comicList) {
                            if (c.comic_id == s.refreshComicShelf.baseComic.comic_id) {
                                comicList.remove(c);
                                break Flag;
                            }
                            ++position;
                        }
                        objectList.remove(position);
                    } else {
                        if (!comicList.contains(s.refreshComicShelf.baseComic)) {
                            setComicBean(false, s.refreshComicShelf);
                        }
                    }
                } else if (s.refreshBookSelf != null) {
                    // 操作小说数据
                    if (s.refreshBookSelf.Books != null) {
                        setBookBean(true, s.refreshBookSelf);
                    } else if (s.refreshBookSelf.ADD == -1) {
                        int position = 0;
                        Flag:
                        for (Book c : BookList) {
                            if (c.book_id == s.refreshBookSelf.Book.book_id) {
                                BookList.remove(c);
                                break Flag;
                            }
                            ++position;
                        }
                        objectList.remove(position);
                    } else {
                        if (!BookList.contains(s.refreshBookSelf.Book)) {
                            setBookBean(false, s.refreshBookSelf);
                        }
                    }
                } else if (s.refreshAudioShelf != null) {
                    // 操作有声数据
                    if (s.refreshAudioShelf.audioList != null) {
                        setAudioBean(true, s.refreshAudioShelf);
                    } else if (s.refreshAudioShelf.ADD == -1) {
                        int position = 0;
                        Flag:
                        for (Audio c : audioList) {
                            if (c.audio_id == s.refreshAudioShelf.audio.audio_id) {
                                audioList.remove(c);
                                break Flag;
                            }
                            ++position;
                        }
                        objectList.remove(position);
                    } else {
                        if (!audioList.contains(s.refreshAudioShelf.audio)) {
                            setAudioBean(false, s.refreshAudioShelf);
                        }
                    }
                }
                shelfAdapter.notifyDataSetChanged();

                addHttpBookShelf();
            }
        }
    }

    /**
     * 界面发生切换时取消删除状态
     *
     * @param refresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShelfDeleteRefresh(ShelfDeleteRefresh refresh) {
        setCancelDelete();
    }

    public class ViewHolder {

        @BindView(R.id.fragment_shelf_banner_male)
        ConvenientBanner fragmentShelfBannerMale;
        @BindView(R.id.fragment_shelf_banner_layout)
        LinearLayout fragmentShelfBannerLayout;
        @BindView(R.id.fragment_Bookshelf_sign)
        LinearLayout fragmentBookshelfSign;
        @BindView(R.id.fragment_Bookshelf_marquee)
        MarqueeTextView fragmentBookshelfMarquee;
        @BindView(R.id.fragment_Bookshelf_marquee_layout)
        LinearLayout fragmentBookshelfMarqueeLayout;
        @BindView(R.id.fragment_Bookshelf_head)
        LinearLayout fragmentBookshelfHead;

        @OnClick({R.id.fragment_Bookshelf_sign})
        public void onViewClicked(View view) {
            if (UserUtils.isLogin(activity)) {
                final ReaderParams params = new ReaderParams(activity);
                HttpUtils.getInstance().sendRequestRequestParams(activity, Api.sIgninhttp, params.generateParamsJson(), new HttpUtils.ResponseListener() {
                            @Override
                            public void onResponse(final String result) {
                                int width = ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 80);
                                int height = (width - ImageUtil.dp2px(activity, 140)) / 3 * 4 / 3 + ImageUtil.dp2px(activity, 200);
                                new TaskCenterSignPopupWindow(activity, false, result, width, height);
                                EventBus.getDefault().post(new RefreshMine());
                            }

                            @Override
                            public void onErrorResponse(String ex) {
                                if (!ex.equals("no_net")) {
                                    startActivity(new Intent(activity, TaskCenterActivity.class));
                                }
                            }
                        }
                );
            } else {
                startActivity(new Intent(activity, LoginActivity.class));
            }
        }

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public void onDestroy() {
        viewHolder.fragmentBookshelfMarquee.releaseResources();
        super.onDestroy();
    }

    /**
     * 同步书籍
     */
    private void addHttpBookShelf() {
        if (UserUtils.isLogin(activity)) {
            StringBuilder builder = new StringBuilder();
            http_flag = -2;
            if (productType == BOOK_CONSTANT) {
                for (Book book : BookList) {
                    builder.append("," + book.book_id);
                }
            } else if (productType == COMIC_CONSTANT) {
                for (Comic book : comicList) {
                    builder.append("," + book.comic_id);
                }
            } else if (productType == AUDIO_CONSTANT) {
                for (Audio book : audioList) {
                    builder.append("," + book.audio_id);
                }
            }
            String str = builder.toString();
            String BookIdArr = str.substring(1);
            readerParams = new ReaderParams(activity);
            if (auto_add) {
                auto_add = false;
                readerParams.putExtraParams("auto_add", 1);
            }
            if (productType == BOOK_CONSTANT) {
                readerParams.putExtraParams("book_id", BookIdArr);
                HttpUtils.getInstance().sendRequestRequestParams(activity, Api.mBookAddCollectUrl, readerParams.generateParamsJson(), responseListener);
            } else if (productType == COMIC_CONSTANT) {
                readerParams.putExtraParams("comic_id", BookIdArr);
                HttpUtils.getInstance().sendRequestRequestParams(activity, Api.COMIC_SHELF_ADD, readerParams.generateParamsJson(), responseListener);
            } else if (productType == AUDIO_CONSTANT) {
                readerParams.putExtraParams("audio_id", BookIdArr);
                HttpUtils.getInstance().sendRequestRequestParams(activity, Api.AUDIO_FAV_ADD, readerParams.generateParamsJson(), responseListener);
            }
        }
    }

    /**
     * 添加小说数据
     *
     * @param isList
     * @param refreshBookSelf
     */
    private void setBookBean(boolean isList, RefreshBookSelf refreshBookSelf) {
        if (!BookList.isEmpty()) {
            Book oldBook = BookList.get(0);
            oldBook.setBookselfPosition(0);
            ObjectBoxUtils.addData(oldBook, Book.class);
        }
        Book topBook;
        if (isList) {
            auto_add=true;
            topBook = refreshBookSelf.Books.get(0);
        } else {
            topBook = refreshBookSelf.Book;
        }
        topBook.setBookselfPosition(10000);
        ObjectBoxUtils.addData(topBook, Book.class);
        if (isList) {
            objectList.addAll(0, refreshBookSelf.Books);
            BookList.addAll(0, refreshBookSelf.Books);
        } else {
            objectList.add(0, topBook);
            BookList.add(0, topBook);
        }
    }

    /**
     * 添加漫画数据
     *
     * @param isList
     * @param refreshComicShelf
     */
    private void setComicBean(boolean isList, RefreshComicShelf refreshComicShelf) {
        // 需要满足新的书籍在首位的需求
        if (!comicList.isEmpty()) {
            Comic oldComic = comicList.get(0);
            oldComic.setBookselfPosition(0);
            ObjectBoxUtils.addData(oldComic, Comic.class);
        }
        Comic topComic;
        if (isList) {
            auto_add=true;
            topComic = refreshComicShelf.baseComics.get(0);
        } else {
            topComic = refreshComicShelf.baseComic;
        }
        topComic.setBookselfPosition(10000);
        ObjectBoxUtils.addData(topComic, Comic.class);
        if (isList) {
            objectList.addAll(0, refreshComicShelf.baseComics);
            comicList.addAll(0, refreshComicShelf.baseComics);
        } else {
            objectList.add(0, topComic);
            comicList.add(0, topComic);
        }

    }

    /**
     * 添加有声数据
     *
     * @param isList
     * @param refreshAudioShelf
     */
    private void setAudioBean(boolean isList, RefreshAudioShelf refreshAudioShelf) {
        if (!audioList.isEmpty()) {
            Audio oldAudio = audioList.get(0);
            oldAudio.setBookselfPosition(0);
            ObjectBoxUtils.addData(oldAudio, Audio.class);
        }
        Audio topAudio;
        if (isList) {
            auto_add=true;
            topAudio = refreshAudioShelf.audioList.get(0);
        } else {
            topAudio = refreshAudioShelf.audio;
        }
        topAudio.setBookselfPosition(10000);
        ObjectBoxUtils.addData(topAudio, Audio.class);
        if (isList) {
            objectList.addAll(0, refreshAudioShelf.audioList);
            audioList.addAll(0, refreshAudioShelf.audioList);
        } else {
            objectList.add(0, topAudio);
            audioList.add(0, topAudio);
        }
    }
}

