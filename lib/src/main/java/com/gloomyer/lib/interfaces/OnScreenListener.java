package com.gloomyer.lib.interfaces;

import com.gloomyer.lib.bean.ResultInfo;

import java.util.List;

/**
 * 本地图片扫描的回调接口
 */

public interface OnScreenListener {
    /**
     * 开始扫描本地图片了
     */
    void start();


    /**
     * 完成扫描
     *
     * @param results 扫描结果
     * @param isCache 是否是从缓存中读取的
     */
    void finish(List<ResultInfo> results, boolean isCache);


    /**
     * 读取异常
     *
     * @param e 异常信息
     */
    void error(Exception e);
}
