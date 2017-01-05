package com.gloomyer.lib;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import com.gloomyer.lib.bean.ResultInfo;
import com.gloomyer.lib.interfaces.OnScreenListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * lib库的核心管理者
 * 单例模式
 * 通过getInstance获取
 *
 * @author Gloomy
 * @date 2017年01月05日
 */

public class Manager {
    private static Manager instance;

    /**
     * 获取管理者实例
     *
     * @param cache     是否启用缓存
     * @param cacheTime 缓存时间
     * @return
     */
    public static Manager getInstance(boolean cache, long cacheTime) {
        synchronized (Manager.class) {
            if (instance == null)
                instance = new Manager(cache, cacheTime);
        }
        return instance;
    }

    /**
     * 获取管理者实例
     *
     * @return
     */
    public static Manager getInstance() {
        return getInstance(true, CACHE_TIME);
    }

    private Manager(boolean cache, long cacheTime) {
        this.isEnableCache = cache;
        this.screenCacheTime = cacheTime;
    }

    public static final String TAG = "Manager";
    public static final long CACHE_TIME = 1000 * 60;
    private OnScreenListener mOnScreenListener;
    private boolean isEnableCache;
    private long screenCacheTime;
    private long lastScreenTime;
    private List<ResultInfo> cacheResults;
    private Context mContext;
    private Handler mHandler;


    /**
     * 开始扫描本地图片
     */
    public void startScreen(Context mContext, OnScreenListener mOnScreenListener) {
        mHandler = new Handler();
        if (mOnScreenListener == null)
            throw new NullPointerException("扫描监听不能为null");

        this.mOnScreenListener = mOnScreenListener;
        this.mContext = mContext;

        mOnScreenListener.start(); //通知开始扫描了

        //启用了缓存
        if (isEnableCache && cacheResults != null && cacheResults.size() > 0) {
            //获取现在时间和上一次时间做对比，看是否构成扫描新的的条件
            long time = System.currentTimeMillis();
            time -= lastScreenTime;

            if (time <= screenCacheTime) {
                //直接返回缓存数据
                mOnScreenListener.finish(cacheResults, true);
                return;
            }
        }

        //开始重新读取新的数据,耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startScreen();
                } catch (final Exception e) {
                    post(new GLibMsg(new Runnable() {
                        @Override
                        public void run() {
                            Manager.this.mOnScreenListener.error(e);
                        }
                    }));
                } finally {
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Manager.this.mContext = null;
                    Manager.this.mOnScreenListener = null;
                    mHandler = null;
                }
            }
        }).start();

    }

    /**
     * 开始扫描任务
     */
    private void startScreen() throws Exception {
        ContentResolver contentResolver = mContext.getContentResolver();
        String selection =
                MediaStore.Images.Media.MIME_TYPE
                        + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE
                        + "=?";
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , null
                , selection
                , new String[]{"image/jpeg", "image/png"}
                , MediaStore.Images.Media.DEFAULT_SORT_ORDER);

        if (cursor == null) {
            //读取出现异常!
            throw new IOException("内容解析者解析异常!");
        }

        final List<ResultInfo> results = new ArrayList<>();

        while (cursor.moveToNext()) {
            /**
             * 图片路径
             */
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            /**
             * 图片所在相册名称
             */
            String folderName = new File(path).getParentFile().getName();

            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setName(folderName);

            if (!results.contains(resultInfo)) {
                resultInfo.getImages().add(path);
                resultInfo.setFirstImagePath(path);
                resultInfo.addCount();
                results.add(resultInfo);
            } else {
                int index = results.indexOf(resultInfo);
                results.get(index).getImages().add(path);
                results.get(index).addCount();
            }

        }

        post(new GLibMsg(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "results:" + results);
                mOnScreenListener.finish(results, false);
            }
        }));

        if (isEnableCache) {
            cacheResults = results;
            lastScreenTime = System.currentTimeMillis();
        }
        cursor.close();
    }

    /**
     * 提交任务到主线程
     *
     * @param msg
     */
    private void post(GLibMsg msg) {
        mHandler.post(msg.runnable);
    }
}
