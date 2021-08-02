package com.example.bears.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BookmarkEntity {
    @PrimaryKey(autoGenerate = true)    // 기본키 자동생성
    public int id;

    @ColumnInfo(name="stationName")
    public String stationName;

    @ColumnInfo(name="stationId")
    public String stationId;

//    @ColumnInfo(name="busStopDirec")
//    public String busStopDirec;

    @ColumnInfo(name="busNum")
    public String busNum;

    public BookmarkEntity(String stationName, String stationId, String busNum) {
        this.stationName = stationName;
        this.stationId = stationId;
        this.busNum = busNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getBusNum() {
        return busNum;
    }

    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }
}
