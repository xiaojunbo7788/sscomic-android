package com.ssreader.novel.ui.adapter;



import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> listfragment; //创建一个List<Fragment>
    private List<String> stringList; //创建一个List<Fragment>

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.listfragment = list;

    }
    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list,List<String> stringList) {
        super(fm);
        this.listfragment = list;
        this.stringList = stringList;
    }

    @Override
    public Fragment getItem(int arg0) {
        return listfragment.get(arg0); //返回第几个fragment
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(stringList!=null&&!stringList.isEmpty()){
            return stringList.get(position);
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listfragment.size(); //总共有多少个fragment
    }
}