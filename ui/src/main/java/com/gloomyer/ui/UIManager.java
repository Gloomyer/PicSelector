package com.gloomyer.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

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
    OnSelectedListener mSelectedListener;
    List<Activity> activities;
    OnImageClickListener mImageClickListener;
    boolean autoSave;

    /**
     * 设置图片的点击事件
     *
     * @param listener
     */
    public void setOnImageClickListener(OnImageClickListener listener) {
        this.mImageClickListener = listener;
    }

    public void removeOnImageClickListener() {
        mImageClickListener = null;
    }

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
        activities = new ArrayList<>();
    }

    /**
     * 获取要显示的相册的图片集合数量
     *
     * @return
     */
    int getPreCount() {
        return preList == null ? 0 : preList.size();
    }

    /**
     * 提交数据
     * 调用回调
     * 销毁界面
     *
     * @param mContext
     */
    void save(Context mContext) {
        if (select != null && select.size() > 0) {
            for (Activity activity : activities) {
                activity.finish();
            }
            if (mSelectedListener != null)
                mSelectedListener.onSelect(select);
        } else {
            Toast.makeText(mContext, "你还没有选择图片!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 启动图片选择器
     *
     * @param mContext     上下文
     * @param totalSize    要选择的图片
     * @param autoSave     当如果用户选择足够的图片之后,是否自动保存提交
     * @param selectedImgs 已经选择的图片
     * @param listener     结果回调
     */
    public void start(Activity mContext,
                      int totalSize,
                      boolean autoSave,
                      List<String> selectedImgs,
                      OnSelectedListener listener) {
        this.autoSave = autoSave;
        totalCount = totalSize;
        select = new ArrayList<>();
        mSelectedListener = listener;
        if (selectedImgs != null && selectedImgs.size() > 0) {
            select.addAll(selectedImgs);
        }
        Intent intent = new Intent(mContext, FolderAct.class);
        mContext.startActivity(intent);
    }

    /**
     * 启动图片选择器
     *
     * @param mContext  上下文
     * @param totalSize 要选择的图片数量
     * @param listener  结果回调
     */
    public void start(Activity mContext, int totalSize, OnSelectedListener listener) {
        start(mContext, totalSize, false, null, listener);
    }

    /**
     * 选择一张单图
     * 选择之后自动保存
     *
     * @param mContext
     */
    public void start(Activity mContext, OnSelectedListener listener) {
        start(mContext, 1, true, null, listener);
    }

    void startPre(Activity mContext, ResultInfo resultInfo) {
        preList = resultInfo.getImages();
        Intent intent = new Intent(mContext, PreAct.class);
        mContext.startActivity(intent);
    }
}
