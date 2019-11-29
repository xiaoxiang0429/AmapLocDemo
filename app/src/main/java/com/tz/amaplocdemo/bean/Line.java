package com.tz.amaplocdemo.bean;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class Line extends LitePalSupport {
    List<GPS> gpsList = new ArrayList<GPS>();

    private int id;

    private String lineName;

    private long createTime;

    private long endTime;

    public List<GPS> getGpsList() {
        return LitePal.where("lineId = ?",String.valueOf(id)).find(GPS.class);
    }

    public void setGpsList(List<GPS> gpsList) {
        this.gpsList = gpsList;
    }

    public int getId() {
        return id;
    }


    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
