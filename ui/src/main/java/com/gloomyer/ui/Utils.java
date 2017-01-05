package com.gloomyer.ui;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

/**
 * Created by Gloomy on 2017/1/5.
 */

public class Utils {
    public static int getScreenWidth(Context mContext) {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static void refresh(Toolbar mToolbar) {
        mToolbar.setSubtitle("已选 : "
                + UIManager.getInstance().select.size() + "/"
                + UIManager.getInstance().totalCount);
    }
}
