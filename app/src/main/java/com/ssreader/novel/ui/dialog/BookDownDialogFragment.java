package com.ssreader.novel.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BWNApplication;
import com.ssreader.novel.base.BaseDialogFragment;
import com.ssreader.novel.constant.Api;
import com.ssreader.novel.constant.Constant;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.model.BookChapter;
import com.ssreader.novel.model.ChapterContent;
import com.ssreader.novel.model.Downoption;
import com.ssreader.novel.net.HttpUtils;
import com.ssreader.novel.net.ReaderParams;
import com.ssreader.novel.ui.activity.BaseOptionActivity;
import com.ssreader.novel.ui.adapter.BookDownDialogAdapter;
import com.ssreader.novel.ui.read.manager.ChapterManager;
import com.ssreader.novel.ui.utils.ImageUtil;
import com.ssreader.novel.ui.utils.MyShape;
import com.ssreader.novel.ui.utils.MyToash;
import com.ssreader.novel.utils.FileManager;
import com.ssreader.novel.utils.LanguageUtil;
import com.ssreader.novel.utils.ObjectBoxUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.DOWN;

/**
 * 小说下载弹窗
 */
public class BookDownDialogFragment extends BaseDialogFragment {

    @BindView(R.id.dialog_book_down_bg_layout)
    LinearLayout bookDownBgLayout;
    @BindView(R.id.dialog_book_down_list_layout)
    RelativeLayout bookDownListLayout;
    @BindView(R.id.dialog_book_down_list)
    ListView bookDownList;

    // 是否正在下载
    private static boolean isDowning = false;
    // 数据
    private List<Downoption> downOptions;
    private BookDownDialogAdapter bookDownDialogAdapter;
    private int position;
    private Book baseBook;
    private BookChapter chapterItem;
    private boolean isReadBook;

    public BookDownDialogFragment() {

    }

    public BookDownDialogFragment(Activity activity, Book baseBook, BookChapter chapterItem, List<Downoption> downOptions) {
        super(true);
        this.activity = activity;
        this.baseBook = baseBook;
        this.chapterItem = chapterItem;
        if (chapterItem != null) {
            isReadBook = true;
        }
        if (this.downOptions == null) {
            this.downOptions = new ArrayList<>();
        }
        this.downOptions.clear();
        this.downOptions.addAll(downOptions);
    }

    @Override
    public int initContentView() {
        return R.layout.dialog_down;
    }

    @Override
    public void initView() {
        bookDownBgLayout.setBackground(MyShape.setMyshape(ImageUtil.dp2px(activity, 8),
                ContextCompat.getColor(activity, R.color.white_f8)));
        ViewGroup.LayoutParams layoutParams = bookDownList.getLayoutParams();
        layoutParams.height = ImageUtil.dp2px(activity, 40) * downOptions.size();
        bookDownList.setLayoutParams(layoutParams);
        ViewGroup.LayoutParams listParams = bookDownListLayout.getLayoutParams();
        listParams.height = layoutParams.height + ImageUtil.dp2px(activity, 90);
        bookDownListLayout.setLayoutParams(listParams);
        bookDownDialogAdapter = new BookDownDialogAdapter(activity, downOptions);
        bookDownList.setAdapter(bookDownDialogAdapter);
        initListener();
    }

