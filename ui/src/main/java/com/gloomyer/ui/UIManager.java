package com.gloomyer.ui;

import android.app.Activity;
import android.content.Intent;

import com.gloomyer.lib.bean.ResultInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择管理者
 */

public class UIManager {

    private static UIManager instance;
    List<String> select;
    int totalCount;
    List<String> preList;

    /**
     * 获取管理者实例
     */
    public static UIManager getInstance() {
        synchronized (com.gloomyer.lib.Manager.class) {
            if (instance == null)
                instance = new UIManager();
        }
        return instance;
    }

    private UIManager() {

    }

    int getPreCount() {
        return preList == null ? 0 : preList.size();
    }

    public void start(Activity mContext, int totalSize, List<String> selectedImgs) {
        totalCount = totalSize;
        select = new ArrayList<>();
        if (selectedImgs != null && selectedImgs.size() > 0) {
            select.addAll(selectedImgs);
        }
        Intent intent = new Intent(mContext, FolderAct.class);
        mContext.startActivity(intent);
    }

    public void start(Activity mContext, int totalSize) {
        start(mContext, totalSize, null);
    }

    void startPre(Activity mContext, ResultInfo resultInfo) {
        preList = resultInfo.getImages();
        Intent intent = new Intent(mContext, PreAct.class);
        mContext.startActivity(intent);
    }
}
