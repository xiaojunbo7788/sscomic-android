package com.ssreader.novel.ui.dialog;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ssreader.novel.R;

import com.ssreader.novel.base.BasePopupWindow;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.RefreshAudioShelf;
import com.ssreader.novel.eventbus.RefreshBookSelf;
import com.ssreader.novel.eventbus.RefreshComicShelf;
import com.ssreader.novel.eventbus.RefreshShelf;
import com.ssreader.novel.model.Audio;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.Comic;
import com.ssreader.novel.model.Recommend;
import com.ssreader.novel.model.SigninSuccess;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyGlide;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.ShareUitls;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用户签发弹窗
 */
public class TaskCenterSignPopupWindow extends BasePopupWindow {

    // 获取视图
    private HolderSign holderSign;
    private List<Recommend.RecommendProduc> recommendProducs = new ArrayList<>();
    private List<Recommend.RecommendProduc> addrecommendProducs = new ArrayList<>();
    // 弹窗宽高
    private int mWidth, mHeight;
    private boolean isClick;
    private String mResult;

    public TaskCenterSignPopupWindow(Activity activity, boolean isOnClick, String result, int width, int height) {
        super(width, height);
        mResult = result;
        isClick = isOnClick;
        onCreate(activity);
    }

    @Override
    public int initContentView() {
        return R.layout.dialog_sign;
    }

    @Override
    public void initView() {
        holderSign = new HolderSign(view);
    }

