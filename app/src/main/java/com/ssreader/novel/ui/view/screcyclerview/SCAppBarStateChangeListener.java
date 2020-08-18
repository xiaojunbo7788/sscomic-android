package com.ssreader.novel.ui.view.screcyclerview;


import com.google.android.material.appbar.AppBarLayout;

public abstract class SCAppBarStateChangeListener  implements AppBarLayout.OnOffsetChangedListener {

    public enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private SCAppBarStateChangeListener.State mCurrentState = SCAppBarStateChangeListener.State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState != SCAppBarStateChangeListener.State.EXPANDED) {
                onStateChanged(appBarLayout, SCAppBarStateChangeListener.State.EXPANDED);
            }
            mCurrentState = SCAppBarStateChangeListener.State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != SCAppBarStateChangeListener.State.COLLAPSED) {
                onStateChanged(appBarLayout, SCAppBarStateChangeListener.State.COLLAPSED);
            }
            mCurrentState = SCAppBarStateChangeListener.State.COLLAPSED;
        } else {
            if (mCurrentState != SCAppBarStateChangeListener.State.IDLE) {
                onStateChanged(appBarLayout, SCAppBarStateChangeListener.State.IDLE);
            }
            mCurrentState = SCAppBarStateChangeListener.State.IDLE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, SCAppBarStateChangeListener.State state);
}