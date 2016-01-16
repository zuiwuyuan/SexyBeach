package com.lnyp.sexybeach.rspdata;

import com.lnyp.sexybeach.entry.BeautySimple;

import java.util.List;

public class RspBeautySimple {

    private int total;
    private List<BeautySimple> tngou;

    public void setTotal(int total) {
        this.total = total;
    }

    public void setTngou(List<BeautySimple> tngou) {
        this.tngou = tngou;
    }

    public int getTotal() {
        return total;
    }

    public List<BeautySimple> getTngou() {
        return tngou;
    }
}
