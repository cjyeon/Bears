package com.example.bears;

public class BookmarkData {
    String busnum;
    String arrival_time;
    String arrival_busstop;
    boolean star;

    public BookmarkData(String busnum, String arrival_time, String arrival_busstop, boolean star) {
        this.busnum = busnum;
        this.arrival_time = arrival_time;
        this.arrival_busstop = arrival_busstop;
        this.star = star;
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
