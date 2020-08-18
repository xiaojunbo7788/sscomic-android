package com.ssreader.novel.ui.activity;

import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseActivity;
import com.ssreader.novel.eventbus.BookCatalogMarkRefresh;
import com.ssreader.novel.model.Book;
import com.ssreader.novel.ui.adapter.MyFragmentPagerAdapter;
import com.ssreader.novel.ui.fragment.BookCatalogFragment;
import com.ssreader.novel.ui.fragment.BookMarkFragment;
import com.ssreader.novel.utils.LanguageUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.ssreader.novel.constant.Constant.AgainTime;
import static com.ssreader.novel.constant.Constant.LOCAL_BOOKID;

/**
 * 小说目录加章节
 */
public class BookCatalogMarkActivity extends BaseActivity {

    @BindView(R.id.book_catalog_mark_smartTabLayout)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.book_catalog_mark_order)
    RelativeLayout orderLayout;
    @BindView(R.id.book_catalog_mark_order_img)
    ImageView orderImage;
    @BindView(R.id.book_catalog_mark_viewPager)
    ViewPager viewPager;

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> tabList = new ArrayList<>();
    private List<TextView> textViewList = new ArrayList<>();
    private Fragment fragment1, fragment2;

    private boolean isFromBookRead;

    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.activity_book_catalog_mark;
    }

    @Override
    public void initView() {
        Book book = (Book) formIntent.getSerializableExtra("book");
        isFromBookRead = formIntent.getBooleanExtra("isFromBookRead", false);
        fragment1 = new BookCatalogFragment(book, isFromBookRead);
        fragmentList.add(fragment1);
        tabList.add(LanguageUtil.getString(activity, R.string.BookInfoActivity_mulu));
        if (book.book_id < LOCAL_BOOKID) {
            fragment2 = new BookMarkFragment(book, isFromBookRead);
            fragmentList.add(fragment2);
            tabList.add(LanguageUtil.getString(activity, R.string.BookInfoActivity_mark));
        }
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, tabList));
        smartTabLayout.setViewPager(viewPager);
        textViewList.add(smartTabLayout.getTabAt(0).findViewById(R.id.item_tablayout_text));
        if (tabList.size() > 1) {
            textViewList.add(smartTabLayout.getTabAt(1).findViewById(R.id.item_tablayout_text));
        }
        initListener();
    }

    private void initListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int index) {
                if (tabList.size() > 1) {
                    textViewList.get(index).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    textViewList.get(1 - index).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    if (index == 1) {
                        orderLayout.setVisibility(View.GONE);
                    } else {
                        orderLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public interface IsOrderChange {

        void isOrderChange(boolean isOrder);
    }

    private boolean IsOrder;

    @OnClick({R.id.book_catalog_mark_back, R.id.book_catalog_mark_order})
    public void getEvent(View view) {
        long ClickTimeNew = System.currentTimeMillis();
        if (ClickTimeNew - ClickTime > AgainTime) {
            ClickTime = ClickTimeNew;
            switch (view.getId()) {
                case R.id.book_catalog_mark_back:
                    finish();
                    break;
                case R.id.book_catalog_mark_order:
                    ((BookCatalogFragment) fragment1).setOrder(new IsOrderChange(){
                        @Override
                        public void isOrderChange(boolean isOrder) {
                            if(isOrder) {
                                IsOrder = !IsOrder;
                                orderImage.setImageResource(!IsOrder ? R.mipmap.dsc : R.mipmap.asc);
                            }
                        }
                    });
                    break;
            }
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(final String response) {

    }

    /**
     * 用于关闭目录、书签界面
     *
     * @param refresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(BookCatalogMarkRefresh refresh) {
        if (refresh.isFinish() && isFromBookRead) {
            finish();
        }
    }
}
