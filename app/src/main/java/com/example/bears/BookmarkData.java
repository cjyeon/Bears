package com.example.bears;

public class BookmarkData {
    String stationName, stationId, stationDirec;
    String busnum, arrival_time, arrival_busstop;
    boolean star;

    public BookmarkData(String stationName, String stationId, String stationDirec, String busnum, String arrival_time, String arrival_busstop, boolean star) {
        this.stationName = stationName;
        this.stationId = stationId;
        this.stationDirec = stationDirec;
        this.busnum = busnum;
        this.arrival_time = arrival_time;
        this.arrival_busstop = arrival_busstop;
        this.star = star;
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

    public String getStationDirec() {
        return stationDirec;
    }

    public void setStationDirec(String stationDirec) {
        this.stationDirec = stationDirec;
    }

    public String getBusnum() {
        return busnum;
    }

    public void setBusnum(String busnum) {
        this.busnum = busnum;
    }

    public String getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(String arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getArrival_busstop() {
        return arrival_busstop;
    }

    public void setArrival_busstop(String arrival_busstop) {
        this.arrival_busstop = arrival_busstop;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }
}
