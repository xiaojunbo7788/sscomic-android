package com.ssreader.novel.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.eventbus.RefreshReadHistory;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.BaseReadHistory;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.Comic;
import com.ssreader.novel.model.PublicPage;
import com.ssreader.novel.model.ReadHistory;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.AudioInfoActivity;
import com.ssreader.novel.ui.activity.BookInfoActivity;
import com.ssreader.novel.ui.activity.ComicInfoActivity;
import com.ssreader.novel.ui.activity.ComicLookActivity;
import com.ssreader.novel.ui.activity.LoginActivity;
import com.ssreader.novel.ui.adapter.ReadHistoryBookAdapter;
import com.ssreader.novel.ui.dialog.PublicDialog;
import com.ssreader.novel.ui.audio.AudioSoundActivity;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.RegularUtlis;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.BOOK_CONSTANT;
import static com.ssreader.novel.constant.Constant.COMIC_CONSTANT;
import static com.ssreader.novel.constant.Constant.AUDIO_CONSTANT;

public class ReadHistoryFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView mFragmentReadhistoryReadhistory;
    @BindView(R.id.fragment_option_noresult)
    LinearLayout mFragmentReadhistoryPop;
    @BindView(R.id.fragment_option_noresult_text)
    TextView mFragmentHistoryText;
    @BindView(R.id.fragment_option_noresult_try)
    TextView mFragmentHistoryGoLogin;

    private PublicPage publicPage;
    private List<BaseReadHistory> optionBeenList;
    private ReadHistoryBookAdapter optionAdapter;
    private int requestCode = 889;
    private int productType;

    public ReadHistoryFragment(int productType) {
        this.productType = productType;
    }

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.fragment_readhistory;
    }

    @Override
    public void initView() {
        initSCRecyclerView(mFragmentReadhistoryReadhistory, RecyclerView.VERTICAL, 0);
        optionBeenList = new ArrayList<>();
        mFragmentHistoryGoLogin.setText(LanguageUtil.getString(activity, R.string.app_login_now));
        mFragmentHistoryGoLogin.setBackground(MyShape.setMyshapeStroke(activity, ImageUtil.dp2px(activity, 1),
                1, ContextCompat.getColor(activity, R.color.maincolor)));
        optionAdapter = new ReadHistoryBookAdapter(activity, optionBeenList, getPosition, productType);
        mFragmentReadhistoryReadhistory.setAdapter(optionAdapter, true);
        setNoResult(true);
    }

    @OnClick({R.id.fragment_option_noresult_try})
    public void onClick(View view) {
        startActivity(new Intent(activity, LoginActivity.class));
    }

    /**
     * 点击事件
     */
    ReadHistoryFragment.GetPosition getPosition = new ReadHistoryFragment.GetPosition() {
        @Override
        public void getPosition(int falg, BaseReadHistory readHistoryBook, int position, int productType) {
            Intent intent;
            switch (falg) {
                case 1:
                    if (productType == BOOK_CONSTANT) {
                        Book book = ObjectBoxUtils.getBook(readHistoryBook.getbook_id());
                        if (book != null) {
                            book.setCurrent_chapter_id(readHistoryBook.chapter_id);
                        } else {
                            book = new Book();
                            book.setbook_id(readHistoryBook.getbook_id());
                            book.setName(readHistoryBook.getName());
                            book.setCover(readHistoryBook.getCover());
                            book.setCurrent_chapter_id(readHistoryBook.chapter_id);
                            book.setDescription(readHistoryBook.getDescription());
                            if(!TextUtils.isEmpty(readHistoryBook.total_chapters)&& RegularUtlis.isNumber(readHistoryBook.total_chapters)) {
                                book.total_chapter=(Integer.parseInt(readHistoryBook.total_chapters));
                            }
                            ObjectBoxUtils.addData(book, Book.class);
                        }
                        ChapterManager.getInstance().openBook(activity,book);
                    } else if (productType == COMIC_CONSTANT) {
                        Comic baseComic = ObjectBoxUtils.getComic(readHistoryBook.getComic_id());
                        if (baseComic == null) {
                            baseComic = new Comic();
                            baseComic.setComic_id(readHistoryBook.comic_id);
                            baseComic.setCurrent_chapter_id(readHistoryBook.chapter_id);
                            baseComic.setCurrent_display_order(readHistoryBook.chapter_index);
                            if(!TextUtils.isEmpty(readHistoryBook.total_chapters)&& RegularUtlis.isNumber(readHistoryBook.total_chapters)) {
                                baseComic.setTotal_chapters(Integer.parseInt(readHistoryBook.total_chapters));
                            }
                            baseComic.setName(readHistoryBook.name);
                            baseComic.setDescription(readHistoryBook.description);
                            ObjectBoxUtils.addData(baseComic, Comic.class);
                        } else {
                            baseComic.setCurrent_chapter_id(readHistoryBook.chapter_id);
                            baseComic.setCurrent_display_order(readHistoryBook.chapter_index);
                        }
                        intent = new Intent(activity, ComicLookActivity.class);
                        intent.putExtra("baseComic", baseComic);
                        startActivityForResult(intent, requestCode);
                    } else if (productType == AUDIO_CONSTANT) {
                        Audio audio = ObjectBoxUtils.getAudio(readHistoryBook.audio_id);
                        if (audio == null) {
                            audio = new Audio();
                            audio.setAudio_id(readHistoryBook.audio_id);
                            audio.current_chapter_id = readHistoryBook.chapter_id;
                            audio.current_chapter_displayOrder = readHistoryBook.chapter_index;
                            if(!TextUtils.isEmpty(readHistoryBook.total_chapters)&& RegularUtlis.isNumber(readHistoryBook.total_chapters)) {
                                audio.total_chapter=Integer.parseInt(readHistoryBook.total_chapters);
                            }

                            audio.name = readHistoryBook.name;
                            audio.cover = readHistoryBook.cover;
                            audio.description = readHistoryBook.description;
                            ObjectBoxUtils.addData(audio, Audio.class);
                        }else {
                            audio.current_chapter_id = readHistoryBook.chapter_id;
                            audio.current_chapter_displayOrder = readHistoryBook.chapter_index;
                        }
                        intent = new Intent(activity, AudioSoundActivity.class);
                        intent.putExtra("audio", audio);
                        startActivity(intent);
                        activity.overridePendingTransition(R.anim.activity_bottom_top_open, R.anim.activity_stay);
                    }
                    break;
                case 0:
                    if (productType == BOOK_CONSTANT) {
                        intent = new Intent(activity, BookInfoActivity.class);
                        intent.putExtra("book_id", readHistoryBook.getbook_id());
                        activity.startActivity(intent);
                    } else if (productType == COMIC_CONSTANT) {
                        intent = new Intent(activity, ComicInfoActivity.class);
                        intent.putExtra("comic_id", readHistoryBook.getComic_id());
                        startActivityForResult(intent, requestCode);
                    } else if (productType == AUDIO_CONSTANT) {
                        intent = new Intent(activity, AudioInfoActivity.class);
                        intent.putExtra("audio_id", readHistoryBook.audio_id);
                        activity.startActivity(intent);
                    }
                    break;
                case 2:
                    if (readHistoryBook.ad_type == 0) {
                        delete(readHistoryBook.log_id, productType);
                    }
                    optionBeenList.remove(position);
                    optionAdapter.notifyDataSetChanged();
                    if (optionBeenList.isEmpty()) {
                        setNoResult(true);
                    }
                    break;
            }
        }
    };

    @Override
    public void initData() {
        String url = "";
        readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("page_num", current_page + "");
        if (productType == BOOK_CONSTANT) {
            url = Api.read_log;
        } else if (productType == COMIC_CONSTANT) {
            url = Api.COMIC_read_log;
        } else if (productType == AUDIO_CONSTANT) {
            url = Api.AUDIO_READ_LOG;
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }
        HttpUtils.getInstance().sendRequestRequestParams(activity,url, readerParams.generateParamsJson(),responseListener);
    }

    @Override
    public void initInfo(String json) {
        ReadHistory optionItem = gson.fromJson(json, ReadHistory.class);
        publicPage = optionItem;
        if (optionItem.list != null) {
            if (publicPage.current_page <= publicPage.total_page && !optionItem.list.isEmpty()) {
                if (current_page == 1) {
                    optionAdapter.NoLinePosition = -1;
                    mFragmentReadhistoryReadhistory.setLoadingMoreEnabled(true);
                    optionBeenList.clear();
                }
                optionBeenList.addAll(optionItem.list);
            }
        }
        if (!optionBeenList.isEmpty()) {
            if (publicPage.current_page >= publicPage.total_page) {
                optionAdapter.NoLinePosition = optionBeenList.size() - 1;
                mFragmentReadhistoryReadhistory.setLoadingMoreEnabled(false);
            }
            setNoResult(false);
            optionAdapter.notifyDataSetChanged();
        } else {
            setNoResult(true);
        }
    }

    /**
     * 登录回调
     *
     * @param refreshMine
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        if (UserUtils.isLogin(activity)) {
            setNoResult(false);
            current_page = 1;
            if (optionBeenList != null) {
                optionBeenList.clear();
            }
            initData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshReadHistory refreshReadHistory) {
        if (refreshReadHistory.getProductType() == productType) {
            current_page = 1;
            if (optionBeenList != null) {
                optionBeenList.clear();
            }
            initData();
        }
    }

    /**
     * 设置没有内容时的UI
     *
     * @param isShow
     */
    private void setNoResult(boolean isShow) {
        if (isShow) {
            mFragmentReadhistoryReadhistory.setVisibility(View.GONE);
            mFragmentReadhistoryPop.setVisibility(View.VISIBLE);
            if (!UserUtils.isLogin(activity)) {
                mFragmentHistoryGoLogin.setVisibility(View.VISIBLE);
                mFragmentHistoryText.setText(LanguageUtil.getString(activity, R.string.app_history_no_login));
            } else {
                mFragmentHistoryGoLogin.setVisibility(View.GONE);
                mFragmentHistoryText.setText(LanguageUtil.getString(activity, R.string.app_history_no_result));
            }
        } else {
            mFragmentReadhistoryPop.setVisibility(View.GONE);
            mFragmentReadhistoryReadhistory.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 清空
     */
    public void clean() {
        if (optionBeenList.isEmpty()) {
            return;
        }
        PublicDialog.publicDialogVoid(activity, "",
                LanguageUtil.getString(activity, R.string.ReadHistoryFragment_is_clean),
                LanguageUtil.getString(activity, R.string.app_cancle),
                LanguageUtil.getString(activity, R.string.app_confirm), new PublicDialog.OnPublicListener() {
                    @Override
                    public void onClickConfirm(boolean isConfirm) {
                        if (isConfirm) {
                            delete("all", productType);
                            current_page = 1;
                            optionBeenList.clear();
                            optionAdapter.notifyDataSetChanged();
                            if (optionBeenList.isEmpty()) {
                                setNoResult(true);
                            }
                            MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.BookInfoActivity_down_delete_success));
                        }
                    }
                });
    }

    /**
     * 单独删除
     * @param log_id
     * @param productType
     */
    private void delete(String log_id, int productType) {
        String url = "";
        http_flag = 2;
        httpUtils = HttpUtils.getInstance();
        readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("log_id", log_id);
        if (productType == BOOK_CONSTANT) {
            url = Api.del_read_log;
        } else if (productType == COMIC_CONSTANT) {
            url = Api.COMIC_read_log_del;
        } else if (productType == AUDIO_CONSTANT) {
            url = Api.AUDIO_DEL_READ_LOG;
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }
        httpUtils.sendRequestRequestParams(activity,url, readerParams.generateParamsJson(), null);
    }

    public interface GetPosition {

        void getPosition(int falg, BaseReadHistory readHistoryBook, int position, int PRODUCT);
    }
}
