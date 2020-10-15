package com.ssreader.novel.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ssreader.novel.BuildConfig;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.dialog.VipAlertDialog;
import com.ssreader.novel.eventbus.DownComicEvenbus;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.manager.UserDataEnum;
import com.ssreader.novel.manager.UserManager;
import com.ssreader.novel.model.Comic;
import com.ssreader.novel.model.ComicChapter;
import com.ssreader.novel.model.ComicDownOptionData;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.adapter.ComicDownOptionAdapter;
import com.ssreader.novel.ui.dialog.PublicPurchaseDialog;
import com.ssreader.novel.ui.dialog.WaitDialog;
import com.ssreader.novel.ui.fragment.ComicinfoMuluFragment;
import com.ssreader.novel.ui.service.DownComicService;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.utils.AppCommon;
import com.ssreader.novel.utils.FileManager;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.MyShare;
import com.ssreader.novel.utils.ObjectBoxUtils;
import com.ssreader.novel.utils.SystemUtil;
import com.ssreader.novel.utils.UserUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.AgainTime;
import static com.ssreader.novel.constant.Constant.getCurrencyUnit;

/**
 * 漫画下载界面
 */
public class ComicDownActivity extends BaseActivity {

    @BindView(R.id.activity_comicdown_choose_count)
    public TextView activity_comicdown_choose_count;
    @BindView(R.id.fragment_comicinfo_mulu_zhuangtai)
    public TextView fragment_comicinfo_mulu_zhuangtai;
    @BindView(R.id.activity_comicdown_down)
    public TextView activity_comicdown_down;

    @BindView(R.id.activity_comicdown_gridview)
    public GridView activity_comicdown_gridview;

    @BindView(R.id.fragment_bookshelf_noresult)
    public LinearLayout fragment_bookshelf_noresult;

    @BindView(R.id.fragment_comicinfo_mulu_xu)
    public TextView fragment_comicinfo_mulu_xu;
    @BindView(R.id.fragment_comicinfo_mulu_xu_img)
    public ImageView fragment_comicinfo_mulu_xu_img;
    @BindView(R.id.fragment_comicinfo_mulu_layout)
    public RelativeLayout fragment_comicinfo_mulu_layout;
    @BindView(R.id.activity_comicdown_quanxuan)
    TextView activity_comicdown_quanxuan;

    @BindView(R.id.comic_line_btn)
    TextView comic_line_btn;

    @BindView(R.id.comic_clear_btn)
    TextView comic_clear_btn;


    private boolean shunxu;
    private boolean IsLocal;
    private long comic_id;
    private List<ComicChapter> comicDownOptionList;
    private ComicDownOptionAdapter comicDownOptionAdapter;
    private int downNum = 0;
    private Comic baseComic;
    private int Size;
    private WaitDialog waitDialog;
    private PublicPurchaseDialog purchaseDialog;

