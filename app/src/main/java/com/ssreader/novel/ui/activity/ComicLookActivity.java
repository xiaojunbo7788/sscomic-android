package com.ssreader.novel.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.util.ColorParser;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.umeng.socialize.UMShareAPI;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.LineSpinFadeLoaderIndicator;
import com.ssreader.novel.BuildConfig;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.eventbus.OpenComicChapter;
import com.ssreader.novel.eventbus.RefreshComicShelf;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.eventbus.RefreshShelf;
import com.ssreader.novel.eventbus.RefreshShelfCurrent;
import com.ssreader.novel.model.BaseAd;
import com.ssreader.novel.model.BaseComicImage;
import com.ssreader.novel.model.Comic;
import com.ssreader.novel.model.ComicChapter;
import com.ssreader.novel.model.ComicChapterEventbus;
import com.ssreader.novel.model.ComicChapterItem;
import com.ssreader.novel.model.ComicDanmuBean;
import com.ssreader.novel.eventbus.ChapterBuyRefresh;
import com.ssreader.novel.model.Comment;
import com.ssreader.novel.model.ReadHistory;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.ComicRecyclerViewAdapter;
import com.ssreader.novel.ui.bwad.AdHttp;
import com.ssreader.novel.ui.dialog.LookComicSetDialog;
import com.ssreader.novel.ui.dialog.PublicPurchaseDialog;
import com.ssreader.novel.ui.fragment.ComicinfoMuluFragment;
import com.ssreader.novel.ui.read.util.BrightnessUtil;
import com.ssreader.novel.ui.utils.AndroidBug5497Workaround;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.Input;
import com.ssreader.novel.ui.view.comiclookview.SComicRecyclerView;
import com.ssreader.novel.utils.FileManager;
import com.ssreader.novel.utils.InternetUtils;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.MyShare;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.ScreenSizeUtils;
import com.ssreader.novel.utils.ShareUitls;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

import static com.ssreader.novel.constant.Constant.AgainTime;
import static com.ssreader.novel.constant.Constant.COMIC_CONSTANT;
import static com.ssreader.novel.constant.Constant.GETNotchHeight;
import static com.ssreader.novel.constant.Constant.isIsComicDanmu;
import static com.ssreader.novel.constant.Constant.setComicIsDanmu;
import static com.ssreader.novel.constant.Constant.getIsReadBottomAd_COMIC;
import static com.ssreader.novel.ui.utils.LoginUtils.goToLogin;

public class ComicLookActivity extends BaseActivity {

    @BindView(R.id.activity_comiclook_none)
    public View activity_comiclook_none;
    @BindView(R.id.fragment_option_noresult_text)
    public TextView noresult_text;
    @BindView(R.id.fragment_option_noresult_try)
    public TextView noresult_try;

    @BindView(R.id.activity_comiclook_xiayihua)
    public ImageView activity_comiclook_xiayihua;
    @BindView(R.id.activity_comiclook_shangyihua)
    public ImageView activity_comiclook_shangyihua;
    @BindView(R.id.item_dialog_downadapter_RotationLoadingView)
    public AVLoadingIndicatorView avLoadingIndicatorView;
    @BindView(R.id.activity_comiclook_danmu_dangqianhua)
    public TextView activity_comiclook_danmu_dangqianhua;
    @BindView(R.id.activity_comiclook_share)
    public RelativeLayout activity_comiclook_share;

    @BindView(R.id.activity_comiclook_lording)
    public RelativeLayout activity_comiclook_lording;
    @BindView(R.id.activity_comiclook_lording_img)
    ImageView lordingImage;
    @BindView(R.id.activity_comic_look_danmu_layout_bg)
    View activity_comic_look_danmu_layout_bg;

    @BindView(R.id.titlebar_text)
    public TextView titlebar_text;
    @BindView(R.id.activity_comiclook_RecyclerView)
    public SComicRecyclerView activity_comiclook_RecyclerView;

    @BindView(R.id.refreshLayout)
    public SmartRefreshLayout refreshLayout;

    @BindView(R.id.activity_comiclook_head)
    public RelativeLayout activity_comiclook_head;
    @BindView(R.id.activity_comiclook_foot)
    public LinearLayout activity_comiclook_foot;
    @BindView(R.id.activity_comiclook_shoucang)
    public ImageView activity_comiclook_shoucang;
    @BindView(R.id.activity_comiclook_dingbu)
    public ImageView activity_comiclook_dingbu;
    @BindView(R.id.activity_comiclook_danmu_layout)
    public LinearLayout activity_comiclook_danmu_layout;
    @BindView(R.id.fragment_comicinfo_mulu_dangqian_layout)
    public RelativeLayout fragment_comicinfo_mulu_dangqian_layout;

    @BindView(R.id.activity_comiclook_danmakuView)
    DanmakuView comicLookDanmuView;
    @BindView(R.id.activity_comiclook_danmu_img)
    public ImageView activity_comiclook_danmu_img;
    @BindView(R.id.activity_comiclook_danmu_img2)
    public ImageView activity_comiclook_danmu_img2;
    @BindView(R.id.activity_comiclook_danmu_edit)
    public EditText activity_comiclook_danmu_edit;
    @BindView(R.id.activity_comiclook_danmu_fashe)
    public TextView activity_comiclook_danmu_fashe;
    @BindView(R.id.activity_comiclook_pinglunshu)
    public TextView activity_comiclook_pinglunshu;

    // 弹幕
    private DanmakuContext mDanmakuContext = null;
    private BaseDanmakuParser mParser = null;
    // 当前弹窗的页码
    private int currentDanmuPage, danmuPageCount;
    private List<ComicDanmuBean.DanmuBean> danmuBeanList = new ArrayList<>();
    private int danmuPotion;
    // 用于判断是否获取焦点
    private boolean isShowFocus = false;
    private boolean isHideFocus = false;

    private Map<Long, ComicChapterItem> map = new HashMap();//临时存储章节数据
    private boolean click_next;
    private MyViewHolder holderFoot;

    private ComicRecyclerViewAdapter comicChapterCatalogAdapter;
    private List<BaseComicImage> baseComicImages;
    private boolean MenuSHOW, CommentFlag;
    private ComicChapterItem comicChapterItem;

    private Comic comic;
    private List<ComicChapter> comicChapter;
    private int WIDTH, HEIGHT, scrollHeight;
    private int current_read_img_order;//当前的图片位置

    private long comic_id,Chapter_id;
    private String Chapter_title = "";
    private boolean first = true;
    private ComicChapter CurrentComicChapter;
    private View footView;

    private boolean canScrollVertically;
    private boolean isTop;
    private int totalDy;
    private int MaxTop;
    private boolean IsDragging;

