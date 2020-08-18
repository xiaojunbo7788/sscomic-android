package com.ssreader.novel.ui.localshell.localapp;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ssreader.novel.R;
import com.ssreader.novel.base.BaseFragment;
import com.ssreader.novel.ui.localshell.bean.LocalNotesBean;
import com.ssreader.novel.ui.localshell.adapter.LocalNotesAdapter;
import com.ssreader.novel.ui.view.screcyclerview.SCOnItemClickListener;
import com.ssreader.novel.ui.view.screcyclerview.SCRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LocalNotesFragment extends BaseFragment {

    @BindView(R.id.public_recycleview)
    SCRecyclerView public_recycleview;
    private LocalNotesAdapter localNotesAdapter;
    private int OPTION;
    private List<LocalNotesBean> list;
    public LocalNotesFragment() {
    }

    public LocalNotesFragment(int OPTION) {
        this.OPTION = OPTION;
    }



    @Override
    public int initContentView() {
        USE_EventBus = true;
        return R.layout.fragment_local_notes;
    }

    @Override
    public void initView() {
        initSCRecyclerView(public_recycleview, RecyclerView.VERTICAL,0);
        list = new ArrayList<>();
        if(OPTION==0) {
            // String frombookTitle, String notesTime, String notesContent, String notesTitle
            list.add(new LocalNotesBean(1, "魔物祭坛", "2020.01.17", "穿越到与地球类似但却魔物横行的危险世界，方平在即将被魔物撕碎前激活魔物祭坛，得到献祭魔物获得动漫人物能力与天赋的专属技能。", "银霜骑士"));
            list.add(new LocalNotesBean(2, "诸天神话群", "2020.01.17", "孟陆有点慌，他穿越了，还穿成了西游中的虎力大仙，最终惨死在孙悟空手中的三个霉憨憨之一，好在，随身激活了神奇的诸天群。", "左铭左铸"));
            list.add(new LocalNotesBean(3, "在士兵突击中当兵王", "2020.01.19", "　许韵达重生成了许三多的二哥，看韵达速递如何冲刺军旅人生。", "青青子衿"));
            list.add(new LocalNotesBean(4, "重装勇士 ", "2020.01.20", "本故事由FC经典初代“重装机兵”改编。最强电脑“诺亚”毁灭了的人类文明，大陆上各种机械和变异怪物横行，来自不同城镇的三位勇士驾驶战车组成小队，走上了寻找并且消灭诺亚的旅程。下一个城镇他们将会遇到什么样的敌人呢？消灭了诺亚就能拯救世界吗？他们的命运，将归去何方？", "红心白蔡"));
        }else {
            list.add(new LocalNotesBean(-1, "魔物祭坛", "2020.01.17", "穿越到与地球类似但却魔物横行的危险世界，方平在即将被魔物撕碎前激活魔物祭坛，得到献祭魔物获得动漫人物能力与天赋的专属技能。", "银霜骑士"));
        }
        localNotesAdapter = new LocalNotesAdapter(list, activity, new SCOnItemClickListener<LocalNotesBean>() {
            @Override
            public void OnItemClickListener(int flag, int position, LocalNotesBean bean) {
                if(bean.book_id!=-1) {
                    Intent intent = new Intent(activity, LocalNoteDetailActivity.class);
                    intent.putExtra("LocalNotesBean", bean);
                    activity.startActivity(intent);
                }else {
                    Intent intent = new Intent(activity, LocalEditNotesActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void OnItemLongClickListener(int flag, int p, LocalNotesBean bean) {
              /*  title = bean.frombookTitle;
                auther =  bean.notesTitle;
                context = bean.notesContent;
                position = p;
                registerForContextMenu(view);*/
            }
        });
        public_recycleview.setAdapter(localNotesAdapter);

    }

    @Override
    public void initData() {
        if (SCRecyclerViewRefresh ) {
            public_recycleview.refreshComplete();
            SCRecyclerViewRefresh = false;
        } else if (SCRecyclerViewLoadMore ) {
            public_recycleview.loadMoreComplete();
            SCRecyclerViewLoadMore = false;
        }
    }

    @Override
    public void initInfo(String json) {

    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "修改笔记");
        menu.add(0, 2, 0, "增加笔记");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case 1:
                intent.setClass(activity, ModifyNoteActivity.class);
                break;
            case 2:
                intent.setClass(activity, LocalEditNotesActivity.class);
                break;
        }
        startActivity(intent);
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshMineListItem(LocalNotesBean localNotesBean) {
        if(OPTION==1) {
            for (LocalNotesBean notesBean : list) {
                if (notesBean.book_id==-1) {
                    list.remove(notesBean);
                }
            }
            list.add(localNotesBean);
            localNotesAdapter.notifyDataSetChanged();
        }
    }
}