    @OnClick(value = {R.id.activity_comicdown_quanxuan, R.id.activity_comicdown_down, R.id.fragment_comicinfo_mulu_layout})
    public void getEvent(View view) {
        long ClickTimeNew = System.currentTimeMillis();
        if (ClickTimeNew - ClickTime > AgainTime) {
            ClickTime = ClickTimeNew;
            try {
                if (comicDownOptionAdapter != null) {
                    switch (view.getId()) {
                        case R.id.activity_comicdown_quanxuan:
                            comicDownOptionAdapter.selectAll(false);
                            break;
                        case R.id.fragment_comicinfo_mulu_layout:
                            shunxu = !shunxu;
                            if (!shunxu) {
                                fragment_comicinfo_mulu_xu.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_zhengxu));
                                fragment_comicinfo_mulu_xu_img.setImageResource(R.mipmap.positive_order);
                            } else {
                                fragment_comicinfo_mulu_xu.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_daoxu));
                                fragment_comicinfo_mulu_xu_img.setImageResource(R.mipmap.reverse_order);
                            }
                            Collections.reverse(comicDownOptionList);
                            comicDownOptionAdapter.notifyDataSetChanged();
                            break;
                        case R.id.activity_comicdown_down:
                            if (!IsLocal) {//下载
                                String Chapter_id = "";
                                for (ComicChapter comicDownOption : comicDownOptionAdapter.comicDownOptionListChooseDwn) {
                                    Chapter_id += "," + comicDownOption.chapter_id;
                                }
                                httpDownChapter(Chapter_id.substring(1));
                            } else {//删除
                                for (ComicChapter comicDownOption : comicDownOptionAdapter.comicDownOptionListChooseDwn) {
                                    comicDownOption.downStatus = 0;
                                    comicDownOption.ImagesText = "";
                                    ObjectBoxUtils.addData(comicDownOption, ComicChapter.class);
                                    String localPath = FileManager.getManhuaSDCardRoot().concat(baseComic.getComic_id() + "/").concat(String.valueOf(comicDownOption.chapter_id));
                                    FileManager.deleteFile(localPath);//删除章节的图片
                                }
                                int size = comicDownOptionAdapter.comicDownOptionListChooseDwn.size();
                                int deleteSize = Size - size;
                                if (deleteSize == 0) {
                                    fragment_bookshelf_noresult.setVisibility(View.VISIBLE);
                                }
                                baseComic.setDown_chapters(deleteSize);
                                ObjectBoxUtils.addData(baseComic, Comic.class);
                                EventBus.getDefault().post(baseComic);//更新上一界面的 数据
                                comicDownOptionList.removeAll(comicDownOptionAdapter.comicDownOptionListChooseDwn);
                                MyToash.ToashSuccess(activity, R.string.local_delete_Success);
                                if (!comicDownOptionList.isEmpty()) {
                                    comicDownOptionAdapter = new ComicDownOptionAdapter(activity, comicDownOptionList, activity_comicdown_choose_count, activity_comicdown_down, IsLocal, activity_comicdown_quanxuan);
                                    activity_comicdown_gridview.setAdapter(comicDownOptionAdapter);
                                    comicDownOptionAdapter.comicDownOptionListChooseDwn.clear();
                                } else {
                                    MyToash.setDelayedFinash(activity);
                                }
                            }
                            break;
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public int initContentView() {
        USE_PUBLIC_BAR = true;
        USE_EventBus = true;
        return R.layout.activity_comic_down;
    }

    @Override
    public void initView() {
        fragment_comicinfo_mulu_xu_img.getLayoutParams();
        fragment_comicinfo_mulu_layout.setVisibility(View.VISIBLE);
        baseComic = (Comic) formIntent.getSerializableExtra("baseComic");
        IsLocal = formIntent.getBooleanExtra("flag", false);
        comic_id = baseComic.getComic_id();
        comicDownOptionList = new ArrayList<>();
        comicDownOptionAdapter = new ComicDownOptionAdapter(activity, comicDownOptionList,
                activity_comicdown_choose_count, activity_comicdown_down, IsLocal, activity_comicdown_quanxuan);
        activity_comicdown_gridview.setAdapter(comicDownOptionAdapter);
        httpData();
        makeBottomViewData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (UserManager.getInstance().getLineData() == UserDataEnum.UserLineData.UserLineNormal) {
            comic_line_btn.setText("普通线路");
        } else {
            comic_line_btn.setText("VIP线路");
        }

        if (UserManager.getInstance().getClearData() == UserDataEnum.UserClearData.UserClearNormal) {
            comic_clear_btn.setText("标清");
        } else {
            comic_clear_btn.setText("超清");
        }
    }

    private void makeBottomViewData() {

        if (UserManager.getInstance().getLineData() == UserDataEnum.UserLineData.UserLineNormal) {
            comic_line_btn.setText("普通线路");
        } else {
            comic_line_btn.setText("VIP线路");
        }

        if (UserManager.getInstance().getClearData() == UserDataEnum.UserClearData.UserClearNormal) {
            comic_clear_btn.setText("标清");
        } else {
            comic_clear_btn.setText("超清");
        }


        //线路/清晰度
        comic_line_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[]lines = {"普通线路","VIP线路"};
                AppCommon.showSelectedDialog(activity, lines, new SCOnItemClickListener() {
                    @Override
                    public void OnItemClickListener(int flag, int position, Object O) {
                        if (!UserUtils.isLogin(activity)) {
                            Intent intent = new Intent();
                            intent.setClass(activity, LoginActivity.class);
                            activity.startActivity(intent);
                            return;
                        }
                        if (position == 0) {
                            UserManager.getInstance().setLineData(UserDataEnum.UserLineData.UserLineNormal);
                            comic_line_btn.setText("普通线路");
                            //TODO:刷新一下
                        } else if (position == 1) {
                            //是否vip
                            if (UserManager.getInstance().getIsVip() == 1) {
                                UserManager.getInstance().setLineData(UserDataEnum.UserLineData.UserLineVip);
                                comic_line_btn.setText("VIP线路");
                                //TODO:刷新一下
                            } else {
                                showVipDialog();
                            }
                        }
                    }

                    @Override
                    public void OnItemLongClickListener(int flag, int position, Object O) {
                    }
                });
            }
        });

        comic_clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[]lines = {"标清","超清"};
                AppCommon.showSelectedDialog(activity, lines, new SCOnItemClickListener() {
                    @Override
                    public void OnItemClickListener(int flag, int position, Object O) {
                        if (!UserUtils.isLogin(activity)) {
                            Intent intent = new Intent();
                            intent.setClass(activity, LoginActivity.class);
                            activity.startActivity(intent);
                            return;
                        }
                        if (position == 0) {
                            UserManager.getInstance().setClearData(UserDataEnum.UserClearData.UserClearNormal);
                            comic_clear_btn.setText("标清");
                            //TODO:刷新一下
                        } else if (position == 1) {
                            //是否vip
                            if (UserManager.getInstance().getIsVip() == 1) {
                                UserManager.getInstance().setClearData(UserDataEnum.UserClearData.UserClearVip);
                                comic_clear_btn.setText("超清");
                                //TODO:刷新一下
                            } else {
                                showVipDialog();
                            }
                        }
                    }

                    @Override
                    public void OnItemLongClickListener(int flag, int position, Object O) {

                    }
                });
            }
        });
    }

    private void showVipDialog() {
        VipAlertDialog dialog = new VipAlertDialog.Builder(activity).setHiddenRecharge(true).setContent("升级VIP后即可享受高清漫画，还有提供国内高速线路！").setVipAlertDialogListener(new VipAlertDialog.VipAlertDialogListener() {
            @Override
            public void onClickGoToVip() {
                activity.startActivity(new Intent(activity, NewRechargeActivity.class)
                        .putExtra("RechargeTitle", LanguageUtil.getString(activity, R.string.BaoyueActivity_title))
                        .putExtra("RechargeType", "vip"));
            }

            @Override
            public void onClickGoToRecharge() {
                activity.startActivity(new Intent(activity, NewRechargeActivity.class)
                        .putExtra("RechargeTitle", getCurrencyUnit(activity) + LanguageUtil.getString(activity, R.string.MineNewFragment_chongzhi))
                        .putExtra("RechargeRightTitle", LanguageUtil.getString(activity, R.string.BaoyueActivity_chongzhijilu))
                        .putExtra("RechargeType", "gold"));
            }

            @Override
            public void onClickGoToShare() {
                new MyShare(activity)
                        .setFlag(Constant.COMIC_CONSTANT)
                        .setId(comic_id)
                        .Share();

            }
        }).create();
        dialog.show();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String result) {
        Collections.sort(comicDownOptionAdapter.comicDownOptionListChooseDwn); // 排序
        for (ComicChapter comicDownOption : comicDownOptionAdapter.comicDownOptionListChooseDwn) {
            comicDownOption.downStatus = 2;
            ++comicDownOptionAdapter.YetDownNum;
        }
        ObjectBoxUtils.addData(comicDownOptionAdapter.comicDownOptionListChooseDwn, ComicChapter.class);
        comicDownOptionAdapter.selectAll(true);
        int num = 0;
        for (ComicChapter comicDownOption : comicDownOptionList) {
            if (comicDownOption.downStatus == 1 || comicDownOption.downStatus == 2) {
                ++num;
            }
        }
        if(num==Size){
            activity_comicdown_quanxuan.setClickable(false);
        }
        MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.BookInfoActivity_down_adddown));
        Intent intent = new Intent();
        intent.setAction(BuildConfig.APPLICATION_ID + ".ui.service.DownComicService");
        intent.setPackage(BuildConfig.APPLICATION_ID);
        String fileName = System.currentTimeMillis() + "";
        boolean re = FileManager.writeToXML(fileName, result);
        if (re) {
            intent.putExtra("comic_id", comic_id);
            intent.putExtra("result", fileName);
            startService(intent);
        }
    }

    public void httpData() {
        if (IsLocal) {
            //下载管理
            comicDownOptionList = ObjectBoxUtils.getcomicDownOptionList(comic_id);
            Size = comicDownOptionList.size();
            if (Size != 0) {
                activity_comicdown_down.setText(LanguageUtil.getString(activity, R.string.app_delete));
                fragment_comicinfo_mulu_zhuangtai.setText(String.format(LanguageUtil.getString(activity, R.string.ComicDownActivity_yixiazai), Size));
                comicDownOptionAdapter = new ComicDownOptionAdapter(activity, comicDownOptionList, activity_comicdown_choose_count, activity_comicdown_down, IsLocal, activity_comicdown_quanxuan);
                activity_comicdown_gridview.setAdapter(comicDownOptionAdapter);
            } else {
                fragment_bookshelf_noresult.setVisibility(View.VISIBLE);
            }
            public_sns_topbar_title.setText(baseComic.name);
        } else {
            public_sns_topbar_title.setText(LanguageUtil.getString(activity, R.string.ComicDownActivity_title));
            ReaderParams params = new ReaderParams(activity);
            params.putExtraParams("comic_id", comic_id);
            String json = params.generateParamsJson();
            httpUtils.sendRequestRequestParams(activity,Api.COMIC_down_option, json,  new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(final String result) {
                            ComicDownOptionData comicDownOptionData = gson.fromJson(result, ComicDownOptionData.class);
                            fragment_comicinfo_mulu_zhuangtai.setText(comicDownOptionData.base_info.display_label);
                            Size = comicDownOptionData.down_list.size();
                            if (Size != 0) {
                                for (ComicChapter comicChapter : comicDownOptionData.down_list) {
                                    ComicChapter comicChapterLocal = ObjectBoxUtils.getComicChapter(comicChapter.chapter_id);
                                    if (comicChapterLocal != null) {
                                        if (comicChapterLocal.downStatus == 1) {
                                            ++comicDownOptionAdapter.YetDownNum;
                                        } else if (comicChapterLocal.downStatus == 2 &&
                                                !SystemUtil.isServiceExisted(activity, DownComicService.class.getName())) {
                                            comicChapterLocal.downStatus = 3;
                                        }
                                        comicChapter.downStatus = comicChapterLocal.downStatus;
                                        if (comicChapter.downStatus == 1) {
                                            ++downNum;
                                        }
                                    }
                                }
                                comicDownOptionList.addAll(comicDownOptionData.down_list);
                                comicDownOptionAdapter.notifyDataSetChanged();
                                if (Size == downNum) {
                                    activity_comicdown_quanxuan.setTextColor(ContextCompat.getColor(activity, R.color.gray_b0));
                                    activity_comicdown_quanxuan.setText(LanguageUtil.getString(activity, R.string.ComicDownActivity_downprocess_all));
                                    activity_comicdown_quanxuan.setClickable(false);
                                    activity_comicdown_down.setVisibility(View.GONE);
                                }
                            }
                        }

                        @Override
                        public void onErrorResponse(String ex) {

                        }
                    }
            );
        }
    }

    /**
     * 下载
     *
     * @param chapter_id
     */
    public void httpDownChapter(String chapter_id) {
        waitDialog = new WaitDialog(activity, 1);
        waitDialog.showDailog();
        baseComic.GetCOMIC_catalog(activity, new ComicinfoMuluFragment.GetCOMIC_catalogList() {
            @Override
            public void GetCOMIC_catalogList(List<ComicChapter> comicChapterList) {
                if (comicChapterList != null && !comicChapterList.isEmpty()) {
                    readerParams = new ReaderParams(activity);
                    readerParams.putExtraParams("comic_id", comic_id);
                    readerParams.putExtraParams("chapter_id", chapter_id);
                    HttpUtils.getInstance().sendRequestRequestParams(activity, Api.COMIC_down, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(String response) {
                            initInfo(response);
                            waitDialog.dismissDialog();
                        }

                        @Override
                        public void onErrorResponse(String ex) {
                            waitDialog.dismissDialog();
                            if (ex != null && ex.equals("701")) {
                                purchaseDialog = new PublicPurchaseDialog(activity,"", Constant.COMIC_CONSTANT, true, new PublicPurchaseDialog.BuySuccess() {
                                    @Override
                                    public void buySuccess(long[] ids, int num) {
                                        for (ComicChapter comicDownOption : comicDownOptionAdapter.comicDownOptionListChooseDwn) {
                                            comicDownOption.is_preview = 1;
                                        }
                                        comicDownOptionAdapter.notifyDataSetChanged();
                                        httpDownChapter(chapter_id);
                                    }
                                }, true);
                                purchaseDialog.initData(comic_id, chapter_id, true, null);
                                purchaseDialog.show();
                            }
                        }
                    });
                }else {
                    MyToash.ToashError(activity, R.string.chapterupdateing);
                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        if (activity == null || comic_id == 0) {
            return;
        }
        if (purchaseDialog != null && purchaseDialog.isShowing()) {
            purchaseDialog.dismiss();
        }
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("comic_id", comic_id);
        HttpUtils.getInstance().sendRequestRequestParams(activity,Api.COMIC_down_option, params.generateParamsJson(), new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JsonParser jsonParser = new JsonParser();
                            JsonArray jsonElements = jsonParser.parse(jsonObject.getString("down_list")).getAsJsonArray();//获取JsonArray对象
                            int i = 0;
                            for (JsonElement jsonElement : jsonElements) {
                                ComicChapter comicChapter = gson.fromJson(jsonElement, ComicChapter.class);
                                comicDownOptionList.get(i).is_preview = comicChapter.is_preview;
                                ++i;
                            }
                            comicDownOptionAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }
        );
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void DownComicEvenbus(DownComicEvenbus downComicEvenbus) {
        try {
            if (downComicEvenbus.comic.getComic_id() == comic_id) {
                if (!downComicEvenbus.flag) {
                    int display_order = downComicEvenbus.comicChapter.display_order;
                    comicDownOptionList.get(display_order).downStatus = downComicEvenbus.comicChapter.downStatus;
                    if (downComicEvenbus.comicChapter.downStatus == 1) {
                        ++downNum;
                    }else {
                        --comicDownOptionAdapter.YetDownNum;
                    }
                    comicDownOptionAdapter.notifyDataSetChanged();
                    if (Size == downNum) {
                        activity_comicdown_quanxuan.setTextColor(ContextCompat.getColor(activity, R.color.gray_b0));
                        activity_comicdown_quanxuan.setText(LanguageUtil.getString(activity, R.string.ComicDownActivity_downprocess_all));
                        activity_comicdown_quanxuan.setClickable(false);
                        activity_comicdown_down.setVisibility(View.GONE);
                    }else{
                        //MyToash.Log("downNum","1111");
                        activity_comicdown_quanxuan.setClickable(true);
                    }
                } else {
                    //MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.BookInfoActivity_down_downcomplete));
                }

            }
        } catch (Exception e) {
        }
    }
}