    @Override
    public void initData() {
        if (TextUtils.isEmpty(mResult)) {
            dismiss();
            return;
        }
        final SigninSuccess signinSuccess = HttpUtils.getGson().fromJson(mResult, SigninSuccess.class);
        if (signinSuccess == null) {
            dismiss();
            return;
        }
        if (signinSuccess.getBook() != null && !signinSuccess.getBook().isEmpty()) {
            recommendProducs.addAll(signinSuccess.getBook());
        }
        if (signinSuccess.getComic() != null && !signinSuccess.getComic().isEmpty()) {
            recommendProducs.addAll(signinSuccess.getComic());
        }
        if (signinSuccess.getAudio() != null && !signinSuccess.getAudio().isEmpty()) {
            recommendProducs.addAll(signinSuccess.getAudio());
        }
        if (recommendProducs.isEmpty()) {
            dismiss();
            return;
        }
        if (recommendProducs.size() > 3) {
            recommendProducs = recommendProducs.subList(0, 3);
        }
        mWidth = (width - ImageUtil.dp2px(activity, 60)) / 3;
        mHeight = mWidth * 4 / 3;
        if (recommendProducs.size() < 3) {
            int w;
            if (recommendProducs.size() == 2) {
                w = width - ImageUtil.dp2px(activity, 50) - mWidth;
            } else {
                w = mWidth;
            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holderSign.gridView.getLayoutParams();
            params.width = w;
            holderSign.gridView.setLayoutParams(params);
        }
        setViewPager(holderSign.gridView, activity, isClick);
        holderSign.popwindow_layout.setBackground(MyShape.setMyshapeStroke(activity, 10, 1,
                ContextCompat.getColor(activity, R.color.white)));
        holderSign.popwindow_sign_golds.setText(signinSuccess.getAward());
        holderSign.popwindow_sign_golds_tomorrow.setText(signinSuccess.getTomorrow_award());
        initListener();
        setPopupWindowAttribute();
    }

    private void initListener() {
        holderSign.popwindow_sign_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        holderSign.popwindow_sign_alladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Book> list = new ArrayList<>();
                List<Comic> comics = new ArrayList<>();
                List<Audio> audios = new ArrayList<>();
                for (Recommend.RecommendProduc addrecommendProducs : recommendProducs) {
                    if (addrecommendProducs.book_id != 0 && ObjectBoxUtils.getBook(addrecommendProducs.book_id) == null) {
                        Book mBook = new Book();
                        mBook.setbook_id(addrecommendProducs.book_id);
                        mBook.setName(addrecommendProducs.name);
                        mBook.setCover(addrecommendProducs.cover);
                        mBook.setTotal_chapter(addrecommendProducs.total_chapter);
                        mBook.setDescription(addrecommendProducs.description);
                        mBook.setName(addrecommendProducs.name);
                        mBook.is_collect = 1;
                        ObjectBoxUtils.addData(mBook, Book.class);
                        list.add(mBook);
                    } else if (addrecommendProducs.comic_id != 0) {
                        Comic baseComic = new Comic();
                        baseComic.setComic_id(addrecommendProducs.comic_id);
                        baseComic.setName(addrecommendProducs.name);
                        baseComic.setVertical_cover(addrecommendProducs.cover);
                        baseComic.setTotal_chapters(addrecommendProducs.total_chapter);
                        baseComic.setDescription(addrecommendProducs.description);
                        baseComic.is_collect = 1;
                        ObjectBoxUtils.addData(baseComic, Comic.class);
                        comics.add(baseComic);
                    } else {
                        Audio baseAudio = new Audio();
                        baseAudio.setAudio_id(addrecommendProducs.audio_id);
                        baseAudio.setName(addrecommendProducs.name);
                        baseAudio.setCover(addrecommendProducs.cover);
                        baseAudio.setTotal_chapter(addrecommendProducs.total_chapter);
                        baseAudio.setDescription(addrecommendProducs.description);
                        baseAudio.is_collect = 1;
                        ObjectBoxUtils.addData(baseAudio, Audio.class);
                        audios.add(baseAudio);
                    }
                }
                EventBus.getDefault().post(new RefreshShelf(Constant.BOOK_CONSTANT, new RefreshBookSelf(list), null, null));
                EventBus.getDefault().post(new RefreshShelf(Constant.COMIC_CONSTANT, null, new RefreshComicShelf(comics), null));
                EventBus.getDefault().post(new RefreshShelf(Constant.AUDIO_CONSTANT, null, null, new RefreshAudioShelf(audios)));
                dismiss();
            }
        });
    }

    @Override
    public void initInfo(String json) {

    }

    /**
     * 设置弹窗属性
     */
    private void setPopupWindowAttribute() {
        setFocusable(true);
        setOutsideTouchable(false);
        setAnimationStyle(R.style.sign_pop_anim);
        setBackgroundDrawable(new BitmapDrawable(activity.getResources()));
        // 显示弹窗
        showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    public class HolderSign {

        @BindView(R.id.popwindow_sign_golds)
        TextView popwindow_sign_golds;
        @BindView(R.id.popwindow_sign_golds_tomorrow)
        TextView popwindow_sign_golds_tomorrow;
        @BindView(R.id.popwindow_sign_layout)
        FrameLayout popwindow_sign_layout;
        @BindView(R.id.popwindow_sign_book_havebook)
        LinearLayout popwindow_sign_book_havebook;
        @BindView(R.id.popwindow_sign_book_GridView)
        GridView gridView;
        @BindView(R.id.popwindow_layout)
        LinearLayout popwindow_layout;
        @BindView(R.id.popwindow_sign_no)
        TextView popwindow_sign_no;
        @BindView(R.id.popwindow_sign_alladd)
        TextView popwindow_sign_alladd;

        public HolderSign(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 适配器
     * @param gridView
     * @param activity
     * @param isClick
     */
    private void setViewPager(GridView gridView, Activity activity,boolean isClick) {
        gridView.setNumColumns(recommendProducs.size());
        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return recommendProducs.size();
            }

            @Override
            public Recommend.RecommendProduc getItem(int i) {
                return recommendProducs.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = LayoutInflater.from(activity).inflate(R.layout.activity_home_viewpager_classfy_gridview_item, null);
                }
                ImageView imageView = view.findViewById(R.id.activity_home_viewpager_classfy_GridView_img);
                TextView activity_home_viewpager_classfy_GridView_tv = (TextView)
                        view.findViewById(R.id.activity_home_viewpager_classfy_GridView_tv);
                TextView activity_home_flag = view.findViewById(R.id.activity_home_flag);
                RelativeLayout contentLayout = view.findViewById(R.id.activity_home_viewpager_classfy_GridView_laiout);

                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) imageView.getLayoutParams();
                layoutParams.width = mWidth;
                layoutParams.height = mHeight;
                imageView.setLayoutParams(layoutParams);

                final CheckBox activity_home_viewpager_classfy_GridView_box = view.findViewById(R.id.activity_home_viewpager_classfy_GridView_box);
                if(isClick){
                    activity_home_viewpager_classfy_GridView_box.setVisibility(View.VISIBLE);
                }else{
                    activity_home_viewpager_classfy_GridView_box.setVisibility(View.GONE);
                }
                final Recommend.RecommendProduc recommendProduc = getItem(i);
                if (recommendProduc.book_id != 0) {
                    activity_home_flag.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 5),
                            ContextCompat.getColor(activity, R.color.maincolor)));
                    activity_home_flag.setText(LanguageUtil.getString(activity, R.string.noverfragment_xiaoshuo));
                } else if (recommendProduc.comic_id != 0) {
                    activity_home_flag.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 5),
                            ContextCompat.getColor(activity, R.color.red)));
                    activity_home_flag.setText(LanguageUtil.getString(activity, R.string.noverfragment_manhua));
                } else if (recommendProduc.audio_id != 0){
                    activity_home_flag.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 5),
                            ContextCompat.getColor(activity, R.color.audio_mark_bg)));
                    activity_home_flag.setText(LanguageUtil.getString(activity, R.string.noverfragment_audio));
                }
                MyGlide.GlideImage(activity, recommendProduc.cover, imageView, mWidth, mHeight);
                activity_home_viewpager_classfy_GridView_tv.setText(recommendProduc.name + "");
                contentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (recommendProduc.isChoose) {
                            recommendProduc.isChoose = false;
                            activity_home_viewpager_classfy_GridView_box.setChecked(false);
                            addrecommendProducs.remove(recommendProduc);
                        } else {
                            recommendProduc.isChoose = true;
                            activity_home_viewpager_classfy_GridView_box.setChecked(true);
                            addrecommendProducs.add(recommendProduc);
                        }
                    }
                });
                return view;
            }
        });
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
        ShareUitls.putString(activity, "sign_pop", "");
    }
}
