package com.example.wytings.graph;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Rex on 2016/4/10.
 * https://github.com/wytings
 */
public class KLineModel implements Serializable {
    private boolean isHead;
    private String date;
    private String time;
    private float highest;
    private float lowest;
    private float open;
    private float close;
    private float changeRatio;
    private float tradeVolume;
    private double tradeAmount;
    private List<Float> MAValues;


    public KLineModel(String time, float highest, float lowest, float open, float close) {
        this.time = time;
        this.highest = highest;
        this.lowest = lowest;
        this.open = open;
        this.close = close;
    }

    public KLineModel(String time, float highest, float lowest, float open, float close, float changeRatio, float volume, double amount) {
        this.time = time;
        this.highest = highest;
        this.lowest = lowest;
        this.open = open;
        this.close = close;
        this.changeRatio = changeRatio;
        this.tradeVolume = volume;
        this.tradeAmount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getHighest() {
        return highest;
    }

    public void setHighest(float highest) {
        this.highest = highest;
    }

    public float getLowest() {
        return lowest;
    }

    public void setLowest(float lowest) {
        this.lowest = lowest;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public float getChangeRatio() {
        return changeRatio;
    }

    public void setChangeRatio(float changeRatio) {
        this.changeRatio = changeRatio;
    }

    public float getTradeVolume() {
        return tradeVolume;
    }

    public void setTradeVolume(float tradeVolume) {
        this.tradeVolume = tradeVolume;
    }

    public double getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public List<Float> getMAValues() {
        return MAValues;
    }

    public void setMAValues(List<Float> MAValues) {
        this.MAValues = MAValues;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isHead() {
        return isHead;
    }

    public void setIsHead(boolean isHead) {
        this.isHead = isHead;
    }

    @Override
    public String toString() {
        return "KLineModel{" +
                "time='" + time + '\'' +
                ", highest=" + highest +
                ", lowest=" + lowest +
                ", open=" + open +
                ", close=" + close +
                ", changeRatio=" + changeRatio +
                ", tradeVolume=" + tradeVolume +
                ", tradeAmount=" + tradeAmount +
                ", MAValues=" + MAValues +
                '}';
    }
}
