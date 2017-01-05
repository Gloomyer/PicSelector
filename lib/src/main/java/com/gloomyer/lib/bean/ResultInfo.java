package com.gloomyer.lib.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回对象bean
 */

public class ResultInfo {

    public ResultInfo() {
        images = new ArrayList<>();
    }

    private String name;
    private int count;
    private String firstImagePath;
    private List<String> images;

    public void addCount() {
        count++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }


    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "ResultInfo{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", firstImagePath='" + firstImagePath + '\'' +
                ", images=" + images +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (!(o instanceof ResultInfo)) {
            return false;
        }

        if (name.equalsIgnoreCase(((ResultInfo) o).getName())) {
            return true;
        }

        return false;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + count;
        result = 31 * result + (firstImagePath != null ? firstImagePath.hashCode() : 0);
        result = 31 * result + (images != null ? images.hashCode() : 0);
        return result;
    }
}
