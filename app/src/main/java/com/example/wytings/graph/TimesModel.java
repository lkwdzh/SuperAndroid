package com.example.wytings.graph;

import java.io.Serializable;

/**
 * Created by Rex on 2016/4/10.
 * https://github.com/wytings
 */
public class TimesModel implements Serializable {

    private String date;
    private String time;
    private float lastPrice;
    private float changeRatio;
    private float averagePrice;
    private float tradeVolume;
    private double tradeAmount;

    public TimesModel(String time, float lastPrice, float changeRatio, float averagePrice, float volume, double tradeAmount) {
        this.time = time;
        this.lastPrice = lastPrice;
        this.changeRatio = changeRatio;
        this.tradeVolume = volume;
        this.averagePrice = averagePrice;
        this.tradeAmount = tradeAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(float lastPrice) {
        this.lastPrice = lastPrice;
    }

    public float getChangeRatio() {
        return changeRatio;
    }

    public void setChangeRatio(float changeRatio) {
        this.changeRatio = changeRatio;
    }

    public float getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(float averagePrice) {
        this.averagePrice = averagePrice;
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

    @Override
    public String toString() {
        return "TimesModel{" +
                "time='" + time + '\'' +
                ", lastPrice=" + lastPrice +
                ", changeRatio=" + changeRatio +
                ", averagePrice=" + averagePrice +
                ", tradeVolume=" + tradeVolume +
                ", tradeAmount=" + tradeAmount +
                '}';
    }
}
