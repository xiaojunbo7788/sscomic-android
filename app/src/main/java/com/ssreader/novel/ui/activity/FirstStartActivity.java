package com.ssreader.novel.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.gyf.immersionbar.ImmersionBar;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.Comic;
import com.ssreader.novel.model.FirstNovelRecommend;
import com.ssreader.novel.model.Recommend;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.FirstChooseBookAdapter;
import com.ssreader.novel.ui.dialog.WaitDialog;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.ShareUitls;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.PUTNotchHeightE;

public class FirstStartActivity extends BaseActivity {

    @BindView(R.id.activity_home_Book_layout)
    public LinearLayout activity_home_viewpager_Book_ScrollView;
    @BindView(R.id.activity_home_sex_layout)
    public LinearLayout activity_home_sex_layout;

    @BindView(R.id.activity_home_viewpager_sex_boy)
    public ImageView chooseBoyImage;
    @BindView(R.id.activity_home_viewpager_sex_gril)
    public ImageView chooseGirlImage;

    @BindView(R.id.activity_first_viewpager_ok)
    public TextView activity_home_viewpager_ok;

    @BindView(R.id.activity_home_viewpager_Book_GridView)
    public GridView activity_home_viewpager_Book_GridView;

    private WaitDialog waitDialog;
    private boolean current_book;

    private FirstChooseBookAdapter firstChooseBookAdapter;
    private boolean isNext = false;
    private boolean is_red;
    private boolean chooseBoy = false, chooseGirl = false;

    private Recommend recommend;
    private List<Recommend.RecommendProduc> recommendProducs = new ArrayList<>();
    private List<Recommend.RecommendProduc> addrecommendProducs = new ArrayList<>();

    @Override
    public int initContentView() {
        int NotchHeight = ImmersionBar.with(activity).getNotchHeight(activity);
        PUTNotchHeightE(activity, NotchHeight);
        FULL_CCREEN = true;
        USE_AUDIO_STATUS_LAYOUT = false;
        return R.layout.activity_firststart;
    }

    @Override
    public void initView() {
        waitDialog = new WaitDialog(activity, true);
        waitDialog.setCancleable(true);
        activity_home_viewpager_ok.setBackground(MyShape.setMyshape(60, ContextCompat.getColor(activity, R.color.gray1)));
        activity_home_viewpager_ok.setTextColor(ContextCompat.getColor(activity, R.color.gray));
        activity_home_viewpager_Book_GridView.setAdapter(firstChooseBookAdapter =
                new FirstChooseBookAdapter(activity, recommendProducs, scOnItemClickListener));
    }

