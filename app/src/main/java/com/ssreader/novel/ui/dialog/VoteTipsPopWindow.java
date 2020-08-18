package com.ssreader.novel.ui.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.ssreader.novel.R;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.eventbus.BookBottomTabRefresh;
import com.ssreader.novel.eventbus.RefreshMine;
import com.ssreader.novel.model.MineModel;
import com.ssreader.novel.model.PurchaseOption;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.NewRechargeActivity;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.ui.view.SizeAnmotionTextview;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ScreenSizeUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.getCurrencyUnit;

/**
 * 投票提示
 */
public class VoteTipsPopWindow {

    public Activity activity;
    private PopupWindow popupWindow;
    private ViewHolder viewHolder;
    public static long book_id, chapter_id;
    public static int num;

    public VoteTipsPopWindow(Activity activity, String respone) {
        this.activity = activity;
        PurchaseOption purchaseOption = new Gson().fromJson(respone, PurchaseOption.class);
        showVoteTip(purchaseOption);
    }

    public void showVoteTip(PurchaseOption purchaseOption) {
        ScreenSizeUtils.getInstance(activity).setBackgroundAlpha(0.4f, activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_reward_buydialog, null);
        viewHolder = new ViewHolder(view);
        view.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 5), Color.WHITE));
        viewHolder.dialogTips.setText(purchaseOption.title);

        popupWindow = new PopupWindow(view,
                ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 80),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        if (purchaseOption.desc != null && purchaseOption.desc.length > 0) {
            for (String string : purchaseOption.desc) {
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ImageUtil.dp2px(activity, 25));
                SizeAnmotionTextview textView = new SizeAnmotionTextview(activity);
                textView.setMyText(activity, string, 1, ContextCompat.getColor(activity, R.color.add_shelf_bg), 0);
                textView.setTextSize(15);
                textView.setPadding(ImageUtil.dp2px(activity, 38), 0, 0, 0);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                viewHolder.dialogDes.addView(textView, layoutParams1);
            }
        }

        if (purchaseOption.items != null && !purchaseOption.items.isEmpty()) {
            for (MineModel videoInfoPurchaseinfoButtomItem : purchaseOption.items) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, ImageUtil.dp2px(activity, 45));
                SizeAnmotionTextview textView = new SizeAnmotionTextview(activity);
                textView.setMyText(activity, videoInfoPurchaseinfoButtomItem.title, 1,
                        ContextCompat.getColor(activity, R.color.add_shelf_bg), 0);
                textView.setTextSize(15);
                textView.setTextColor(ContextCompat.getColor(activity,R.color.maincolor));
                textView.setGravity(Gravity.CENTER);
                viewHolder.dialogButton.addView(textView, params);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (videoInfoPurchaseinfoButtomItem.action.equals("recharge")) {
                            Intent intent = new Intent(activity, NewRechargeActivity.class);
                            intent.putExtra("RechargeTitle", getCurrencyUnit(activity) + LanguageUtil.getString(activity, R.string.MineNewFragment_chongzhi));
                            intent.putExtra("RechargeRightTitle", LanguageUtil.getString(activity, R.string.BaoyueActivity_chongzhijilu));
                            intent.putExtra("RechargeType", "gold");
                            activity.startActivity(intent);
                        } else if (videoInfoPurchaseinfoButtomItem.action.equals("exchange")) {
                            if (num != 0) {
                                ReaderParams readerParams = new ReaderParams(activity);
                                readerParams.putExtraParams("book_id", book_id);
                                readerParams.putExtraParams("chapter_id", chapter_id);
                                readerParams.putExtraParams("num", num);
                                readerParams.putExtraParams("use_gold", 1);
                                HttpUtils.getInstance().sendRequestRequestParams(activity,Api.REWARD_TICKET_VOTE, readerParams.generateParamsJson(), new HttpUtils.ResponseListener() {
                                    @Override
                                    public void onResponse(String response) {
                                        MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.dialog_vote_success));
                                        EventBus.getDefault().post(new RefreshMine());
                                        if (!TextUtils.isEmpty(response)) {
                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(response);
                                                if (jsonObject.has("ticket_num")) {
                                                    EventBus.getDefault().post(new BookBottomTabRefresh(2, jsonObject.getString("ticket_num")));
                                                }
                                            } catch (JSONException e) {
                                            }
                                        }
                                    }

                                    @Override
                                    public void onErrorResponse(String ex) {

                                    }
                                });
                            }
                        }
                        popupWindow.dismiss();
                    }
                });
            }
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setAnimationStyle(R.style.sign_pop_anim);
            popupWindow.showAtLocation(new View(activity), Gravity.CENTER, 0, 0);
            popupWindow.update();
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                // 在dismiss中恢复透明度
                public void onDismiss() {
                    ScreenSizeUtils.getInstance(activity).setBackgroundAlpha(1f, activity);
                }
            });
        }
    }

    public class ViewHolder {

        @BindView(R.id.public_dialog_tips)
        TextView dialogTips;
        @BindView(R.id.dialog_videoinfo_buydialog_des)
        LinearLayout dialogDes;
        @BindView(R.id.dialog_videoinfo_buydialog_button)
        LinearLayout dialogButton;

        @OnClick({R.id.dialog_add_shelf_finish})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.dialog_add_shelf_finish:
                    popupWindow.dismiss();
                    break;
            }
        }

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public static void setId(long book_id1, long chapter_id1, int num1) {
        chapter_id = chapter_id1;
        book_id = book_id1;
        num = num1;
    }
}