    private void initListener() {
        bookDownList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                Downoption downOption = downOptions.get(i);
                if (!downOption.isdown && baseBook != null && !isDowning) {
                    downOption.isDowning = true;
                    getDownUrl(activity, downOption);
                    if (!isReadBook) {
                        ObjectBoxUtils.addData(baseBook, Book.class);
                    }
                    position = i;
                    if (bookDownDialogAdapter != null) {
                        bookDownDialogAdapter.notifyDataSetChanged();
                    }
                    dismissAllowingStateLoss();
                }
            }
        });
    }

    @OnClick({R.id.dialog_book_down_manger})
    public void onBookDownDialog(View view) {
        dismissAllowingStateLoss();
        activity.startActivity(new Intent(activity, BaseOptionActivity.class)
                .putExtra("OPTION", DOWN)
                .putExtra("ONLY_NOVER", true)
                .putExtra("title", LanguageUtil.getString(activity, R.string.BookInfoActivity_down_manger)));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.BookInfoActivity_down_adddown));
                    if (bookDownDialogAdapter != null && downOptions != null && downOptions.size() > position) {
                        downOptions.get(position).isDowning = false;
                        bookDownDialogAdapter.notifyDataSetChanged();
                    }
                    if (baseBook != null) {
                        dismissAllowingStateLoss();
                    }
                    break;
                case 1:
                    MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.BookInfoActivity_down_downcomplete));
                    break;
                case 2:
                    MyToash.ToashSuccess(activity, msg.obj.toString());
                    break;
            }
        }
    };

    private void getDownUrl(final Activity activity, final Downoption downoption) {
        isDowning = true;
        ReaderParams readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("book_id", downoption.book_id);
        readerParams.putExtraParams("chapter_id", downoption.s_chapter);
        readerParams.putExtraParams("num", downoption.down_num + "");
        String json = readerParams.generateParamsJson();
        HttpUtils.getInstance().sendRequestRequestParams(activity, Api.DOWN_URL, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            final String file_url = jsonObject.getString("file_url");
                            if (!TextUtils.isEmpty(file_url)) {
                                ChapterManager.downChapter(activity, downoption.book_id, new ChapterManager.DownChapter() {
                                    @Override
                                    public void success(List<BookChapter> bookChapterList) {
                                        isDowning = false;
                                        downFile(BWNApplication.applicationContext.getActivity(), file_url, downoption, bookChapterList);
                                    }

                                    @Override
                                    public void fail() {
                                        WaitDialogUtils.dismissDialog();
                                        isDowning = false;
                                        MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.ComicDownActivity_downfail));
                                    }
                                });
                            } else {
                                isDowning = false;
                            }
                        } catch (JSONException e) {
                            isDowning = false;
                            WaitDialogUtils.dismissDialog();
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        WaitDialogUtils.dismissDialog();
                        if (ex != null && ex.equals("701")) {
                            if (baseBook != null) {
                                dismissAllowingStateLoss();
                            }
                            PublicPurchaseDialog mPurchaseDialog = new PublicPurchaseDialog(activity, Constant.BOOK_CONSTANT,
                                    true, new PublicPurchaseDialog.BuySuccess() {
                                @Override
                                public void buySuccess(long[] ids, int num) {
                                    getDownUrl(activity, downoption);
                                }
                            }, true);
                            mPurchaseDialog.initData(downoption.book_id, downoption.s_chapter + "", true, downoption);
                            mPurchaseDialog.setIsreaderbook(isReadBook);
                            mPurchaseDialog.show();
                        }
                        isDowning = false;
                    }
                }
        );
    }

    private void downFile(Activity activity, String url, final Downoption downoption, List<BookChapter> bookChapterList) {
        HttpUtils.getInstance().downloadText(url, activity, new HttpUtils.OnDownloadListenerText() {
            @Override
            public void onDownloadSuccess(String txt2String) {
                MyToash.Log("file_url", txt2String + "");
                if (TextUtils.isEmpty(txt2String)) {
                    return;
                }
                if (txt2String.startsWith("{")) {
                    errorHandle(txt2String);
                } else
                    try {
                        JsonParser jsonParser = new JsonParser();
                        JsonArray jsonElements = jsonParser.parse(txt2String).getAsJsonArray();
                        Book book = ObjectBoxUtils.getBook(downoption.book_id);
                        downoption.downoption_date = System.currentTimeMillis();
                        List<Downoption> localDownOptions = ObjectBoxUtils.getDownoptionsfData(downoption.book_id);
                        for (Downoption downoption1 : localDownOptions) {
                            if (downoption.start_order <= downoption1.start_order && downoption1.end_order <= downoption.end_order) {
                                ObjectBoxUtils.deleteDownoption(downoption1.id);
                            } else if (downoption1.start_order <= downoption.start_order && downoption.start_order <= downoption1.end_order && downoption.end_order > downoption1.end_order) {
                                ObjectBoxUtils.deleteDownoption(downoption1.id);
                                downoption.start_order = downoption1.start_order;
                            } else {
                                if (downoption1.start_order <= downoption.end_order && downoption.end_order <= downoption1.end_order && downoption.start_order < downoption1.start_order) {
                                    ObjectBoxUtils.deleteDownoption(downoption1.id);
                                    downoption.end_order = downoption1.end_order;
                                }
                            }
                        }
                        ObjectBoxUtils.addData(downoption, Downoption.class);
                        handler.sendEmptyMessage(0);
                        EventBus.getDefault().post(downoption);//通知进度更新
                        int size = 0;

                        Gson gson = HttpUtils.getGson();
                        int temp = 0;
                        for (JsonElement jsonElement : jsonElements) {
                            ChapterContent chapterContent = gson.fromJson(jsonElement, ChapterContent.class);//解析
                            if (chapterContent != null && chapterContent.getContent() != null) {
                                String s = chapterContent.getContent();// RSAEncrypt.tranKey(chapterContent.getContent());
                                BookChapter bookChapter = getChapter(chapterContent.getChapter_id(), bookChapterList);
                                bookChapter.chapter_text = s;
                                bookChapter.is_preview=0;
                                ObjectBoxUtils.addData(bookChapter, BookChapter.class);
                               // chapterContent.setContent(s);
                               // chapterContent.setIs_preview(0);
                                String filepath = FileManager.getLocalBookTxtPath(bookChapter);
                                FileManager.createFile(filepath, s.getBytes());
                                size += chapterContent.getWords() * 3;
                                ++temp;
                                downoption.down_cunrrent_num = temp;
                                if (book.current_chapter_id == 0) {
                                    book.current_chapter_id = chapterContent.getChapter_id();
                                    ObjectBoxUtils.addData(book, Book.class);
                                }
                                EventBus.getDefault().post(downoption);//通知进度更新
                            }
                        }
                        downoption.downoption_size = (int) (((float) size / 1048576f) * 100) / 100f + "M";
                        downoption.isdown = true;
                        ObjectBoxUtils.addData(downoption, Downoption.class);
                        EventBus.getDefault().post(downoption);
                        handler.sendEmptyMessage(1);
                    } catch (Exception f) {
                        errorHandle(txt2String);
                    }
            }
        });
    }

    private BookChapter getChapter(final long chapterId, List<BookChapter> mChapterList) {
        for (BookChapter chapterItem : mChapterList) {
            if (chapterItem.getChapter_id() == chapterId) {
                return chapterItem;
            }
        }
        return null;
    }

    private void errorHandle(String txt2String) {
        try {
            Message message = Message.obtain();
            message.what = 2;
            message.obj = new JSONObject(txt2String).getString("msg");
            handler.sendMessage(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取下载目录
     *
     * @param activity
     * @param baseBook
     * @param chapterItem
     */
    public static void getDownBookChapters(Activity activity, final Book baseBook, BookChapter chapterItem, OnGetDownOptions getDownOptions) {
        if (isDowning) {
            return;
        }
        long chapter_id;
        if (chapterItem != null) {
            chapter_id = chapterItem.getChapter_id();
        } else {
            chapter_id = baseBook.getCurrent_chapter_id();
        }
        ReaderParams readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("book_id", baseBook.getBook_id());
        if (chapter_id != 0) {
            readerParams.putExtraParams("chapter_id", chapter_id);
        }
        WaitDialogUtils.showDialog(activity);
        String json = readerParams.generateParamsJson();
        HttpUtils.getInstance().sendRequestRequestParams(activity, Api.DOWN_OPTION, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        try {
                            JSONObject JsonObject = new JSONObject(result);
                            JSONArray jsonArray = JsonObject.getJSONArray("down_option");
                            if (jsonArray.length() > 0) {
                                List<Downoption> localDownOptions = ObjectBoxUtils.getDownoptionsfData(baseBook.book_id);
                                List<Downoption> list = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Downoption downoption = new Downoption();
                                    downoption.label = jsonObject.getString("label");
                                    downoption.tag = jsonObject.getString("tag");
                                    downoption.s_chapter = jsonObject.getLong("s_chapter");
                                    downoption.down_num = jsonObject.getInt("down_num");
                                    downoption.file_name = jsonObject.getString("file_name");
                                    downoption.tag = jsonObject.getString("tag");
                                    downoption.book_id = baseBook.getBook_id();
                                    downoption.cover = baseBook.getCover();
                                    downoption.bookname = baseBook.getName();
                                    downoption.description = baseBook.getDescription();
                                    downoption.start_order = jsonObject.getInt("start_order");
                                    downoption.end_order = jsonObject.getInt("end_order");
                                    downoption.isdown = false;
                                    for (Downoption downoption1 : localDownOptions) {//去重 起始 和结束序号 包含在已下载内 设为已下载
                                        if (downoption1.start_order <= downoption.start_order && downoption.end_order <= downoption1.end_order) {
                                            downoption.isdown = true;
                                            break;
                                        }
                                    }
                                    list.add(downoption);
                                }
                                if (getDownOptions != null) {
                                    getDownOptions.onOptions(list);
                                }
                            } else {
                                MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.chapterupdateing));
                            }
                        } catch (JSONException e) {
                            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.chapterupdateing));
                        }
                        WaitDialogUtils.dismissDialog();
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        WaitDialogUtils.dismissDialog();
                    }
                }
        );
    }

    public interface OnGetDownOptions {

        void onOptions(List<Downoption> downOptions);
    }
}