    @Override
    public void initData() {
        // 请求书架推荐书籍接口
        readerParams = new ReaderParams(activity);
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.SHELF_RECOMMEND, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    FirstNovelRecommend recommend = HttpUtils.getGson().fromJson(response, FirstNovelRecommend.class);
                    if (recommend.getBook() != null && !recommend.getBook().isEmpty()) {
                        for (Book book : recommend.getBook()) {
                            book.is_collect = 1;
                            book.isRecommend = true;
                            ObjectBoxUtils.addData(book, Book.class);
                        }
                    }
                    if (recommend.getComic() != null && !recommend.getComic().isEmpty()) {
                        for (Comic comic : recommend.getComic()) {
                            comic.is_collect = 1;
                            comic.isRecommend = true;
                            ObjectBoxUtils.addData(comic, Comic.class);
                        }
                    }
                    if (recommend.getAudio() != null && !recommend.getAudio().isEmpty()) {
                        for (Audio audio : recommend.getAudio()) {
                            audio.is_collect = 1;
                            audio.isRecommend = true;
                            ObjectBoxUtils.addData(audio, Audio.class);
                        }
                    }
                }
            }

            @Override
            public void onErrorResponse(String ex) {

            }
        });
    }

    @Override
    public void initInfo(String json) {
        current_book = true;
        activity_home_viewpager_ok.setClickable(true);
        recommend = HttpUtils.getGson().fromJson(json, Recommend.class);
        if (recommend != null) {
            if (recommend.book != null) {
                recommendProducs.addAll(recommend.book);
            }
            if (recommend.comic != null) {
                recommendProducs.addAll(recommend.comic);
            }
            if (recommend.audio != null) {
                recommendProducs.addAll(recommend.audio);
            }
            if (recommendProducs != null && !recommendProducs.isEmpty()) {
                activity_home_sex_layout.setVisibility(View.GONE);
                for (Recommend.RecommendProduc recommendProduc : recommendProducs) {
                    recommendProduc.isChoose = true;
                }
                addrecommendProducs.addAll(recommendProducs);
                setOkBg(true);
            }else {
                startMainActivity(activity);
            }
            firstChooseBookAdapter.notifyDataSetChanged();
        } else {
            startMainActivity(activity);
        }
    }

    @Override
    public void errorInfo(String json) {
        super.errorInfo(json);
        startMainActivity(activity);
    }

    @OnClick(value = {R.id.activity_home_viewpager_sex_boy, R.id.activity_home_viewpager_sex_gril,
            R.id.activity_home_viewpager_next, R.id.activity_first_viewpager_ok,})
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.activity_home_viewpager_sex_boy:
                chooseBoy = !chooseBoy;
                setOkBg(chooseBoy);
                if (chooseBoy) {
                    chooseGirl = false;
                    ShareUitls.putInt(activity, "sex", 1);
                    ShareUitls.putString(activity, "sextemp", "boy");
                    chooseBoyImage.setImageResource(R.mipmap.insterest_boy_select);
                    chooseGirlImage.setImageResource(R.mipmap.insterest_girl_normal);
                } else {
                    ShareUitls.putString(activity, "sextemp", "");
                    chooseBoyImage.setImageResource(R.mipmap.insterest_boy_normal);
                }
                break;
            case R.id.activity_home_viewpager_sex_gril:
                chooseGirl = !chooseGirl;
                setOkBg(chooseGirl);
                if (chooseGirl) {
                    chooseBoy = false;
                    ShareUitls.putInt(activity, "sex", 2);
                    ShareUitls.putString(activity, "sextemp", "gril");
                    chooseBoyImage.setImageResource(R.mipmap.insterest_boy_normal);
                    chooseGirlImage.setImageResource(R.mipmap.insterest_girl_select);
                } else {
                    ShareUitls.putInt(activity, "sex", 1);
                    ShareUitls.putString(activity, "sextemp", "");
                    chooseGirlImage.setImageResource(R.mipmap.insterest_girl_normal);
                }
                break;
            case R.id.activity_home_viewpager_next:
                if (!isNext) {
                    isNext = true;
                    startMainActivity(activity);
                }
                break;
            case R.id.activity_first_viewpager_ok:
                if (!current_book) {
                    if (!chooseBoy && !chooseGirl) {
                        MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.firstStartactivity_choosesex));
                    } else {
                        getRecommend(chooseBoy ? 1 : 2);
                        ShareUitls.putInt(activity,"ChooseSex",!chooseBoy ? 1 : 2);
                    }
                } else {
                    if (addrecommendProducs.isEmpty()) {
                        MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.firstStartactivity_chooseBooks));
                    } else {
                        activity_home_viewpager_ok.setClickable(false);
                        waitDialog.showDailog();
                        for (Recommend.RecommendProduc addrecommendProducs : addrecommendProducs) {
                            if (addrecommendProducs.book_id != 0) {
                                Book mBook = new Book();
                                mBook.setbook_id(addrecommendProducs.book_id);
                                mBook.setName(addrecommendProducs.name);
                                if (addrecommendProducs.author != null && !addrecommendProducs.author.isEmpty() && !TextUtils.isEmpty(addrecommendProducs.author)) {
                                    StringBuilder name = new StringBuilder();
                                    if (addrecommendProducs.author != null) {
                                        name.append(addrecommendProducs.author);
                                    }
                                    mBook.setAuthor_name(name.toString());
                                }
                                mBook.setCover(addrecommendProducs.cover);
                                mBook.setTotal_chapter(addrecommendProducs.total_chapter);
                                mBook.setDescription(addrecommendProducs.description);
                                mBook.is_collect = 1;
                                ObjectBoxUtils.addData(mBook, Book.class);
                            } else if (addrecommendProducs.comic_id != 0) {
                                Comic baseComic = new Comic();
                                baseComic.setComic_id(addrecommendProducs.comic_id);
                                baseComic.setName(addrecommendProducs.name);
                                if (addrecommendProducs.author != null && !addrecommendProducs.author.isEmpty() && !TextUtils.isEmpty(addrecommendProducs.author)) {
                                    StringBuilder name = new StringBuilder();
                                    if (addrecommendProducs.author != null) {
                                        name.append(addrecommendProducs.author);
                                    }
                                    baseComic.author = name.toString();
                                }
                                baseComic.setVertical_cover(addrecommendProducs.cover);
                                baseComic.setTotal_chapters(addrecommendProducs.total_chapter);
                                baseComic.setDescription(addrecommendProducs.description);
                                baseComic.is_collect = 1;
                                ObjectBoxUtils.addData(baseComic, Comic.class);
                            } else if (addrecommendProducs.audio_id != 0) {
                                Audio baseAudio = new Audio();
                                baseAudio.setAudio_id(addrecommendProducs.audio_id);
                                baseAudio.setName(addrecommendProducs.name);
                                if (addrecommendProducs.author != null && !addrecommendProducs.author.isEmpty() && !TextUtils.isEmpty(addrecommendProducs.author)) {
                                    StringBuilder name = new StringBuilder();
                                    if (addrecommendProducs.author != null) {
                                        name.append(addrecommendProducs.author);
                                    }
                                    baseAudio.author = name.toString();
                                }
                                baseAudio.setCover(addrecommendProducs.cover);
                                baseAudio.setTotal_chapter(addrecommendProducs.total_chapter);
                                baseAudio.setDescription(addrecommendProducs.description);
                                baseAudio.is_collect = 1;
                                ObjectBoxUtils.addData(baseAudio, Audio.class);
                            }
                        }
                        waitDialog.ShowDialog(false);
                        startMainActivity(activity);
                        break;
                    }
                }
                break;
        }
    }

    private void setOkBg(boolean is_red) {
        if (this.is_red != is_red) {
            if (is_red) {
                activity_home_viewpager_ok.setBackground(MyShape.setMyshape(60, ContextCompat.getColor(activity, R.color.maincolor)));
                activity_home_viewpager_ok.setTextColor(ContextCompat.getColor(activity, R.color.white));

            } else {
                activity_home_viewpager_ok.setBackground(MyShape.setMyshape(60, ContextCompat.getColor(activity, R.color.gray1)));
                activity_home_viewpager_ok.setTextColor(ContextCompat.getColor(activity, R.color.gray));

            }
            this.is_red = is_red;
        }
    }

    public SCOnItemClickListener scOnItemClickListener = new SCOnItemClickListener<Recommend.RecommendProduc>() {
        @Override
        public void OnItemClickListener(int flag, int position, Recommend.RecommendProduc recommendProduc) {
            if (recommendProduc.isChoose) {
                recommendProduc.isChoose = false;
                addrecommendProducs.remove(recommendProduc);
                if (addrecommendProducs.isEmpty()) {
                    setOkBg(false);
                }
            } else {
                recommendProduc.isChoose = true;
                addrecommendProducs.add(recommendProduc);
                if (!addrecommendProducs.isEmpty()) {
                    setOkBg(true);
                }
            }
        }

        @Override
        public void OnItemLongClickListener(int flag, int position, Recommend.RecommendProduc o) {

        }
    };

    private void getRecommend(int flag) {
        activity_home_viewpager_ok.setClickable(false);
        readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("channel_id", flag + "");
        String json = readerParams.generateParamsJson();
        httpUtils.sendRequestRequestParams(activity,Api.start_recommend, json,responseListener);
    }

    /**
     * 打开主界面
     * @param activity
     */
    public void startMainActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