    private BaseAd baseAd;
    private PublicPurchaseDialog purchaseDialog;

    private int selModel = 1;

    private String vipContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 用于控制软件盘
        AndroidBug5497Workaround.assistActivity(activity, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (comicLookDanmuView != null && comicLookDanmuView.isPrepared() &&
                isIsComicDanmu(activity) && !MenuSHOW) {
            comicLookDanmuView.resume();
        }
    }

    @Override
    public int initContentView() {
        USE_EventBus = true;
        FULL_CCREEN = true;
        return R.layout.activity_comiclook;
    }

    @Override
    public void initView() {
        ViewGroup.LayoutParams layoutParams = activity_comiclook_head.getLayoutParams();
        layoutParams.height = MaxTop = ImageUtil.dp2px(activity, 50) + TOP_HEIGTH;
        activity_comiclook_head.setLayoutParams(layoutParams);
        noresult_try.setVisibility(View.VISIBLE);
        noresult_try.setText(LanguageUtil.getString(activity, R.string.app_try));
        noresult_try.setBackground(MyShape.setMyshapeStroke(activity, 5, 1,
                ContextCompat.getColor(activity, R.color.maincolor), Color.TRANSPARENT));
        activity_comiclook_pinglunshu.setBackground(MyShape.setMyshapeOVAL(ContextCompat.getColor(activity, R.color.red)));
        // 设置弹幕UI
        setDanmuLayout();
        Glide.with(activity)
                .load(R.drawable.bianfu)
                .into(lordingImage);
        HEIGHT = ScreenSizeUtils.getInstance(activity).getScreenHeight();
        WIDTH = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        scrollHeight = HEIGHT / 5 * 4;
        initRecyclerview();
        avLoadingIndicatorView.setIndicator(new LineSpinFadeLoaderIndicator());
        if (ShareUitls.getBoolean(activity, "yejian_ToggleButton", false)) {
            BrightnessUtil.setBrightness(this, 30);
        }
        // 菜单
        showMenu(true);
        comic = (Comic) formIntent.getSerializableExtra("baseComic");
        comic_id = comic.getComic_id();
        if (comic.is_read == 0) {
            comic.is_read = 1;
            comic.isRecommend = false;
            ObjectBoxUtils.addData(comic, Comic.class);
            EventBus.getDefault().post(new RefreshShelfCurrent(Constant.COMIC_CONSTANT, comic));
        }
        Chapter_id = comic.current_chapter_id;
        // 获取目录
        getCatalog();
        initEditTextListener();

        //监听RecyclerView滚动状态
        activity_comiclook_RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(recyclerView.getLayoutManager() != null) {
                    getPositionAndOffset();
                }
            }
        });
    }

    private void initEditTextListener() {
        activity_comiclook_danmu_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isShowFocus = true;
                    isHideFocus = true;
                }
            }
        });
        activity_comiclook_danmu_edit.clearFocus();
        activity_comiclook_danmu_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendTanmuOrComment();
                }
                return false;
            }
        });
    }

    private int lastOffset;
    private int lastPosition;

    /**
     * 记录RecyclerView当前位置
     */
    private void getPositionAndOffset() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) activity_comiclook_RecyclerView.getLayoutManager();
        //获取可视的第一个view
        View topView = layoutManager.getChildAt(0);
        if(topView != null) {
            //获取与该view的顶部的偏移量
            lastOffset = topView.getTop();
            //得到该View的数组位置
            lastPosition = layoutManager.getPosition(topView);
        }
    }

    @OnClick({R.id.titlebar_back,
            R.id.activity_comiclook_shoucang,
            R.id.activity_comiclook_dingbu,
            R.id.activity_comiclook_danmu_layout,
            R.id.activity_comiclook_danmu_fashe,
            R.id.activity_comiclook_xiayihua_layout,
            R.id.activity_comiclook_shangyihua_layout,
            R.id.activity_comiclook_set,
            R.id.activity_comiclook_tucao_layout,
            R.id.activity_comiclook_share,
            R.id.activity_comiclook_xiazai,
            R.id.activity_comiclook_quanji,
            R.id.activity_comiclook_danmu_img2,
            R.id.activity_comiclook_foot,
            R.id.activity_comic_look_danmu_layout_bg,
            R.id.fragment_option_noresult_try,
            R.id.activity_comiclook_none})
    public void getEvent(View view) {
        long ClickTimeNew = System.currentTimeMillis();
        if (ClickTimeNew - ClickTime > AgainTime) {
            ClickTime = ClickTimeNew;
            switch (view.getId()) {
                case R.id.titlebar_back:
                    finish();
                    break;
                case R.id.activity_comiclook_foot:
                case R.id.activity_comic_look_danmu_layout_bg:
                    // 防止透传
                    break;
                case R.id.activity_comiclook_shoucang:
                    comic.is_collect = 1;
                    ObjectBoxUtils.addData(comic, Comic.class);
                    EventBus.getDefault().post(new RefreshShelf(Constant.COMIC_CONSTANT, new RefreshComicShelf(comic, 1)));
                    EventBus.getDefault().post(new RefreshShelfCurrent(Constant.COMIC_CONSTANT, comic));
                    activity_comiclook_shoucang.setVisibility(View.GONE);
                    MyToash.ToashSuccess(activity, LanguageUtil.getString(this, R.string.BookInfoActivity_jiarushujias));
                    break;
                case R.id.activity_comiclook_dingbu:
                    // 顶部
                    activity_comiclook_RecyclerView.scrollToPosition(0);
                    break;
                case R.id.activity_comiclook_danmu_layout:
                    if (isIsComicDanmu(activity)) {
                        setComicIsDanmu(activity, false);
                        activity_comiclook_danmu_img.setImageResource(R.mipmap.comic_danmu_no);
                        if (!CommentFlag) {
                            CommentFlag = true;
                            activity_comiclook_danmu_img2.setImageResource(R.mipmap.comic_comment);
                            activity_comiclook_danmu_edit.setHint(LanguageUtil.getString(activity, R.string.ComicLookActivity_fapinglun));
                            activity_comiclook_danmu_fashe.setText(LanguageUtil.getString(activity, R.string.ComicLookActivity_fasong));
                        }
                        hideDanmu();
                    } else {
                        setComicIsDanmu(activity, true);
                        activity_comiclook_danmu_img.setImageResource(R.mipmap.comic_danmu);
                        showDanmu();
                        if (CommentFlag) {
                            CommentFlag = false;
                            activity_comiclook_danmu_img2.setImageResource(R.mipmap.comic_tan);
                            activity_comiclook_danmu_edit.setHint(LanguageUtil.getString(activity, R.string.ComicLookActivity_fadanmu));
                            activity_comiclook_danmu_fashe.setText(LanguageUtil.getString(activity, R.string.ComicLookActivity_fashe));
                        }
                        // 请求弹幕
                        currentDanmuPage = 0;
                        postDanmuBean(Chapter_id);
                    }
                    break;
                case R.id.activity_comiclook_danmu_fashe:
                    sendTanmuOrComment();
                    break;
                case R.id.activity_comiclook_xiayihua_layout:
                    if (comicChapterItem != null && comicChapterItem.next_chapter != 0) {
                        getData(activity, comic_id, comicChapterItem.next_chapter, true, true);
                    } else {
                        MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.ComicLookActivity_end));
                    }
                    break;
                case R.id.activity_comiclook_shangyihua_layout:
                    if (comicChapterItem != null && comicChapterItem.last_chapter != 0) {
                        getData(activity, comic_id, comicChapterItem.last_chapter, true, true);
                    } else {
                        MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.ComicLookActivity_start));
                    }
                    break;
                case R.id.activity_comiclook_tucao_layout:
                    // 底部评论图标
                    Intent intent1 = new Intent(activity, CommentActivity.class)
                            .putExtra("current_id", comic_id)
                            .putExtra("chapter_id", Chapter_id)
                            .putExtra("is_from_comic", true)
                            .putExtra("productType", Constant.COMIC_CONSTANT);
                    startActivityForResult(intent1, 111);
                    overridePendingTransition(R.anim.activity_bottom_top_open, R.anim.activity_stay);
                    break;
                case R.id.activity_comiclook_share:
                    if (comic != null) {
                        MyShare.chapterShare(activity, comic.getComic_id(), comic.current_chapter_id, 2);
                    }
                    break;
                case R.id.activity_comiclook_set:
                    LookComicSetDialog.getLookComicSetDialog(activity,selModel, new LookComicSetDialog.LookComicSetDialogListener() {
                        @Override
                        public void changeReaderMode(int mode) {
                            if (mode == selModel) return;
                            //TODO:更改模式
                            if (mode == 1) {
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
                                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                activity_comiclook_RecyclerView.setLayoutManager(linearLayoutManager);
                                comicChapterCatalogAdapter.setV(false);
                                comicChapterCatalogAdapter.notifyDataSetChanged();

                            } else {
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
                                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                activity_comiclook_RecyclerView.setLayoutManager(linearLayoutManager);
                                comicChapterCatalogAdapter.setV(true);
                                comicChapterCatalogAdapter.notifyDataSetChanged();
                            }
                            selModel = mode;
                            scrollToPosition();
                        }
                    });
                    break;
                case R.id.activity_comiclook_xiazai:
                    Intent intentxiazai = new Intent(activity, ComicDownActivity.class);
                    intentxiazai.putExtra("baseComic", comic);
                    startActivity(intentxiazai);
                    break;
                case R.id.activity_comiclook_danmu_img2:
                    CommentFlag = !CommentFlag;
                    if (!CommentFlag) {
                        activity_comiclook_danmu_img2.setImageResource(R.mipmap.comic_tan);
                        activity_comiclook_danmu_edit.setHint(LanguageUtil.getString(activity, R.string.ComicLookActivity_fadanmu));
                        activity_comiclook_danmu_fashe.setText(LanguageUtil.getString(activity, R.string.ComicLookActivity_fashe));
                        if (!isIsComicDanmu(activity)) {
                            setComicIsDanmu(activity, true);
                            activity_comiclook_danmu_img.setImageResource(R.mipmap.comic_danmu);
                            showDanmu();
                        }
                    } else {
                        activity_comiclook_danmu_img2.setImageResource(R.mipmap.comic_comment);
                        activity_comiclook_danmu_edit.setHint(LanguageUtil.getString(activity, R.string.ComicLookActivity_fapinglun));
                        activity_comiclook_danmu_fashe.setText(LanguageUtil.getString(activity, R.string.ComicLookActivity_fasong));
                    }
                    break;
                case R.id.activity_comiclook_quanji:
                    Intent intent = new Intent(activity, ComicinfoMuluActivity.class);
                    intent.putExtra("comicname", comic.name);
                    intent.putExtra("baseComic", comic);
                    intent.putExtra("currentChapter_id", Chapter_id);
                    startActivity(intent);
                   // showMenu(false);
                    break;
                case R.id.fragment_option_noresult_try:
                    // 重试
                    if (comicChapter == null || comicChapter.isEmpty()) {
                        // 获取目录
                        getCatalog();
                    } else {
                        getData(this, comic_id, Chapter_id, true, true);
                    }
                    break;
                case R.id.activity_comiclook_none:
                    break;
            }
        }
    }

    /**
     * 让RecyclerView滚动到指定位置
     */
    private void scrollToPosition() {
        if(activity_comiclook_RecyclerView.getLayoutManager() != null && lastPosition >= 0) {
            ((LinearLayoutManager) activity_comiclook_RecyclerView.getLayoutManager()).scrollToPositionWithOffset(lastPosition, lastOffset);
        }
    }


    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    private void sendTanmuOrComment() {
        String danmutext = activity_comiclook_danmu_edit.getText().toString();
        if (TextUtils.isEmpty(danmutext) || Pattern.matches("\\s*", danmutext)) {
            MyToash.ToashError(activity, LanguageUtil.getString(this, R.string.CommentListActivity_some));
            return;
        }
        if (!CommentFlag && isIsComicDanmu(activity)) {
            if (goToLogin(activity)) {
                sendTucao(danmutext);
                activity_comiclook_danmu_edit.setText("");
            }
        } else {
            CommentActivity.sendComment(activity, false, Constant.COMIC_CONSTANT, comic_id,
                    "", Chapter_id, danmutext, new CommentActivity.SendSuccess() {
                        @Override
                        public void Success(Comment c) {
                            if (c != null) {
                                setLookComment(c.comment_num);
                            }
                        }
                    });
            activity_comiclook_danmu_edit.setText("");
        }
        Input.getInstance().hindInput(activity_comiclook_danmu_edit, activity);
    }

    /**
     * 购买
     */
    private void getBuy() {
        refreshLayout.setEnableLoadMore(false);
        purchaseDialog = new PublicPurchaseDialog(activity,vipContent, Constant.COMIC_CONSTANT, false, new PublicPurchaseDialog.BuySuccess() {
            @Override
            public void buySuccess(long[] ids, int num) {
                refreshLayout.setEnableLoadMore(true);
                map.remove(Chapter_id);
                getData(activity, comic_id, Chapter_id, true, true);
            }
        }, false);
        // 监听事件
        purchaseDialog.setOnPurchaseClickListener(new PublicPurchaseDialog.OnPurchaseClickListener() {
            @Override
            public void onBack() {
                purchaseDialog.dismiss();
                ComicLookActivity.this.finish();
            }

            @Override
            public void onGotoList() {
                Intent intent = new Intent(activity, ComicinfoMuluActivity.class);
                intent.putExtra("comicname", comic.name);
                intent.putExtra("baseComic", comic);
                intent.putExtra("currentChapter_id", Chapter_id);
                startActivity(intent);
            }
        });
        purchaseDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                finish();
                return true;
            }
        });
        purchaseDialog.initData(comic_id, Chapter_id + "", false, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity_comiclook_RecyclerView.setContextClickable(false);
        }
        purchaseDialog.show();
    }

    private void backRefresh() {
        if (CurrentComicChapter != null) {
            CurrentComicChapter.current_read_img_order = current_read_img_order;
            ObjectBoxUtils.addData(CurrentComicChapter, ComicChapter.class);
            EventBus.getDefault().post(new ChapterBuyRefresh(COMIC_CONSTANT, true, CurrentComicChapter.chapter_id));
        }
    }

    public interface ItemOnclick {

        void onClick(int position, BaseComicImage baseComicImage);
    }

    private ItemOnclick itemOnclick = new ItemOnclick() {
        @Override
        public void onClick(int position, BaseComicImage baseComicImagee) {

        }
    };

    private SComicRecyclerView.OnTouchListener onTouchListener = new SComicRecyclerView.OnTouchListener() {
        @Override
        public void clickScreen(float x, float y, float RawY) {
            if (!click_next) {
                boolean fanye_ToggleButton = ShareUitls.getBoolean(activity, "fanye_ToggleButton", true);
                if (!fanye_ToggleButton) {
                    if (!MenuSHOW || !isTop) {
                        showMenu(!MenuSHOW);
                    }
                } else {
                    if (y <= HEIGHT / 3) {
                        activity_comiclook_RecyclerView.smoothScrollBy(0, -scrollHeight);
                        if (!isTop) {
                            showMenu(false);
                        }
                    } else if (y <= HEIGHT * 2 / 3) {
                        if (!MenuSHOW || !isTop) {
                            showMenu(!MenuSHOW);
                        }
                    } else {
                        activity_comiclook_RecyclerView.smoothScrollBy(0, scrollHeight);
                        showMenu(false);
                    }
                }
                Input.getInstance().hindInput(activity_comiclook_danmu_edit, activity);
            }
            click_next = false;
        }
    };

    public void showMenu(boolean VISIBLE) {
        if (VISIBLE) {
            if (activity_comiclook_head.getVisibility() == View.GONE) {
                ImmersionBar.with(this)
                        .hideBar(BarHide.FLAG_SHOW_BAR)
                        .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                        .init();
                MenuSHOW = true;
                Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.menu_ins);
                activity_comiclook_head.startAnimation(topAnim);
                activity_comiclook_head.setVisibility(View.VISIBLE);
                Animation footAnim = AnimationUtils.loadAnimation(this, R.anim.lookcomic_buttom_ins);
                activity_comiclook_foot.startAnimation(footAnim);
                activity_comiclook_foot.setVisibility(View.VISIBLE);
                activity_comiclook_danmu_layout.setVisibility(View.VISIBLE);
                fragment_comicinfo_mulu_dangqian_layout.setVisibility(View.VISIBLE);
                if (isIsComicDanmu(activity) && comicLookDanmuView != null && comicLookDanmuView.isPrepared()) {
                    comicLookDanmuView.pause();
                }
            }
        } else {
            if (activity_comiclook_head.getVisibility() == View.VISIBLE) {
                ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
                activity_comiclook_danmu_layout.setVisibility(View.GONE);
                fragment_comicinfo_mulu_dangqian_layout.setVisibility(View.GONE);
                MenuSHOW = false;
                Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.menu_outs);
                if (activity_comiclook_head.getVisibility() == View.VISIBLE) {
                    activity_comiclook_head.startAnimation(topAnim);
                    activity_comiclook_head.setVisibility(View.GONE);
                }
                Animation footAnim = AnimationUtils.loadAnimation(this, R.anim.lookcomic_buttom_outs);
                activity_comiclook_foot.startAnimation(footAnim);
                activity_comiclook_foot.setVisibility(View.GONE);
                if (isIsComicDanmu(activity) && comicLookDanmuView != null && comicLookDanmuView.isPrepared()) {
                    showDanmu();
                    comicLookDanmuView.resume();
                }
            }
        }
    }

    /**
     * 设置弹幕Layout
     */
    public void setDanmuLayout() {
        FrameLayout.LayoutParams danmuParams = (FrameLayout.LayoutParams) comicLookDanmuView.getLayoutParams();
        danmuParams.topMargin = GETNotchHeight(activity) + ImageUtil.dp2px(activity, 60);
        danmuParams.height = ScreenSizeUtils.getScreenHeight(activity) / 3;
        comicLookDanmuView.setLayoutParams(danmuParams);

        activity_comic_look_danmu_layout_bg.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 50), Color.WHITE));
        activity_comiclook_danmu_layout.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 60), "#4d000000"));
        if (!isIsComicDanmu(activity)) {
            activity_comiclook_danmu_img.setImageResource(R.mipmap.comic_danmu_no);
            CommentFlag = true;
            activity_comiclook_danmu_img2.setImageResource(R.mipmap.comic_comment);
            activity_comiclook_danmu_edit.setHint(LanguageUtil.getString(activity, R.string.ComicLookActivity_fapinglun));
            activity_comiclook_danmu_fashe.setText(LanguageUtil.getString(activity, R.string.ComicLookActivity_fasong));
        }


        mDanmakuContext = DanmakuContext.create();
        HashMap<Integer, Integer> maxLine = new HashMap<>();
        maxLine.put(BaseDanmaku.TYPE_SCROLL_RL, 6);// 滚动弹幕最大显示3行
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
        mDanmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3)//设置描边样式
                .setDuplicateMergingEnabled(false)//是否启用合并重复弹幕
                .setScrollSpeedFactor(1.6f) //设置弹幕滚动速度系数,只对滚动弹幕有效
                .setScaleTextSize(1.2f)//设置字体缩放
                .setMaximumLines(maxLine)//设置最大显示行数
                .preventOverlapping(overlappingEnablePair);//设置防弹幕重叠
        mParser = new BaseDanmakuParser() {
            @Override
            protected IDanmakus parse() {
                return new Danmakus();
            }
        };
        setDanmuCallck();
    }

    private void initRecyclerview() {
        footView = LayoutInflater.from(this).inflate(R.layout.activity_comic_look_foot, null);
        holderFoot = new MyViewHolder(footView);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setDisableContentWhenRefresh(false);
        refreshLayout.setRefreshFooter(new ClassicsFooter(activity));//设置Footer
        refreshLayout.setDisableContentWhenLoading(false);//是否在加载的时候禁止列表的操作
        refreshLayout.setOnMultiPurposeListener(new OnMultiPurposeListener() {
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
              //MyToash.Log("onRefresh", " 222");
            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int maxDragHeight) {

            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {

            }

            @Override
            public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {
                if (percent > 1) {
                    if (IsDragging != isDragging) {
                        IsDragging = isDragging;
                        if (isDragging) {
                            holderFoot.activity_comiclook_xiayihua_foot_move.setText(LanguageUtil.getString(activity, R.string.app_load_more_now));
                        } else {
                            holderFoot.activity_comiclook_xiayihua_foot_move.setText(LanguageUtil.getString(activity, R.string.ComicLookActivity_end));
                            click_next = true;
                            if (comicChapterItem != null && !activity_comiclook_RecyclerView.canScrollVertically(1)) {
                                if (comicChapterItem.next_chapter != 0) {
                                    getData(activity, comic_id, comicChapterItem.next_chapter, true, true);
                                } else {
                                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.ComicLookActivity_end));
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {
                //MyToash.Log("onFooterMoving2", " ");
            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int maxDragHeight) {
                //MyToash.Log("onFooterMoving13", " ");
            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {

            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                // MyToash.Log("onFooterMoving5", " ");
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                ////MyToash.Log("onRefresh", " 1111");
            }

            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
                // MyToash.Log("onRefresh", " 333");
            }
        });
        activity_comiclook_RecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        activity_comiclook_RecyclerView.setTouchListener(onTouchListener);
        activity_comiclook_RecyclerView.setEnableScale(true);
        activity_comiclook_RecyclerView.setScrollViewListener(new SComicRecyclerView.ScrollViewListener() {
            @Override
            public void onScroll(int FirstCompletelyVisibleItemPosition, int FirstVisibleItemPosition,
                                 int LastCompletelyVisibleItemPosition, int LastVisibleItemPosition) {
                current_read_img_order = (FirstCompletelyVisibleItemPosition <= 0) ? FirstVisibleItemPosition : FirstCompletelyVisibleItemPosition;
            }
        });
        activity_comiclook_RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalDy -= dy;
                boolean tempIsTop = (!activity_comiclook_RecyclerView.canScrollVertically(-1) || Math.abs(totalDy) < MaxTop);
                boolean tempIsBottom = !activity_comiclook_RecyclerView.canScrollVertically(1);
                if (tempIsTop || tempIsBottom) {
                    isTop = true;
                } else {
                    if (Math.abs(totalDy) >= MaxTop) {
                        isTop = false;
                    }
                }
                if (tempIsTop || tempIsBottom) {
                    showMenu(true);
                    canScrollVertically = true;
                } else {
                    if (!canScrollVertically) {
                        if (isShowFocus) {
                            isShowFocus = false;
                        } else if (isHideFocus) {
                            isHideFocus = false;
                            Input.getInstance().hindInput(activity_comiclook_danmu_edit, activity);
                            activity_comiclook_danmu_edit.clearFocus();
                        } else {
                            if (!isTop) {
                                showMenu(false);
                            }
                        }
                    }
                    canScrollVertically = false;
                }
            }
        });
    }

    /**
     * 设置数据
     *
     * @param activity
     * @param comic_id
     * @param chapter_id
     * @param HandleData
     * @param isChangeChapter
     */
    public void getData(Activity activity, long comic_id, long chapter_id, boolean HandleData, boolean isChangeChapter) {
        if (chapter_id != 0) {
            if (isChangeChapter) {
                Chapter_id = chapter_id;
                // 切换章节，需要重新获取弹幕
                currentDanmuPage = 0;
                postDanmuBean(chapter_id);
            }
            if (HandleData) {
                setBottomText(false);
            }
            ComicChapterItem comicChapterItem = map.get(chapter_id);
            if (comicChapterItem != null && comicChapterItem.is_preview == 0) {
                if (HandleData) {
                    HandleData(comicChapterItem, chapter_id, comic_id, activity);
                }
                return;
            }
            http_flag = 1;
            readerParams = new ReaderParams(this);
            readerParams.putExtraParams("comic_id", comic_id);
            readerParams.putExtraParams("chapter_id", chapter_id);
            HttpUtils.getInstance().sendRequestRequestParams(activity,Api.COMIC_chapter, readerParams.generateParamsJson(),  new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(final String result) {
                            ComicChapterItem comicChapterItem = gson.fromJson(result, ComicChapterItem.class);
                            vipContent = comicChapterItem.recharge_content;
                            map.put(chapter_id, comicChapterItem);
                            if (HandleData) {
                                HandleData(comicChapterItem, chapter_id, comic_id, activity);
                            }
                            Intent intent = new Intent();
                            intent.setAction(BuildConfig.APPLICATION_ID + ".ui.service.DownComicService");
                            intent.setPackage(BuildConfig.APPLICATION_ID);
                            String fileName = System.currentTimeMillis() + "";
                            boolean re = FileManager.writeToXML(fileName, result);
                            if (re) {
                                intent.putExtra("isOne", true);
                                intent.putExtra("comic_id", comic_id);
                                intent.putExtra("result", fileName);
                                startService(intent);
                            }
                        }

                        @Override
                        public void onErrorResponse(String ex) {
                            if (!TextUtils.isEmpty(ex)) {
                                if (ex.equals("no_net")) {
                                    ComicChapter comicChapter = getCurrentComicChapter(chapter_id);
                                    if (comicChapter != null) {
                                        String s = comicChapter.ImagesText;
                                        if (s != null) {
                                            ComicChapterItem comicChapterItem = gson.fromJson(s, ComicChapterItem.class);
                                            map.put(chapter_id, comicChapterItem);
                                            if (HandleData) {
                                                HandleData(comicChapterItem, chapter_id, comic_id, activity);
                                            }
                                        } else {
                                            if (HandleData) {
                                                showMenu(true);
                                                titlebar_text.setText(comicChapter.chapter_title);
                                                Chapter_title = comicChapter.chapter_title;
                                                Chapter_id = comicChapter.chapter_id;
                                                CurrentComicChapter = comicChapter;
                                                ComicLookActivity.this.comicChapterItem = new ComicChapterItem();
                                                ComicLookActivity.this.comicChapterItem.chapter_id = Chapter_id;
                                                ComicLookActivity.this.comicChapterItem.last_chapter = CurrentComicChapter.last_chapter;
                                                ComicLookActivity.this.comicChapterItem.next_chapter = CurrentComicChapter.next_chapter;
                                                ComicLookActivity.this.comicChapterItem.comic_id = CurrentComicChapter.comic_id;
                                                baseComicImages.clear();
                                                comicChapterCatalogAdapter.notifyDataSetChanged();
                                                activity_comiclook_RecyclerView.setVisibility(View.GONE);
                                                activity_comiclook_lording.setVisibility(View.GONE);
                                                setBottomText(true);
                                                activity_comiclook_none.setVisibility(View.VISIBLE);
                                                setNoResultText();
                                            }
                                        }
                                    }
                                } else if (ex.equals("750")) {
                                    activity_comiclook_RecyclerView.setVisibility(View.GONE);
                                    activity_comiclook_lording.setVisibility(View.GONE);
                                    setBottomText(true);
                                    activity_comiclook_none.setVisibility(View.VISIBLE);
                                }
                                lordingImage.setVisibility(View.GONE);
                            }
                        }
                    }
            );
        } else {
            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.ReadActivity_chapterfail));
        }
    }

    private void UpdateLocalData(ComicChapterItem comicChapterItem) {
        if (comicChapterItem.is_preview != CurrentComicChapter.is_preview) {
            CurrentComicChapter.is_preview = comicChapterItem.is_preview;
        }
        CurrentComicChapter.IsRead = true;
        ReadHistory.addReadHistory(activity, Constant.COMIC_CONSTANT, comic_id, CurrentComicChapter.chapter_id);
        ObjectBoxUtils.addData(CurrentComicChapter, ComicChapter.class);
    }

    private ComicChapter getCurrentComicChapter(long chapter_id) {
        return ObjectBoxUtils.getComicChapter(chapter_id);
    }

    /**
     * 更新内容
     *
     * @param comicChapterItem
     * @param chapter_id
     * @param comic_id
     * @param activity
     */
    private void HandleData(ComicChapterItem comicChapterItem, long chapter_id, long comic_id, Activity activity) {
        try {
            holderFoot.activity_comiclook_xiayihua_foot_move.setText(LanguageUtil.getString(activity, R.string.app_load_more));
            this.comicChapterItem = comicChapterItem;
            if (comicChapterItem != null && !comicChapterItem.image_list.isEmpty()) {
                activity_comiclook_RecyclerView.setVisibility(View.VISIBLE);
                activity_comiclook_none.setVisibility(View.GONE);
                titlebar_text.setText(comicChapterItem.chapter_title);
                Chapter_title = comicChapterItem.chapter_title;
                Chapter_id = comicChapterItem.chapter_id;
                CurrentComicChapter = getCurrentComicChapter(Chapter_id);
                current_read_img_order=CurrentComicChapter.current_read_img_order;
                if (comicChapterItem.is_preview == 1) {
                    if (CurrentComicChapter.is_preview != comicChapterItem.is_preview) {
                        CurrentComicChapter.is_preview = comicChapterItem.is_preview;
                        ObjectBoxUtils.addData(CurrentComicChapter, ComicChapter.class);
                    }
                    getBuy();
                } else {
                    if (CurrentComicChapter.is_preview != comicChapterItem.is_preview) {
                        CurrentComicChapter.is_preview = comicChapterItem.is_preview;
                        ObjectBoxUtils.addData(CurrentComicChapter, ComicChapter.class);
                        EventBus.getDefault().post(new ChapterBuyRefresh(COMIC_CONSTANT, 1, new long[]{CurrentComicChapter.chapter_id}));
                    }
                    refreshLayout.setEnableLoadMore(true);
                    if (purchaseDialog != null && purchaseDialog.isShowing()) {
                        purchaseDialog.dismiss();
                    }
                }
                comic.current_chapter_id = Chapter_id;
                comic.current_display_order = CurrentComicChapter.display_order;
                comic.current_chapter_name = Chapter_title;
                // 刷新书架这本漫画的最近阅读章节ID
                EventBus.getDefault().post(new RefreshShelfCurrent(Constant.COMIC_CONSTANT, comic));
                ObjectBoxUtils.addData(comic, Comic.class);
                baseComicImages.clear();
                for (BaseComicImage baseComicImage : comicChapterItem.image_list) {
                    baseComicImage.chapter_id = chapter_id;
                    baseComicImage.comic_id = comic_id;
                }
                baseComicImages.addAll(comicChapterItem.image_list);
                if (first&&comicChapterItem.is_preview == 0) {
                    comicChapterCatalogAdapter.notifyDataSetChanged();
                   if(current_read_img_order>0) {
                       activity_comiclook_RecyclerView.scrollToPosition(current_read_img_order);
                   }
                    activity_comiclook_lording.setVisibility(View.GONE);
                    setLookComment(comicChapterItem.total_comment);
                    first = false;
                } else {
                    comicChapterCatalogAdapter.notifyDataSetChanged();
                    activity_comiclook_RecyclerView.scrollToPosition(0);
                }

                UpdateLocalData(comicChapterItem);
                if (comicChapterItem.next_chapter == 0) {
                    holderFoot.activity_comiclook_xiayihua_foot.setImageResource(R.mipmap.right_gray);
                    activity_comiclook_xiayihua.setImageResource(R.mipmap.right_gray);
                } else {
                    holderFoot.activity_comiclook_xiayihua_foot.setImageResource(R.mipmap.right_black);
                    activity_comiclook_xiayihua.setImageResource(R.mipmap.right_black);
                    if (comicChapterItem.is_preview == 0) {
                        getData(activity, comic_id, comicChapterItem.next_chapter, false, false);
                    }
                }

                if (comicChapterItem.last_chapter == 0) {
                    holderFoot.activity_comiclook_shangyihua_foot.setImageResource(R.mipmap.left_gray);
                    activity_comiclook_shangyihua.setImageResource(R.mipmap.left_gray);
                } else {
                    holderFoot.activity_comiclook_shangyihua_foot.setImageResource(R.mipmap.left_black);
                    activity_comiclook_shangyihua.setImageResource(R.mipmap.left_black);
                    if (comicChapterItem.is_preview == 0) {
                        getData(activity, comic_id, comicChapterItem.last_chapter, false, false);
                    }
                }
                if (getIsReadBottomAd_COMIC(activity)) {
                    holderFoot.list_ad_view_layout.setVisibility(View.VISIBLE);
                    getWebViewAD(activity);
                }
            } else {
                titlebar_text.setText(comicChapterItem.chapter_title);
                activity_comiclook_RecyclerView.setVisibility(View.GONE);
                activity_comiclook_lording.setVisibility(View.GONE);
                activity_comiclook_none.setVisibility(View.VISIBLE);
                setNoResultText();
            }
        } catch (Exception e) {
        } catch (Error e) {
        } finally {
            setBottomText(true);
        }
    }

    /**
     * 设置评论数
     *
     * @param totalComment
     */
    private void setLookComment(int totalComment) {
        if (totalComment > 0) {
            activity_comiclook_pinglunshu.setVisibility(View.VISIBLE);
            activity_comiclook_pinglunshu.setText(totalComment > 99 ? "99+" : (totalComment + ""));
        } else {
            activity_comiclook_pinglunshu.setVisibility(View.GONE);
        }
    }

    /**
     * 设置提示文字
     */
    private void setNoResultText() {
        if (InternetUtils.internet(activity)) {
            noresult_text.setText(LanguageUtil.getString(activity, R.string.ComicLookActivity_no_bean));
        } else {
            noresult_text.setText(LanguageUtil.getString(activity, R.string.audio_switch_sound_nonet));
        }
    }

    private void setBottomText(boolean isShow) {
        if (isShow) {
            avLoadingIndicatorView.setVisibility(View.GONE);
            activity_comiclook_danmu_dangqianhua.setVisibility(View.VISIBLE);
        } else {
            activity_comiclook_danmu_dangqianhua.setVisibility(View.GONE);
            avLoadingIndicatorView.setVisibility(View.VISIBLE);
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
            if (purchaseDialog != null && purchaseDialog.isShowing()) {
                purchaseDialog.Dismiss();
                initData2();
            }
        }
    }

    /**
     * 打开指定章节
     *
     * @param openComicChapter
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshComicChapterList(OpenComicChapter openComicChapter) {
        if (Chapter_id != openComicChapter.chapter_id) {
            Chapter_id = openComicChapter.chapter_id;
            getData(this, comic_id, Chapter_id, true, true);
        }
    }

    /**
     * 更新当前目录集合的 最近阅读图片记录
     *
     * @param comicChapterte
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshComicChapterList(ComicChapterEventbus comicChapterte) {
        ComicChapter comicChaptert = comicChapterte.comicChapter;
        ComicChapter c = comicChapter.get(comicChaptert.display_order);
        switch (comicChapterte.Flag) {
            case 1://更新下载数据
                c.downStatus = 1;
                c.setImagesText(comicChaptert.ImagesText);
                ObjectBoxUtils.addData(c, ComicChapter.class);
                break;
        }
    }

    /**
     * 用户下载刷新下载数据
     *
     * @param c
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnComicDown(Comic c) {
        if (c.comic_id == comic.comic_id) {
            if (c.down_chapters != 0) {
                comic.down_chapters = c.down_chapters;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            if (data != null) {
                int count = data.getIntExtra("comment_count", 0);
                setLookComment(count);
            }
        }
    }

    public class MyViewHolder {

        @BindView(R.id.activity_comic_look_foot_shangyihua)
        public LinearLayout activity_comic_look_foot_shangyihua;
        @BindView(R.id.activity_comic_look_foot_xiayihua)
        public LinearLayout activity_comic_look_foot_xiayihua;

        @BindView(R.id.activity_comiclook_shangyihua_foot)
        public ImageView activity_comiclook_shangyihua_foot;
        @BindView(R.id.activity_comiclook_xiayihua_foot)
        public ImageView activity_comiclook_xiayihua_foot;

        @BindView(R.id.list_ad_view_layout)
        FrameLayout list_ad_view_layout;

        @BindView(R.id.activity_comiclook_xiayihua_foot_move)
        TextView activity_comiclook_xiayihua_foot_move;

        public MyViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(value = {R.id.activity_comic_look_foot_shangyihua, R.id.activity_comic_look_foot_xiayihua, R.id.list_ad_view_layout})
        public void getEvent(View view) {
            switch (view.getId()) {
                case R.id.activity_comic_look_foot_xiayihua:
                    click_next = true;
                    if (comicChapterItem != null && comicChapterItem.next_chapter != 0) {
                        getData(activity, comic_id, comicChapterItem.next_chapter, true, true);
                    } else {
                        MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.ComicLookActivity_end));
                    }
                    break;
                case R.id.activity_comic_look_foot_shangyihua:
                    click_next = true;
                    if (comicChapterItem != null && comicChapterItem.last_chapter != 0) {
                        getData(activity, comic_id, comicChapterItem.last_chapter, true, true);
                    } else {
                        MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.ComicLookActivity_start));
                    }
                    break;
                case R.id.list_ad_view_layout:
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (comicLookDanmuView != null && comicLookDanmuView.isPrepared() && isIsComicDanmu(activity)) {
            comicLookDanmuView.pause();
        }
    }

    @Override
    public void finish() {
        backRefresh();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.clear();
        danmuBeanList.clear();
        if (comicLookDanmuView != null) {
            // 释放资源
            comicLookDanmuView.release();
            comicLookDanmuView = null;
        }
    }

    /**
     * 获取目录
     */
    private void getCatalog() {
        comic.GetCOMIC_catalog(activity, new ComicinfoMuluFragment.GetCOMIC_catalogList() {
            @Override
            public void GetCOMIC_catalogList(List<ComicChapter> comicChapterList) {
                if (comicChapterList != null && !comicChapterList.isEmpty()) {
                    comicChapter = comicChapterList;
                    if (Chapter_id == 0) {
                        Chapter_id = comicChapter.get(0).chapter_id;
                    }
                    initData2();
                } else {
                    MyToash.ToashError(activity, R.string.chapterupdateing);
                    activity_comiclook_RecyclerView.setVisibility(View.GONE);
                    activity_comiclook_lording.setVisibility(View.GONE);
                    activity_comiclook_none.setVisibility(View.VISIBLE);
                    setBottomText(true);
                    setNoResultText();
                }
                lordingImage.setVisibility(View.GONE);
            }
        });
    }

    public void initData2() {
        CurrentComicChapter = getCurrentComicChapter(Chapter_id);
        if (comic.is_collect == 1) {
            activity_comiclook_shoucang.setVisibility(View.GONE);
        }
        baseComicImages = new ArrayList<>();
        comicChapterCatalogAdapter = new ComicRecyclerViewAdapter(activity, WIDTH, MaxTop,
                baseComicImages, footView, itemOnclick);
        activity_comiclook_RecyclerView.setAdapter(comicChapterCatalogAdapter);
        getData(activity, comic_id, Chapter_id, true, true);
    }

    /**
     * 请求广告
     *
     * @param activity
     */
    private void getWebViewAD(Activity activity) {
        if (baseAd == null) {
            AdHttp.getWebViewAD(activity, 2, 8, new AdHttp.GetBaseAd() {
                @Override
                public void getBaseAd(BaseAd baseAdd) {
                    baseAd = baseAdd;
                    if (baseAd != null && activity != null && holderFoot != null &&  holderFoot.list_ad_view_layout != null)  {
                        baseAd.setAd(activity, holderFoot.list_ad_view_layout, 8);
                    }

                }
            });
        } else if (baseAd.ad_type != 1) {
            if (holderFoot != null &&  holderFoot.list_ad_view_layout != null) {
                baseAd.setAd(activity, holderFoot.list_ad_view_layout, 8);
            }
        }
    }

    /***************************************  弹幕  ***********************************/

    /**
     * 发送弹幕
     *
     * @param content
     */
    public void sendTucao(String content) {
        ReaderParams readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("comic_id", comic_id);
        readerParams.putExtraParams("chapter_id", Chapter_id);
        readerParams.putExtraParams("content", content);
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.COMIC_tucao, readerParams.generateParamsJson(),  new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.ComicLookActivity_send_success));
                        activity_comiclook_danmu_edit.setText("");
                        ComicDanmuBean.DanmuBean danmuBean = new ComicDanmuBean.DanmuBean();
                        danmuBean.setContent(content);
                        danmuBean.setColor("#FDF03E");
                        try {
                            if (danmuBeanList.isEmpty()) {
                                danmuBeanList.add(danmuBean);
                            } else {
                                if (danmuPotion < 0) {
                                    danmuPotion = 0;
                                }
                                if (danmuPotion > danmuBeanList.size()) {
                                    danmuPotion = danmuBeanList.size();
                                }
                                danmuBeanList.add(danmuPotion, danmuBean);
                            }
                        } catch (Exception e) {
                            danmuBeanList.add(danmuBean);
                        }
                        setDanmuData(0, true, content, "");
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }
        );
    }

    /**
     * 展示弹幕
     */
    private void showDanmu() {
        if (comicLookDanmuView != null && comicLookDanmuView.isPrepared() && !comicLookDanmuView.isShown()) {
            comicLookDanmuView.show();
        }
    }

    /**
     * 隐藏弹窗
     */
    private void hideDanmu() {
        if (comicLookDanmuView != null && comicLookDanmuView.isPrepared() && comicLookDanmuView.isShown()) {
            comicLookDanmuView.hide();
        }
    }

    /**
     * 请求弹幕数据
     *
     * @param chapterId
     */
    private void postDanmuBean(long chapterId) {
        if (currentDanmuPage == 0) {
            comicLookDanmuView.clearDanmakusOnScreen();
            comicLookDanmuView.clear();
        }
        if (comic != null && chapterId != 0 && isIsComicDanmu(activity)) {
            if (currentDanmuPage == 0 || currentDanmuPage < danmuPageCount) {
                ReaderParams params = new ReaderParams(activity);
                params.putExtraParams("comic_id", comic.getComic_id());
                params.putExtraParams("chapter_id", chapterId);
                params.putExtraParams("page_num", currentDanmuPage + 1);
                HttpUtils.getInstance().sendRequestRequestParams(activity,Api.COMIC_BARRAGE, params.generateParamsJson(),  new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            ComicDanmuBean comicDanmuBean = HttpUtils.getGson().fromJson(response, ComicDanmuBean.class);
                            if (comicDanmuBean != null) {
                                currentDanmuPage = comicDanmuBean.current_page;
                                danmuPageCount = comicDanmuBean.total_page;
                                if (comicDanmuBean.current_page == 1) {
                                    danmuBeanList.clear();
                                }
                                if (comicDanmuBean.getList() != null && !comicDanmuBean.getList().isEmpty()) {
                                    danmuBeanList.addAll(comicDanmuBean.getList());
                                    setDanmuBeanList(comicDanmuBean.getList());
                                } else {
                                    setDanmuBeanList(danmuBeanList);
                                }
                            } else {
                                setDanmuBeanList(danmuBeanList);
                            }
                        } else {
                            setDanmuBeanList(danmuBeanList);
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        setDanmuBeanList(danmuBeanList);
                    }
                });
            } else {
                setDanmuBeanList(danmuBeanList);
            }
        }
    }

    /**
     * 添加弹幕
     *
     * @param list
     */
    private void setDanmuBeanList(List<ComicDanmuBean.DanmuBean> list) {
        if (list != null && !list.isEmpty()) {
            danmuPotion = 0;
            for (int i = 0; i < list.size(); i++) {
                ComicDanmuBean.DanmuBean danmuBean = list.get(i);
                setDanmuData(i, false, danmuBean.getContent(), danmuBean.getColor());
            }
        }
    }

    /**
     * 添加弹幕
     *
     * @param index
     * @param isSelf
     * @param content
     * @param color
     */
    private void setDanmuData(int index, boolean isSelf, String content, String color) {
        BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null) {
            return;
        }
        danmaku.text = content;
        danmaku.padding = 10;
        danmaku.priority = 1;
        danmaku.setTime(comicLookDanmuView.getCurrentTime() + index * (new Random().nextInt(4)) * 1000); //显示时间
        danmaku.textSize = 19f * (mParser.getDisplayer().getDensity() - 0.6f);
        if (isSelf) {
            danmaku.textColor = ColorParser.parseCssColor("#FDF03E");
        } else {
            try {
                danmaku.textColor = ColorParser.parseCssColor(color);
            } catch (Exception e) {
                danmaku.textColor = ColorParser.parseCssColor("#ffffff");
            }
        }
        danmaku.textShadowColor = Color.parseColor("#333333");
        comicLookDanmuView.addDanmaku(danmaku);
    }

    private void setDanmuCallck() {
        comicLookDanmuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                // 开始播放弹幕
                comicLookDanmuView.start();
                if (MenuSHOW) {
                    if (!isIsComicDanmu(activity)) {
                        hideDanmu();
                    }
                    comicLookDanmuView.pause();
                }
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {
                danmuPotion++;
                if (MenuSHOW) {
                    if (!isIsComicDanmu(activity)) {
                        hideDanmu();
                    }
                    comicLookDanmuView.pause();
                }
            }

            @Override
            public void drawingFinished() {
                postDanmuBean(Chapter_id);
            }
        });
        comicLookDanmuView.prepare(mParser, mDanmakuContext);
        comicLookDanmuView.enableDanmakuDrawingCache(false);
    }
}
