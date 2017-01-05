package com.gloomyer.ui;

import java.util.List;

/**
 * Created by MSI-PC on 2017/1/5.
 */

public interface OnSelectedListener {
    /**
     * 完成选择
     * @param selecteds 用户选择的图片
     */
    void onSelect(List<String> selecteds);
}
